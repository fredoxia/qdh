package qdh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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
	
	public DataGrid getCustAccts(Boolean isChain, String namePY, String sort, String order) {
		DataGrid dataGrid = new DataGrid();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		
		System.out.println(sort + "," + order);
		
		if (isChain!= null){
			if (isChain){
				criteria.add(Restrictions.isNotNull("chainId"));
			} else {
				criteria.add(Restrictions.isNull("chainId"));
			}
		}
		
		if (namePY != null){
			
		}
		
		if (sort != null && !sort.trim().equals("")){
			if (order != null ){
				if (order.equalsIgnoreCase("asc") )
					criteria.addOrder(Order.asc(sort));
				else 
					criteria.addOrder(Order.desc(sort));
			}
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
		ChainStore2 heapStore2 = new ChainStore2();
		heapStore2.setChainId(-1);
		heapStore2.setChainName("");
		chainStores.add(0, heapStore2);
		dataMap.put("chainStores", chainStores);
		
		response.setReturnValue(dataMap);
		
		return response;
	}

	public Response addUpdateAcct(Customer cust, String userName) {
		Response response = new Response();
		
		Integer chainId = cust.getChainId();
		if (chainId != null && chainId != -1){
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
		
		cust.setUpdateUser(userName);
		cust.setUpdateDate(DateUtility.getToday());
		if (custOrig == null){
			cust.setPassword(StringUtility.getRandomPassword());
			customerDaoImpl.save(cust, true);
		} else {
			String[] ignoreProperties = new String[]{"password"};
			BeanUtils.copyProperties(cust, custOrig,ignoreProperties);
			customerDaoImpl.update(custOrig, true);
		}

		return response;
	}

	public Response deleteCustAcct(Integer id, String userName) {
		Response response = new Response();
		
		Customer cust = customerDaoImpl.get(id, true);
		
		if (cust == null){
			response.setFail("客户信息已经删除，不能再重复删除");
		} else {
			customerDaoImpl.delete(cust, true);
			response.setSuccess("客户信息 : " + cust.getCustName() + " 已经成功从订货系统删除");
		}
		return response;
	}

}
