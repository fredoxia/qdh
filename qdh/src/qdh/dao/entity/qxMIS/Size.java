package qdh.dao.entity.qxMIS;

import java.io.Serializable;

public class Size implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6930290273038803472L;
	private int sizeId;
    private String code;
    private String name;
    private int deleted;

    public Size(){
    	
    }
	public Size(Integer sizeId) {
		setSizeId(sizeId);
	}
	public int getSizeId() {
		return sizeId;
	}
	public void setSizeId(int sizeId) {
		this.sizeId = sizeId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	@Override
	public String toString() {
		return "Size [sizeId=" + sizeId + ", code=" + code + ", name=" + name
				+ ", deleted=" + deleted + "]";
	}

    
}
