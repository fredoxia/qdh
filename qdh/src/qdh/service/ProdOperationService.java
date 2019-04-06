package qdh.service;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import qdh.controller.formBean.ProductBasicFormBean;
import qdh.dao.config.EntityConfig;
import qdh.dao.entity.VO.CurrentBrandVO;
import qdh.dao.entity.VO.ProductBarcodeVO;
import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.dao.entity.order.ProductCategoryInSystem;
import qdh.dao.entity.product.Area;
import qdh.dao.entity.product.BarcodeImportTemplate;
import qdh.dao.entity.product.Brand;
import qdh.dao.entity.product.Category;
import qdh.dao.entity.product.Color;
import qdh.dao.entity.product.NumPerHand;
import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;
import qdh.dao.entity.product.ProductUnit;
import qdh.dao.entity.product.Quarter;
import qdh.dao.entity.product.Size;
import qdh.dao.entity.product.Year;
import qdh.dao.entity.qxMIS.Brand2;
import qdh.dao.entity.qxMIS.ProductBarcode2;
import qdh.dao.entity.qxMIS.Quarter2;
import qdh.dao.entity.qxMIS.Year2;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CurrentBrandsDaoImpl;
import qdh.dao.impl.order.CustOrderProdDaoImpl;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.dao.impl.order.ProductCategoryInSystemDaoImpl;
import qdh.dao.impl.product.AreaDaoImpl;
import qdh.dao.impl.product.BrandDaoImpl;
import qdh.dao.impl.product.CategoryDaoImpl;
import qdh.dao.impl.product.ColorDaoImpl;
import qdh.dao.impl.product.NumPerHandDaoImpl;
import qdh.dao.impl.product.ProductBarcodeDaoImpl;
import qdh.dao.impl.product.ProductDaoImpl;
import qdh.dao.impl.product.ProductUnitDaoImpl;
import qdh.dao.impl.product.QuarterDaoImpl;
import qdh.dao.impl.product.SizeDaoImpl;
import qdh.dao.impl.product.YearDaoImpl;
import qdh.dao.impl.qxMIS.Brand2DaoImpl;
import qdh.dao.impl.qxMIS.ProductBarcode2DaoImpl;
import qdh.dao.impl.qxMIS.Quarter2DaoImpl;
import qdh.dao.impl.qxMIS.Year2DaoImpl;
import qdh.dao.impl.systemConfig.SystemConfigDaoImpl;
import qdh.pageModel.DataGrid;
import qdh.pageModel.PageHelper;
import qdh.utility.DateUtility;
import qdh.utility.NumUtility;
import qdh.utility.StringUtility;

@Service
public class ProdOperationService {
	@Autowired
	private ProductCategoryInSystemDaoImpl ProductCategoryInSystemDaoImpl;
	
	@Autowired
	private CurrentBrandsDaoImpl currentBrandsDaoImpl;
	
	@Autowired
	private ProductBarcodeDaoImpl productBarcodeDaoImpl;
	
	@Autowired
	private YearDaoImpl yearDaoImpl;
	
	@Autowired
	private QuarterDaoImpl quarterDaoImpl;
	
	@Autowired
	private BrandDaoImpl brandDaoImpl;
	
	@Autowired
	private AreaDaoImpl areaDaoImpl;
	
	@Autowired
	private CategoryDaoImpl categoryDaoImpl;
	
	@Autowired
	private ProductDaoImpl productDaoImpl;
	
	@Autowired
	private ColorDaoImpl colorDaoImpl;
	
	@Autowired
	private SizeDaoImpl sizeDaoImpl;
	
	@Autowired
	private ProductBarcode2DaoImpl ProductBarcode2DaoImpl;
	
	@Autowired
	private Year2DaoImpl year2DaoImpl;
	
	@Autowired
	private Quarter2DaoImpl quarter2DaoImpl;
	
	@Autowired
	private Brand2DaoImpl brand2DaoImpl;
	
	@Autowired
	private SystemConfigDaoImpl systemConfigDaoImpl;
	
	@Autowired
	private CustOrderProdDaoImpl custOrderProductDaoImpl;
	
	@Autowired
	private CustomerDaoImpl customerDaoImpl;
	
	@Autowired
	private NumPerHandDaoImpl numPerHandDaoImpl;
	
	@Autowired
	private ProductUnitDaoImpl productUnitDaoImpl;
	
