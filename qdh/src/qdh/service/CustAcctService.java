package qdh.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.comparator.CustOrderProductComparatorByYQBrandProductCode;
import qdh.dao.config.EntityConfig;
import qdh.dao.entity.SQLServer.ClientsMS;
import qdh.dao.entity.VO.ClientJinSuanVO;
import qdh.dao.entity.VO.CustOrderProductVO;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.dao.entity.qxMIS.ChainStore2;
import qdh.dao.impl.Response;
import qdh.dao.impl.SQLServer.ClientDAOImpl;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.dao.impl.order.CustOrderDaoImpl;
import qdh.dao.impl.order.CustOrderProductDaoImpl;
import qdh.dao.impl.qxMIS.ChainStore2DaoImpl;
import qdh.dao.impl.systemConfig.SystemConfigDaoImpl;
import qdh.pageModel.DataGrid;
import qdh.utility.DateUtility;
import qdh.utility.StringUtility;

@Service
public class CustAcctService {
	private final int CUST_TYPE_CHAIN = 1;
	private final int CUST_TYPE_OTHER = 2;

	@Autowired
	private CustomerDaoImpl customerDaoImpl;
	
	@Autowired
	private ChainStore2DaoImpl chainStore2DaoImpl;
	
	@Autowired
	private CustOrderDaoImpl orderDaoImpl;
	
	@Autowired
	private CustOrderProductDaoImpl custOrderProductDaoImpl;
	
	@Autowired
	private SystemConfigDaoImpl systemConfigDaoImpl;
	
