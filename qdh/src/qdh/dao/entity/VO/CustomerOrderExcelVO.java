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
import qdh.utility.DateUtility;
import qdh.utility.ExcelUtility;

public class CustomerOrderExcelVO extends AbstractExcelView {
	private int DATA_ROW = 4;
	private int DATE_ROW = 1;
	private int CUST_ROW = 2;

	private int YEAR_COLUMN = 0;
	private int QUARTER_COLUMN =1;
	private int BRAND_COLUMN =2;
	private int PRODUCT_CODE_COLUMN =3;
	private int ORDER_IDENTITY_COLUMN =4;
	private int COLOR_COLUMN =4;
	private int CATEGORY_COLUMN =5;
	private int NUM_PER_HAND_COLUMN =6;
	private int QUANTITY_COLUMN =7;
	private int QUANTITY_SUM_COLUMN =8;
	private int RETAIL_PRICE_COLUMN =9;
	private int RETAIL_PRICE_SUM_COLUMN =10;
	private int BARCODE_COLUMN =11;
	private String defaulFileName ="KeHuBaoBiao.xls";
	
	@Override
	protected void buildExcelDocument(Map<String, Object> dataMap,
			HSSFWorkbook wb, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<CustOrderProduct> orderProducts = (List<CustOrderProduct>)dataMap.get("data");
		Collections.sort(orderProducts, new CustOrderProductComparatorByYQBrandProductCode());
		
		Customer cust = (Customer)dataMap.get("customer");
		
		String fileName = getFileName(cust);
		
		wb = process(orderProducts,cust);
		
        response.setContentType("application/vnd.ms-excel");     
        response.setHeader("Content-disposition", "attachment;filename=" + fileName); 
        OutputStream ouputStream = response.getOutputStream();     
        wb.write(ouputStream);     
        ouputStream.flush();     
        ouputStream.close(); 
	}
	
	private String getFileName(Customer cust) {
		String fileName = cust.getId() + "-" + cust.getCustName() + "-" + cust.getChainStoreName() + ".xls";
		return  ExcelUtility.encodeExcelDownloadName(fileName,defaulFileName);
	}

	public HSSFWorkbook process(List<CustOrderProduct> orderProducts,Customer cust){
		HSSFWorkbook wb = null;
		InputStream is = null;
		try {
			is = CustomerOrderExcelVO.class.getClassLoader()
			 .getResourceAsStream("qdh/templates/CustomerOrderTemplate.xls");
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
		HSSFRow dateRow = sheet.getRow(DATE_ROW);
		dateRow.createCell(1).setCellValue(DateUtility.getToday().toString());

		HSSFRow custRow = sheet.getRow(CUST_ROW);
		custRow.createCell(1).setCellValue(cust.getCustName() + "-" + cust.getChainStoreName());

		int subTotalHands = 0;
		int subTotalQuantity = 0;
		double subTotalRetailPrice = 0;
		
		int sumQuantity = 0;
		int sumHands = 0;
		double sumRetailPrice = 0;
		String orderIdentity = "";
		
		//2. process elements
		int brandId = 0;
		int countSubTotal = 0;
		for (int i =0; i < orderProducts.size(); i++){
			CustOrderProduct cop = orderProducts.get(i);
			ProductBarcode pb = cop.getProductBarcode();
			Product p = pb.getProduct();
			Color color = pb.getColor();
			
			if (i != 0 && p.getBrand().getBrand_ID() != brandId){
				//1. 写subtotal
				HSSFRow dataRow = sheet.createRow(DATA_ROW + i + countSubTotal);
				
				dataRow.createCell(BRAND_COLUMN).setCellValue("小计:");
				dataRow.createCell(QUANTITY_COLUMN).setCellValue(subTotalHands);
				dataRow.createCell(QUANTITY_SUM_COLUMN).setCellValue(subTotalQuantity);
				dataRow.createCell(RETAIL_PRICE_SUM_COLUMN).setCellValue(subTotalRetailPrice);
				
				subTotalHands = 0;
				subTotalQuantity = 0;
				subTotalRetailPrice = 0;
				countSubTotal++;
			}
			brandId = p.getBrand().getBrand_ID();
			
			HSSFRow dataRow = sheet.createRow(DATA_ROW + i + countSubTotal);
			//dataRow.createCell(BARCODE_COLUMN).setCellValue(pb.getBarcode());
			dataRow.createCell(YEAR_COLUMN).setCellValue(p.getYear().getYear());
			dataRow.createCell(QUARTER_COLUMN).setCellValue(p.getQuarter().getQuarter_Name());
			dataRow.createCell(BRAND_COLUMN).setCellValue(p.getBrand().getBrand_Name());
			dataRow.createCell(PRODUCT_CODE_COLUMN).setCellValue(p.getProductCode());
			if (color != null)
				dataRow.createCell(COLOR_COLUMN).setCellValue(color.getName());
			dataRow.createCell(CATEGORY_COLUMN).setCellValue(p.getCategory().getCategory_Name());
			dataRow.createCell(NUM_PER_HAND_COLUMN).setCellValue(p.getNumPerHand());
			dataRow.createCell(QUANTITY_COLUMN).setCellValue(cop.getQuantity());
			int qSum = p.getNumPerHand() * cop.getQuantity();
			dataRow.createCell(QUANTITY_SUM_COLUMN).setCellValue(qSum);
			dataRow.createCell(RETAIL_PRICE_COLUMN).setCellValue(p.getSalesPrice());

			dataRow.createCell(RETAIL_PRICE_SUM_COLUMN).setCellValue(cop.getSumRetailPrice());
			dataRow.createCell(BARCODE_COLUMN).setCellValue(cop.getProductBarcode().getBarcode());
			
			sumQuantity += qSum;
			sumHands += cop.getQuantity();
			sumRetailPrice += cop.getSumRetailPrice(); 
			
			subTotalHands += cop.getQuantity();
			subTotalQuantity += qSum;
			subTotalRetailPrice += cop.getSumRetailPrice(); 
			
			if (orderIdentity.equals(""))
				orderIdentity = cop.getOrderIdentity();
		}
		
		if (subTotalHands != 0 && subTotalRetailPrice != 0){
			//1. 写subtotal
			HSSFRow dataRow = sheet.createRow(DATA_ROW + orderProducts.size() + countSubTotal);
			
			dataRow.createCell(BRAND_COLUMN).setCellValue("小计:");
			dataRow.createCell(QUANTITY_COLUMN).setCellValue(subTotalHands);
			dataRow.createCell(QUANTITY_SUM_COLUMN).setCellValue(subTotalQuantity);
			dataRow.createCell(RETAIL_PRICE_SUM_COLUMN).setCellValue(subTotalRetailPrice);
			
			subTotalHands = 0;
			subTotalQuantity = 0;
			subTotalRetailPrice = 0;
			countSubTotal++;
		}
		
		dateRow.createCell(ORDER_IDENTITY_COLUMN).setCellValue(orderIdentity);
		
		HSSFRow dataRow = sheet.createRow(DATA_ROW + orderProducts.size() + countSubTotal);
		dataRow.createCell(YEAR_COLUMN).setCellValue("总计:");
		dataRow.createCell(QUANTITY_COLUMN).setCellValue(sumHands);
		dataRow.createCell(QUANTITY_SUM_COLUMN).setCellValue(sumQuantity);
		dataRow.createCell(RETAIL_PRICE_SUM_COLUMN).setCellValue(sumRetailPrice);
		
		return wb;
	}

}
