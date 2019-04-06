package qdh.dao.entity.product;


import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import qdh.dao.entity.systemConfig.ExcelTemplate;
import qdh.dao.impl.product.CategoryDaoImpl;
import qdh.dao.impl.product.ColorDaoImpl;
import qdh.dao.impl.product.ProductUnitDaoImpl;
import qdh.utility.DateUtility;
import qdh.utility.ExcelUtility;


public class BarcodeImportTemplate extends ExcelTemplate{
	private boolean isSuccess = true;
	private String validateMsg = "";
	
	private final int data_row = 1;

	private final int productCode_column= 3;
	private final int category_column= 5;
	private final int recCost_column= 6;
	private final int color_column= 4;
	private final int factorySalesPrice_column= 7;
	private final int discount_column= 8;
	private final int wholePrice_column= 9;
	private final int salePrice_column= 10;	
	private final int unit_column = 11;
	private final int fullHandQ_column = 12;
	private Area area = null;
	private Year year = null;
	private Quarter quarter = null;
	private Brand brand = null;
	
	private List<Object> wsData = new ArrayList<Object>();
	
	/**
	 * 传入文件，生出条码
	 */
	public BarcodeImportTemplate(File file, Year year, Quarter quarter, Brand brand, Area area){
		super();
		try {
		  templateWorkbook = new HSSFWorkbook(new FileInputStream(file));
			this.brand = brand;
			this.year = year;
			this.quarter = quarter;
			this.area = area;
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 验证所有信息正确
	 * @param categoryDaoImpl
	 * @param colorDaoImpl
	 * @return
	 */
	private void validate(CategoryDaoImpl categoryDaoImpl, ColorDaoImpl colorDaoImpl, ProductUnitDaoImpl productUnitDaoImpl){
		StringBuilder errorMsg = new StringBuilder();
		boolean validate = true;
		
		if (templateWorkbook == null){
			isSuccess = false;
			validateMsg = "上传的文件是空文件";
			return ;
		}
		
		HSSFSheet sheet = templateWorkbook.getSheetAt(0);

		int rowDisplay = 0;
		boolean rowError = true;
		for (int i = data_row; ;i++){
			rowError = true;
			rowDisplay = i +1;
			HSSFRow row = sheet.getRow(i);
			if (row == null)
				break;
			
			String productCode = ExcelUtility.getPuzzleString(row.getCell(productCode_column));
			

			if (productCode.equals("")){
				rowError = false;
				errorMsg.append("行" + rowDisplay + ": 产品货号为空. ");
			}
			
			HSSFCell categoryCell = row.getCell(category_column);
			HSSFCell colourCell = row.getCell(color_column);
			if (categoryCell == null){
				rowError = false;
				errorMsg.append("行" + rowDisplay + ": 产品种类为空. ");
			} else {
				String categoryString = categoryCell.getStringCellValue().trim();
				if (!categoryDaoImpl.checkCategoryExist(categoryString)){
					rowError = false;
					errorMsg.append("行" + rowDisplay + ": 产品种类 (" + categoryString + ") 不存在. ");
				}
			}
			
			if (colourCell == null){
				rowError = false;
				errorMsg.append("行" + rowDisplay + ": 颜色为空. ");
			} else {
				String colorsString = colourCell.getStringCellValue().trim();
				if (colorsString.equals(""))
					continue;
				else {
					String colorStringArray[] = colorsString.split(",");
					for (String colorString: colorStringArray){
						if (!colorDaoImpl.checkColorExist(colorString)){
							rowError = false;
							errorMsg.append("行" + rowDisplay + ": 颜色(" + colorString + ") 不存在. ");
						}
					}
				}
			}
			
			try {
			   double recCost = row.getCell(recCost_column).getNumericCellValue();
			   if (recCost <= 0) {
			     rowError = false;
				 errorMsg.append("行" + rowDisplay + ": 进价("+ recCost+")错误. ");
			   }
			} catch (Exception e){
				rowError = false;
				errorMsg.append("行" + rowDisplay + ": 进价错误. ");
			}
			
			try {
				   double salePrice = row.getCell(salePrice_column).getNumericCellValue();
				   if (salePrice <= 0) {
				     rowError = false;
					 errorMsg.append("行" + rowDisplay + ": 发价("+ salePrice+")错误. ");
				   }
				} catch (Exception e){
					rowError = false;
					errorMsg.append("行" + rowDisplay + ": 发价错误. ");
				}
			
			String unit = ExcelUtility.getPuzzleString(row.getCell(unit_column));
			
			if (unit.equals("")){
				rowError = false;
				errorMsg.append("行" + rowDisplay + ": 产品单位为空. ");
			} else {
				if (!productUnitDaoImpl.checkExist(unit)){
					rowError = false;
					errorMsg.append("行" + rowDisplay + ": 产品单位 (" + unit + ") 不存在. ");
				}
			}
			
			try {
				   int numPerHand = (int) row.getCell(fullHandQ_column).getNumericCellValue();
				   if (numPerHand <= 0) {
				     rowError = false;
					 errorMsg.append("行" + rowDisplay + ": 齐手数量("+ numPerHand+")错误. ");
				   }
				} catch (Exception e){
					rowError = false;
					errorMsg.append("行" + rowDisplay + ": 齐手数量 错误. ");
				}
			
			
			if (rowError == false){
				validate = false;
				errorMsg.append("<br>");
			}
		}
		
		isSuccess = validate;
		
		validateMsg =  errorMsg.toString();
	}
	
	/**
	 * 通过文件读出条码，返回
	 * @return
	 */
	public void proccess(CategoryDaoImpl categoryDaoImpl, ColorDaoImpl colorDaoImpl, ProductUnitDaoImpl productUnitDaoImpl){
		validate(categoryDaoImpl, colorDaoImpl, productUnitDaoImpl);
		
		if (!isSuccess)
			return ;

		HSSFSheet sheet = templateWorkbook.getSheetAt(0);

		for (int i = data_row; ;i++){

			HSSFRow row = sheet.getRow(i);
			if (row == null)
				break;
			
			HSSFCell categoryCell = row.getCell(category_column);
			HSSFCell colourCell = row.getCell(color_column);
			String categoryString = categoryCell.getStringCellValue().trim();
			Category category = categoryDaoImpl.getCategoryByName(categoryString);
			
			HSSFCell productUnitCell = row.getCell(unit_column);
			String productUnitString = productUnitCell.getStringCellValue().trim();
			
			HSSFCell numPerHandCell = row.getCell(fullHandQ_column);
			int numPerHand = (int)numPerHandCell.getNumericCellValue();
			
			
			double salePrice = row.getCell(salePrice_column).getNumericCellValue();
			double recCost = row.getCell(recCost_column).getNumericCellValue();
			double factorySalePrice = row.getCell(factorySalesPrice_column).getNumericCellValue();
			double discount = row.getCell(discount_column).getNumericCellValue();
			double wholePrice = row.getCell(wholePrice_column).getNumericCellValue();
			String productCode = ExcelUtility.getPuzzleString(row.getCell(productCode_column));
			
			Product product = new Product();
			product.setArea(area);
			product.setBrand(brand);
			product.setYear(year);
			product.setQuarter(quarter);
			product.setCategory(category);
			product.setCreateDate(DateUtility.getToday());
			product.setProductCode(productCode);
			product.setDiscount(discount);
			product.setSalesPrice(salePrice);
			product.setWholeSalePrice(wholePrice);
			product.setUnit(productUnitString);
			product.setNumPerHand(numPerHand);
			product.setSalesPriceFactory(factorySalePrice);
			product.setRecCost(recCost);
			
			
			String colorsString = colourCell.getStringCellValue().trim();
			List<Integer> colorIds = new ArrayList<Integer>();
			if (!colorsString.equals("")){
				String colorStringArray[] = colorsString.split(",");
				
				for (String colorString: colorStringArray){
					Color color = colorDaoImpl.getColorByName(colorString);
					colorIds.add(color.getColorId());
				}
			}
		
			List<Object> rowData = new ArrayList<Object>();
			rowData.add(product);
			rowData.add(colorIds);
			
			wsData.add(rowData);
		}
	}

	public boolean isSuccess() {
		return isSuccess;
	}


	public String getValidateMsg() {
		return validateMsg;
	}


	public List<Object> getWsData() {
		return wsData;
	}



}
