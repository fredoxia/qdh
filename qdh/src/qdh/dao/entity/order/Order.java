package qdh.dao.entity.order;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import qdh.utility.DateUtility;

public class Order implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1151511966760113877L;
	private String custName = "";
    private Integer chainId = null;
    private String chainStoreName = "";
    private int totalQuantity = 0;
    private Timestamp createDate = DateUtility.getToday();
	private Customer customer = null;
	private List<OrderProduct> orderProducts = new ArrayList<>();
	private int custId ;
	
	public List<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
