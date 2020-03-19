package qdh.dao.entity.product;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;


public class BarcodeTemplate extends qdh.dao.entity.systemConfig.ExcelTemplate{
	protected final int data_row = 2;
	protected final int year_column= 0;
	protected final int quarter_column= 1;
	protected final int brand_column= 2;
	protected final int productCode_column= 3;
	protected final int barcode_column= 4;
	protected final int category_column= 5;
	
	protected final int recCost_column= 6;
	protected final int wholePrice_column= 7;
	protected final int salePrice_column= 8;
	protected final int delete_column= 9;
	
	
	private List<ProductBarcode> products = new ArrayList<ProductBarcode>();

	public BarcodeTemplate(){
		super();
	}
	
	/**
	 * 传入barcode对象，生成文件
	 * @param ProductBarcodes
	 * @param templateWorkbookPath
	 * @throws IOException
	 */
	public BarcodeTemplate(List<ProductBarcode> ProductBarcodes, String templateWorkbookPath) throws IOException{
		super(templateWorkbookPath);
		this.products = ProductBarcodes;	
	}
	
	/**
	 * 传入文件，生出条码
	 */
	public BarcodeTemplate(File file){
		super();
		try {
		  templateWorkbook = new HSSFWorkbook(new FileInputStream(file));
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过文件读出条码，返回
	 * @return
	 */
	public List<String> proccess(){
		List<String> barcodeList = new ArrayList<String>();
		
		if (templateWorkbook == null)
			return null;
		
		HSSFSheet sheet = templateWorkbook.getSheetAt(0);

		for (int i = data_row; ;i++){
			HSSFRow row = sheet.getRow(i);
			if (row == null)
				break;
			
			HSSFCell cell = row.getCell(barcode_column);
			if (cell == null)
				break;
			else {
				String barcodeValue = cell.getStringCellValue();
				if (barcodeValue == null || barcodeValue.trim().equals(""))
					break;
				else {
					HSSFCell deleteCell = row.getCell(delete_column);
					
					if (deleteCell == null)
						continue;
					
					String deleteValue = deleteCell.getStringCellValue();
					
					if (deleteValue != null && deleteValue.trim().equalsIgnoreCase("y")){
						barcodeList.add(barcodeValue);
					}
				}
			}
		}
		
		return barcodeList;
	}
	
	/**
	 * 通过条码创建excel下载
	 * @return
	 */
	public HSSFWorkbook process(){
		HSSFSheet sheet = templateWorkbook.getSheetAt(0);

		if (products != null)
			for (int i = 0; i < products.size(); i++){
				ProductBarcode productBarcode = products.get(i);
				Product product = productBarcode.getProduct();
				
				Row row = sheet.createRow(data_row + i);
				
				row.createCell(year_column).setCellValue(product.getYear().getYear());
				row.createCell(quarter_column).setCellValue(product.getQuarter().getQuarter_Name());
				row.createCell(brand_column).setCellValue(product.getBrand().getBrand_Name());
				Color color = productBarcode.getColor();
				if (color == null)
				    row.createCell(productCode_column).setCellValue(product.getProductCode());
				else 
					row.createCell(productCode_column).setCellValue(product.getProductCode() + color.getName());
				row.createCell(barcode_column).setCellValue(productBarcode.getBarcode());
				
				row.createCell(category_column).setCellValue(product.getCategory().getCategory_Name());
				
				row.createCell(wholePrice_column).setCellValue(product.getWholeSalePrice());
				row.createCell(recCost_column).setCellValue(product.getRecCost());
				row.createCell(salePrice_column).setCellValue(product.getSalesPrice());
			}

		return templateWorkbook;
	}

}
