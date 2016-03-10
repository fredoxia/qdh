package qdh.dao.entity.qxMIS;

import java.io.Serializable;

public class NumPerHand implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4420317812621918098L;
	private int id;
    private int numPerHand;
    
    public NumPerHand(){
    	
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumPerHand() {
		return numPerHand;
	}

	public void setNumPerHand(int numPerHand) {
		this.numPerHand = numPerHand;
	}


  
}
