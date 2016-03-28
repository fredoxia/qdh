package qdh.comparator;

import java.util.Comparator;

import qdh.dao.entity.VO.CustOrderProductVO;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.product.Product;

public class CustOrderProductComparatorByYQBrandProductCode implements Comparator<CustOrderProduct> {

	@Override
	public int compare(CustOrderProduct o1, CustOrderProduct o2) {
		Product p1 = o1.getProductBarcode().getProduct();
		Product p2 = o2.getProductBarcode().getProduct();
		
		int y1 = p1.getYear().getYear_ID();
		int y2 = p2.getYear().getYear_ID();
		int q1 = p1.getQuarter().getQuarter_ID();
		int q2 = p2.getQuarter().getQuarter_ID();
		int b1 = p1.getBrand().getBrand_ID();
		int b2 = p2.getBrand().getBrand_ID();
		String productCode1 = p1.getProductCode();
		String productCode2 = p2.getProductCode();
		
		int cp1 = y1 - y2;
		if (cp1 == 0){
			int cp2 = q1 - q2;
			if (cp2 == 0){
				int cp3 = b1 - b2;
				if (cp3 == 0){
					return productCode1.compareTo(productCode2);
				} else 
					return cp3;
			} else 
				return cp2;
		} else 
			return cp1;
	}

}
