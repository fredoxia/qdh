package qdh.dao.entity.qxMIS;

import java.io.Serializable;

public class ChainStore2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 190083570538777704L;
	
	public static final int STATUS_DISABLED = 1;
	public static final int STATUS_NOT_ACTIVE = 2;
	public static final int STATUS_ACTIVE = 0;
	public static final int STATUS_DELETE = -1;
	
	private int chainId;
	private String chainName;
	private String ownerName;
	private int clientId;
	/**
	 * 正常活跃:0, 停用:1, 偶尔使用:2 
	 */
	private int status;
	public int getChainId() {
		return chainId;
	}
	public void setChainId(int chainId) {
		this.chainId = chainId;
	}
	public String getChainName() {
		return chainName;
	}
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	
}