	/**
	 * 获取所有的current brands然后组成datagrid
	 * @return
	 */
	public DataGrid getCurrentBrands(){
		DataGrid dg = new DataGrid();
		
		List<CurrentBrands> currentBrands = currentBrandsDaoImpl.getAll(true);
		List<CurrentBrandVO> currentBrandVOs = new ArrayList<>();
		for (CurrentBrands currrentBrand : currentBrands){
			currentBrandVOs.add(new CurrentBrandVO(currrentBrand));
		}
		dg.setRows(currentBrandVOs);
		dg.setTotal(new Long(currentBrandVOs.size()));
		try {
		   refreshProductCategoryInSystem();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return dg;		
	}
	
	/**
	 * 删除某个current brands
	 * @param id
	 * @return
	 */
	@Transactional
	public Response deleteCurrentBrand(int id){
		Response response = new Response();
		
		if (!systemConfigDaoImpl.canUpdateProduct()){
			response.setFail("管理员已经锁定产品资料更新,请联系管理员");
			return response;
		}
		
		CurrentBrands currentBrands = currentBrandsDaoImpl.get(id, true);
		if (currentBrands != null){
			int yearId = currentBrands.getYear().getYear_ID();
			int quarterId = currentBrands.getQuarter().getQuarter_ID();
			int brandId = currentBrands.getBrand().getBrand_ID();
			
			String deletePB = "DELETE FROM ProductBarcode pb WHERE pb.product.productId in (SELECT p.productId FROM Product p WHERE p.year.year_ID =? AND p.quarter.quarter_ID =? AND p.brand.brand_ID=?)";
			Object[] deletePBArg = new Object[]{yearId, quarterId, brandId};
			productBarcodeDaoImpl.executeHQLUpdateDelete(deletePB, deletePBArg, true);
			
			String deleteProducts = "DELETE FROM Product WHERE year.year_ID =? AND quarter.quarter_ID =? AND brand.brand_ID=?";
			Object[] deleteProductArg = new Object[]{yearId, quarterId, brandId};
			productDaoImpl.executeHQLUpdateDelete(deleteProducts, deleteProductArg, true);
			
			currentBrandsDaoImpl.delete(currentBrands, true);
			
			//刷新production category in system
			refreshProductCategoryInSystem();
			
			response.setSuccess("当前季度的产品已经被删除");
		} else {
			response.setSuccess("警告 : 当前季度的产品在操作前已经被删除");
		}
		
		return response;
	}

	/**
	 * 从千禧系统导入产品信息
	 * @param yearId
	 * @param quarterId
	 * @param brandId
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@Transactional
	public Response addCurrentBrand(int yearId, int quarterId, int brandId, String updateUser) throws IllegalAccessException, InvocationTargetException {
		Response response = new Response();
		
		//@1. 先检查是否已经导入过
		CurrentBrands currentBrands = currentBrandsDaoImpl.getByKey(yearId, quarterId, brandId);
		
		if (currentBrands != null){
			response.setFail(currentBrands.getYear().getYear() + "-" + currentBrands.getQuarter().getQuarter_Name() + "-" + currentBrands.getBrand().getBrand_Name() + " 已经导入。请先删除再导入。");
			return response;
		} else if (!systemConfigDaoImpl.canUpdateProduct()){
			response.setFail("管理员已经锁定产品资料更新,请联系管理员");
			return response;
		}
		
		//@2. 如果导入，就导入。
		DetachedCriteria productBarcodeCriteria = DetachedCriteria.forClass(ProductBarcode2.class);
		productBarcodeCriteria.add(Restrictions.isNull("chainId"));
		DetachedCriteria productCriteria = productBarcodeCriteria.createCriteria("product");
		productCriteria.add(Restrictions.eq("year.year_ID", yearId));
		productCriteria.add(Restrictions.eq("quarter.quarter_ID", quarterId));
		productCriteria.add(Restrictions.eq("brand.brand_ID", brandId));
		
		List<ProductBarcode2> barcodes = ProductBarcode2DaoImpl.getByCritera(productBarcodeCriteria, true);
		
		if (barcodes.size() == 0){
			response.setFail("没找到存在的条码");
		} else {
			for (ProductBarcode2 productBarcode : barcodes){
				ProductBarcode pb = new ProductBarcode(productBarcode);
				Product p = pb.getProduct();
			    Area area = p.getArea();
			    Year year = p.getYear();
			    Quarter quarter = p.getQuarter();
			    Brand brand = p.getBrand();
			    Category category = p.getCategory();
			    
			    areaDaoImpl.merge(area);
			    categoryDaoImpl.merge(category);
			    brandDaoImpl.merge(brand);
				quarterDaoImpl.merge(quarter);
				yearDaoImpl.merge(year);
				productDaoImpl.merge(p);
				
				Color color = pb.getColor();
				Size size = pb.getSize();
				
				if (color != null)
					colorDaoImpl.merge(color);
				
				if (size != null)
					sizeDaoImpl.merge(size);
				
			    productBarcodeDaoImpl.merge(pb);
			}
			
			CurrentBrands currentBrands2 = new CurrentBrands(yearId, quarterId, brandId, barcodes.size(), updateUser);
			currentBrandsDaoImpl.saveOrUpdate(currentBrands2, true);

			//刷新production category in system
			refreshProductCategoryInSystem();
		}
		
		return response;
	}

	/**
	 * 准备添加产品信息的功能
	 * @return
	 */
	@Transactional
	public Response preAddCurrentBrand() {
		Response response = new Response();
		Map dataMap = new HashMap();
		
		List<Year2> years = year2DaoImpl.getAllByOrder();
		List<Quarter2> quarters = quarter2DaoImpl.getAll(true);
		List<Brand2> brands = brand2DaoImpl.getAllHq();
		
		dataMap.put("year", years);
		dataMap.put("quarter", quarters);
		dataMap.put("brand", brands);
		
		response.setReturnValue(dataMap);
		
		return response;
	}

	@Transactional
	public Response preBarcodeSearch() {
		Response response = new Response();
		Map dataMap = new HashMap();
		
		List<CurrentBrandVO> brands = transferCB(currentBrandsDaoImpl.getAll(true));
		CurrentBrandVO emptyVo = new CurrentBrandVO();
		brands.add(0, emptyVo);
		
		dataMap.put("cbId", 0);
		dataMap.put("cb", brands);

		response.setReturnValue(dataMap);
		return response;
	}

	@Transactional
	public DataGrid getBarcodes(Integer cbId, String sort, String order, Integer pageNum, Integer rowsPerPage) {
		DataGrid dg = new DataGrid();
		PageHelper pHelper = new PageHelper();
		
		List<ProductBarcodeVO> pbs = new ArrayList<>();
		if (cbId != null && cbId != 0){
			CurrentBrands cb = currentBrandsDaoImpl.get(cbId, true);
			if (cb != null){
				Year year = cb.getYear();
				Quarter quarter = cb.getQuarter();
				Brand brand = cb.getBrand();
				
				DetachedCriteria countCriteria = constructProductBarcodeCriteria(year, quarter, brand, sort, order);
				DetachedCriteria searchCriteria = constructProductBarcodeCriteria(year, quarter, brand, sort, order);

				
				//1. get total rows
				int totalRow = 0;
				countCriteria.setProjection(Projections.rowCount());
				List<Object> totalObj = productBarcodeDaoImpl.getByCriteriaProjection(countCriteria, true);
				totalRow = NumUtility.getProjectionIntegerValue(totalObj);
				

				pHelper = new PageHelper(rowsPerPage, pageNum, totalRow);

				pbs = transferPB(productBarcodeDaoImpl.getByCritera(searchCriteria,pHelper.getFirstRow(), pHelper.getRowPerPage(), true));
			}
		}
		
		dg.setTotal(new Long(pHelper.getTotalRows()));
		dg.setRows(pbs);
		return dg;
	}
	
	private DetachedCriteria constructProductBarcodeCriteria(Year year, Quarter quarter, Brand brand, String sort, String order){
		DetachedCriteria productBarcodeCriteria = DetachedCriteria.forClass(ProductBarcode.class);
		
		if (sort != null && !sort.trim().equals("")){
			if (order != null){
				if (order.equals("asc"))
			        productBarcodeCriteria.addOrder(Order.asc(sort));
				else 
					productBarcodeCriteria.addOrder(Order.desc(sort));
			}
		}
		
		DetachedCriteria productCriteria = productBarcodeCriteria.createCriteria("product");
		productCriteria.add(Restrictions.eq("year.year_ID", year.getYear_ID()));
		productCriteria.add(Restrictions.eq("quarter.quarter_ID", quarter.getQuarter_ID()));
		productCriteria.add(Restrictions.eq("brand.brand_ID", brand.getBrand_ID()));
		return productBarcodeCriteria;
	}
	
	public static List<CurrentBrandVO> transferCB(List<CurrentBrands> currentBrands){
		List<CurrentBrandVO> currentBrandVOs = new ArrayList<>();
		if (currentBrands != null)
			for (CurrentBrands brand: currentBrands){
				CurrentBrandVO vo = new CurrentBrandVO(brand);
				currentBrandVOs.add(vo);
			}
		return currentBrandVOs;
	}
	
	private List<ProductBarcodeVO> transferPB(List<ProductBarcode> productBarcodes){
		List<ProductBarcodeVO> productBarcodeVOs = new ArrayList<>();
		if (productBarcodes != null)
			for (ProductBarcode pb: productBarcodes){
				ProductBarcodeVO vo = new ProductBarcodeVO(pb);
				productBarcodeVOs.add(vo);
			}
		return productBarcodeVOs;
	}

	/**
	 * update某个current brands
	 * @param id
	 * @return
	 */
	@Transactional
	public Response updateCurrentBrand(int id,String updateUser) {
		Response response = new Response();
		
		if (!systemConfigDaoImpl.canUpdateProduct()){
			response.setFail("管理员已经锁定产品资料更新,请联系管理员");
			return response;
		}
		
		CurrentBrands currentBrands = currentBrandsDaoImpl.get(id, true);
		if (currentBrands != null){
			int yearId = currentBrands.getYear().getYear_ID();
			int quarterId = currentBrands.getQuarter().getQuarter_ID();
			int brandId = currentBrands.getBrand().getBrand_ID();
			
			//1. 获取千禧系统的正常条码
			Set<Integer> pbIdInQXMIS = new HashSet<>();
			DetachedCriteria productBarcodeCriteria2 = DetachedCriteria.forClass(ProductBarcode2.class);
			productBarcodeCriteria2.add(Restrictions.isNull("chainId"));
			productBarcodeCriteria2.add(Restrictions.eq("status", ProductBarcode2.STATUS_OK));
			DetachedCriteria productCriteria2 = productBarcodeCriteria2.createCriteria("product");
			productCriteria2.add(Restrictions.eq("year.year_ID", yearId));
			productCriteria2.add(Restrictions.eq("quarter.quarter_ID", quarterId));
			productCriteria2.add(Restrictions.eq("brand.brand_ID", brandId));
			
			List<ProductBarcode2> barcodesInQXMIS = ProductBarcode2DaoImpl.getByCritera(productBarcodeCriteria2, true);
			for (ProductBarcode2 pb : barcodesInQXMIS){
				pbIdInQXMIS.add(pb.getId());
			}
			
			//2. 获取当前系统的条码
			Set<Integer> pbIdInQDH = new HashSet<>();
			DetachedCriteria productBarcodeCriteria = DetachedCriteria.forClass(ProductBarcode.class);
			DetachedCriteria productCriteria = productBarcodeCriteria.createCriteria("product");
			productCriteria.add(Restrictions.eq("year.year_ID", yearId));
			productCriteria.add(Restrictions.eq("quarter.quarter_ID", quarterId));
			productCriteria.add(Restrictions.eq("brand.brand_ID", brandId));
			
			List<ProductBarcode> barcodesInQDH = productBarcodeDaoImpl.getByCritera(productBarcodeCriteria, true);
			for (ProductBarcode pb : barcodesInQDH){
				pbIdInQDH.add(pb.getId());
			}
			
			//3. 获取订单内已经使用的条码
			Set<Integer> pbIdInUse = new HashSet<>();
			DetachedCriteria custOrderProdCriteria = DetachedCriteria.forClass(CustOrderProduct.class);
			custOrderProdCriteria.add(Restrictions.in("productBarcode.id", pbIdInQDH));
			List<CustOrderProduct> custOrderProd = custOrderProductDaoImpl.getByCritera(custOrderProdCriteria, true);
			
			for (CustOrderProduct pb : custOrderProd){
				pbIdInUse.add(pb.getProductBarcode().getId());
			}
			
			//4. 如果有些货品已经订货但是在qxmis端被删除了，需要提醒
			if (!pbIdInQXMIS.containsAll(pbIdInUse)){
				Set<Integer> idDeletedInUse = new HashSet<>();
				for (Integer pbId : pbIdInUse){
					if (!pbIdInQXMIS.contains(pbId)){
						idDeletedInUse.add(pbId);
					}
				}
				
				DetachedCriteria custOrderProdCriteria2 = DetachedCriteria.forClass(CustOrderProduct.class);
				custOrderProdCriteria2.add(Restrictions.in("productBarcode.id", idDeletedInUse));
				List<CustOrderProduct> custOrderProd2 = custOrderProductDaoImpl.getByCritera(custOrderProdCriteria2, true);
				
				String errorMsg = "";
				for (CustOrderProduct orderProduct : custOrderProd2){
					Customer cust = customerDaoImpl.get(orderProduct.getCustId(), true);
					Product p = orderProduct.getProductBarcode().getProduct();
					
					String colorS = "";
					Color color = orderProduct.getProductBarcode().getColor();
					if (color != null){
						colorS = color.getName();
					}
					
					errorMsg += cust.getCustName() + " " + p.getProductCode() + " " + colorS + " " + orderProduct.getQuantity() + "<br/>";
				}
				
				errorMsg = "部分删除的条码已经在客户订单，请客户删除之后再继续更新条码: <br/>" + errorMsg;
				
				response.setFail(errorMsg);
				
				return response;
			} else if (!pbIdInQXMIS.containsAll(pbIdInQDH)){
				for (Integer pbId : pbIdInQDH){
					if (!pbIdInQXMIS.contains(pbId)){
						ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
						productBarcodeDaoImpl.delete(pb, true);
					}
				}
			}
			
			for (ProductBarcode2 productBarcode : barcodesInQXMIS){
				ProductBarcode pb = new ProductBarcode(productBarcode);
				Product p = pb.getProduct();
			    Area area = p.getArea();
			    Year year = p.getYear();
			    Quarter quarter = p.getQuarter();
			    Brand brand = p.getBrand();
			    Category category = p.getCategory();
			    
			    areaDaoImpl.merge(area);
			    categoryDaoImpl.merge(category);
			    brandDaoImpl.merge(brand);
				quarterDaoImpl.merge(quarter);
				yearDaoImpl.merge(year);
				productDaoImpl.merge(p);
				
				Color color = pb.getColor();
				Size size = pb.getSize();
				
				if (color != null)
					colorDaoImpl.merge(color);
				
				if (size != null)
					sizeDaoImpl.merge(size);
				
			    productBarcodeDaoImpl.merge(pb);
			}
			
			CurrentBrands cuBrands = currentBrandsDaoImpl.getByKey(yearId, quarterId, brandId);
			if (cuBrands == null){
			    CurrentBrands currentBrands2 = new CurrentBrands(yearId, quarterId, brandId, barcodesInQXMIS.size(), updateUser);
			    currentBrandsDaoImpl.save(currentBrands2, true);
			} else {
				cuBrands.setNumOfBarcodes(barcodesInQXMIS.size());
				cuBrands.setUpdateUser(updateUser);
				cuBrands.setUpdateDate(DateUtility.getToday());
				currentBrandsDaoImpl.update(cuBrands, true);
			}
			
			response.setSuccess("当前季度的产品已经成功更新");
		} else {
			response.setSuccess("警告 : 当前季度的产品在操作前已经被删除");
		}
		
		return response;
	}
	
	/**
	 * 刷新所有产品在系统里面的种类
	 */
	private void refreshProductCategoryInSystem(){
		ProductCategoryInSystemDaoImpl.deleteAll();
		
		List<Integer> categories = productDaoImpl.getDistinctCategory();
		for (Integer categoryId : categories){
			ProductCategoryInSystem productCategoryInSystem = new ProductCategoryInSystem(categoryId);
			ProductCategoryInSystemDaoImpl.saveOrUpdate(productCategoryInSystem, true);
		}
	}

	/**
	 * 获取基础资料
	 * @param basicData
	 * @param basicDataId
	 * @return
	 */
	public Object getBasicData(String basicData, Integer basicDataId) {
		if (basicDataId != null)
			if (basicData.equalsIgnoreCase("quarter")){
				Quarter quarter = quarterDaoImpl.get(basicDataId, true);
				return quarter;
			} else if (basicData.equalsIgnoreCase("brand")){
				Brand brand = brandDaoImpl.get(basicDataId, true);
				return brand;	
			} else if (basicData.equalsIgnoreCase("category")){
				Category category = categoryDaoImpl.get(basicDataId, true);
				return category;
			} else if (basicData.equalsIgnoreCase("color")){ 
				Color color = colorDaoImpl.get(basicDataId, true);
				return color;
			} else if (basicData.equalsIgnoreCase("year")){ 
				Year year = yearDaoImpl.get(basicDataId, true);
				return year;
			} else if (basicData.equalsIgnoreCase("numPerHand")){
                NumPerHand numPerHand = numPerHandDaoImpl.get(basicDataId, true);
				return numPerHand;	
			} else if (basicData.equalsIgnoreCase("productUnit")){ 
				ProductUnit productUnit = productUnitDaoImpl.get(basicDataId, true);
				return productUnit;
			}
			
		return null;
	}

	/**
	 * 更新brand
	 * @param brand
	 * @return
	 */
	@Transactional
	public Response updateBrand(Brand brand) {
	     Response response = new Response();
		
		try {
			int brandId = brand.getBrand_ID();
			String brandName = brand.getBrand_Name();
			
			DetachedCriteria criteria = DetachedCriteria.forClass(Brand.class);
			criteria.add(Restrictions.eq("brand_Name", brandName));
			
			List<Brand> brands = brandDaoImpl.getByCritera(criteria, true);

			if (brands == null || brands.size() == 0){
				brand.setBrand_Code(String.valueOf(brandId).substring(0,1));
				brand.setPinyin(StringUtility.getPinyinCode(brandName, true));
				brandDaoImpl.saveOrUpdate(brand, true);
				response.setReturnCode(Response.SUCCESS);
			} else {
				Brand brand2 = brands.get(0);
				if (brandId != brand2.getBrand_ID()){
					response.setQuickValue(Response.FAIL, "品牌 重复,请检查重新输入");
				} else {
					brandDaoImpl.evict(brand2);
					brand.setBrand_Code(String.valueOf(brandId).substring(0,1));
					brand.setPinyin(StringUtility.getPinyinCode(brandName, true));
					brandDaoImpl.saveOrUpdate(brand, true);
					response.setReturnCode(Response.SUCCESS);
				}
			}
		} catch (Exception e) {
			response.setFail(e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 更新year
	 * @param year
	 * @return
	 */
	@Transactional
	public Response updateYear(Year year) {
		Response response = new Response();
		
		int yearId = year.getYear_ID();
		String yearS = year.getYear();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Year.class);
		criteria.add(Restrictions.eq("year", yearS));
		List<Year> years = yearDaoImpl.getByCritera(criteria, true);
		
		if (years == null || years.size() == 0){
			year.setYear_Code(yearS.substring(3));
			yearDaoImpl.saveOrUpdate(year, true);
			response.setReturnCode(Response.SUCCESS);
		} else {
			Year year2 = years.get(0);
			if (yearId != year2.getYear_ID()){
				response.setQuickValue(Response.FAIL, "年份 重复,请检查重新输入");
			} else {
				year2.setYear_Code(yearS.substring(3));
				yearDaoImpl.saveOrUpdate(year2, true);
				response.setReturnCode(Response.SUCCESS);
			}
		}
		
		return response;
	}
	
	@Transactional
	public Response updateQuarter(Quarter quarter) {
		Response response = new Response();
		
		int quarterId = quarter.getQuarter_ID();
		String quarterS = quarter.getQuarter_Name();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Quarter.class);
		criteria.add(Restrictions.eq("quarter_Name", quarterS));
		List<Quarter> quarters = quarterDaoImpl.getByCritera(criteria, true);
		
		if (quarters == null || quarters.size() == 0){
			quarter.setQuarter_Code(String.valueOf(quarterId).substring(0,1));
			quarterDaoImpl.saveOrUpdate(quarter, true);
			response.setReturnCode(Response.SUCCESS);
		} else {
			Quarter quarter2 = quarters.get(0);
			if (quarterId != quarter2.getQuarter_ID()){
				response.setQuickValue(Response.FAIL, "季度 重复,请检查重新输入");
			} else {
				quarter.setQuarter_Code(String.valueOf(quarterId).substring(0,1));
				quarterDaoImpl.saveOrUpdate(quarter, true);
				response.setReturnCode(Response.SUCCESS);
			}
		}
		
		return response;
	}

	

	@Transactional
	public Response updateCategory(Category category) {
		Response response = new Response();
		
		int categoryId = category.getCategory_ID();
		String categoryName = category.getCategory_Name();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);
		criteria.add(Restrictions.eq("category_Name", categoryName));
		List<Category> categories = categoryDaoImpl.getByCritera(criteria, true);
		
		if (categories == null || categories.size() == 0){
			category.setCategory_Code(String.valueOf(categoryId).substring(0,1));
			categoryDaoImpl.saveOrUpdate(category, true);
			response.setReturnCode(Response.SUCCESS);
		} else {
			Category category2 = categories.get(0);
			if (categoryId != category2.getCategory_ID() && (category.getChainId() == Category.TYPE_CHAIN || (category.getChainId() == Category.TYPE_HEAD && category2.getChainId() == Category.TYPE_HEAD))){
				response.setQuickValue(Response.FAIL, "类别 重复,请检查重新输入");
			} else {
				categoryDaoImpl.evict(category2);
				categoryDaoImpl.saveOrUpdate(category, true);
				response.setReturnCode(Response.SUCCESS);
			}
		}
		
		return response;
	}

	public Response updateProductUnit(ProductUnit productUnit) {
		Response response = new Response();
		
		int id = productUnit.getId();
		String unit = productUnit.getProductUnit();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ProductUnit.class);
		criteria.add(Restrictions.eq("productUnit", unit));
		List<ProductUnit> units = productUnitDaoImpl.getByCritera(criteria, true);
		
		if (units == null || units.size() == 0){
			productUnitDaoImpl.saveOrUpdate(productUnit, true);
			response.setReturnCode(Response.SUCCESS);
		} else {
			ProductUnit unit2 = units.get(0);
			if (id != unit2.getId()){
				response.setQuickValue(Response.FAIL, "货品单位 重复,请检查重新输入");
			} else {
				productUnitDaoImpl.saveOrUpdate(productUnit, true);
				response.setReturnCode(Response.SUCCESS);
			}
		}
		
		return response;
	}

