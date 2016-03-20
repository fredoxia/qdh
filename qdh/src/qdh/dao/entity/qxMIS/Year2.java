package qdh.dao.entity.qxMIS;

import java.io.Serializable;

public class Year2 implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4420317812621918098L;
	private int year_ID;
    private String year;
    private String year_Code;
    
    public Year2(){
    	
    }
    
    public Year2(int year_ID, String year){
    	this.year_ID = year_ID;
    	this.year = year;
    }
    
	public int getYear_ID() {
		return year_ID;
	}
	public void setYear_ID(int year_ID) {
		this.year_ID = year_ID;
	}

	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getYear_Code() {
		return year_Code;
	}
	public void setYear_Code(String year_Code) {
		this.year_Code = year_Code;
	}

	@Override
	public String toString() {
		return "Year [year_ID=" + year_ID + ", year=" + year + "]";
	}
     
     
}
