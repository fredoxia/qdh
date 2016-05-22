package qdh.dao.entity.VO;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import qdh.dao.entity.order.Customer;

public class HQCustRptVO extends Customer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8494586214121087738L;

	private int quantity;
	private double sumRetailPrice;

	

	public double getSumRetailPrice() {
		return sumRetailPrice;
	}

	public void setSumRetailPrice(double sumRetailPrice) {
		this.sumRetailPrice = sumRetailPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public HQCustRptVO(Customer cust, int quantity, double sumRetailPrice) throws IllegalAccessException, InvocationTargetException{
		BeanUtils.copyProperties(this, cust);
		this.quantity = quantity;
		this.sumRetailPrice = sumRetailPrice;
	}
	
}
