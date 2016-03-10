package qdh.dao.entity.qxMIS;

import java.io.Serializable;

public class Quarter  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8446289926712130617L;
	public static final int QUARTER_FOUR_SEARSON = 8;
	private int quarter_ID;
    private String quarter_Name;
    private String quarter_Code;
    
    public Quarter(){
    	
    }
    
    public Quarter(int quarterId, String quarterName){
    	this.quarter_ID = quarterId;
    	this.quarter_Name = quarterName;
    }
    
	public int getQuarter_ID() {
		return quarter_ID;
	}
	public void setQuarter_ID(int quarter_ID) {
		this.quarter_ID = quarter_ID;
	}
	public String getQuarter_Name() {
		return quarter_Name;
	}
	public void setQuarter_Name(String quarter_Name) {
		this.quarter_Name = quarter_Name;
	}
	public String getQuarter_Code() {
		return quarter_Code;
	}
	public void setQuarter_Code(String quarter_Code) {
		this.quarter_Code = quarter_Code;
	}

	@Override
	public String toString() {
		return "Quarter [quarter_ID=" + quarter_ID + ", quarter_Name="
				+ quarter_Name + "]";
	}
    
    
}
