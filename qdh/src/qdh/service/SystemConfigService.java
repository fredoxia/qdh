package qdh.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.ProductCategoryInSystem;
import qdh.dao.entity.product.Brand;
import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.Quarter;
import qdh.dao.entity.product.Year;
import qdh.dao.entity.systemConfig.SystemConfig;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CurrentBrandsDaoImpl;
import qdh.dao.impl.order.CustOrderProdDaoImpl;
import qdh.dao.impl.order.ProductCategoryInSystemDaoImpl;
import qdh.dao.impl.product.BrandDaoImpl;
import qdh.dao.impl.product.ProductBarcodeDaoImpl;
import qdh.dao.impl.product.ProductDaoImpl;
import qdh.dao.impl.product.QuarterDaoImpl;
import qdh.dao.impl.product.YearDaoImpl;
import qdh.dao.impl.systemConfig.OrderExportLogDaoImpl;
import qdh.dao.impl.systemConfig.SystemConfigDaoImpl;

@Service
public class SystemConfigService {
	
	@Autowired
	private SystemConfigDaoImpl systemConfigDaoImpl;
	
	@Autowired
	private CustOrderProdDaoImpl custOrderProductDaoImpl;
	
	@Autowired
	private OrderExportLogDaoImpl OrderExportLogDaoImpl;
	
	@Autowired
	private ProductBarcodeDaoImpl productBarcodeDaoImpl;
	@Autowired
	private ProductDaoImpl productDaoImpl;
	
	@Autowired
	private YearDaoImpl yearDaoImpl;
	
	@Autowired
	private QuarterDaoImpl quarterDaoImpl;
	
	@Autowired
	private BrandDaoImpl brandDaoImpl;
	
	@Autowired
	private ProductCategoryInSystemDaoImpl productCategoryInSystemDaoImpl;
	
	@Autowired
	private CurrentBrandsDaoImpl CurrentBrandsDaoImpl;

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

	@Transactional
	public Response deleteProdData() {
		Response response = new Response();

		String hql = "DELETE FROM ProductBarcode";
		String hql2 = "DELETE FROM Product";
		String hql3 = "DELETE FROM Brand";
		String hql4 = "DELETE FROM Quarter";
		String hql5 = "DELETE FROM Year";
		String hql6 = "DELETE FROM ProductCategoryInSystem";
		String hql7 = "DELETE FROM CurrentBrands";
		
		try {
			productBarcodeDaoImpl.executeHQLUpdateDelete(hql, null, true);
			
			productDaoImpl.executeHQLUpdateDelete(hql2, null, true);
			
			brandDaoImpl.executeHQLUpdateDelete(hql3, null, true);
			
			yearDaoImpl.executeHQLUpdateDelete(hql4, null, true);
			
			quarterDaoImpl.executeHQLUpdateDelete(hql5, null, true);
			
			productCategoryInSystemDaoImpl.executeHQLUpdateDelete(hql6, null, true);
			
			CurrentBrandsDaoImpl.executeHQLUpdateDelete(hql7, null, true);
			response.setSuccess("成功删除数据");
		} catch (Exception e){
			response.setFail("删除数据失败 : " + e.getMessage());
		}
		
		return response;
	}

	@Transactional
	public Response updateCurrentBrands() {
		Response response = new Response();
		
		Set<String> currentBrandsString = new HashSet<>();
		Set<Integer> categoryIds = new HashSet<>();
		
		try {
			List<Product> products = productDaoImpl.getAll(true);
			if (products == null || products.size() == 0){
				response.setReturnCode(Response.WARNING);
				response.setMessage("目前系统中没有条码,请录入条码后更新");
				return response;
			}
			
			
			for (Product product : products){
				int yearId = product.getYear().getYear_ID();
				int quarterId = product.getQuarter().getQuarter_ID();
			    int brandId = product.getBrand().getBrand_ID();
			    int categoryId = product.getCategory().getCategory_ID();
			    
			    currentBrandsString.add(yearId +"," +quarterId +"," + brandId);
			    categoryIds.add(categoryId);
			}
			
			for (String currentBrand: currentBrandsString){
				String[] currentBrandArray = currentBrand.split(",");
				int yearId = Integer.parseInt(currentBrandArray[0]);
				int quarterId = Integer.parseInt(currentBrandArray[1]);
			    int brandId = Integer.parseInt(currentBrandArray[2]);
			    
			    CurrentBrands currentBrands = new CurrentBrands(yearId, quarterId, brandId, 0, "");
			    CurrentBrandsDaoImpl.saveOrUpdate(currentBrands, true);
			}
			
			for (Integer categoryId : categoryIds){
				ProductCategoryInSystem productCategoryInSystem = new ProductCategoryInSystem(categoryId);
				productCategoryInSystemDaoImpl.saveOrUpdate(productCategoryInSystem, true);
			}
			
			response.setSuccess("成功更新");
		} catch (Exception e){
			response.setFail("更新失败 : " + e.getMessage());
		}
		return response;
	}

}