	public Response updateNumPerHand(NumPerHand numPerHand) {
		Response response = new Response();
		
		int id = numPerHand.getId();
		int num = numPerHand.getNumPerHand();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(NumPerHand.class);
		criteria.add(Restrictions.eq("numPerHand", num));
		List<NumPerHand> numPerHands = numPerHandDaoImpl.getByCritera(criteria, true);
		
		if (numPerHands == null || numPerHands.size() == 0){
			numPerHandDaoImpl.saveOrUpdate(numPerHand, true);
			response.setReturnCode(Response.SUCCESS);
		} else {
			NumPerHand num2 = numPerHands.get(0);
			if (id != num2.getId()){
				response.setQuickValue(Response.FAIL, "齐码数量 重复,请检查重新输入");
			} else {
				numPerHandDaoImpl.saveOrUpdate(numPerHand, true);
				response.setReturnCode(Response.SUCCESS);
			}
		}
		
		return response;
	}

	public Response updateColor(Color color) {
		Response response = new Response();
		
		int id = color.getColorId();
		String colorName = color.getName();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Color.class);
		criteria.add(Restrictions.eq("name", colorName));
		List<Color> colors = colorDaoImpl.getByCritera(criteria, true);
		
		if (colors == null || colors.size() == 0){
			String code = StringUtility.getPinyinCode(color.getName(), true);
			color.setCode(code);
			colorDaoImpl.saveOrUpdate(color, true);
			response.setReturnCode(Response.SUCCESS);
		} else {
			Color colorName2 = colors.get(0);
			if (id != colorName2.getColorId()){
				response.setQuickValue(Response.FAIL, "相同颜色 重复,请检查重新输入");
			} else {
				String code = StringUtility.getPinyinCode(color.getName(), true);
				color.setCode(code);
				colorDaoImpl.saveOrUpdate(color, true);
				response.setReturnCode(Response.SUCCESS);
			}
		}
		
