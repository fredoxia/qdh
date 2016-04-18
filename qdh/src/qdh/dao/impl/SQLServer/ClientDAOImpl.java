package qdh.dao.impl.SQLServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import qdh.dao.entity.SQLServer.ClientsMS;
import qdh.dao.entity.SQLServer.RegionMS;
import qdh.dao.impl.BaseDAOMS;
import qdh.utility.loggerLocal;


@Repository
public class ClientDAOImpl extends BaseDAOMS<ClientsMS> {

	@Autowired
	private RegionMSDAOImpl regionMSDAOImpl;
	
	/**
	 * get clients by the name like
	 * @param clientName
	 * @return
	 */
	public List<ClientsMS> getClientsByCriteria(DetachedCriteria criteria){
		List<ClientsMS> clients = null;
		try{
			clients = getByCritera(criteria, false);
			
		} catch (Exception e) {
			e.printStackTrace();
			loggerLocal.error(e);
		}		
		return buildClient(clients);
	}
	
	/**
	 * to get the region map
	 * @param criteria
	 * @return
	 */
	private Map<Integer, RegionMS> getRegions(DetachedCriteria criteria){
		List<RegionMS> regions = null;
		Map<Integer, RegionMS> regionMap = new HashMap<Integer,RegionMS>();
		try{
			regions = regionMSDAOImpl.getByCritera(criteria, false);
			
			if (regions != null){
				for (RegionMS regionMS: regions){
					int key = regionMS.getRegionID();
					regionMap.put(key, regionMS);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			loggerLocal.error(e);
		}		
		return regionMap;		
	}
	
	/**
	 * to get the client inforamtion by pinyin like
	 * @param pinyin
	 * @return
	 */
	public List<ClientsMS> getClientByPinyin(String pinyin){
		DetachedCriteria criteria = DetachedCriteria.forClass(ClientsMS.class);
		criteria.add(Restrictions.like("pinyin", pinyin +"%"));
		criteria.add(Restrictions.ne("deleted", true));
		
		return this.getByCritera(criteria, false);
		
	}
	
	/**
	 * get client by client id
	 * @param clientID
	 * @return
	 */
	public ClientsMS getClientsByID(int clientID){
		ClientsMS client = get(clientID, false);

		if (client == null)
			return null;
		else {
		    List<ClientsMS> tempList = new ArrayList<ClientsMS>();
		    tempList.add(client);
		
		    return buildClient(tempList).get(0);
		}
	}
	
	/**
	 * to add the region in the clients
	 * @param clients
	 * @return
	 */
	private List<ClientsMS> buildClient(List<ClientsMS> clients){
		if (clients != null && clients.size()!=0) {
			List<Integer> client_keys = new ArrayList<Integer>();
			RegionMS emptyRegionMS = new RegionMS();
			
			for (ClientsMS client: clients){
				client_keys.add(client.getRegion_id());
			}
			
			DetachedCriteria criteria = DetachedCriteria.forClass(RegionMS.class,"region");
			criteria.add(Restrictions.in("region.regionID", client_keys));
			
			Map<Integer, RegionMS> regionMap = getRegions(criteria);
			
			for (ClientsMS client: clients){
				if (client.getRegion_id() != 0)
				     client.setRegion(regionMap.get(client.getRegion_id()));
				else 
					client.setRegion(emptyRegionMS);
			}
		}
		return clients;
	}

	public Map<Integer, ClientsMS> getClientMap(List<Integer> clientIDs) {
		Map<Integer, ClientsMS> clientMap = new HashMap<Integer,ClientsMS>();
		
		if (clientIDs != null && clientIDs.isEmpty() == false){
			DetachedCriteria criteria = DetachedCriteria.forClass(ClientsMS.class,"client");
			criteria.add(Restrictions.in("client.client_id", clientIDs));
			
			List<ClientsMS> clients = null;
			
			try{
				clients = getClientsByCriteria(criteria);
				
				if (clients != null){
					for (ClientsMS client: clients){
						int key = client.getClient_id();
						clientMap.put(key, client);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				loggerLocal.error(e);
			}	
		}
		
		return clientMap;
	}
}
