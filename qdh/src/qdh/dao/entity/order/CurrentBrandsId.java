package qdh.dao.entity.order;

import java.io.Serializable;

import qdh.dao.entity.product.Brand;
import qdh.dao.entity.product.Quarter;
import qdh.dao.entity.product.Year;

public class CurrentBrandsId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	
	
}
