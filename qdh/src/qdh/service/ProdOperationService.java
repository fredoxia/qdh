package qdh.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.dao.entity.VO.CurrentBrandVO;
import qdh.dao.entity.VO.ProductBarcodeVO;
import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.dao.entity.order.ProductCategoryInSystem;
import qdh.dao.entity.product.Area;
import qdh.dao.entity.product.Brand;
import qdh.dao.entity.product.Category;
import qdh.dao.entity.product.Color;
import qdh.dao.entity.product.NumPerHand;
import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;
import qdh.dao.entity.product.Quarter;
import qdh.dao.entity.product.Size;
import qdh.dao.entity.product.Year;
import qdh.dao.entity.qxMIS.Brand2;
import qdh.dao.entity.qxMIS.ProductBarcode2;
import qdh.dao.entity.qxMIS.Quarter2;
import qdh.dao.entity.qxMIS.Year2;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CurrentBrandsDaoImpl;
import qdh.dao.impl.order.CustOrderProductDaoImpl;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.dao.impl.order.ProductCategoryInSystemDaoImpl;
import qdh.dao.impl.product.AreaDaoImpl;
import qdh.dao.impl.product.BrandDaoImpl;
import qdh.dao.impl.product.CategoryDaoImpl;
import qdh.dao.impl.product.ColorDaoImpl;
import qdh.dao.impl.product.ProductBarcodeDaoImpl;
import qdh.dao.impl.product.ProductDaoImpl;
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
	private CustOrderProductDaoImpl custOrderProductDaoImpl;
	
	@Autowired
	private CustomerDaoImpl customerDaoImpl;
	
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

}
