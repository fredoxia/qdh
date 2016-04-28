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
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.dao.config.EntityConfig;
import qdh.dao.entity.VO.CustOrderProductVO;
import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;
import qdh.dao.entity.qxMIS.CustPreOrder;
import qdh.dao.entity.qxMIS.CustPreOrderProduct;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CurrentBrandsDaoImpl;
import qdh.dao.impl.order.CustOrderProdDaoImpl;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.dao.impl.product.ProductBarcodeDaoImpl;
import qdh.dao.impl.qxMIS.CustPreOrderDaoImpl;
import qdh.dao.impl.qxMIS.CustPreOrderProductDaoImpl;
import qdh.dao.impl.systemConfig.SystemConfigDaoImpl;
import qdh.pageModel.SessionInfo;
import qdh.utility.DateUtility;
import qdh.utility.loggerLocal;


@Service
public class OrderService {

	@Autowired
	private CustOrderProdDaoImpl custOrderProdDaoImpl;
	
	@Autowired
	private ProductBarcodeDaoImpl productBarcodeDaoImpl;
	
	@Autowired
	private SystemConfigDaoImpl systemConfigDaoImpl;
	
	@Autowired
	private CurrentBrandsDaoImpl currentBrandsDaoImpl;

	@Autowired
	private CustPreOrderDaoImpl custPreOrderDaoImpl;
	
	@Autowired
	private CustPreOrderProductDaoImpl custPreOrderProductDaoImpl;
	
	@Autowired
	private CustomerDaoImpl customerDaoImpl;
	
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
			custOrderProduct = new CustOrderProduct(userId, pBarcode, quantity, systemConfigDaoImpl.getOrderIdentity());
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
				custOrderProduct = new CustOrderProduct(userId, pBarcode, quantity, systemConfigDaoImpl.getOrderIdentity());
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
			CustOrderProduct newCOP = new CustOrderProduct(custId, pb, 0, "");
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
				custOrderProduct = new CustOrderProduct(userId, pBarcode, quantity, systemConfigDaoImpl.getOrderIdentity());
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

	/**
	 * 导出订单，如果订货会还不是完成的状态不能导出
	 * @param userId
	 * @return
	 */
	@Transactional
	public Response exportOrders(int userId) {
		int totalExportedRecords = 0;
		int totalErrorCust = 0;
		int totalExportedCust = 0;
		
		Response response = new Response();
		
		String orderIdentity = systemConfigDaoImpl.getOrderIdentity();
		
		//1. 获取多少个customer
		DetachedCriteria criteriaCount = DetachedCriteria.forClass(CustOrderProduct.class);
		criteriaCount.add(Restrictions.eq("orderIdentity", orderIdentity));
		criteriaCount.setProjection(Projections.distinct(Projections.property("custId")));
		List<Object> custObj = custOrderProdDaoImpl.getByCriteriaProjection(criteriaCount, false);
		if (custObj == null || custObj.size() == 0){
			loggerLocal.info("没有找到当前订货会数据 " + orderIdentity);
			response.setFail("没有找到当前订货会的订单数据");
		} else {
			for (Object custIdObj :  custObj){
				Integer custId = (Integer)custIdObj;
				loggerLocal.info("导出客户数据 : " + orderIdentity + "," + custId);
				
				try {
					//2. 删除原始数据
					CustPreOrder preOrder = custPreOrderDaoImpl.getByCustIdOrderIdentity(custId, orderIdentity);
					if (preOrder != null){
						int custPreOrderId = preOrder.getId();
						custPreOrderProductDaoImpl.deleteByOrderId(custPreOrderId);
					} else {
					    //3. 保存custPreOrder 数据
					    preOrder = new CustPreOrder();
					    
					    Customer cust = customerDaoImpl.get(custId, true);
					    if (cust == null){
					    	loggerLocal.error("客户Id为 " + custId + " 无法找到信息");
					    	continue;
					    } else {
					    	preOrder.setOrderIdentity(orderIdentity);
					    	preOrder.setChainId(cust.getChainId());
					    	preOrder.setChainStoreName(cust.getChainStoreName());
					    	preOrder.setCustId(custId);
					    	preOrder.setCustName(cust.getCustName());
					    	custPreOrderDaoImpl.save(preOrder, false);
					    }
					}
					
					//4. 获取客户数据
					DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
					criteria.add(Restrictions.eq("custId", custId));
					criteria.add(Restrictions.eq("orderIdentity", orderIdentity));
					criteria.add(Restrictions.ne("status", EntityConfig.INACTIVE));
					List<CustOrderProduct> custOrderProducts = custOrderProdDaoImpl.getByCritera(criteria, false);
					
					if (custOrderProducts == null || custOrderProducts.size() == 0){
						custPreOrderDaoImpl.delete(preOrder, false);
						continue;
					}
					
					int indexNum = 1;
					int totalQ = 0;
					double sumCost = 0;
					double sumWholePrice = 0;
					double sumRetailPrice = 0;
					for (CustOrderProduct cop : custOrderProducts){
						Product product = cop.getProductBarcode().getProduct();
						
						totalQ += cop.getQuantity();
						sumCost += product.getRecCost();
						sumWholePrice += product.getWholePrice();
						sumRetailPrice += product.getSalesPrice();
						
						CustPreOrderProduct cpop = new CustPreOrderProduct(cop, indexNum++, preOrder.getId());
						custPreOrderProductDaoImpl.save(cpop, false);
						totalExportedRecords++;
					}
					
					//5. 更新order 信息
					if (preOrder.getCreateDate() == null)
						preOrder.setCreateDate(DateUtility.getToday());
					preOrder.setExportDate(DateUtility.getToday());
					preOrder.setSumCost(sumCost);
					preOrder.setSumRetailPrice(sumRetailPrice);
					preOrder.setSumWholePrice(sumWholePrice);
					preOrder.setTotalQuantity(totalQ);
					
					custPreOrderDaoImpl.update(preOrder, false);
					
					totalExportedCust++;
				} catch (Exception e) {
					totalErrorCust++;
					loggerLocal.error("导出订单发生错误 : " + custId);
					loggerLocal.error(e);
				}
			}
		}
		
		String msg = "成功导出的客户订单数量 : " + totalExportedCust + "\n"
				   + "导出失败的客户订单数量 : " + totalErrorCust;
		
		response.setMessage(msg);
		
		return response;
	}

}
