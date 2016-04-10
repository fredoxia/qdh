package qdh.service;


import java.util.HashMap;
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
			custOrderProduct = new CustOrderProduct(userId, pBarcode, 1);
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

}
