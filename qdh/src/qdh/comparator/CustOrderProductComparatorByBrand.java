package qdh.comparator;

import java.util.Comparator;

import qdh.dao.entity.VO.CustOrderProductVO;

public class CustOrderProductComparatorByBrand implements Comparator<CustOrderProductVO> {

	@Override
	public int compare(CustOrderProductVO o1, CustOrderProductVO o2) {
		String brand1 = o1.getBrand();
		String brand2 = o2.getBrand();

		return brand1.compareTo(brand2);
	}

}
