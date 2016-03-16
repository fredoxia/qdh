package qdh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qdh.dao.entity.VO.CurrentBrandVO;
import qdh.dao.entity.order.CurrentBrands;
import qdh.dao.impl.order.CurrentBrandsDaoImpl;
import qdh.pageModel.DataGrid;

@Service
public class ProdOperationService {
	
	@Autowired
	private CurrentBrandsDaoImpl currentBrandsDaoImpl;
	
	/**
	 * 获取所有的current brands然后组成datagrid
	 * @return
	 */
	public DataGrid getCurrentBrands(){
		DataGrid dg = new DataGrid();
		
		List<CurrentBrands> currentBrands = currentBrandsDaoImpl.getAll(true);
		List<CurrentBrandVO> currentBrandVOs = new ArrayList<>();
		for (CurrentBrands currrentBrand : currentBrands){
			currentBrandVOs.add(new CurrentBrandVO(currrentBrand));
		}
		dg.setRows(currentBrandVOs);
		dg.setTotal(new Long(currentBrandVOs.size()));
		return dg;		
	}
}
