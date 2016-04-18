package qdh.dao.entity.SQLServer;

import java.io.Serializable;

public class ClientsMS implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8838884107154260065L;
	private int client_id;
    private String name;
    private String pinyin;
    private int region_id;
    private boolean deleted;
    private RegionMS region = new RegionMS();
    
    
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public int getRegion_id() {
		return region_id;
	}
	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}
	public RegionMS getRegion() {
		return region;
	}
	public void setRegion(RegionMS region) {
		this.region = region;
	}
	public int getClient_id() {
		return client_id;
	}
	public void setClient_id(int client_id) {
		this.client_id = client_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
