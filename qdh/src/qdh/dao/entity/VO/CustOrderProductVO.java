package qdh.dao.entity.VO;

import java.io.Serializable;
import java.sql.Timestamp;

import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;
import qdh.utility.DateUtility;

public class CustOrderProductVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8713286109297381876L;
	private Integer pbId = null;
	private String year = "";
	private String quarter = "";
	private String brand = "";
	private String productCode ="";
	private String category ="";
	private String barcode = "";
	private String color = "";
	private String size = "";
    private int numPerHand;
    private String unit;
	private int quantity;
	private double sumWholePrice;
	private Timestamp lastUpdateTime = DateUtility.getToday();
	
	public CustOrderProductVO(){
		
	}
	
	public CustOrderProductVO(CustOrderProduct crp){
		ProductBarcode pb = crp.getProductBarcode();
		
    	Product p = pb.getProduct();
    	
    	pbId = pb.getId();
    	year = p.getYear().getYear();
    	quarter = p.getQuarter().getQuarter_Name();
    	brand = p.getBrand().getBrand_Name();
    	productCode = p.getProductCode();
    	numPerHand = p.getNumPerHand();
    	unit = p.getUnit();
    	barcode = pb.getBarcode();
    	if (pb.getColor() != null)
    		color = pb.getColor().getName();
    	
    	if (pb.getSize() != null)
    		size = pb.getSize().getName();
    	
    	category = p.getCategory().getCategory_Name();
    	
    	quantity = crp.getQuantity();
    	sumWholePrice = crp.getSumWholePrice();
    	lastUpdateTime = crp.getLastUpdateTime();
	}

	public Integer getPbId() {
		return pbId;
	}

	public void setPbId(Integer pbId) {
		this.pbId = pbId;
	}

	public int getNumPerHand() {
		return numPerHand;
	}

	public void setNumPerHand(int numPerHand) {
		this.numPerHand = numPerHand;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getSumWholePrice() {
		return sumWholePrice;
	}

	public void setSumWholePrice(double sumWholePrice) {
		this.sumWholePrice = sumWholePrice;
	}

	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	
}
