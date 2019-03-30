package qdh.dao.entity.VO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import qdh.comparator.CustOrderProductComparatorByBrandProductCode;
import qdh.dao.config.EntityConfig;
import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.dao.entity.product.Brand;
import qdh.dao.entity.product.Color;
import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;
import qdh.utility.DateUtility;
import qdh.utility.ExcelUtility;

public class CustInforExcelVO extends AbstractExcelView {
	private int DATA_ROW = 3;
	private int HEADER_ROW = 1;
	private int CUST_NAME_COLUMN = 0;
	private int CUST_AREA_COLUMN = 1;
	private int CUST_CATEGORY_COLUMN = 2;
	private int LOGIN_NAME_COLUMN =3;
	private int PASSWORD_COLUMN =4;
	private int STATUS_COLUMN =5;
	private String defaulFileName ="KeHuXinXi.xls";
	
	@Override
	protected void buildExcelDocument(Map<String, Object> dataMap,
			HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Customer> customers = (List<Customer>)dataMap.get("customer");
		
		String fileName = getFileName(request);
		
		wb = process(customers);
		
        response.setContentType("application/vnd.ms-excel");     
        response.setHeader("Content-disposition", "attachment;filename=" + fileName); 
        OutputStream ouputStream = response.getOutputStream();     
        wb.write(ouputStream);     
        ouputStream.flush();     
        ouputStream.close(); 
	}
	
	private String getFileName(HttpServletRequest request) {
		String fileName = "订货会客户信息.xls";
		return  ExcelUtility.encodeExcelDownloadName(fileName,defaulFileName);
	}

	private HSSFWorkbook process(List<Customer> customers){
		HSSFWorkbook wb = null;
		InputStream is = null;
		try {
			is = CustInforExcelVO.class.getClassLoader()
			 .getResourceAsStream("qdh/templates/CustInforTemplate.xls");
		} catch (Exception e) {
				e.printStackTrace();
		}
		
		try {
			wb = new HSSFWorkbook(is);   
		} catch (FileNotFoundException e) {
			qdh.utility.loggerLocal.error(e);
		} catch (IOException e) {
			qdh.utility.loggerLocal.error(e);
		}   
		
		//1. process data
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow headerRow = sheet.getRow(HEADER_ROW);
		headerRow.createCell(1).setCellValue(DateUtility.getToday().toString());
		
		//2. process elements
		for (int i =0; i < customers.size(); i++){
			Customer cust = customers.get(i);

			HSSFRow dataRow = sheet.createRow(DATA_ROW + i);
			dataRow.createCell(CUST_NAME_COLUMN).setCellValue(cust.getCustName());
			
			dataRow.createCell(CUST_AREA_COLUMN).setCellValue(cust.getCustRegion());
			
//			Integer chainId = cust.getChainId();
//			if (chainId == null)
//			    dataRow.createCell(CUST_CATEGORY_COLUMN).setCellValue("散客");
			
			//dataRow.createCell(CHAIN_NAME_COLUMN).setCellValue(cust.getChainStoreName());
			dataRow.createCell(LOGIN_NAME_COLUMN).setCellValue(cust.getId() + " ");
			dataRow.createCell(PASSWORD_COLUMN).setCellValue(cust.getPassword() + " ");
			
			int status = cust.getStatus();
			if (status == EntityConfig.ACTIVE)
				dataRow.createCell(STATUS_COLUMN).setCellValue("正常");
			else 
				dataRow.createCell(STATUS_COLUMN).setCellValue("冻结");
		}
		
		return wb;
	}

}
