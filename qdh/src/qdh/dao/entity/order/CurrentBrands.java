package qdh.dao.entity.order;

import java.io.Serializable;
import java.util.Date;

public class CurrentBrands implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4688789245578068255L;
	private CurrentBrandsId id;
	private String updateUser = "";
	private Date updateDate;

	public CurrentBrandsId getId() {
		return id;
	}


	public void setId(CurrentBrandsId id) {
		this.id = id;
	}


	public Date getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	public String getUpdateUser() {
		return updateUser;
	}


	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
