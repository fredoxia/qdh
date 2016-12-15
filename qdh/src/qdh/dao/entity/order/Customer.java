package qdh.dao.entity.order;

import java.beans.Transient;
import java.io.Serializable;
import java.sql.Timestamp;

public class Customer implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -572552511649843048L;
	private int id;
	private String custName;
	private String custRegion;
	private String password;
	private String chainStoreName;
	private Integer chainId;
	private String updateUser;
	private Timestamp updateDate;
	private int status;
	
	/**
	 * custFullName = custName + region
	 */
	private String custFullName;
	
	public String getCustRegion() {
		return custRegion;
	}
	public void setCustRegion(String custRegion) {
		this.custRegion = custRegion;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getChainStoreName() {
		return chainStoreName;
	}
	public void setChainStoreName(String chainStoreName) {
		this.chainStoreName = chainStoreName;
	}
	public Integer getChainId() {
		return chainId;
	}
	public void setChainId(Integer chainId) {
		this.chainId = chainId;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	
	public String getCustFullName() {
		return custName + " " + custRegion;
	}
	public String getCustNameVO(){
		String name = custName;
		
		if (chainStoreName != null && chainStoreName.trim().length() > 0){
			String storeNamec = chainStoreName.trim();
			int length = storeNamec.length();
			
			if (length >4)
				name += " " + storeNamec.substring(0, 4);
			else 
				name += " " + storeNamec;
		}
		
		return name;
	}
	
}