	@Autowired
	private ClientDAOImpl clientDAOImpl;
	
	
	public DataGrid getCustAccts(Integer isChain, Integer status, String name, String sort, String order) {
		DataGrid dataGrid = new DataGrid();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		
		System.out.println(sort + "," + order);
		
		if (isChain!= null && isChain != EntityConfig.ALL_RECORD){
			if (isChain == CUST_TYPE_CHAIN){
				criteria.add(Restrictions.isNotNull("chainId"));
			} else {
				criteria.add(Restrictions.isNull("chainId"));
			}
		}
		
		if (status == null)
			status = EntityConfig.ACTIVE;
		if (status != null && status != EntityConfig.ALL_RECORD)
			criteria.add(Restrictions.eq("status", status));
		
		if (name != null && !name.trim().equals("")){
			criteria.add(Restrictions.or(Restrictions.like("chainStoreName", "%" + name + "%"), Restrictions.like("custName", "%" + name + "%")));
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

//	public Response addUpdateAcct(Customer cust, String userName) {
//		Response response = new Response();
//		
//		Integer chainId = cust.getChainId();
//		if (chainId != null && chainId != -1){
//			ChainStore2 chainStore = chainStore2DaoImpl.get(chainId, true);
//			cust.setChainStoreName(chainStore.getChainName());
//		} else {
//			cust.setChainId(null);
//			cust.setChainStoreName("");
//		}
//		
//		Customer custOrig = null;
//		if (cust.getId() != 0){
//			if (!systemConfigDaoImpl.canUpdateCust()){
//				response.setFail("管理员已经锁定客户信息和单据更新,请联系管理员");
//				return response;
//			}
//			custOrig = customerDaoImpl.get(cust.getId(), true);
//		}
//		
//		cust.setUpdateUser(userName);
//		cust.setUpdateDate(DateUtility.getToday());
//		if (custOrig == null){
//			cust.setPassword(StringUtility.getRandomPassword());
//			customerDaoImpl.save(cust, true);
//		} else {
//			String[] ignoreProperties = new String[]{"password"};
//			BeanUtils.copyProperties(cust, custOrig,ignoreProperties);
//			customerDaoImpl.update(custOrig, true);
//		}
//
//		return response;
//	}

	@Transactional
	public Response inactiveCustAcct(Integer id, String userName) {
		Response response = new Response();
		
		//1. delete the customer information
		Customer cust = customerDaoImpl.get(id, true);
		if (cust == null){
			response.setFail("客户信息不存在");
			return response;
		} else {
			if (!systemConfigDaoImpl.canUpdateCust()){
				response.setFail("管理员已经锁定客户信息和单据更新,请联系管理员");
				return response;
			}
			cust.setStatus(EntityConfig.INACTIVE);
			customerDaoImpl.update(cust, true);
		}
		
		//2。delete the order and order_product
		Object[] values = new Object[]{EntityConfig.INACTIVE, id};
		String U_ORDER_PRO = "UPDATE CustOrderProduct SET status =? WHERE custId =?";
		int numOfRecords = custOrderProductDaoImpl.executeHQLUpdateDelete(U_ORDER_PRO, values, true);
		String activeRecords = "";
		if (numOfRecords > 0)
			activeRecords = numOfRecords + "条客户订单记录也被冻结";
		
		response.setSuccess("客户信息 : " + cust.getCustName() + " 已经成功被冻结." + activeRecords);
		return response;
	}

	/**
	 * 通过Id查找有无订单
	 * @param id
	 * @return
	 */
	@Transactional
	public Response checkCustOrder(Integer id) {
		Response response = new Response();
		
		String queryString = "SELECT COUNT(*) FROM CustOrderProduct WHERE custId =? AND status <> ?";
		Object[] values = new Object[]{id, EntityConfig.INACTIVE};
		
		int rows = custOrderProductDaoImpl.executeHQLCount(queryString, values, true);
		
		if (rows == 0)
			response.setFail("当前客户没有订单");
		else {
			Customer cust = customerDaoImpl.get(id, true);
			response.setSuccess("");
			response.setReturnValue(cust);
		}
		
		return response;
	}
	
	/**
	 * 通过Id查找有无订单
	 * @param id
	 * @return
	 */
	public DataGrid getCustOrder(Integer id) {
		DataGrid dataGrid = new DataGrid();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
		criteria.add(Restrictions.eq("custId", id));
		criteria.add(Restrictions.ne("status", EntityConfig.INACTIVE));
		
		List<CustOrderProduct> products = custOrderProductDaoImpl.getByCritera(criteria, true);

		List<CustOrderProductVO> footer = new ArrayList<>();
		
		dataGrid.setRows(transferCustOrderProductVOs(products,footer));
		dataGrid.setFooter(footer);
		dataGrid.setTotal(new Long(20));
		
		return dataGrid;
	}



	public static List<CustOrderProductVO> transferCustOrderProductVOs(List<CustOrderProduct> products,List<CustOrderProductVO> footer){
		List<CustOrderProductVO> vos = new ArrayList<>();
		int totalQ = 0;
		double totalSum = 0;
		if (products != null){
			Collections.sort(products, new CustOrderProductComparatorByYQBrandProductCode());
			
			for (CustOrderProduct crp : products){
				CustOrderProductVO vo = new CustOrderProductVO(crp);
				vos.add(vo);
				//System.out.println(vo.getBrand() + "," + vo.getProductCode() + "," + vo.getQuantity() + "," + vo.getSumWholePrice());
				totalQ += vo.getQuantity();
				totalSum += vo.getSumRetailPrice();
			}
		}
		
		if (footer != null){
			CustOrderProductVO footerVo = new CustOrderProductVO();
			footerVo.setQuantity(totalQ);
			footerVo.setSumRetailPrice(totalSum);
			footerVo.setLastUpdateTime(null);
			footerVo.setProductCode("总计");
			footer.add(footerVo);
		}
		
		return vos;
	}

	public Response getCustOrderProducts(Integer custId) {
		Response response = new Response();
		
		Map<String, Object> dataMap= new HashMap<>();
		
		Customer cust = customerDaoImpl.get(custId, true);
	
		DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
		criteria.add(Restrictions.eq("custId", custId));
		criteria.add(Restrictions.ne("status", EntityConfig.INACTIVE));
		
		List<CustOrderProduct> products = custOrderProductDaoImpl.getByCritera(criteria, true);
		
		dataMap.put("customer", cust);
		dataMap.put("data", products);
		
		response.setReturnValue(dataMap);
		
		return response;
	}
	
	@Transactional
	public Response activeCustAcct(Integer id, String userName) {
		Response response = new Response();
		
		//1. active the customer information
		Customer cust = customerDaoImpl.get(id, true);
		if (cust == null){
			response.setFail("客户信息不存在");
			return response;
		} else {
			if (!systemConfigDaoImpl.canUpdateCust()){
				response.setFail("管理员已经锁定客户信息和单据更新,请联系管理员");
				return response;
			}
			
			//2. 更新精算信息
			ClientsMS clientMs = clientDAOImpl.getClientsByID(id);
			if (clientMs == null){
				cust.setCustName("");
				cust.setCustRegion("");
				cust.setStatus(EntityConfig.INACTIVE);
			} else {
				cust.setCustName(clientMs.getName());
				cust.setCustRegion(clientMs.getRegion().getName());
				cust.setStatus(EntityConfig.ACTIVE);
			}
				
			
			//3. 更新连锁店信息
			ChainStore2 chainStore = chainStore2DaoImpl.getByClientId(id);
			if (chainStore == null){
				cust.setChainId(null);
				cust.setChainStoreName("");
			} else {
				cust.setChainId(chainStore.getChainId());
				cust.setChainStoreName(chainStore.getChainName());
			}
			
			customerDaoImpl.update(cust, true);
		}
		
		//2。active the order_product
		Object[] values = new Object[]{EntityConfig.ACTIVE, id};
		String U_ORDER_PRO = "UPDATE CustOrderProduct SET status =? WHERE custId =?";
		int numOfRecords = custOrderProductDaoImpl.executeHQLUpdateDelete(U_ORDER_PRO, values, true);
		String activeRecords = "";
		if (numOfRecords > 0)
			activeRecords = numOfRecords + "条客户订单记录也被激活";
		
		response.setSuccess("客户信息 : " + cust.getCustName() + " 已经成功被激活。" + activeRecords);
		return response;
	}

	/**
	 * 搜索精算的客户数据
	 * @param custName
	 * @return
	 */
	public DataGrid searchJinSuanClients(String custName) {
		DataGrid dataGrid = new DataGrid();
		
		if (custName != null && !custName.trim().equals("")){
			DetachedCriteria criteria = DetachedCriteria.forClass(ClientsMS.class,"client");
			criteria.add(Restrictions.like("client.name", custName,MatchMode.ANYWHERE));
			criteria.add(Restrictions.eq("client.deleted", false));
			List<ClientsMS> clients = clientDAOImpl.getClientsByCriteria(criteria);
			
			List<ClientJinSuanVO> clientVos = transferClients(clients);
			
			dataGrid.setRows(clientVos);
		}
		
		return dataGrid;
	}
	
	private List<ClientJinSuanVO> transferClients(List<ClientsMS> clients){
		List<ClientJinSuanVO> clientVO = new ArrayList<>();
		if (clients != null && clients.size() > 0){
			for (ClientsMS client : clients){
				ClientJinSuanVO clientJinSuanVO = new ClientJinSuanVO(client);
				clientVO.add(clientJinSuanVO);
			}
		}
		
		return clientVO;
	}

	/**
	 * 添加精算用户
	 * @param clientIds
	 * @param userName
	 * @return
	 */
	public Response addCust(String clientIds, String userName) {
		Response response = new Response();
		int total = 0;
		
		if (clientIds == null || clientIds.trim().equals("")){
			response.setFail("没有添加客户到系统");
		} else {
			if (!systemConfigDaoImpl.canUpdateCust()){
				response.setFail("管理员已经锁定客户信息和单据更新,请联系管理员");
				return response;
			}
			
			String[] ids = clientIds.split(",");
			for (String idString : ids){
				int id = Integer.parseInt(idString);
				
				Customer custOriginal = customerDaoImpl.get(id, true);
				ClientsMS client = clientDAOImpl.getClientsByID(id);
				ChainStore2 chainStore2 = chainStore2DaoImpl.getByClientId(id);
				
				Customer customer = null;
				if (custOriginal != null){
					customer = custOriginal;
				}
				
				if (client != null){
					if (customer == null)
						customer = new Customer();
					
					customer.setCustName(client.getName());
					customer.setCustRegion(client.getRegion().getName());
					customer.setUpdateUser(userName);
					customer.setUpdateDate(DateUtility.getToday());
					if (customer.getPassword() == null ||customer.getPassword().equals(""))
						customer.setPassword(StringUtility.getRandomPassword());
					customer.setId(id);
					customer.setStatus(EntityConfig.ACTIVE);
				}
				
				if (customer != null && chainStore2 != null){
					customer.setChainId(chainStore2.getChainId());
					customer.setChainStoreName(chainStore2.getChainName());
				} else {
					customer.setChainId(null);
					customer.setChainStoreName("");
				}
					
				if (customer != null){
					total++;
				    customerDaoImpl.saveOrUpdate(customer, true);
				}
			}
		}
		
		response.setSuccess("成功 添加/更新 " + total + " 客户");
		return response;
	}
}
