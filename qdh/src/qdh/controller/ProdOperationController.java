package qdh.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import qdh.controller.formBean.ProductBasicFormBean;
import qdh.dao.entity.product.Brand;
import qdh.dao.entity.product.Category;
import qdh.dao.entity.product.Color;
import qdh.dao.entity.product.NumPerHand;
import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;
import qdh.dao.entity.product.ProductUnit;
import qdh.dao.entity.product.Quarter;
import qdh.dao.entity.product.Year;
import qdh.dao.impl.Response;
import qdh.pageModel.DataGrid;
import qdh.pageModel.Json;
import qdh.pageModel.SessionInfo;
import qdh.service.ProdOperationService;
import qdh.utility.DateUtility;
import qdh.utility.StringUtility;




@Controller
@RequestMapping("/prodOptController")
public class ProdOperationController {
	
	@Autowired
	private ProdOperationService prodOperationService;
	
	
	@RequestMapping("/HQMainBasicData")
	public ModelAndView HQMainBasicData() {
		
		ModelAndView mav = new ModelAndView();
        
		
		ProductBasicFormBean bean = new ProductBasicFormBean();
		bean.setBasicData("-");
		mav.addObject("command", bean);
		mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/MaintainBarcodeData.jsp");
		
		return mav;
	}
	
