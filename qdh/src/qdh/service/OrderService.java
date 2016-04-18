package qdh.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.dao.entity.VO.CustOrderProductVO;
import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CurrentBrandsDaoImpl;
import qdh.dao.impl.order.CustOrderProdDaoImpl;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.dao.impl.product.ProductBarcodeDaoImpl;
import qdh.pageModel.SessionInfo;


@Service
public class OrderService {

	@Autowired
	private CustOrderProdDaoImpl custOrderProdDaoImpl;
	
	@Autowired
	private ProductBarcodeDaoImpl productBarcodeDaoImpl;
	
	@Autowired
	private CustomerDaoImpl customerDaoImpl;
	
	@Autowired
	private CurrentBrandsDaoImpl currentBrandsDaoImpl;

	/**
	 * 在品牌排名中点击 加订
	 * @param loginUser
	 * @param pbId
	 * @param quantity
	 * @return
	 */
	@Transactional
	public Response orderMore(SessionInfo loginUser, Integer pbId, Integer quantity) {
		Response response = new Response();
		ProductBarcode pBarcode = productBarcodeDaoImpl.get(pbId, true);
		if (pBarcode == null){
			response.setFail("无法找到当前产品");
			return response;
		}
		
		if (quantity == null)
			quantity = 1;
		
		int userId = loginUser.getUserId();
		
		CustOrderProduct custOrderProduct = custOrderProdDaoImpl.getByPk(userId, pbId);
		if (custOrderProduct == null){
			custOrderProduct = new CustOrderProduct(userId, pBarcode, quantity);
			custOrderProdDaoImpl.save(custOrderProduct, true);
		} else {
			custOrderProduct.addQ(quantity);
			custOrderProdDaoImpl.update(custOrderProduct, true);
		}
		
		//2.获取这个货品的当前数量
		int myQ = custOrderProdDaoImpl.getMyQ(userId, pbId);
		int totalQ = custOrderProdDaoImpl.getTotalQ(pbId);
		
		Map<String, Integer> qMap = new HashMap();
		qMap.put("myQ", myQ);
		qMap.put("totalQ", totalQ);
		
		response.setReturnValue(qMap);
		
		return response;
	}

	@Transactional
	public Response myOrderMore(SessionInfo loginUser, Integer pbId, Integer cbId,
			Integer quantity) {
		Response response = new Response();
		ProductBarcode pBarcode = productBarcodeDaoImpl.get(pbId, true);
		if (pBarcode == null){
			response.setFail("无法找到当前产品");
			return response;
		}
		
		if (quantity == null)
			quantity = 1;
		
		int userId = loginUser.getUserId();
		
		CustOrderProduct custOrderProduct = custOrderProdDaoImpl.getByPk(userId, pbId);
		if (custOrderProduct == null){
			if (quantity > 0){
				custOrderProduct = new CustOrderProduct(userId, pBarcode, quantity);
				custOrderProdDaoImpl.save(custOrderProduct, true);
			} else {
				response.setFail("还没有此货品定单,无法减订");
				return response;
			}
				
		} else {
			int newQ = quantity + custOrderProduct.getQuantity();
			
			if (newQ > 0){
				custOrderProduct.addQ(quantity);
				custOrderProdDaoImpl.update(custOrderProduct, true);
			} else {
				custOrderProdDaoImpl.delete(custOrderProduct, true);
				response.setReturnCode(Response.WARNING);
			}

		}
		
		//2.获取用户的当前数量
		//1. 限制产品信息
		Set<Integer> barcodeIds = null;
		if (cbId != null){
				CurrentBrands currentBrands = currentBrandsDaoImpl.get(cbId, true);
				
				if (currentBrands != null){
					int yearId = currentBrands.getYear().getYear_ID();
					int quarterId = currentBrands.getQuarter().getQuarter_ID();
					int brandId = currentBrands.getBrand().getBrand_ID();
					
					//1. find all products barcode
					barcodeIds = productBarcodeDaoImpl.getIds(yearId, quarterId, brandId);
				}
		}
		List<Object> myTotal = custOrderProdDaoImpl.getMyTotal(userId, barcodeIds);
		
		//3. 获取这个货品的当前信息
		CustOrderProduct coProduct = custOrderProdDaoImpl.getByPk(userId, pbId);
		
		Map<String, Object> qMap = new HashMap();
		qMap.put("myQ", myTotal.get(0));
		qMap.put("mySum", myTotal.get(1));
		
		System.out.println(myTotal.get(0) +"," + myTotal.get(1));
		
		if (coProduct != null){
			qMap.put("pQ", coProduct.getQuantity());
			qMap.put("pSum", coProduct.getSumWholePrice());
		}
		
		response.setReturnValue(qMap);
		
		return response;
	}

