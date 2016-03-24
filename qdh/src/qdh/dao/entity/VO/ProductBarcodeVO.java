package qdh.dao.entity.VO;

import java.io.Serializable;
import java.sql.Timestamp;

import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;

public class ProductBarcodeVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7107967680824134744L;
	private String year = "";
	private String quarter = "";
	private String brand = "";
	private String productCode ="";
	private String category ="";
	private String barcode = "";
	private String color = "";
	private String size = "";
	private Timestamp createDate ;
    private int numPerHand;
    private String unit;

    /**
     * the chain store's sale price连锁店零售价
     */
    private double salesPrice;
    /**
     * the whole saler's sale price/chain store's cost,批发商发价1/连锁店进价
     */
    private double wholeSalePrice;
    /**
     * the whole saler's sale price/chain store's cost,批发商发价2/连锁店进价
     */    
    private double wholeSalePrice2;
    /**
     * the whole saler's sale price/chain store's cost,批发商发价3/连锁店进价
     */    
    private double wholeSalePrice3;

    /**
     * the whole saler's cost price批发商进价
     */
    private double recCost;
    /**
     * the price decided by factory 厂家零售价
     */
    private double salesPriceFactory;
    /**
     * the product's discount 默认折扣
     */
    private double discount;
    
    public ProductBarcodeVO(){
    	
    }
    
    public ProductBarcodeVO(ProductBarcode pb){
    	Product p = pb.getProduct();
    	
    	year = p.getYear().getYear();
    	quarter = p.getQuarter().getQuarter_Name();
    	brand = p.getBrand().getBrand_Name();
    	productCode = p.getProductCode();
    	createDate = pb.getCreateDate();
    	numPerHand = p.getNumPerHand();
    	unit = p.getUnit();
    	salesPrice = p.getSalesPrice();
    	wholeSalePrice = p.getWholeSalePrice();
    	wholeSalePrice2 = p.getWholeSalePrice2();
    	wholeSalePrice3 = p.getWholeSalePrice3();
    	recCost = p.getRecCost();
    	salesPriceFactory = p.getSalesPriceFactory();
    	discount = p.getDiscount();
    	barcode = pb.getBarcode();
    	if (pb.getColor() != null)
    		color = pb.getColor().getName();
    	
    	if (pb.getSize() != null)
    		size = pb.getSize().getName();
    	
    	category = p.getCategory().getCategory_Name();
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public int getNumPerHand() {
		return numPerHand;
	}
	public void setNumPerHand(int numPerHand) {
		this.numPerHand = numPerHand;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}
	public double getWholeSalePrice() {
		return wholeSalePrice;
	}
	public void setWholeSalePrice(double wholeSalePrice) {
		this.wholeSalePrice = wholeSalePrice;
	}
	public double getWholeSalePrice2() {
		return wholeSalePrice2;
	}
	public void setWholeSalePrice2(double wholeSalePrice2) {
		this.wholeSalePrice2 = wholeSalePrice2;
	}
	public double getWholeSalePrice3() {
		return wholeSalePrice3;
	}
	public void setWholeSalePrice3(double wholeSalePrice3) {
		this.wholeSalePrice3 = wholeSalePrice3;
	}
	public double getRecCost() {
		return recCost;
	}
	public void setRecCost(double recCost) {
		this.recCost = recCost;
	}
	public double getSalesPriceFactory() {
		return salesPriceFactory;
	}
	public void setSalesPriceFactory(double salesPriceFactory) {
		this.salesPriceFactory = salesPriceFactory;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
    
    

}
