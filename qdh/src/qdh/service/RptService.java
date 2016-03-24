package qdh.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qdh.dao.config.EntityConfig;
import qdh.dao.entity.VO.HQProdRptVO;
import qdh.dao.entity.order.CustOrder;
import qdh.dao.entity.product.ProductBarcode;
import qdh.dao.impl.order.CustOrderProdDaoImpl;
import qdh.dao.impl.product.ProductBarcodeDaoImpl;
import qdh.pageModel.DataGrid;
import qdh.pageModel.PageHelper;

@Service
public class RptService {

	@Autowired
	private CustOrderProdDaoImpl custOrderProdDaoImpl;
	
	@Autowired
	private ProductBarcodeDaoImpl productBarcodeDaoImpl;
	
	public DataGrid generateHQProdRpt(Integer page, Integer rowsPerPage, String sort, String order) {
		DataGrid dataGrid = new DataGrid();
		
		Object[] values = new Object[]{EntityConfig.DELETED};
		String countCriteria = "SELECT COUNT(DISTINCT productBarcode.id) FROM CustOrderProduct WHERE status != ?";
		String rptCriteria = "SELECT productBarcode.id, SUM(quantity) FROM CustOrderProduct WHERE status != ? GROUP BY productBarcode.id ORDER BY SUM(quantity) DESC";
		
		int totalRow = custOrderProdDaoImpl.executeHQLCount(countCriteria, values, false);
		PageHelper pHelper = new PageHelper(rowsPerPage, page, totalRow);
		dataGrid.setTotal(new Long(totalRow));
		
		List<Object> data = custOrderProdDaoImpl.executeHQLSelect(rptCriteria, values, pHelper.getPager(), false);
	     if (data != null && data.size() > 0){	
	    	 
	    	List<HQProdRptVO> rpts = new ArrayList<HQProdRptVO>();
			for (Object object : data)
			  if (object != null){
				Object[] recordResult = (Object[])object;
				if (recordResult[0] == null || recordResult[1] == null)
					continue;
				Integer productId = (Integer)recordResult[0];
				Long quantity =  (Long)recordResult[1];
				
				ProductBarcode pBarcode = productBarcodeDaoImpl.get(productId, true);
				HQProdRptVO rpt = new HQProdRptVO(pBarcode, quantity.intValue());
				rpts.add(rpt);
			  } 
			dataGrid.setRows(rpts);
	     }
		return dataGrid;
	}

}
