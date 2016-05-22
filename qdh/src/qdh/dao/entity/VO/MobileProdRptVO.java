package qdh.dao.entity.VO;

import qdh.dao.entity.product.ProductBarcode;

public class MobileProdRptVO extends HQProdRptVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8494586214121087738L;
	private Integer rank;
	private Integer myQuantity;

	
	public MobileProdRptVO(ProductBarcode pb, Integer quantity, Integer myQuantity, Integer rank){
		super(pb, quantity);
		this.rank = rank;
		this.myQuantity = myQuantity;
		setRetailPrice(pb.getProduct().getSalesPrice());
	}


	public Integer getRank() {
		return rank;
	}


	public void setRank(Integer rank) {
		this.rank = rank;
	}


	public Integer getMyQuantity() {
		return myQuantity;
	}


	public void setMyQuantity(Integer myQuantity) {
		this.myQuantity = myQuantity;
	}

}
