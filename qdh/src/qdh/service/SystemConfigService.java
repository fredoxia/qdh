package qdh.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.systemConfig.SystemConfig;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CustOrderProductDaoImpl;
import qdh.dao.impl.systemConfig.OrderExportLogDaoImpl;
import qdh.dao.impl.systemConfig.SystemConfigDaoImpl;

@Service
public class SystemConfigService {
	
	@Autowired
	private SystemConfigDaoImpl systemConfigDaoImpl;
	
	@Autowired
	private CustOrderProductDaoImpl custOrderProductDaoImpl;
	
	@Autowired
	private OrderExportLogDaoImpl OrderExportLogDaoImpl;

	@Transactional
	public Response PreSystemConfigPage() {
		Response response = new Response();
		
		SystemConfig systemConfig = systemConfigDaoImpl.getSystemConfig();
		response.setReturnValue(systemConfig);
		return response;
	}

	public Response updateSystemConfig(SystemConfig systemConfig, String userName) {
		Response response = new Response();
		
		SystemConfig systemConfigOrigin = systemConfigDaoImpl.getSystemConfig();
		if (systemConfigOrigin == null){
			systemConfig.setId(SystemConfig.DEFAULT_KEY);
			systemConfigDaoImpl.merge(systemConfig);
			response.setMessage("成功更新配置信息");
		} else {
			String newOrderIdentity = systemConfig.getOrderIdentity();
			
			response.setMessage("成功更新配置信息");
			if (!systemConfigOrigin.getOrderIdentity().equals(newOrderIdentity)){
				if (OrderExportLogDaoImpl.hasExportHistory(newOrderIdentity)){
					response.setReturnCode(Response.WARNING);
					response.setMessage("更新成功.有个警告,当前订货会代码 " + newOrderIdentity + " 已经在以前使用过,请确认");
				}
			}
			systemConfigDaoImpl.merge(systemConfig);
		}
		
		return response;
	}

	/**
	 * 删除当前客户订单数据
	 * 1. CustOrderProduct
	 * @return
	 */
	@Transactional
	public Response deleteCurrentOrderData() {
		Response response = new Response();
		
//		SystemConfig systemConfig = systemConfigDaoImpl.getSystemConfig();
//		
		String hql_delete_CustOrderProduct = "DELETE FROM CustOrderProduct";
//		if (systemConfig != null && !systemConfig.getOrderIdentity().equals("")){
//			hql_delete_CustOrderProduct += " WHERE orderIdentity = '" + systemConfig.getOrderIdentity() + "'"; 
//		}
		
		int numOfRow = custOrderProductDaoImpl.executeHQLUpdateDelete(hql_delete_CustOrderProduct, null, false);
		
		response.setSuccess("成功删除 " + numOfRow + " 条数据");
		
		return response;
	}

}
