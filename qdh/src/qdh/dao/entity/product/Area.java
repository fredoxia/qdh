package qdh.dao.entity.product;

import java.io.Serializable;

import qdh.dao.entity.qxMIS.Area2;

public class Area extends Area2{
  public static final Serializable CURRENT_AREA = 1;
public Area(){
	  
  }
  public Area(int id){
	  this.setArea_ID(id);
  }
    
}
