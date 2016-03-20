package qdh.dao.entity.qxMIS;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import qdh.dao.entity.product.Size;


public class ProductBarcode2 implements Serializable {
	public static final String BARCODE_PREFIX ="1";
	public static final String SERIAL_PREFIX = "2";
    public static final int STATUS_DELETE = 2;
    public static final int STATUS_OK = 1;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7555848597032873392L;
	private int id;
	private Product2 product = new Product2();
	private Color2 color;
	private Size2 size;
	private String barcode;
	private Integer chainId;

    private int status = 1;

	public ProductBarcode2(){
		
	}
	
	public ProductBarcode2(int id){
		this.setId(id);
	}
	
	public ProductBarcode2(Product2 product , Color2 color, Size2 size){
		this.setColor(color);
		this.setProduct(product);
		this.setSize(size);
		
	}
	
	public Integer getChainId() {
		return chainId;
	}

	public void setChainId(Integer chainId) {
		this.chainId = chainId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Product2 getProduct() {
		return product;
	}
	public void setProduct(Product2 product) {
		this.product = product;
	}
	public Color2 getColor() {
		return color;
	}
	public void setColor(Color2 color) {
		this.color = color;
	}
	public Size2 getSize() {
		return size;
	}
	public void setSize(Size2 size) {
		this.size = size;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductBarcode2 other = (ProductBarcode2) obj;
		if (id != 0 && id == other.getId())
			return true;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (other.color == null)
		    return false;
		  else if (color.getColorId() != other.color.getColorId())
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (other.product == null) 
			return false;
		  else if (!product.equals(other.product))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (other.size == null)
		    return false;
		  else if (size.getSizeId() != other.size.getSizeId())
			return false;
		return true;
	}
	
	
}
