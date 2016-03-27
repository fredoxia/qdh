package qdh.dao.entity.VO;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import qdh.dao.entity.order.CustOrderProduct;

public class FactoryOrderExcelVO extends AbstractExcelView {

	@Override
	protected void buildExcelDocument(Map<String, Object> dataMap,
			HSSFWorkbook wb, HttpServletRequest arg2, HttpServletResponse arg3)
			throws Exception {
		List<CustOrderProduct> orderProducts = (List<CustOrderProduct>)dataMap.get("data");

		for (CustOrderProduct cop : orderProducts){
			System.out.println(cop.getQuantity());
		}
		
	}

}
