package qdh.dao.entity.VO;

import java.io.Serializable;

import qdh.dao.entity.SQLServer.ClientsMS;

public class ClientJinSuanVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5239663675189434084L;
	private int clientId ;
	private String clientName;
	private String regionName;
	
	public ClientJinSuanVO(ClientsMS clientsMS){
		this.clientId = clientsMS.getClient_id();
		this.clientName = clientsMS.getName();
		this.regionName = clientsMS.getRegion().getName();
	}
	
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	
}
