package qdh.dao.entity.order;

import java.io.Serializable;
import java.util.Date;

import qdh.dao.entity.product.Brand;
import qdh.dao.entity.product.Quarter;
import qdh.dao.entity.product.Year;

public class CurrentBrands implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4688789245578068255L;
	private int id;
	private String updateUser = "";
	private Date updateDate;
	private Year year;
	private Quarter quarter;
	private Brand brand;
	
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


	public void setUpdateDate(Date updateDate) {
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
