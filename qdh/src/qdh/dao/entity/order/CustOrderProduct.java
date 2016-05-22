package qdh.dao.entity.order;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import qdh.dao.entity.product.ProductBarcode;
import qdh.utility.DateUtility;

public class CustOrderProduct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6386603104692691461L;
	private ProductBarcode productBarcode = new ProductBarcode();
    private int custId ;
	private int quantity;
//	private double sumWholePrice = 0;
	private double sumRetailPrice;
	private Timestamp lastUpdateTime = DateUtility.getToday();
	private int status;
	private String orderIdentity = "";
	
	public CustOrderProduct(){
		
	}
	
	public CustOrderProduct(int custId, ProductBarcode pb, int quantity, String orderIdentity){
		this.custId = custId;
		this.productBarcode = pb;
		this.quantity = quantity;
		//this.sumWholePrice = quantity * pb.getProduct().getWholePrice();
		this.sumRetailPrice = quantity * pb.getProduct().getSalesPrice() * pb.getProduct().getNumPerHand();
		this.orderIdentity = orderIdentity;
	}
	
	
	public double getSumRetailPrice() {
		return sumRetailPrice;
	}

	public void setSumRetailPrice(double sumRetailPrice) {
		this.sumRetailPrice = sumRetailPrice;
	}

	public String getOrderIdentity() {
		return orderIdentity;
	}
	public void setOrderIdentity(String orderIdentity) {
		this.orderIdentity = orderIdentity;
	}
//	public double getSumWholePrice() {
//		return sumWholePrice;
//	}
//	public void setSumWholePrice(double sumWholePrice) {
//		this.sumWholePrice = sumWholePrice;
//	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public ProductBarcode getProductBarcode() {
		return productBarcode;
	}
	public void setProductBarcode(ProductBarcode productBarcode) {
		this.productBarcode = productBarcode;
	}
	public int getCustId() {
		return custId;
	}
	public void setCustId(int custId) {
		this.custId = custId;
	}    

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public void addQ(Integer q) {
		quantity += q;
		sumRetailPrice = productBarcode.getProduct().getSalesPrice() * quantity * this.getProductBarcode().getProduct().getNumPerHand();
		lastUpdateTime = DateUtility.getToday();
	}
	
	public void reduceQ(Integer q){
		if (quantity == 0)
			return ;
		else {
			if (quantity <= q)
				q = quantity;
			
			quantity -= q;
			sumRetailPrice = productBarcode.getProduct().getSalesPrice() * quantity  * this.getProductBarcode().getProduct().getNumPerHand();
			lastUpdateTime = DateUtility.getToday();
		}
	}
}
