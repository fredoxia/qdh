package qdh.dao.entity.qxMIS;

import java.io.Serializable;

public class Color2 implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6930290273038803472L;
	private int colorId;
    private String code;
    private String name;
    private int deleted;
    
    public Color2(){
    	
    }
    
    public Color2(int colorId){
    	setColorId(colorId);
    }
    
    
	public int getColorId() {
		return colorId;
	}
	public void setColorId(int colorId) {
		this.colorId = colorId;
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
		return "Color [colorId=" + colorId + ", code=" + code + ", name="
				+ name + ", deleted=" + deleted + "]";
	}

    
}
