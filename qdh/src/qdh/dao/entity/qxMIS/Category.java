package qdh.dao.entity.qxMIS;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Category  implements Serializable{
//	public final static int CATEGORY_ID_NOT_PRODUCTS = 9;
	public final static int CATEGORY_NEI_YI = 7;
	public final static int CATEGORY_SHI_PING =11;
	public static final int TYPE_HEAD = -1;
	public static final int TYPE_CHAIN = 1;
    private int category_ID;
    private String category_Name;
    private String category_Code; 
    private int chainId = -1;
    private String typeS;
     
 	private static Map<Integer, String> typesMap = new HashMap<Integer, String>();
 	static {
 		typesMap.put(TYPE_HEAD, "总部类别");
 		typesMap.put(TYPE_CHAIN, "连锁店类别");
 	}

 	public int getChainId() {
		return chainId;
	}

	public void setChainId(int chainId) {
		this.chainId = chainId;
	}

	public String getTypeS() {
		return typesMap.get(chainId);
	}
	public int getCategory_ID() {
		return category_ID;
	}
	public void setCategory_ID(int category_ID) {
		this.category_ID = category_ID;
	}
	public String getCategory_Name() {
		return category_Name;
	}
	public void setCategory_Name(String category_Name) {
		this.category_Name = category_Name;
	}
	public String getCategory_Code() {
		return category_Code;
	}
	public void setCategory_Code(String category_Code) {
		this.category_Code = category_Code;
	}
     
     
}