		return response;
	}

	/**
	 * 获取所有basic data
	 * @param basicData
	 * @return
	 */
	public DataGrid getBasicData(String basicData, Integer page, Integer rowPerPage, String sort, String sortOrder) {
		DataGrid dg = new DataGrid();
		PageHelper pHelper = new PageHelper();

		if (basicData.equalsIgnoreCase("quarter")){
			//1. 获取总条数
			DetachedCriteria quarterTotalCriteria = DetachedCriteria.forClass(Quarter.class);
			quarterTotalCriteria.setProjection(Projections.rowCount());
			int total = NumUtility.getProjectionIntegerValue(quarterDaoImpl.getByCriteriaProjection(quarterTotalCriteria, true));
			pHelper = new PageHelper(rowPerPage, page, total);
			
			//2. 获取当页数据
			DetachedCriteria quarterCriteria = DetachedCriteria.forClass(Quarter.class);
			List<Quarter> quarters = quarterDaoImpl.getByCritera(quarterCriteria, pHelper.getFirstRow(), pHelper.getRowPerPage(), true);
			
			dg.setTotal(new Long(pHelper.getTotalRows()));
			dg.setRows(quarters);
		} else if (basicData.equalsIgnoreCase("brand")){
			//1. 获取总条数
			DetachedCriteria brandTotalCriteria = DetachedCriteria.forClass(Brand.class);
			brandTotalCriteria.setProjection(Projections.rowCount());
			int total = NumUtility.getProjectionIntegerValue(brandDaoImpl.getByCriteriaProjection(brandTotalCriteria, true));
			pHelper = new PageHelper(rowPerPage, page, total);
			
			//2. 获取当页数据
			DetachedCriteria brandCriteria = DetachedCriteria.forClass(Brand.class);
			if (sortOrder.equalsIgnoreCase("desc"))
				brandCriteria.addOrder(Order.desc(sort));
			else 
				brandCriteria.addOrder(Order.asc(sort));
			List<Brand> brands = brandDaoImpl.getByCritera(brandCriteria, pHelper.getFirstRow(), pHelper.getRowPerPage(), true);
			
			dg.setTotal(new Long(pHelper.getTotalRows()));
			dg.setRows(brands);
		} else if (basicData.equalsIgnoreCase("category")){
			//1. 获取总条数
			DetachedCriteria categoryTotalCriteria = DetachedCriteria.forClass(Category.class);
			categoryTotalCriteria.setProjection(Projections.rowCount());
			int total =  NumUtility.getProjectionIntegerValue(categoryDaoImpl.getByCriteriaProjection(categoryTotalCriteria, true));
			pHelper = new PageHelper(rowPerPage, page, total);
			
			//2. 获取当页数据
			DetachedCriteria categoryCriteria = DetachedCriteria.forClass(Category.class);
			List<Category> categories = categoryDaoImpl.getByCritera(categoryCriteria, pHelper.getFirstRow(), pHelper.getRowPerPage(), true);
			
			dg.setTotal(new Long(pHelper.getTotalRows()));
			dg.setRows(categories);
		} else if (basicData.equalsIgnoreCase("color")){ 
			//1. 获取总条数
			DetachedCriteria colorTotalCriteria = DetachedCriteria.forClass(Color.class);
			colorTotalCriteria.setProjection(Projections.rowCount());
			int total =  NumUtility.getProjectionIntegerValue(colorDaoImpl.getByCriteriaProjection(colorTotalCriteria, true));
			pHelper = new PageHelper(rowPerPage, page, total);
			
			//2. 获取当页数据
			DetachedCriteria colorCriteria = DetachedCriteria.forClass(Color.class);
			if (sortOrder.equalsIgnoreCase("desc"))
				colorCriteria.addOrder(Order.desc(sort));
			else 
				colorCriteria.addOrder(Order.asc(sort));
			List<Color> colors = colorDaoImpl.getByCritera(colorCriteria, pHelper.getFirstRow(), pHelper.getRowPerPage(), true);
			
			dg.setTotal(new Long(pHelper.getTotalRows()));
			dg.setRows(colors);
		} else if (basicData.equalsIgnoreCase("productUnit")){ 
			//1. 获取总条数
			DetachedCriteria productUnitTotalCriteria = DetachedCriteria.forClass(ProductUnit.class);
			productUnitTotalCriteria.setProjection(Projections.rowCount());
			int total =  NumUtility.getProjectionIntegerValue(productUnitDaoImpl.getByCriteriaProjection(productUnitTotalCriteria, true));
			pHelper = new PageHelper(rowPerPage, page, total);
			
			//2. 获取当页数据
			DetachedCriteria productUnitCriteria = DetachedCriteria.forClass(ProductUnit.class);
			List<ProductUnit> productUnits = productUnitDaoImpl.getByCritera(productUnitCriteria, pHelper.getFirstRow(), pHelper.getRowPerPage(), true);
			
			dg.setTotal(new Long(pHelper.getTotalRows()));
			dg.setRows(productUnits);
		} else if (basicData.equalsIgnoreCase("numPerHand")){ 
			//1. 获取总条数
			DetachedCriteria numPerHandTotalCriteria = DetachedCriteria.forClass(NumPerHand.class);
			numPerHandTotalCriteria.setProjection(Projections.rowCount());
			int total =  NumUtility.getProjectionIntegerValue(numPerHandDaoImpl.getByCriteriaProjection(numPerHandTotalCriteria, true));
			pHelper = new PageHelper(rowPerPage, page, total);
			
			//2. 获取当页数据
			DetachedCriteria numPerHandCriteria = DetachedCriteria.forClass(NumPerHand.class);
			List<NumPerHand> numPerHands = numPerHandDaoImpl.getByCritera(numPerHandCriteria, pHelper.getFirstRow(), pHelper.getRowPerPage(), true);
			
			dg.setTotal(new Long(pHelper.getTotalRows()));
			dg.setRows(numPerHands);
		} else {
			//1. 获取总条数
			DetachedCriteria yearTotalCriteria = DetachedCriteria.forClass(Year.class);
			yearTotalCriteria.setProjection(Projections.rowCount());
			int total =  NumUtility.getProjectionIntegerValue(yearDaoImpl.getByCriteriaProjection(yearTotalCriteria, true));
			pHelper = new PageHelper(rowPerPage, page, total);
			
			//2. 获取当页数据
			DetachedCriteria yearCriteria = DetachedCriteria.forClass(Year.class);
			List<Year> years = yearDaoImpl.getByCritera(yearCriteria, pHelper.getFirstRow(), pHelper.getRowPerPage(), true);
			
			dg.setTotal(new Long(pHelper.getTotalRows()));
			dg.setRows(years);
		}

		return dg;
	}

	@Transactional
	public Response getBarcodeSourceData(){
        Response response = new Response();
        
        Map<String, Object> data = new HashMap<String, Object>();
		List<Year> yearList =  yearDaoImpl.getAll(true); 

		List<Brand> brandList =  brandDaoImpl.getAll(true);    

		List<Category> categoryList =  categoryDaoImpl.getAll(true);
		
		List<Quarter> quarterList =  quarterDaoImpl.getAll(true);  

		List<ProductUnit> unitList = productUnitDaoImpl.getAll(true);

		List<NumPerHand> numPerHandList = numPerHandDaoImpl.getAll(true);
		
		data.put("yearList", yearList);
		data.put("quarterList", quarterList);
		data.put("brandList", brandList);
		data.put("categoryList", categoryList);
		data.put("unitList", unitList);
		data.put("numPerHandList", numPerHandList);
		data.put("productBarcode", new ProductBarcode());
		
		response.setReturnValue(data);
		return response;
	}

	@Transactional
	public Response checkErrorBeforeGenerateBarcode(Product product) {
		Response response = new Response();
		Map<String, Object> data = new HashMap<String, Object>();
		
		String serialNum = product.getSerialNum();
		String productCode = product.getProductCode();
		
		if (serialNum != null && !serialNum.trim().equals("")){
			Product product2 = productDaoImpl.getBySerialNum(serialNum);
			if (product2 == null){
				response.setReturnCode(Response.FAIL);
				response.setMessage("此商品编码不存在于条码系统中");
				return response;
			} else if (!product.equals(product2)) {
				response.setReturnCode(Response.FAIL);
				response.setMessage("此商品编码的基础资料和以前所建基础资料不同");
				return response;
			} 
		} else {
		    String tip = "";
			List<Product> products = productDaoImpl.getProductsByProductCode(productCode);
	
			if (products != null && products.size() >0){
				for (Product product2 : products){
					productDaoImpl.initialize(product2);
					tip += "          " + product2.toString() + "\n";
				}
				
				response.setReturnCode(Response.WARNING);
				response.setMessage("相同货号已经存在 : \n" + tip);
				response.setReturnValue(products);
				return response;
			}
		}

		
		response.setReturnCode(Response.SUCCESS);
		return response;
	}

	@Transactional
	public Response saveProduct(Product product, Integer[] colorIds) {
	    Response response = new Response();
		
		String serialNum = product.getSerialNum();

		/**
		 * 3.0 if it is existing product, no need to save information again, save the product information
		 */
		Product product2 = null;
		if (!org.apache.commons.lang.StringUtils.isEmpty(serialNum)){
			product2 = productDaoImpl.getBySerialNum(serialNum);
		}
		
		if (product2 == null){
	    	double discount = product.getDiscount();
	    	if (discount <= 0 || discount >1)
	    		product.setDiscount(1);
			productDaoImpl.save(product, true);
			product.setSerialNum(String.valueOf(product.getProductId()));
			productDaoImpl.update(product, true);
			generateProductBarcodes(product, colorIds);
		} else 
			generateProductBarcodes(product2, colorIds);
		
		response.setReturnCode(Response.SUCCESS);
		response.setMessage("成功生成条形码");
		
		return response;
		
	}
	
	@Transactional
	public Response getProductBarcodesFromSameGroup(Product product){
		Response response = new Response();
		
		List<ProductBarcode> barcodes = productBarcodeDaoImpl.getProductBracodeFromSameGroup(product);
		for (ProductBarcode barcode: barcodes)
		      productBarcodeDaoImpl.initialize(barcode);
		
		response.setReturnValue(barcodes);
		response.setReturnCode(Response.SUCCESS);
		response.setMessage("成功生成条形码");
		
		return response;
	}
	
	/**
	 * 
	 * @param product
	 * @param colors
	 * @param sizes
	 */
	private void generateProductBarcodes(Product product, Integer[] colorIds) {
		List<Color> colors = new ArrayList<Color>();

		if (colorIds == null || colorIds.length==0)
			colors.add(null);
		else{
			for (Integer colorId: colorIds){
				if (colorId != 0)
					colors.add(new Color(colorId));
			}
		}

			
		List<ProductBarcode> productBarcodes = productBarcodeDaoImpl.getBarcodeFromProduct(product.getProductId());
		List<ProductBarcode> deletedProductBarcodes = new ArrayList<ProductBarcode>();
		for (ProductBarcode barcode : productBarcodes){
			if (barcode.getStatus() == ProductBarcode.STATUS_DELETE)
				deletedProductBarcodes.add(barcode);
		}
		
		for (Color color: colors){
				//save the productBarcode information
				ProductBarcode productBarcode = new ProductBarcode(product, color, null);
				if (productBarcodes.contains(productBarcode)){
					if (deletedProductBarcodes.contains(productBarcode)){
						for (ProductBarcode pBarcode : deletedProductBarcodes){
							if (pBarcode.equals(productBarcode)){
								pBarcode.setStatus(ProductBarcode.STATUS_OK);
								productBarcodeDaoImpl.update(pBarcode, true);
							}
						}
					} 
				} else {
						productBarcodeDaoImpl.save(productBarcode, true);
						
						//generate the barcode
						int id = productBarcode.getId();
						String barcode = generateBarcode(id);
						productBarcode.setBarcode(barcode);
						productBarcodeDaoImpl.update(productBarcode, true);
				}
		}	
		
	}
	
	/**
	 * logic of barcode
	 * odd total = odd number  * 3
	 * even total = even number
	 * total = odd total + even total
	 * check number = 10 - total % 10
	 * barcode = barcode11 + check number
	 * @param barCode
	 * @return
	 */
	public final static String generateBarcode(int sequenceId){
		StringBuffer prefix = null;
		
		prefix = new StringBuffer("1");

		String productIdS = String.valueOf(sequenceId);
		int prefixLength = 11 - productIdS.length();

		
		prefixLength = prefixLength - prefix.length();
		//for the others add 0 or random number to the barcode
		Random random = new Random(new Date().getTime());
		for (int i = 0;  i < prefixLength; i++){
			//for the first two digits, we need the random number
			if (i < 2)
				prefix.append(random.nextInt(10));
			else 
			    prefix.append("0");
		}
		
		productIdS = prefix.toString() + productIdS;

        char[] codeArray  =productIdS.toCharArray();
        
        List<Integer> intArray = new ArrayList<Integer>();
        for (char code: codeArray){
      	  intArray.add(Integer.parseInt(String.valueOf(code)));
        }
        int totalOfOdd = 0;
        for (int i =0; i <intArray.size(); i=i+2){
      	  totalOfOdd+=intArray.get(i);
        }
        totalOfOdd *= 3;
        
        int totalOfEven = 0;
        for (int i =1; i <intArray.size(); i=i+2){
      	  totalOfEven+=intArray.get(i);
        }
        
        int total = totalOfEven + totalOfOdd;
        
        int remain = total % 10;
        
        if (remain != 0)
      	  remain = 10 - remain;
		
		
		return productIdS + remain;
	}

	public Response searchColors(String colorNames) {
		Response response = new Response();
		
		Map<String, Object> data = new HashMap<String, Object>();
		
        if (colorNames != null && !colorNames.trim().equals("")){
        	String[] colorArray = colorNames.split("-");
        	List<String> colorNameList = Arrays.asList(colorArray);
        	
        	List<Color> colors = colorDaoImpl.getColors(colorNameList);
        	data.put("colors", colors);
        }
        
        response.setReturnValue(data);
		return response;
	}

	public Response getProductInforBySerialNum(String serialNum) {
		Response response = new Response();

		Product product = productDaoImpl.getBySerialNum(serialNum);
		
		if (product == null){
			response.setMessage("找不到产品信息");
			response.setReturnCode(Response.FAIL);
			return response;
		}
		
		int productId = product.getProductId();	
		List<ProductBarcode> productBarcodes = productBarcodeDaoImpl.getBarcodeFromProduct(productId);
		  
		String colors = "";
		if (productBarcodes != null && productBarcodes.size() > 0){
			for (ProductBarcode productBarcode: productBarcodes)
				if (productBarcode.getColor() != null)
				    colors += productBarcode.getColor().getName() + " ";
		}
		
		Map dataMap = new HashMap<String, Object>();
		dataMap.put("product", product);
		dataMap.put("color", colors);
		
		response.setReturnValue(dataMap);
		response.setReturnCode(Response.SUCCESS);
		
		return response;
	}

	@Transactional
	public Response prepareSearchBarcode() {
	      Response response = new Response();
	        
	        Map<String, Object> data = new HashMap<String, Object>();
			List<Year> yearList =  yearDaoImpl.getAll(true); 
			
			List<Brand> brandList =  brandDaoImpl.getAll(true);    

			List<Category> categoryList =  categoryDaoImpl.getAll(true);
			
			List<Quarter> quarterList =  quarterDaoImpl.getAll(true);  

			List<ProductUnit> unitList = productUnitDaoImpl.getAll(true);

			List<NumPerHand> numPerHandList = numPerHandDaoImpl.getAll(true);
			
			data.put("yearList", yearList);
			data.put("quarterList", quarterList);
			data.put("brandList", brandList);
			data.put("categoryList", categoryList);
			data.put("unitList", unitList);
			data.put("numPerHandList", numPerHandList);
			data.put("productBarcode", new ProductBarcode());
			
			ProductBasicFormBean formBean = new ProductBasicFormBean();
			data.put("formBean", formBean);
			
			response.setReturnValue(data);
			return response;
	}

	public Response searchBarcodes(ProductBarcode productBarcode, ProductBasicFormBean formBean) {
		List<ProductBarcode> barcodes = new ArrayList<ProductBarcode>();
		Response response = new Response();
		try {
			DetachedCriteria productBarcodeCriteria = DetachedCriteria.forClass(ProductBarcode.class);
			DetachedCriteria productCriteria = productBarcodeCriteria.createCriteria("product");

			
			boolean searchCriteria = false;
			
	        if (!productBarcode.getProduct().getProductCode().trim().equals("")){
	        	searchCriteria = true;
				productCriteria.add(Restrictions.like("productCode", productBarcode.getProduct().getProductCode(),MatchMode.ANYWHERE));
	        }
			if (productBarcode.getProduct().getYear().getYear_ID() != EntityConfig.ALL_RECORD){
				searchCriteria = true;
				productCriteria.add(Restrictions.eq("year.year_ID", productBarcode.getProduct().getYear().getYear_ID()));
			}
			if (productBarcode.getProduct().getQuarter().getQuarter_ID() != EntityConfig.ALL_RECORD){
				searchCriteria = true;
				productCriteria.add(Restrictions.eq("quarter.quarter_ID", productBarcode.getProduct().getQuarter().getQuarter_ID()));
			}
			if (productBarcode.getProduct().getBrand().getBrand_ID() != EntityConfig.ALL_RECORD ){
				searchCriteria = true;
				productCriteria.add(Restrictions.eq("brand.brand_ID", productBarcode.getProduct().getBrand().getBrand_ID()));
			}
			if (productBarcode.getProduct().getCategory().getCategory_ID() != EntityConfig.ALL_RECORD){
				searchCriteria = true;
				productCriteria.add(Restrictions.eq("category.category_ID", productBarcode.getProduct().getCategory().getCategory_ID()));
			}

			if (searchCriteria == false){
				Date startDate = formBean.getStartDate();
				Date endDate = formBean.getEndDate();
				
				java.util.Date endDate2 = new java.util.Date(endDate.getTime());
		        endDate2.setHours(23);
		        endDate2.setMinutes(59);
		        endDate2.setSeconds(59);
				productBarcodeCriteria.add(Restrictions.between("createDate",startDate,endDate2));

			}

	        productBarcodeCriteria.add(Restrictions.eq("status", ProductBarcode.STATUS_OK));
	        
			barcodes =  productBarcodeDaoImpl.getByCritera(productCriteria,true);

			response.setReturnValue(barcodes);
		} catch (Exception e) {
			e.printStackTrace();
			response.setFail(e.getMessage());
		}

		return response;
	}

	@Transactional
	public Response searchForUpdate(String barcode) {
	    Response response = new Response();
	    Map<String, Object> data = new HashMap<String, Object>();
	    ProductBarcode productBarcode = null;
	    
		DetachedCriteria criteria = DetachedCriteria.forClass(ProductBarcode.class);
		criteria.add(Restrictions.eq("barcode", barcode));

	    List<ProductBarcode> barcodes = productBarcodeDaoImpl.getByCritera(criteria, true);
	    if (barcodes != null && barcodes.size() == 1){
	    	productBarcode = barcodes.get(0);
	    } else {
	    	response.setQuickValue(Response.FAIL, "无法在系统中找到当前条码");
	    	return response;
	    }

		List<Year> yearList =  yearDaoImpl.getAll(true); 
		
		List<Brand> brandList =  brandDaoImpl.getAll(true);    

		List<Category> categoryList =  categoryDaoImpl.getAll(true);
		
		List<Quarter> quarterList =  quarterDaoImpl.getAll(true);  

		List<ProductUnit> unitList = productUnitDaoImpl.getAll(true);

		List<NumPerHand> numPerHandList = numPerHandDaoImpl.getAll(true);
		
		data.put("yearList", yearList);
		data.put("quarterList", quarterList);
		data.put("brandList", brandList);
		data.put("categoryList", categoryList);
		data.put("unitList", unitList);
		data.put("numPerHandList", numPerHandList);
		data.put("productBarcode", productBarcode);
		
		response.setReturnValue(data);
		return response;
	}

	@Transactional
	public Response updateProduct(ProductBarcode productBarcode) {
		Response response = new Response();
		
        Product product = productDaoImpl.get(productBarcode.getProduct().getProductId(), true);
        if (product == null){
        	response.setFail("无法找到当前产品，请检查之后重新查询");
        } else {
        	try {
		        	Product newProduct = productBarcode.getProduct();
		        	product.setNumPerHand(newProduct.getNumPerHand());
		        	product.setProductCode(newProduct.getProductCode());
		        	product.setSalesPrice(newProduct.getSalesPrice());
		        	product.setWholeSalePrice(newProduct.getWholeSalePrice());
		        	product.setUnit(newProduct.getUnit());
		        	product.setCategory(newProduct.getCategory());
		        	product.setYear(newProduct.getYear());
		        	product.setQuarter(newProduct.getQuarter());
		        	product.setBrand(newProduct.getBrand());
		        	product.setDiscount(newProduct.getDiscount());
		        	product.setRecCost(newProduct.getRecCost());
		        	product.setSalesPriceFactory(newProduct.getSalesPriceFactory());
	
		        	productDaoImpl.saveOrUpdate(product,true);
        		} catch (Exception e) {
					response.setFail(e.getMessage());
				}
        }

		return response;
	}

	@Transactional
	public Response deleteProduct(ProductBarcode productBarcode) {
		Response response = new Response();
		try {
			ProductBarcode pBarcode = productBarcodeDaoImpl.get(productBarcode.getId(), true);
			productBarcodeDaoImpl.delete(pBarcode, true);
			
			int deleteBarcodes = custOrderProductDaoImpl.deleteProduct(productBarcode.getId());

			response.setSuccess("成功删除条码  " + deleteBarcodes);
		} catch (Exception e) {
			response.setFail("删除条码发生错误 : " + e.getMessage());
		}
		return response;
	}

	@Transactional
	public Response prepareBatchBarcodeImport() {
	    Response response = new Response();
	    Map<String, Object> data = new HashMap<String, Object>();

		List<Year> yearList =  yearDaoImpl.getAll(true); 
		
		List<Brand> brandList =  brandDaoImpl.getAll(true);    

		List<Quarter> quarterList =  quarterDaoImpl.getAll(true);  

		data.put("yearList", yearList);
		data.put("quarterList", quarterList);
		data.put("brandList", brandList);
		data.put("productBarcode", new ProductBarcode());
		
		response.setReturnValue(data);
		return response;
	}

	@Transactional
	public Response batchImportBarcode(MultipartFile barcodeMultiple,Product product) {
		Response response = new Response();
		
		/**
		 * 1. 服务器端验证, 年份, 季度，品牌不能为空，并且 品牌不能是连锁店自己新增品牌
		 */
		Area area = areaDaoImpl.get(Area.CURRENT_AREA, true);
		Year year = yearDaoImpl.get(product.getYear().getYear_ID(), true);
		Quarter quarter = quarterDaoImpl.get(product.getQuarter().getQuarter_ID(), true);
		Brand brand = brandDaoImpl.get(product.getBrand().getBrand_ID(), true);
		if (year == null)
			response.setFail("产品年份不能为空, 请检查");
		else if (quarter == null)
			response.setFail("产品季度不能为空, 请检查");
		else if (brand == null)
			response.setFail("产品品牌不能为空, 请检查");

		CommonsMultipartFile commonsmultipartfile = (CommonsMultipartFile) barcodeMultiple;
        DiskFileItem diskFileItem = (DiskFileItem) commonsmultipartfile.getFileItem();  
        File barcodes = diskFileItem.getStoreLocation();


		BarcodeImportTemplate barcodeTemplate = new BarcodeImportTemplate(barcodes, year, quarter, brand, area);
		barcodeTemplate.proccess(categoryDaoImpl, colorDaoImpl, productUnitDaoImpl);
		
		if (!barcodeTemplate.isSuccess()){
			response.setFail(barcodeTemplate.getValidateMsg());
		} else {
			List<Object> wsData = barcodeTemplate.getWsData();
			if (wsData == null|| wsData.size()==0){
				response.setFail("这个表格无数据");
			} else {
				
				for (Object rowData: wsData){
					List<Object> rowDataList = (List<Object>)rowData;
					Product product2 = (Product)rowDataList.get(0);
					List<Integer> colorIds = (List<Integer>)rowDataList.get(1);
					
					saveProduct(product2, colorIds.toArray(new Integer[colorIds.size()]));
				}
		
				response.setSuccess("成功导入 "+ wsData.size() +"个 产品 . 条码信息 : " + year.getYear() + quarter.getQuarter_Name() + " " + brand.getBrand_Name());
			}
			
		}

		
		return response;
	}

}
