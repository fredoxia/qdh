package qdh.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.comparator.CustOrderProductComparatorByBrandProductCode;
import qdh.comparator.CustOrderProductComparatorByYQBrandProductCode;
import qdh.dao.config.EntityConfig;
import qdh.dao.entity.VO.CustOrderProductVO;
import qdh.dao.entity.order.CustOrderProduct;
import qdh.dao.entity.order.Customer;
import qdh.dao.entity.qxMIS.ChainStore2;
import qdh.dao.entity.systemConfig.SystemConfig;
import qdh.dao.impl.Response;
import qdh.dao.impl.order.CustomerDaoImpl;
import qdh.dao.impl.order.CustOrderDaoImpl;
import qdh.dao.impl.order.CustOrderProdDaoImpl;
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
	private CustOrderProdDaoImpl custOrderProductDaoImpl;
	
	@Autowired
	private SystemConfigDaoImpl systemConfigDaoImpl;
	
	
	public DataGrid getCustAccts(Integer isChain, Integer status, String name, String sort, String order) {
		DataGrid dataGrid = new DataGrid();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		
//		System.out.println(sort + "," + order);
		
		if (status == null)
			status = EntityConfig.ACTIVE;
		if (status != null && status != EntityConfig.ALL_RECORD)
			criteria.add(Restrictions.eq("status", status));
		
		if (name != null && !name.trim().equals("")){
			criteria.add(Restrictions.or(Restrictions.like("custRegion", "%" + name + "%"), Restrictions.like("custName", "%" + name + "%")));
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

	public Response prepareAddEditCustAcct(Integer custId) {
		Response response = new Response();
		Customer cust = new Customer();
		
		if (custId != null && custId != 0)
		    cust = customerDaoImpl.get(custId, true);
		
		Map dataMap = new HashMap();
		dataMap.put("cust", cust);

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
			customerDaoImpl.update(cust, userName);
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
		
		Collections.sort(products, new CustOrderProductComparatorByBrandProductCode());

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
			
			cust.setStatus(EntityConfig.ACTIVE);

			customerDaoImpl.update(cust, userName);
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



	public Response downloadCust(Integer isChain, Integer status) {
		Map<String, Object> dataMap= new HashMap<>();
		Response response = new Response();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		
//		System.out.println(sort + "," + order);
		
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
		
		List<Customer> custs = customerDaoImpl.getByCritera(criteria, true);
		
		dataMap.put("customer", custs);
		response.setReturnValue(dataMap);
		
		return response;
	}

	/**
	 * 打开cust order 时候准备订单上面表单数据
	 * @param id
	 * @return
	 */
	public Response getCustOrderJSP(Integer id) {
		Map<String, Object> dataMap= new HashMap<>();
		Response response = new Response();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Customer.class);
		criteria.add(Restrictions.eq("status", EntityConfig.ACTIVE));
		criteria.add(Restrictions.ne("id", id));
		
		List<Customer> custs = customerDaoImpl.getByCritera(criteria, true);
		Customer emptyCustomer = new Customer();
		emptyCustomer.setCustName("");
		emptyCustomer.setCustRegion("");
		emptyCustomer.setId(0);
		
		custs.add(0, emptyCustomer);
		
		dataMap.put("customer", custs);
		dataMap.put("custId", id);
		dataMap.put("toCustId", id);
		
		response.setReturnValue(dataMap);
		return response;
	}

	/**
	 * 从一个客户那里复制单据到另外一个客户
	 * @param fromCustId
	 * @param toCustId
	 * @return
	 */
	@Transactional
	public Response copyCustOrder(Integer fromCustId, Integer toCustId) {
		Response response = new Response();
		
		SystemConfig conf = systemConfigDaoImpl.getSystemConfig();
		String orderIdentity = conf.getOrderIdentity();	
		
		//1. 检查是否是空客户
		if (fromCustId == 0 || toCustId == 0){
			response.setFail("请选中一个正确的客户再进行操作");
			return response;
		}
		
		//1. 检查是否是同一个客户
		if (fromCustId == toCustId){
			response.setFail("不能复制同一个客户的单据");
			return response;
		}
		
		//2. 检查toCustId 是否是合法的客户
		Customer toCustomer = customerDaoImpl.get(toCustId, true);
		if (toCustomer == null){
			response.setFail("客户信息 无法找到，请查找一个准确的客户再继续");
			return response;
		} else if (toCustomer.getStatus() != EntityConfig.ACTIVE){
			response.setFail("当前客户目前未被激活，无法复制单据到当前客户");
			return response;
		}
		
		//2. 检查是否fromCust有订单了
		String sql_check = "SELECT COUNT(*) FROM CustOrderProduct WHERE custId=? AND orderIdentity=?";
		Object[] values = new Object[]{fromCustId, orderIdentity};
		int count_from = custOrderProductDaoImpl.executeHQLCount(sql_check, values, false);
		if (count_from ==0){
			Customer fromCustomer = customerDaoImpl.get(fromCustId, true);
			response.setFail("当前客户 " + fromCustomer.getCustName() + " 没有订单，无法从他这里复制订单");
			return response;
		}
		
		//3. 检查是否toCust有订单
		values = new Object[]{toCustId, orderIdentity};
		int count_to = custOrderProductDaoImpl.executeHQLCount(sql_check, values, false);
		if (count_to > 0){
			
			response.setFail("当前客户 " + toCustomer.getCustName() + " 已经有订单，无法复制订单");
			return response;
		}
		
		//4. 复制订单
		DetachedCriteria criteria = DetachedCriteria.forClass(CustOrderProduct.class);
		criteria.add(Restrictions.eq("custId", fromCustId));
		criteria.add(Restrictions.ne("status", EntityConfig.INACTIVE));
		criteria.add(Restrictions.eq("orderIdentity", orderIdentity));
		List<CustOrderProduct> orderProducts = custOrderProductDaoImpl.getByCritera(criteria, true);
		
		int count = 0;
		for (CustOrderProduct orderProduct : orderProducts){
			CustOrderProduct cop = new CustOrderProduct();
			BeanUtils.copyProperties(orderProduct, cop);
			
			cop.setLastUpdateTime(DateUtility.getToday());
			cop.setCustId(toCustId);
			custOrderProductDaoImpl.save(cop, true);
			count++;
		}
		
		response.setSuccess("总共复制了 " + count + " 条记录");
		
		return response;
	}

	/**
	 * 添加或者更新客户信息
	 * @param cust
	 * @param userName
	 * @return
	 */
	public Response updateCust(Customer cust, String userName) {
		Response response = new Response();
		int custId = cust.getId();
		
		if (custId != 0){
			Customer custDB = customerDaoImpl.get(custId, true);
			if (custDB == null){
				response.setFail("原始客户资料不存在");
				return response;
			} else {
				if (!systemConfigDaoImpl.canUpdateCust()){
					response.setFail("管理员已经锁定客户信息和单据更新,请联系管理员");
					return response;
				}
				
				if (StringUtils.isEmpty(cust.getPassword()))
					custDB.setPassword(cust.getPassword());
				custDB.setCustName(cust.getCustName());
				custDB.setCustRegion(cust.getCustRegion());
				customerDaoImpl.update(custDB, userName);
				
				response.setSuccess("客户资料已经成功更新");
			}
		} else {
			if (StringUtils.isEmpty(cust.getPassword()))
			     cust.setPassword(StringUtility.getRandomPassword());
			customerDaoImpl.save(cust, userName);
			response.setSuccess("客户资料已经成功添加");
		}
		return response;
	}

}
