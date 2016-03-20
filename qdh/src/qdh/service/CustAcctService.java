package qdh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qdh.dao.entity.order.Customer;
import qdh.dao.entity.qxMIS.ChainStore2;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.dao.impl.qxMIS.ChainStore2DaoImpl;
import qdh.pageModel.DataGrid;
import qdh.utility.DateUtility;
import qdh.utility.StringUtility;

@Service
public class CustAcctService {

	@Autowired
	private CustomerDaoImpl customerDaoImpl;
	
	@Autowired
	private ChainStore2DaoImpl chainStore2DaoImpl;
	
	public DataGrid getCustAccts(Boolean isChain, String namePY) {
		DataGrid dataGrid = new DataGrid();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		
		if (isChain!= null){
			if (isChain){
				criteria.add(Restrictions.isNotNull("chainId"));
			} else {
				criteria.add(Restrictions.isNull("chainId"));
			}
		}
		
		if (namePY != null){
			
		}
		
		List<Customer> custs = customerDaoImpl.getByCritera(criteria, true);
		dataGrid.setRows(custs);
			
		return dataGrid;
	}

	public Response prepareAddEditCustAcct(Customer cust) {
		Response response = new Response();
		
		if (cust.getId() != 0)
		    cust = customerDaoImpl.get(cust.getId(), true);
		List<ChainStore2> chainStores = chainStore2DaoImpl.getActiveChainStores();
		
		Map dataMap = new HashMap();
		dataMap.put("cust", cust);
		dataMap.put("chainStores", chainStores);
		
		response.setReturnValue(dataMap);
		
		return response;
	}

	public Response addUpdateAcct(Customer cust, String userName) {
		Response response = new Response();
		
		Integer chainId = cust.getChainId();
		if (chainId != null && chainId != 0){
			ChainStore2 chainStore = chainStore2DaoImpl.get(chainId, true);
			cust.setChainStoreName(chainStore.getChainName());
		} else {
			cust.setChainId(null);
			cust.setChainStoreName("");
		}
		
		Customer custOrig = null;
		if (cust.getId() != 0){
			custOrig = customerDaoImpl.get(cust.getId(), true);
		}
		
		if (custOrig == null){
			cust.setUpdateUser(userName);
			cust.setUpdateDate(DateUtility.getToday());
			cust.setPassword(StringUtility.getRandomPassword());
			customerDaoImpl.save(cust, true);
		} else {
//			String[] ignoreProperties = new String[]{"password"};
			BeanUtils.copyProperties(cust, custOrig);
			customerDaoImpl.update(custOrig, true);
		}

		return response;
	}

}
