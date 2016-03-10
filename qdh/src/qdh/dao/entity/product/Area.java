package qdh.dao.entity.product;

import java.io.Serializable;

public class Area  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1133169033276192672L;
	private int area_ID;
    private String area_Name;
    private String area_Code;
    
	public int getArea_ID() {
		return area_ID;
	}
	public void setArea_ID(int area_ID) {
		this.area_ID = area_ID;
	}
	public String getArea_Name() {
		return area_Name;
	}
	public void setArea_Name(String area_Name) {
		this.area_Name = area_Name;
	}
	public String getArea_Code() {
		return area_Code;
	}
	public void setArea_Code(String area_Code) {
		this.area_Code = area_Code;
	}
    
}
