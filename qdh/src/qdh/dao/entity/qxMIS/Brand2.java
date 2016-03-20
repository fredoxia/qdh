package qdh.dao.entity.qxMIS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Brand2  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2808801063978393561L;
	/**
	 * 有尺码的货品
	 */
	public static final List<Integer> SIZE_BRAND = new ArrayList<Integer>();
	static {
		//誉尔赛
		SIZE_BRAND.add(66);
		//时尚童年
		SIZE_BRAND.add(63);
	}
	public static final int BRAND_SIZE = 3;
	public static final Integer[] BRAND_NOT_COUNT_INVENTORY = {new Integer(105), new Integer(106)};
	public static final int TYPE_ALL = -1;
	public static final int TYPE_CHAIN = 1;

    private int brand_ID;
    private String brand_Name;
    private String brand_Code;
    private String supplier;
    private String pinyin;
    private Integer chainId ;
 	
 	public Brand2(){
 		
 	}

	public Integer getChainId() {
		return chainId;
	}

	public void setChainId(Integer chainId) {
		this.chainId = chainId;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public Brand2(int brandId, String brandName){
 		this.brand_ID = brandId;
 		this.brand_Name = brandName;
 	}
	public int getBrand_ID() {
		return brand_ID;
	}
	public void setBrand_ID(int brand_ID) {
		this.brand_ID = brand_ID;
	}
	public String getBrand_Name() {
		return brand_Name;
	}
	public void setBrand_Name(String brand_Name) {
		this.brand_Name = brand_Name;
	}
	public String getBrand_Code() {
		return brand_Code;
	}
	public void setBrand_Code(String brand_Code) {
		this.brand_Code = brand_Code;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	@Override
	public String toString() {
		return "Brand [brand_ID=" + brand_ID + ", brand_Name=" + brand_Name
				+ "]";
	}
    
	
}
