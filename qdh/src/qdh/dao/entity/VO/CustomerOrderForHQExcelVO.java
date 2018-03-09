package qdh.dao.entity.VO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import qdh.comparator.CustOrderProductComparatorByBrandProductCode;
import qdh.comparator.CustOrderProductComparatorByYQBrandProductCode;
import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.dao.entity.product.Brand;
import qdh.dao.entity.product.Color;
import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.utility.DateUtility;
import qdh.utility.ExcelUtility;

public class CustomerOrderForHQExcelVO extends AbstractExcelView {
	private int DATA_ROW = 4;
	private int DATE_ROW = 1;
	private int CUST_ROW = 2;

	private int CHAIN_COLUMN = 0;
	private int YEAR_COLUMN = 1;
	private int QUARTER_COLUMN =2;
	private int BRAND_COLUMN =3;
	private int PRODUCT_CODE_COLUMN =4;
	private int COLOR_COLUMN =5;
	private int CATEGORY_COLUMN =6;
	private int NUM_PER_HAND_COLUMN =7;
	private int QUANTITY_COLUMN =8;
	private int BARCODE_COLUMN =9;
	private String defaulFileName ="KeHuBaoBiaoDingdan.xls";
	
	@Override
	protected void buildExcelDocument(Map<String, Object> dataMap,
			HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
//		List<CustOrderProduct> orderProducts = (List<CustOrderProduct>)dataMap.get("data");
//		Collections.sort(orderProducts, new CustOrderProductComparatorByYQBrandProductCode());
//		
//		Customer cust = (Customer)dataMap.get("customer");
//		
//		String fileName = getFileName(cust);
//		
//		wb = process(orderProducts,cust);
//		
//        response.setContentType("application/vnd.ms-excel");     
//        response.setHeader("Content-disposition", "attachment;filename=" + fileName); 
//        OutputStream ouputStream = response.getOutputStream();     
//        wb.write(ouputStream);     
//        ouputStream.flush();     
//        ouputStream.close(); 
	}
	
	private String getFileName(Customer cust) {
		String fileName = cust.getId() + "-" + cust.getCustName() + "-" + cust.getChainStoreName() + ".xls";
		return  ExcelUtility.encodeExcelDownloadName(fileName,defaulFileName);
	}

	public HSSFWorkbook process(List<CustOrderProduct> orderProducts,Customer cust, String orderIdentity, CustomerDaoImpl customerDaoImpl){
		HSSFWorkbook wb = null;
		InputStream is = null;
		try {
			is = CustomerOrderForHQExcelVO.class.getClassLoader()
			 .getResourceAsStream("qdh/templates/CustomerOrderTemplateForHQ.xls");
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

		int sumHands = 0;
		
		//2. process elements
		int brandId = 0;
		boolean changeName = false;
		int sheetCount = 0;
		HSSFSheet sheet = null;
		
		for (int i =0, j = 0; i < orderProducts.size(); i++, j++){
			CustOrderProduct cop = orderProducts.get(i);
			ProductBarcode pb = cop.getProductBarcode();
			Product p = pb.getProduct();
			Color color = pb.getColor();
			
			if (p.getBrand().getBrand_ID() != brandId){
				changeName = false;	
				
				if (i > 0){
					HSSFRow dataRow = sheet.createRow(DATA_ROW + j);
					dataRow.createCell(BRAND_COLUMN).setCellValue("小计:");
					dataRow.createCell(QUANTITY_COLUMN).setCellValue(sumHands);
				}
				
				//1. process data
				sheet = wb.cloneSheet(0);
				
				HSSFRow dateRow = sheet.getRow(DATE_ROW);
				dateRow.createCell(2).setCellValue(orderIdentity);

				HSSFRow custRow = sheet.getRow(CUST_ROW);
				custRow.createCell(2).setCellValue(cust.getCustName() + "-" + cust.getChainStoreName());
				
				sheetCount++;
				sumHands = 0;
				j = 0;
			}

			
			if (changeName == false){
				changeName = true;
				wb.setSheetName(sheetCount,p.getBrand().getBrand_Name());
			}
			

			brandId = p.getBrand().getBrand_ID();
			HSSFRow dataRow = null;
			try {
			        dataRow = sheet.createRow(DATA_ROW + j);
			} catch (Exception e){
				e.printStackTrace();
			}
			//dataRow.createCell(BARCODE_COLUMN).setCellValue(pb.getBarcode());
			if (customerDaoImpl != null){
				Customer cust2 = customerDaoImpl.get(cop.getCustId(), true);
				if (cust2 != null){
					dataRow.createCell(CHAIN_COLUMN).setCellValue(cust2.getCustName() + "-" + cust2.getChainStoreName());
					
				}
			}
			dataRow.createCell(YEAR_COLUMN).setCellValue(p.getYear().getYear());
			dataRow.createCell(QUARTER_COLUMN).setCellValue(p.getQuarter().getQuarter_Name());
			dataRow.createCell(BRAND_COLUMN).setCellValue(p.getBrand().getBrand_Name());
			dataRow.createCell(PRODUCT_CODE_COLUMN).setCellValue(p.getProductCode());
			if (color != null)
				dataRow.createCell(COLOR_COLUMN).setCellValue(color.getName());
			dataRow.createCell(CATEGORY_COLUMN).setCellValue(p.getCategory().getCategory_Name());
			dataRow.createCell(NUM_PER_HAND_COLUMN).setCellValue(p.getNumPerHand());
			dataRow.createCell(QUANTITY_COLUMN).setCellValue(cop.getQuantity());
			dataRow.createCell(BARCODE_COLUMN).setCellValue(cop.getProductBarcode().getBarcode());

			sumHands += cop.getQuantity();
			
			if ((i + 1) == orderProducts.size()){
				dataRow = sheet.createRow(DATA_ROW + j + 1);
				dataRow.createCell(BRAND_COLUMN).setCellValue("小计:");
				dataRow.createCell(QUANTITY_COLUMN).setCellValue(sumHands);
			}
			
			
			
		}

		if (orderProducts.size() > 0){
			wb.setSheetHidden(0, true);
		}
		
		return wb;
	}

}
