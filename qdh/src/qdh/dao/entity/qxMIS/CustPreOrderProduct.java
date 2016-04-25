package qdh.dao.entity.qxMIS;

import java.io.Serializable;

public class CustPreOrderProduct implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6385842852094944665L;
	private CustPreOrder custPreOrder = null;    
    private int pbId;   
    private int indexNum;
    private int totalQuantity;
    private double sumCost;
    private double sumWholePrice;
    private double sumRetailPrice;
    
    
	public CustPreOrder getCustPreOrder() {
		return custPreOrder;
	}
	public void setCustPreOrder(CustPreOrder custPreOrder) {
		this.custPreOrder = custPreOrder;
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
