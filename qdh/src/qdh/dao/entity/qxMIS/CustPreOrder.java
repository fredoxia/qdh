package qdh.dao.entity.qxMIS;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.utility.DateUtility;

public class CustPreOrder implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1151511966760113877L;
	private int id;
	private int custId ;
	private String orderIdentity;
	private String custName = "";
    private Integer chainId = null;
    private String chainStoreName = "";
    private int totalQuantity = 0;
    private Timestamp createDate = DateUtility.getToday();
    private double sumCost = 0;
    private double sumWholePrice = 0;
    private double sumRetailPrice = 0;
    private Timestamp exportDate = null;
    private int status;
    private String comment = "";
    
	public String getOrderIdentity() {
		return orderIdentity;
	}
	public void setOrderIdentity(String orderIdentity) {
		this.orderIdentity = orderIdentity;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Timestamp getExportDate() {
		return exportDate;
	}
	public void setExportDate(Timestamp exportDate) {
		this.exportDate = exportDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return custName;
	}


	public void setCustName(String custName) {
		this.custName = custName;
	}

	public Integer getChainId() {
		return chainId;
	}

	public void setChainId(Integer chainId) {
		this.chainId = chainId;
	}

	public String getChainStoreName() {
		return chainStoreName;
	}

	public void setChainStoreName(String chainStoreName) {
		this.chainStoreName = chainStoreName;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
