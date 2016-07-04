package qdh.dao.entity.VO;

import java.util.ArrayList;
import java.util.List;

public class CustSummaryDataVO {
	private String custName = "";
	private List<Integer> orderQ = new ArrayList<>();
	private int sumQ = 0;
	
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public List<Integer> getOrderQ() {
		return orderQ;
	}
	public void setOrderQ(List<Integer> orderQ) {
		this.orderQ = orderQ;
	}
	public int getSumQ() {
		return sumQ;
	}
	public void setSumQ(int sumQ) {
		this.sumQ = sumQ;
	}
	
	
}
