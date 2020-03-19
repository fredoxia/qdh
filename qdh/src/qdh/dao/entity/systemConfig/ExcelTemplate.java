package qdh.dao.entity.systemConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import qdh.utility.loggerLocal;

public class ExcelTemplate {
	protected HSSFWorkbook templateWorkbook;
	protected String templateWorkbookPath;
	protected CreationHelper createHelper;
	protected CellStyle dateStyle;
	protected CellStyle aroundLineStyle;
	protected CellStyle highLigntStyle;
	
	
	public HSSFWorkbook getTemplateWorkbook() {
		return templateWorkbook;
	}

	public void setTemplateWorkbook(HSSFWorkbook templateWorkbook) {
		this.templateWorkbook = templateWorkbook;
	}

	public String getTemplateWorkbookPath() {
		return templateWorkbookPath;
	}

	public void setTemplateWorkbookPath(String templateWorkbookPath) {
		this.templateWorkbookPath = templateWorkbookPath;
	}

	public CreationHelper getCreateHelper() {
		return createHelper;
	}

	public void setCreateHelper(CreationHelper createHelper) {
		this.createHelper = createHelper;
	}

	public CellStyle getDateStyle() {
		return dateStyle;
	}

	public void setDateStyle(CellStyle dateStyle) {
		this.dateStyle = dateStyle;
	}
	
	public ExcelTemplate(){
		
	}

	public ExcelTemplate(String templateWorkbookPath) throws IOException{
		this.templateWorkbookPath = templateWorkbookPath;
		
		InputStream is;   
		HSSFWorkbook wb = null;
		try {
			is = new FileInputStream(this.templateWorkbookPath);
			wb = new HSSFWorkbook(is);   
		} catch (FileNotFoundException e) {
			loggerLocal.error(e);
			throw e;
		} catch (IOException e) {
			loggerLocal.error(e);
			throw e;
		}   
		
        initialize(wb);
		
	}
	
	public  ExcelTemplate(File templateWorkbookFile) throws IOException{
		InputStream is;   
		HSSFWorkbook wb = null;
		try {
			is = new FileInputStream(templateWorkbookFile);
			wb = new HSSFWorkbook(is);   
		} catch (FileNotFoundException e) {
			loggerLocal.error(e);
			throw e;
		} catch (IOException e) {
			loggerLocal.error(e);
			throw e;
		}   
		
        initialize(wb);
	}
	
	private void initialize(HSSFWorkbook wb){
		this.templateWorkbook = wb;
		
		createHelper = templateWorkbook.getCreationHelper();

		dateStyle = templateWorkbook.createCellStyle();
		dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd hh:mm:ss"));
	
		highLigntStyle = templateWorkbook.createCellStyle();
		highLigntStyle.setFillBackgroundColor(HSSFFont.COLOR_NORMAL);
		Font boldFont = templateWorkbook.createFont();
		boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		highLigntStyle.setFont(boldFont);
		
		aroundLineStyle = templateWorkbook.createCellStyle();
		aroundLineStyle.setBorderLeft((short) 1);
		aroundLineStyle.setBorderRight((short) 1);
		aroundLineStyle.setBorderTop((short) 1);
		aroundLineStyle.setBorderBottom((short) 1);
		Font font = templateWorkbook.createFont();
		font.setFontHeightInPoints((short)17);
		aroundLineStyle.setFont(font);
		aroundLineStyle.setWrapText(true);
	}
	
	protected Cell createCell(Row row, int column){
		Cell cell = row.createCell(column);
		return cell;
	}

}