	@RequestMapping("/ChangeBasicData")
	public ModelAndView ChangeBasicData(String basicData) {
		ModelAndView mav = new ModelAndView();
		
		ProductBasicFormBean bean = new ProductBasicFormBean();
		bean.setBasicData(basicData);
		
		if (basicData.equalsIgnoreCase("quarter")){
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/ListQuarter.jsp");
		} else if (basicData.equalsIgnoreCase("brand")){
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/ListBrand.jsp");
		} else if (basicData.equalsIgnoreCase("category")){	
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/ListCategory.jsp");
		} else if (basicData.equalsIgnoreCase("color")){ 
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/ListColor.jsp");
		} else if (basicData.equalsIgnoreCase("numPerHand")){
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/ListNumPerHand.jsp");
		} else if (basicData.equalsIgnoreCase("productUnit")){ 
			
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/ListProductUnit.jsp");
		} else {
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/ListYear.jsp");
		}

		mav.addObject("command", bean);
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/GetBasicData")
	public DataGrid GetBasicData(String basicData, String sort, String order, Integer page, Integer rows){
		DataGrid dataGrid = prodOperationService.getBasicData(basicData, page, rows, sort, order);
		return dataGrid;
	}
	
	@RequestMapping("/PreAddBasicData")
	public ModelAndView PreAddBasicData(ProductBasicFormBean basicDataBean){
		ModelAndView mav = new ModelAndView();
		Map<String, Object> data = new HashMap<String, Object>();
		
		String basicData = basicDataBean.getBasicData();

		Object object = null ;
		object = prodOperationService.getBasicData(basicData, basicDataBean.getBasicDataId());
		
		if (basicData.equalsIgnoreCase("quarter")){
			Quarter quarter = new Quarter();
			if (object != null)
				quarter = (Quarter)object;
			data.put("quarter", quarter);
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/UpdateQuarter.jsp");
		} else if (basicData.equalsIgnoreCase("brand")){
			Brand brand = new Brand();
			if (object != null)
				brand = (Brand)object;
			data.put("brand", brand);
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/UpdateBrand.jsp");
		} else if (basicData.equalsIgnoreCase("category")){
			Category category = new Category();
			if (object != null)
				category = (Category)object;
			data.put("category", category);
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/UpdateCategory.jsp");
		} else if (basicData.equalsIgnoreCase("color")){ 
			Color color = new Color();
			if (object != null)
				color = (Color)object;
			data.put("color", color);
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/UpdateColor.jsp");
		} else if (basicData.equalsIgnoreCase("year")){
			Year year = new Year();
			if (object != null)
				year = (Year)object;
			else 
				year.setYear(String.valueOf((DateUtility.getToday().getYear() + 1900)));
			data.put("year", year);
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/UpdateYear.jsp");
		} else if (basicData.equalsIgnoreCase("numPerHand")){
			NumPerHand numPerHand = new NumPerHand();
			if (object != null)
				numPerHand = (NumPerHand)object;
			data.put("numPerHand", numPerHand);
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/UpdateNumPerHand.jsp");
		} else if (basicData.equalsIgnoreCase("productUnit")){ 
			ProductUnit productUnit = new ProductUnit();
			if (object != null)
				productUnit = (ProductUnit)object;
			data.put("productUnit", productUnit);
			mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/UpdateProductUnit.jsp");
		}
		
		mav.addAllObjects(data);
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/UpdateBrand")
	public Json UpdateBrand(Brand brand){
		Response response = new Response();
		try {
			response = prodOperationService.updateBrand(brand);
		} catch (Exception e){
			e.printStackTrace();
			
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/UpdateYear")
	public Json UpdateYear(Year year){
		Response response = new Response();
		try {
			response = prodOperationService.updateYear(year);
		} catch (Exception e){
			e.printStackTrace();
			
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/UpdateQuarter")
	public Json UpdateQuarter(Quarter quarter){
		Response response = new Response();
		try {
			response = prodOperationService.updateQuarter(quarter);
		} catch (Exception e){
			e.printStackTrace();
			
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/UpdateCategory")
	public Json UpdateCategory(Category category){
		Response response = new Response();
		try {
			response = prodOperationService.updateCategory(category);
		} catch (Exception e){
			e.printStackTrace();
			
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/UpdateProductUnit")
	public Json UpdateProductUnit(ProductUnit pu){
		Response response = new Response();
		try {
			response = prodOperationService.updateProductUnit(pu);
		} catch (Exception e){
			e.printStackTrace();
			
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/UpdateNumPerHand")
	public Json UpdateNumPerHand(NumPerHand numPerHand){
		Response response = new Response();
		try {
			response = prodOperationService.updateNumPerHand(numPerHand);
		} catch (Exception e){
			e.printStackTrace();
			
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/UpdateColor")
	public Json UpdateColor(Color color){
		Response response = new Response();
		try {
			response = prodOperationService.updateColor(color);
		} catch (Exception e){
			e.printStackTrace();
			
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@RequestMapping("/HQProdMgmt")
	public String HQProdMgmt() {
		
		return "/jsp/hq/hqAdmin/ProdMgmt.jsp";
	}
	
	@ResponseBody
	@RequestMapping("/GetCurrentBrands")
	public DataGrid GetCurrentBrands(){
		DataGrid dataGrid = prodOperationService.getCurrentBrands();
		return dataGrid;
	}
	
	@ResponseBody
	@RequestMapping("/DeleteCurrentBrand")
	public Json DeleteCurrentBrand(int currentBrandId){
		Response response = new Response();
		try {
			response = prodOperationService.deleteCurrentBrand(currentBrandId);
		} catch (Exception e){
			e.printStackTrace();
			
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/UpdateCurrentBrand")
	public Json UpdateCurrentBrand(int currentBrandId, HttpSession session){
		Response response = new Response();
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		try {
			response = prodOperationService.updateCurrentBrand(currentBrandId, loginUser.getUserName());
		} catch (Exception e){
			e.printStackTrace();
			
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}	
	
	@RequestMapping("/PreAddCurrentBrand")
	public ModelAndView PreAddCurrentBrand(){
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = prodOperationService.preAddCurrentBrand();
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/hq/hqAdmin/ProdMgmtAdd.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/AddCurrentBrand")
	public Json AddCurrentBrand(int yearId, int quarterId, int brandId, HttpSession session){
		Response response = new Response();
		SessionInfo loginUser = (SessionInfo)session.getAttribute(ControllerConfig.HQ_SESSION_INFO);
		try {
			response = prodOperationService.addCurrentBrand(yearId, quarterId, brandId,loginUser.getUserName());
		} catch (Exception e){
			e.printStackTrace();
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@RequestMapping("/HQBarcodeMgmt")
	public ModelAndView HQBarcodeMgmt() {
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = prodOperationService.preBarcodeSearch();
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/hq/hqAdmin/BarcodeMgmt.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/GetBarcodes")
	public DataGrid GetBarcodes(Integer cbId, String sort, String order, Integer page, Integer rows){
		DataGrid dataGrid = prodOperationService.getBarcodes(cbId, sort,order,page, rows);
		return dataGrid;
	}

	@RequestMapping("/HQPreBarcodeGen")
	public ModelAndView HQPreBarcodeGen(){
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = prodOperationService.getBarcodeSourceData();
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/GenerateBarcode.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
	}
	
	/**
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/CheckProductCodeSerialNum")
	public Json CheckProductCodeSerialNum(ProductBarcode productBarcode){
		Response response = prodOperationService.checkErrorBeforeGenerateBarcode(productBarcode.getProduct());

		Json json = new Json(response);
		
		return json;
	}
	
	/**
	 * click the 生成货品和货品条码
	 */
	@ResponseBody
	@RequestMapping("/GenerateProductBarcode")
	public Json GenerateProductBarcode(ProductBarcode productBarcode, Integer[] colorIds){
		/**
			 * 1.0 to save the product information
			 */
		Response response = new Response();
		try {
			response = prodOperationService.saveProduct(productBarcode.getProduct(), colorIds);
			
			if (response.isSuccess()){
				response = prodOperationService.getProductBarcodesFromSameGroup(productBarcode.getProduct());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			response.setFail(e.getMessage());
			response.setQuickValue(Response.FAIL, e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@RequestMapping("/SearchColor")
	public ModelAndView SearchColor(String colors){
		ModelAndView mav = new ModelAndView();
		
		String colorNames = StringUtility.decode(colors);
		
		Response response = prodOperationService.searchColors(colorNames);
		
		mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/ColorList.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
		
	}
	
	@ResponseBody
	@RequestMapping("/GetProductInforBySerialNum")
	public Json GetProductInforBySerialNum(String serialNum){
		Response response = new Response();
		try {
			response = prodOperationService.getProductInforBySerialNum(serialNum);
		
		} catch (Exception e) {
			e.printStackTrace();
			response.setFail(e.getMessage());
			response.setQuickValue(Response.FAIL, e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	@RequestMapping("/HQPreSearchBarcode")
	public ModelAndView HQPreSearchBarcode(){
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = prodOperationService.prepareSearchBarcode();
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/SearchBarcode.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/SearchBarcode")
	public Json SearchBarcode(ProductBarcode productBarcode, ProductBasicFormBean formBean){
		Response response = new Response();
		try {
			response = prodOperationService.searchBarcodes(productBarcode, formBean);
		
		} catch (Exception e) {
			e.printStackTrace();
			response.setFail(e.getMessage());
			response.setQuickValue(Response.FAIL, e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}
	
	
	@RequestMapping("/HQSearchForUpdate")
	public ModelAndView HQSearchForUpdate(String barcode){
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = prodOperationService.searchForUpdate(barcode);
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/UpdateProduct.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping("/UpdateProduct")
	public Json UpdateProduct(ProductBarcode productBarcode){
		Response response = new Response();
		try {
			response = prodOperationService.updateProduct(productBarcode);
		
		} catch (Exception e) {
			e.printStackTrace();
			response.setFail(e.getMessage());
			response.setQuickValue(Response.FAIL, e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}

	@ResponseBody
	@RequestMapping("/DeleteProduct")
	public Json DeleteProduct(ProductBarcode productBarcode){
		Response response = new Response();
		try {
			response = prodOperationService.deleteProduct(productBarcode);
		
		} catch (Exception e) {
			e.printStackTrace();
			response.setFail(e.getMessage());
			response.setQuickValue(Response.FAIL, e.getMessage());
		}
		
		Json json = new Json(response);
		
		return json;
	}

	@RequestMapping("/HQPreBatchBarcodeImport")
	public ModelAndView HQPreBatchBarcodeImport(){
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = prodOperationService.prepareBatchBarcodeImport();
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		}
		
		mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/BatchBarcodeImport.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		return mav;
	}
	
	@RequestMapping("/HQBatchBarcodeImport")
	public ModelAndView HQBatchBarcodeImport(@RequestParam("uploadFile")MultipartFile uploadFile, ProductBarcode productBarcode){
		ModelAndView mav = new ModelAndView();
		
		Response response = new Response();
		try {
			response = prodOperationService.batchImportBarcode(uploadFile, productBarcode.getProduct());
		} catch (Exception e){
			response.setFail("系统错误 : " + e.getMessage());
		} finally {
			Response newResponse = prodOperationService.prepareBatchBarcodeImport();
			response.setReturnValue(newResponse.getReturnValue());
		}
		
		mav.setViewName("/jsp/hq/hqAdmin/ProdMgmt/BatchBarcodeImport.jsp");
		mav.addAllObjects((Map<String, ?>)response.getReturnValue());
		mav.addObject("msg", response.getMessage());
		return mav;
	}
	
	
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	    dateFormat.setLenient(false);  
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
	}
}