	/**
	 * 模糊查询product
	 * @param loginUser
	 * @param productCode
	 * @return
	 */
	public Response searchProduct(Integer custId, String productCode) {
		System.out.println("-----------" + productCode);
		productCode = productCode.replaceAll("\\.", "_");
		System.out.println("-----------" + productCode);
		Response response = new Response();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ProductBarcode.class);
		DetachedCriteria productCriteria = criteria.createCriteria("product");
		
		productCriteria.add(Restrictions.like("productCode", productCode, MatchMode.ANYWHERE));

		productCriteria.addOrder(Order.desc("quarter.quarter_ID"));
		productCriteria.addOrder(Order.desc("brand.brand_ID"));
		productCriteria.addOrder(Order.desc("productCode"));
		
		//1. 生成CurrentOrderProduct map
		List<ProductBarcode> productBarcodes = productBarcodeDaoImpl.getByCritera(criteria, 0, 10, true);
		if (productBarcodes == null || productBarcodes.size() == 0)
			return response;
		Map<Integer, CustOrderProductVO> copMap = new HashMap<>();
		List<Integer> pbIds = new ArrayList<>();
		for (ProductBarcode pb : productBarcodes){
			CustOrderProduct newCOP = new CustOrderProduct(custId, pb, 0);
			copMap.put(pb.getId(), new CustOrderProductVO(newCOP));
			pbIds.add(pb.getId());
		}
		
		//2. 获取已定的map
		DetachedCriteria criteria2 = DetachedCriteria.forClass(CustOrderProduct.class);
		criteria2.add(Restrictions.eq("custId", custId));
		criteria2.add(Restrictions.in("productBarcode.id", pbIds));
		List<CustOrderProduct> custOrderProducts = custOrderProdDaoImpl.getByCritera(criteria2, true);
		int quantity = 0;
		double sumWhole = 0;
		int pbId = 0;
		for (CustOrderProduct custOrderProduct : custOrderProducts){
			quantity = custOrderProduct.getQuantity();
			sumWhole = custOrderProduct.getSumWholePrice();
			pbId = custOrderProduct.getProductBarcode().getId();
			
			CustOrderProductVO custOrderProduct2 = copMap.get(pbId);
			custOrderProduct2.setQuantity(quantity);
			custOrderProduct2.setSumWholePrice(sumWhole);
		}
		
		//3. 生成response的
		List<CustOrderProductVO> custProducts = new ArrayList<>();
		for (Integer id : pbIds){
			CustOrderProductVO custOrderProductVO = copMap.get(id);
			custProducts.add(custOrderProductVO);
		}
		
		response.setReturnValue(custProducts);
		
		return response;		
	}

	public Response startOrderMore(SessionInfo loginUser, Integer pbId, Integer quantity) {
		Response response = new Response();
		ProductBarcode pBarcode = productBarcodeDaoImpl.get(pbId, true);
		if (pBarcode == null){
			response.setFail("无法找到当前产品");
			return response;
		}
		
		if (quantity == null)
			quantity = 1;
		
		int userId = loginUser.getUserId();
		
		CustOrderProduct custOrderProduct = custOrderProdDaoImpl.getByPk(userId, pbId);
		if (custOrderProduct == null){
			if (quantity > 0){
				custOrderProduct = new CustOrderProduct(userId, pBarcode, quantity);
				custOrderProdDaoImpl.save(custOrderProduct, true);
			} else {
				response.setFail("还没有此货品定单,无法减订");
				return response;
			}
				
		} else {
			int newQ = quantity + custOrderProduct.getQuantity();
			
			if (newQ > 0){
				custOrderProduct.addQ(quantity);
				custOrderProdDaoImpl.update(custOrderProduct, true);
			} else {
				custOrderProdDaoImpl.delete(custOrderProduct, true);
				response.setReturnCode(Response.WARNING);
			}

		}

		//3. 获取这个货品的当前信息
		CustOrderProduct coProduct = custOrderProdDaoImpl.getByPk(userId, pbId);
		
		Map<String, Object> qMap = new HashMap();

		if (coProduct != null){
			qMap.put("pQ", coProduct.getQuantity());
			qMap.put("pSum", coProduct.getSumWholePrice());
		}
		
		response.setReturnValue(qMap);
		
		return response;
	}

}
