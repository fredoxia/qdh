package qdh.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.comparator.CustSummaryDataVOComparatorBySum;
import qdh.comparator.HQProdRptVOComparatorByProductCode;
import qdh.dao.config.EntityConfig;
import qdh.dao.entity.VO.CurrentBrandVO;
import qdh.dao.entity.VO.CustOrderProductVO;
import qdh.dao.entity.VO.CustSummaryDataVO;
import qdh.dao.entity.VO.HQCustRptVO;
import qdh.dao.entity.VO.HQProdRptVO;
import qdh.dao.entity.VO.MobileProdRptVO;
import qdh.dao.entity.VO.RptSummaryHeaderVO;
import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.dao.entity.product.Brand;
import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;
import qdh.dao.entity.systemConfig.OrderExportLog;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CurrentBrandsDaoImpl;
import qdh.dao.impl.order.CustOrderProdDaoImpl;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.dao.impl.product.ProductBarcodeDaoImpl;
import qdh.dao.impl.systemConfig.OrderExportLogDaoImpl;
import qdh.dao.impl.systemConfig.SystemConfigDaoImpl;
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
	
	@Autowired
	private OrderExportLogDaoImpl orderExportLogDaoImpl;
	
	@Autowired
	private SystemConfigDaoImpl systemConfigDaoImpl;
	
	public DataGrid generateHQProdRpt(Integer currentBrandId, Integer page, Integer rowsPerPage, String sort, String order) {
		DataGrid dataGrid = new DataGrid();
		
		//1. 限制产品信息
		String pbConstraints = "";
		if (currentBrandId != null && currentBrandId != 0){
			CurrentBrands currentBrands = currentBrandsDaoImpl.get(currentBrandId, true);
			
			if (currentBrands != null){
				int yearId = currentBrands.getYear().getYear_ID();
				int quarterId = currentBrands.getQuarter().getQuarter_ID();
				int brandId = currentBrands.getBrand().getBrand_ID();
				
				//1. find all products barcode
				Set<Integer> barcodeIds = productBarcodeDaoImpl.getIds(yearId, quarterId, brandId);
				if (barcodeIds.size() > 0){
					Iterator<Integer> barIterator = barcodeIds.iterator();
					while (barIterator.hasNext()){
						pbConstraints += barIterator.next() + ",";
					}
					
					pbConstraints = pbConstraints.substring(0, pbConstraints.lastIndexOf(","));
					
					pbConstraints = " AND productBarcode.id IN (" + pbConstraints + ") ";
				}
			}
		}
		
		Object[] values = new Object[]{EntityConfig.INACTIVE};
		String countCriteria = "SELECT COUNT(DISTINCT productBarcode.id) FROM CustOrderProduct WHERE status != ?" + pbConstraints;
		String rptCriteria = "SELECT productBarcode.id, SUM(quantity) FROM CustOrderProduct WHERE status != ?" + pbConstraints +" GROUP BY productBarcode.id ORDER BY SUM(quantity) DESC";
		
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
		
		Object[] values = new Object[]{EntityConfig.INACTIVE};
		String countCriteria = "SELECT COUNT(DISTINCT custId) FROM CustOrderProduct WHERE status != ?";
		String rptCriteria = "SELECT custId, SUM(quantity), SUM(sumRetailPrice) FROM CustOrderProduct WHERE status != ? GROUP BY custId ORDER BY SUM(quantity) DESC";
		
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
				Double sumRetailPrice =  (Double)recordResult[2];
				
				Customer customer = customerDaoImpl.get(custId, true);
				HQCustRptVO rpt = null;
				 try {
					 rpt = new HQCustRptVO(customer, quantity.intValue(), sumRetailPrice);
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
			
			
			//1. find all products barcode
			Set<Integer> barcodeIds = productBarcodeDaoImpl.getIds(yearId, quarterId, brandId);

			//2. find all records
			DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
			criteria.add(Restrictions.ne("status", EntityConfig.INACTIVE));
			criteria.add(Restrictions.in("productBarcode.id", barcodeIds));
			
			List<CustOrderProduct> products = custOrderProdDaoImpl.getByCritera(criteria, true);
			
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("data", products);
			dataMap.put("brand", currentBrands);
			response.setReturnValue(dataMap);
		} else 
			response.setFail("无法找到当前品牌 " + cbId);
		
		return response;
	}
	
	@Transactional
	public Response getFactoryOrder(Integer currentBrandId) {
		Response response = new Response();
		CurrentBrands currentBrands = null;
		Map<String, Object> dataMap = new HashMap<>();
		
		//1. 限制产品信息
		String pbConstraints = "";
		if (currentBrandId != null && currentBrandId != 0){
			currentBrands = currentBrandsDaoImpl.get(currentBrandId, true);
			dataMap.put("brand", currentBrands);
			
			if (currentBrands != null){
				int yearId = currentBrands.getYear().getYear_ID();
				int quarterId = currentBrands.getQuarter().getQuarter_ID();
				int brandId = currentBrands.getBrand().getBrand_ID();
				
				//1. find all products barcode
				Set<Integer> barcodeIds = productBarcodeDaoImpl.getIds(yearId, quarterId, brandId);
				if (barcodeIds.size() > 0){
					Iterator<Integer> barIterator = barcodeIds.iterator();
					while (barIterator.hasNext()){
						pbConstraints += barIterator.next() + ",";
					}
					
					pbConstraints = pbConstraints.substring(0, pbConstraints.lastIndexOf(","));
					
					pbConstraints = " AND productBarcode.id IN (" + pbConstraints + ") ";
				}
			}
		}
		
		Object[] values = new Object[]{EntityConfig.INACTIVE};
		String rptCriteria = "SELECT productBarcode.id, SUM(quantity) FROM CustOrderProduct WHERE status != ?" + pbConstraints +" GROUP BY productBarcode.id";
		
		List<HQProdRptVO> rpts = new ArrayList<HQProdRptVO>();
		List<Object> data = custOrderProdDaoImpl.executeHQLSelect(rptCriteria, values, null, false);
	     if (data != null && data.size() > 0){	
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

			Collections.sort(rpts, new HQProdRptVOComparatorByProductCode());	
	     }
	     
	     dataMap.put("data", rpts);
		
	     response.setReturnValue(dataMap); 
		return response;
	}

	@Transactional
	public Response preGenHQProdRpt() {
		Response response = new Response();
		Map dataMap = new HashMap();
		
		List<CurrentBrandVO> brands = ProdOperationService.transferCB(currentBrandsDaoImpl.getAll(true));
		CurrentBrandVO emptyVo = new CurrentBrandVO();
		emptyVo.setFullName("全部");
		brands.add(0, emptyVo);
		
		dataMap.put("cbId", 0);
		dataMap.put("cb", brands);

		response.setReturnValue(dataMap);
		return response;
	}

	/**
	 * 所有连锁店订货产品排名
	 * @param cb
	 * @param custId
	 * @return
	 */
	@Transactional
	public Response generateMobileProdRpt(CurrentBrands cb, Integer custId) {
		Response response = new Response();
		Map dataMap = new HashMap();
		
		//1. 页面下拉菜单
		List<CurrentBrandVO> brands = ProdOperationService.transferCB(currentBrandsDaoImpl.getAll(true));
		CurrentBrandVO emptyVo = new CurrentBrandVO();
		emptyVo.setFullName("所有品牌");
		brands.add(0, emptyVo);
	
		dataMap.put("currentBrand", cb);
		dataMap.put("cb", brands);
		
		//1. 限制产品信息
		String pbConstraints = "";
		if (cb != null && cb.getId() != 0){
			CurrentBrands currentBrands = currentBrandsDaoImpl.get(cb.getId(), true);
			
			if (currentBrands != null){
				int yearId = currentBrands.getYear().getYear_ID();
				int quarterId = currentBrands.getQuarter().getQuarter_ID();
				int brandId = currentBrands.getBrand().getBrand_ID();
				
				//1. find all products barcode
				Set<Integer> barcodeIds = productBarcodeDaoImpl.getIds(yearId, quarterId, brandId);
				if (barcodeIds.size() > 0){
					Iterator<Integer> barIterator = barcodeIds.iterator();
					while (barIterator.hasNext()){
						pbConstraints += barIterator.next() + ",";
					}
					
					pbConstraints = pbConstraints.substring(0, pbConstraints.lastIndexOf(","));
					
					pbConstraints = " AND productBarcode.id IN (" + pbConstraints + ") ";
				}
			}
		}
		
		Object[] values = new Object[]{EntityConfig.INACTIVE};
		String rptCriteria = "SELECT productBarcode.id, SUM(quantity) FROM CustOrderProduct WHERE status != ?" + pbConstraints +" GROUP BY productBarcode.id ORDER BY SUM(quantity) DESC";
		
		Integer[] pager = new Integer[]{0,30};
		
		List<Object> data = custOrderProdDaoImpl.executeHQLSelect(rptCriteria, values, pager, false);
	    if (data != null && data.size() > 0){	
	    	 
	    	List<MobileProdRptVO> rpts = new ArrayList<MobileProdRptVO>();
			for (int i = 0; i < data.size(); i++){
				  Object object = data.get(i);
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
						
						CustOrderProduct custOrderProduct = custOrderProdDaoImpl.getByPk(custId, pBarcode.getId());
						int myQ = 0;
						if (custOrderProduct != null)
							myQ = custOrderProduct.getQuantity();
						
						MobileProdRptVO rpt = new MobileProdRptVO(pBarcode, quantity.intValue(), myQ, i+1);
						rpts.add(rpt);
				  } 
			}
			dataMap.put("barcodeRank",rpts);
	     }
	    
	    response.setReturnValue(dataMap);
		return response;
	}

	/**
	 * 连锁店自己订货的情况
	 * @param userId
	 * @param cb
	 * @return
	 */
	public Response generateMobileCustRpt(int userId, CurrentBrands cb) {
		Response response = new Response();

		Map dataMap = new HashMap();
		
		//1. 页面下拉菜单
		List<CurrentBrandVO> brands = ProdOperationService.transferCB(currentBrandsDaoImpl.getAll(true));
		CurrentBrandVO emptyVo = new CurrentBrandVO();
		emptyVo.setFullName("所有品牌");
		brands.add(0, emptyVo);
	
		dataMap.put("cb", brands);
		if (cb == null || cb.getId() == 0)
		    dataMap.put("currentBrand", emptyVo);
		else
			dataMap.put("currentBrand", cb);
		
//		DetachedCriteria countCriteria = buildMobileCustRptCriteria(cb);
//		countCriteria.setProjection(Projections.rowCount());
//
//		List countObj = custOrderProdDaoImpl.getByCritera(countCriteria, true);
//		int totalRow = 0;
//		if (countObj != null && countObj.size() > 0){
//			totalRow = (Integer)countObj.get(0);
//		}
		List<CustOrderProductVO> footer = new ArrayList<>();
		List<CustOrderProductVO> corpVos  = new ArrayList<CustOrderProductVO>();
		if (cb.getId() == 0){
			
			List<CurrentBrands> allBrands = currentBrandsDaoImpl.getAll(true);
			int totalQ = 0;
			int totalSumRetail = 0;
			
			for (CurrentBrands currentBrand: allBrands){
				int quantity = 0;
				int sumRetail = 0;
				
				int yearId = currentBrand.getYear().getYear_ID();
				int quarterId = currentBrand.getQuarter().getQuarter_ID();
				int brandId = currentBrand.getBrand().getBrand_ID();
				
				//1. find all products barcode
				StringBuffer pbConstraints = new StringBuffer("");
				String pbConstraintsString = "";
				Set<Integer> barcodeIds = productBarcodeDaoImpl.getIds(yearId, quarterId, brandId);
				if (barcodeIds.size() > 0){
					Iterator<Integer> barIterator = barcodeIds.iterator();
					while (barIterator.hasNext()){
						pbConstraints.append(barIterator.next() + ",");
					}
					
					pbConstraintsString = (pbConstraints.toString()).substring(0, pbConstraints.lastIndexOf(","));
					
					pbConstraintsString = " AND productBarcode.id IN (" + pbConstraintsString + ") ";
				}

				Object[] values = new Object[]{userId, EntityConfig.INACTIVE};
				String rptCriteria = "SELECT SUM(quantity), SUM(sumRetailPrice) FROM CustOrderProduct WHERE custId =? AND status != ?" + pbConstraintsString;
				
				List<Object> data = custOrderProdDaoImpl.executeHQLSelect(rptCriteria, values, null, false);
			    if (data != null && data.size() > 0){	
			    	 for (int i = 0; i < data.size(); i++){
						  Object object = data.get(i);
						  if (object != null){
								Object[] recordResult = (Object[])object;
								if (recordResult[0] == null || recordResult[1] == null)
									continue;
								quantity = ((Long)recordResult[0]).intValue();
								sumRetail =  ((Double)recordResult[1]).intValue();
						  }
			    	 }
			    }
			    
			    CustOrderProductVO custVo = new CustOrderProductVO();
			    custVo.setBrand(currentBrand.getBrand().getBrand_Name());
			    custVo.setCopId(currentBrand.getId());
			    custVo.setProductCode(currentBrand.getYear().getYear() + currentBrand.getQuarter().getQuarter_Name());
			    custVo.setQuantity(quantity);
			    custVo.setSumRetailPrice(sumRetail);
			    corpVos.add(custVo);
			    
			    totalQ += quantity;
			    totalSumRetail += sumRetail;
			}
			
			CustOrderProductVO footerVo = new CustOrderProductVO();
			footerVo.setQuantity(totalQ);
			footerVo.setSumRetailPrice(totalSumRetail);
			footerVo.setLastUpdateTime(null);
			footerVo.setProductCode("总计");
			footer.add(footerVo);
		} else {
			DetachedCriteria resultCriteria = buildMobileCustRptCriteria(cb,userId);
			List<CustOrderProduct> products = custOrderProdDaoImpl.getByCritera(resultCriteria, true);

			corpVos = CustAcctService.transferCustOrderProductVOs(products, footer);
		}

		dataMap.put("cops", corpVos);
		dataMap.put("copsFooter", footer.get(0));
	    response.setReturnValue(dataMap);

		return response;
	}
	
	private DetachedCriteria buildMobileCustRptCriteria(CurrentBrands cb, Integer custId){
		//1. 限制产品信息
		Set<Integer> barcodeIds = null;
		int currentBrandId = cb.getId();
		CurrentBrands currentBrands = currentBrandsDaoImpl.get(currentBrandId, true);
		
		if (currentBrands != null){
			int yearId = currentBrands.getYear().getYear_ID();
			int quarterId = currentBrands.getQuarter().getQuarter_ID();
			int brandId = currentBrands.getBrand().getBrand_ID();
			
			//1. find all products barcode
			barcodeIds = productBarcodeDaoImpl.getIds(yearId, quarterId, brandId);
		}

		
		DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
		criteria.add(Restrictions.ne("status", EntityConfig.INACTIVE));
		criteria.add(Restrictions.eq("custId", custId));
		if (barcodeIds != null)
			criteria.add(Restrictions.in("productBarcode.id", barcodeIds));
		
		return criteria;
	}

	public DataGrid getHQOrderExportLog() {
		DataGrid dataGrid = new DataGrid();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OrderExportLog.class);
		criteria.addOrder(Order.desc("importTime"));
		
		List<OrderExportLog> logs = orderExportLogDaoImpl.getByCritera(criteria, 0, 10, true);
		
		dataGrid.setRows(logs);
	 
		return dataGrid;
	}

	/**
	 * 为summary report提供Header
	 * @return
	 */
	@Transactional
	public Response prepareHQCustSummaryRpt() {
		Response response = new Response();
		Map<String, Object> objects = new HashMap<>();
		
		/**
		 * 1. 牌子名字
		 */
		DetachedCriteria cbCriteria = DetachedCriteria.forClass(CurrentBrands.class);
		cbCriteria.addOrder(Order.asc("id"));
		
		List<CurrentBrands> currentBrands = currentBrandsDaoImpl.getByCritera(cbCriteria, true);
		List<RptSummaryHeaderVO> brands = new ArrayList<>();
		List<Integer> cbrandIds = new ArrayList<>();
		for (CurrentBrands brands2 : currentBrands){
			String header = brands2.getBrand().getBrand_Name() + " " + brands2.getQuarter().getQuarter_Name();
			
			RptSummaryHeaderVO summaryHeaderVO = new RptSummaryHeaderVO();
			summaryHeaderVO.setCbId(brands2.getId());
			summaryHeaderVO.setBrandHeader(header);
			
			brands.add(summaryHeaderVO);
			cbrandIds.add(brands2.getId());
		}
		objects.put("brands", brands);
		
		/**
		 * 2. 数据信息
		 */
		String orderIdentity = systemConfigDaoImpl.getOrderIdentity();
		DetachedCriteria copCriteria = DetachedCriteria.forClass(CustOrderProduct.class);
		cbCriteria.add(Restrictions.eq("orderIdentity", orderIdentity));
		List<CustOrderProduct> allRecords = custOrderProdDaoImpl.getByCritera(copCriteria, false);
		
		/**
		 * 处理 map数据 
		 * key : custId#brandId
		 * Value: quantity
		 */
		Map<String, Integer> dataMap = new HashMap<>();
		Set<Integer> custIds = new HashSet<>();
		
		/**
		 * 单项汇总数据
		 * key : brandId
		 * value :sumQ
		 */
		Map<Integer, Integer> brandSumMap = new HashMap<>();
		
		for (CustOrderProduct cop : allRecords){
			int custId = cop.getCustId();
			custIds.add(custId);
			
			Product product = cop.getProductBarcode().getProduct();
			int yearId = product.getYear().getYear_ID();
			int quarterId = product.getQuarter().getQuarter_ID();
			int brandId = product.getBrand().getBrand_ID();
			
			CurrentBrands cb = currentBrandsDaoImpl.getByKey(yearId, quarterId, brandId);
			int cbId = cb.getId();
			
			int q = cop.getQuantity();
			String key = custId + "#" + cbId;
			
			Integer qSum = dataMap.get(key);
			if (qSum == null)
				qSum = 0;
			dataMap.put(key, qSum + q);
			
			Integer brandSum = brandSumMap.get(cbId);
			if (brandSum == null)
				brandSum = 0;
			brandSumMap.put(cbId, brandSum + q);
		}
		
		List<CustSummaryDataVO> summaryDataVOs = new ArrayList<>();
		for (Integer custId : custIds){
			int subTotal = 0;
			CustSummaryDataVO summaryDataVO = new CustSummaryDataVO();
			summaryDataVO.setCustName(customerDaoImpl.get(custId, true).getCustName());
			
			List<Integer> qList = new ArrayList<>();
			for (Integer cbId : cbrandIds){
				String key = custId + "#" + cbId;
				
				Integer qSum = dataMap.get(key);
				if (qSum == null)
					qSum = 0;
				
				subTotal += qSum;
				qList.add(qSum);
			}
			
			summaryDataVO.setOrderQ(qList);
			summaryDataVO.setSumQ(subTotal);
			summaryDataVOs.add(summaryDataVO);
		}
		
		//制造sum 数据行
		CustSummaryDataVO subTotalVO = new CustSummaryDataVO();
		subTotalVO.setCustName("** 所有客户 **");
		List<Integer> qList = new ArrayList<>();
		int total = 0 ;
		for (Integer custId : cbrandIds){
			Integer qSum = brandSumMap.get(custId);
			if (qSum == null)
				qSum = 0;
			
			total += qSum;
			qList.add(qSum);
		}
		subTotalVO.setOrderQ(qList);
		subTotalVO.setSumQ(total);
		summaryDataVOs.add(subTotalVO);
		
		Collections.sort(summaryDataVOs, new CustSummaryDataVOComparatorBySum());
		objects.put("records", summaryDataVOs);
		
		response.setReturnValue(objects);
		return response;
	}



}
