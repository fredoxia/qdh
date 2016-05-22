package qdh.dao.entity.VO;

import java.io.Serializable;

import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;

public class HQProdRptVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8494586214121087738L;
	private int pbId;
	private String year;
	private String quarter;
	private String brand;
	private String barcode ;
	private String productCode;
	private String category ;
	private Integer numPerHand;
	private String color ;
	private String size ;
	private Integer quantity;
	private Integer retailPrice;
	

	public HQProdRptVO(ProductBarcode pb, Integer quantity){
		Product p = pb.getProduct();
		year = p.getYear().getYear();
		quarter = p.getQuarter().getQuarter_Name();
		brand = p.getBrand().getBrand_Name();
		productCode = p.getProductCode();
		numPerHand = p.getNumPerHand();
		category = p.getCategory().getCategory_Name();
		this.quantity = quantity;
		if (pb.getColor() != null){
			color = pb.getColor().getName();
		}
		if (pb.getSize() != null){
			size = pb.getSize().getName();
		}
		barcode = pb.getBarcode();
		pbId = pb.getId();
		
	}

	public int getPbId() {
		return pbId;
	}

	public void setPbId(int pbId) {
		this.pbId = pbId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String fullName) {
		this.barcode = fullName;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public int getNumPerHand() {
		return numPerHand;
	}

	public void setNumPerHand(Integer numPerHand) {
		this.numPerHand = numPerHand;
	}

	public Integer getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = (int)retailPrice;
	}
	
}
