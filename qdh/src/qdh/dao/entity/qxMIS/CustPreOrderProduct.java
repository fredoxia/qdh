package qdh.dao.entity.qxMIS;

import java.io.Serializable;

import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.product.Product;
import qdh.dao.entity.product.ProductBarcode;

public class CustPreOrderProduct implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6385842852094944665L;
	private int preorderId;    
    private int pbId;   
    private int indexNum;
    /**
     * 手
     */
    private int totalQuantity;
    private double sumCost;
    private double sumWholePrice;
    private double sumRetailPrice;
    
    public CustPreOrderProduct(CustOrderProduct cop, int indexNum, int preorderId){
    	ProductBarcode pb = cop.getProductBarcode();
    	Product p = pb.getProduct();
    	
    	this.preorderId = preorderId;
    	this.indexNum = indexNum;
    	this.pbId = pb.getId();
    	this.totalQuantity = cop.getQuantity();
    	this.sumCost = p.getRecCost() * cop.getQuantity() * p.getNumPerHand();
    	this.sumWholePrice = p.getWholeSalePrice()* cop.getQuantity() * p.getNumPerHand();
    	this.sumRetailPrice = cop.getSumRetailPrice();
    }
    
	
	public int getPreorderId() {
		return preorderId;
	}
	public void setPreorderId(int preorderId) {
		this.preorderId = preorderId;
	}
	public int getIndexNum() {
		return indexNum;
	}
	public void setIndexNum(int indexNum) {
		this.indexNum = indexNum;
	}

	public int getPbId() {
		return pbId;
	}
	public void setPbId(int pbId) {
		this.pbId = pbId;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public double getSumCost() {
		return sumCost;
	}
	public void setSumCost(double sumCost) {
		this.sumCost = sumCost;
	}
	public double getSumWholePrice() {
		return sumWholePrice;
	}
	public void setSumWholePrice(double sumWholePrice) {
		this.sumWholePrice = sumWholePrice;
	}
	public double getSumRetailPrice() {
		return sumRetailPrice;
	}
	public void setSumRetailPrice(double sumRetailPrice) {
		this.sumRetailPrice = sumRetailPrice;
	}
    
}
