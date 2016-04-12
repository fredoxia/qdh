package qdh.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.dao.entity.order.CustOrderProduct;
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
	public Response myOrderMore(SessionInfo loginUser, Integer pbId,
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
		List<Object> myTotal = custOrderProdDaoImpl.getMyTotal(userId);
		
		//3. 获取这个货品的当前信息
		CustOrderProduct coProduct = custOrderProdDaoImpl.getByPk(userId, pbId);
		
		Map<String, Object> qMap = new HashMap();
		qMap.put("myQ", myTotal.get(0));
		qMap.put("mySum", myTotal.get(1));
		
		if (coProduct != null){
			qMap.put("pQ", coProduct.getQuantity());
			qMap.put("pSum", coProduct.getSumWholePrice());
		}
		
		response.setReturnValue(qMap);
		
		return response;
	}

}
