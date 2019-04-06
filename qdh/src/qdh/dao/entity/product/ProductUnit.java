package qdh.dao.entity.product;

import java.io.Serializable;

public class ProductUnit implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4420317812621918098L;
	private int id;
    private String productUnit;
    
    public ProductUnit(){
    	
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductUnit() {
		return productUnit;
	}

	public void setProductUnit(String productUnit) {
		this.productUnit = productUnit;
	}
  
}
