package qdh.dao.entity.systemConfig;

import java.io.Serializable;
import java.sql.Timestamp;

public class OrderExportLog implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 7260317855871057124L;
	public OrderExportLog(){
		
	}

	private int id = 0;
	private String orderIdentity;
    private Timestamp importTime;
    private int numOfOrders;
    private int numOfError;
    private String operator;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderIdentity() {
		return orderIdentity;
	}
	public void setOrderIdentity(String orderIdentity) {
		this.orderIdentity = orderIdentity;
	}
	public Timestamp getImportTime() {
		return importTime;
	}
	public void setImportTime(Timestamp importTime) {
		this.importTime = importTime;
	}
	public int getNumOfOrders() {
		return numOfOrders;
	}
	public void setNumOfOrders(int numOfOrders) {
		this.numOfOrders = numOfOrders;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public int getNumOfError() {
		return numOfError;
	}
	public void setNumOfError(int numOfError) {
		this.numOfError = numOfError;
	}

	
}
