package qdh.comparator;

import java.util.Comparator;

import qdh.dao.entity.VO.CustOrderProductVO;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.product.Product;

public class CustOrderProductComparatorByBrandProductCode implements Comparator<CustOrderProduct> {

	@Override
	public int compare(CustOrderProduct o1, CustOrderProduct o2) {
		Product p1 = o1.getProductBarcode().getProduct();
		Product p2 = o2.getProductBarcode().getProduct();
		
		int b1 = p1.getBrand().getBrand_ID();
		int b2 = p2.getBrand().getBrand_ID();
		String productCode1 = p1.getProductCode();
		String productCode2 = p2.getProductCode();
		
		int comp1 = b1 - b2;
		if (comp1 == 0)
			return productCode1.compareTo(productCode2);
		else 
			return comp1;
	}

}
