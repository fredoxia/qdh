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
	private Timestamp lastUpdateTime = DateUtility.getToday();
	private int status;
	
	
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
	
}
