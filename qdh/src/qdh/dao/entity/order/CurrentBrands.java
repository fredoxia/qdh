package qdh.dao.entity.order;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.poi.ss.usermodel.DateUtil;

import qdh.dao.entity.product.Brand;
import qdh.dao.entity.product.Quarter;
import qdh.dao.entity.product.Year;
import qdh.utility.DateUtility;

public class CurrentBrands implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4688789245578068255L;
	private int id;
	private String updateUser = "";
	private Timestamp updateDate;
	private Year year;
	private Quarter quarter;
	private Brand brand;
	private int numOfBarcodes;
	
	public CurrentBrands(){
		
	}
	
	public CurrentBrands(int yearId, int quarterId, int brandId, int numOfBarcodes, String updateUser){
		Year year = new Year();
		year.setYear_ID(yearId);
		
		Quarter quarter =new Quarter();
		quarter.setQuarter_ID(quarterId);
		
		Brand brand = new Brand();
		brand.setBrand_ID(brandId);
		
		this.setYear(year);
		this.setQuarter(quarter);
		this.setBrand(brand);
		this.setUpdateUser(updateUser);
		this.setNumOfBarcodes(numOfBarcodes);
		this.setUpdateDate(DateUtility.getToday());
	}
	
	public int getNumOfBarcodes() {
		return numOfBarcodes;
	}

	public void setNumOfBarcodes(int numOfBarcode) {
		this.numOfBarcodes = numOfBarcode;
	}

	public Year getYear() {
		return year;
	}
	public void setYear(Year year) {
		this.year = year;
	}
	public Quarter getQuarter() {
		return quarter;
	}
	public void setQuarter(Quarter quarter) {
		this.quarter = quarter;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Date getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}


	public String getUpdateUser() {
		return updateUser;
	}


	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
