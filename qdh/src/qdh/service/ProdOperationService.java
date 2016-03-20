package qdh.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.dao.entity.VO.CurrentBrandVO;
import qdh.dao.entity.order.CurrentBrands;
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
import qdh.pageModel.DataGrid;

@Service
public class ProdOperationService {
	
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
}
