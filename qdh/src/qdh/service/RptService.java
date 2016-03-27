package qdh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.dao.config.EntityConfig;
import qdh.dao.entity.VO.CurrentBrandVO;
import qdh.dao.entity.VO.HQCustRptVO;
import qdh.dao.entity.VO.HQProdRptVO;
import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.dao.entity.product.ProductBarcode;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CurrentBrandsDaoImpl;
import qdh.dao.impl.order.CustOrderProdDaoImpl;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.dao.impl.product.ProductBarcodeDaoImpl;
import qdh.pageModel.DataGrid;
import qdh.pageModel.PageHelper;
import qdh.utility.loggerLocal;

@Service
public class RptService {

	@Autowired
	private CustOrderProdDaoImpl custOrderProdDaoImpl;
	
	@Autowired
	private ProductBarcodeDaoImpl productBarcodeDaoImpl;
	
	@Autowired
	private CustomerDaoImpl customerDaoImpl;
	
	@Autowired
	private CurrentBrandsDaoImpl currentBrandsDaoImpl;
	
	public DataGrid generateHQProdRpt(Integer page, Integer rowsPerPage, String sort, String order) {
		DataGrid dataGrid = new DataGrid();
		
		Object[] values = new Object[]{EntityConfig.DELETED};
		String countCriteria = "SELECT COUNT(DISTINCT productBarcode.id) FROM CustOrderProduct WHERE status != ?";
		String rptCriteria = "SELECT productBarcode.id, SUM(quantity) FROM CustOrderProduct WHERE status != ? GROUP BY productBarcode.id ORDER BY SUM(quantity) DESC";
		
		int totalRow = custOrderProdDaoImpl.executeHQLCount(countCriteria, values, false);
		PageHelper pHelper = new PageHelper(rowsPerPage, page, totalRow);
		dataGrid.setTotal(new Long(totalRow));
		
		List<Object> data = custOrderProdDaoImpl.executeHQLSelect(rptCriteria, values, pHelper.getPager(), false);
	     if (data != null && data.size() > 0){	
	    	 
	    	List<HQProdRptVO> rpts = new ArrayList<HQProdRptVO>();
			for (Object object : data)
			  if (object != null){
				Object[] recordResult = (Object[])object;
				if (recordResult[0] == null || recordResult[1] == null)
					continue;
				Integer productId = (Integer)recordResult[0];
				Long quantity =  (Long)recordResult[1];
				
				ProductBarcode pBarcode = productBarcodeDaoImpl.get(productId, true);
				if (pBarcode == null){
					loggerLocal.error("产品信息不存在 : " + productId);
					continue;
				}
				HQProdRptVO rpt = new HQProdRptVO(pBarcode, quantity.intValue());
				rpts.add(rpt);
			  } 
			dataGrid.setRows(rpts);
	     }
		return dataGrid;
	}

	@Transactional
	public DataGrid generateHQCustRpt(Integer page, Integer rows, String sort, String order) {
		DataGrid dataGrid = new DataGrid();
		
		Object[] values = new Object[]{EntityConfig.DELETED};
		String countCriteria = "SELECT COUNT(DISTINCT custId) FROM CustOrderProduct WHERE status != ?";
		String rptCriteria = "SELECT custId, SUM(quantity), SUM(sumWholePrice) FROM CustOrderProduct WHERE status != ? GROUP BY custId ORDER BY SUM(quantity) DESC";
		
		int totalRow = custOrderProdDaoImpl.executeHQLCount(countCriteria, values, false);
		PageHelper pHelper = new PageHelper(rows, page, totalRow);
		dataGrid.setTotal(new Long(totalRow));
		
		List<Object> data = custOrderProdDaoImpl.executeHQLSelect(rptCriteria, values, pHelper.getPager(), false);
	     if (data != null && data.size() > 0){	
	    	 
	    	List<HQCustRptVO> rpts = new ArrayList<HQCustRptVO>();
			for (Object object : data)
			  if (object != null){
				Object[] recordResult = (Object[])object;
				if (recordResult[0] == null || recordResult[1] == null)
					continue;
				Integer custId = (Integer)recordResult[0];
				Long quantity =  (Long)recordResult[1];
				Double sumWholePrice =  (Double)recordResult[2];
				
				Customer customer = customerDaoImpl.get(custId, true);
				HQCustRptVO rpt = null;
				 try {
					 rpt = new HQCustRptVO(customer, quantity.intValue(), sumWholePrice);
				 } catch (Exception e){
					 loggerLocal.error(e);
					 continue;
				 }
				rpts.add(rpt);
			  } 
			dataGrid.setRows(rpts);
	     }
		return dataGrid;
	}

	public Response preFactoryExportPage() {
		Response response = new Response();
		Map dataMap = new HashMap();
		
		List<CurrentBrandVO> brands = ProdOperationService.transferCB(currentBrandsDaoImpl.getAll(true));
		CurrentBrandVO emptyVo = new CurrentBrandVO();
		brands.add(0, emptyVo);
		
		dataMap.put("cbId", 0);
		dataMap.put("cb", brands);

		response.setReturnValue(dataMap);
		return response;
	}

	@Transactional
	public Response getCustOrderProducts(Integer cbId) {
		Response response = new Response();
		
		CurrentBrands currentBrands = currentBrandsDaoImpl.get(cbId, true);
		
		if (currentBrands != null){
			int yearId = currentBrands.getYear().getYear_ID();
			int quarterId = currentBrands.getQuarter().getQuarter_ID();
			int brandId = currentBrands.getBrand().getBrand_ID();
			
			DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
			
			DetachedCriteria pbCriteria = criteria.createAlias("productBarcode","pb");
			pbCriteria.add(Restrictions.ne("pb.id", -1));
//			DetachedCriteria pCriteria = pbCriteria.createCriteria("product");
//			pCriteria.add(Restrictions.eq("year.year_ID", yearId));
//			pCriteria.add(Restrictions.eq("quarter.quarter_ID", quarterId));
//			pCriteria.add(Restrictions.eq("brand.brand_ID", brandId));
//			criteria.add(Restrictions.ne("status", EntityConfig.DELETED));
//			pCriteria.addOrder(Order.asc("productCode"));
			
			List<CustOrderProduct> products = custOrderProdDaoImpl.getByCritera(criteria, true);
			
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("data", products);
			response.setReturnValue(dataMap);
		} else 
			response.setFail("无法找到当前品牌 " + cbId);
		
		return response;
	}

}
