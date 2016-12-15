package qdh.dao.entity.order;

import java.io.Serializable;

import qdh.dao.entity.product.Category;

public class ProductCategoryInSystem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5969851887600146386L;
	private int categoryId ;


	public ProductCategoryInSystem(){
		
	}
	
	public ProductCategoryInSystem(int categoryId){
		this.categoryId = categoryId;
	}
	
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
}
