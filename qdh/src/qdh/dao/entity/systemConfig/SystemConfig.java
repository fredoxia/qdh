package qdh.dao.entity.systemConfig;

import java.io.Serializable;

public class SystemConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4740544847905668922L;
	public static final int DEFAULT_KEY = 1;
	
	public static final int LOCK = 1;
	public static final int UNLOCK = 0;
	public static final int IS_SYSTEM_ADMIN_MODE = 1;
	public static final int NOT_SYSTEM_ADMIN_MODE = 0;

	public SystemConfig(){
		
	}

	private int id = 1;
	private int lockUpdateCust = 0;
	private int lockUpdateProduct = 0;
	private int systemAdminMode = 0;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLockUpdateCust() {
		return lockUpdateCust;
	}
	public void setLockUpdateCust(int lockDeleteCust) {
		this.lockUpdateCust = lockDeleteCust;
	}
	public int getLockUpdateProduct() {
		return lockUpdateProduct;
	}
	public void setLockUpdateProduct(int lockUpdateProduct) {
		this.lockUpdateProduct = lockUpdateProduct;
	}
	public int getSystemAdminMode() {
		return systemAdminMode;
	}
	public void setSystemAdminMode(int systemAdminMode) {
		this.systemAdminMode = systemAdminMode;
	}
	
	
}
