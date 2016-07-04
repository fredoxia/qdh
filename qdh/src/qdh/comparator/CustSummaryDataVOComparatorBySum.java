package qdh.comparator;

import java.util.Comparator;

import qdh.dao.entity.VO.CustOrderProductVO;
import qdh.dao.entity.VO.CustSummaryDataVO;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.product.Product;

public class CustSummaryDataVOComparatorBySum implements Comparator<CustSummaryDataVO> {

	@Override
	public int compare(CustSummaryDataVO o1, CustSummaryDataVO o2) {
		return o2.getSumQ() - o1.getSumQ();
	}

}
