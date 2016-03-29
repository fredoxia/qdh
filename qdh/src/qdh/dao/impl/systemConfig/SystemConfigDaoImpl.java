package qdh.dao.impl.systemConfig;

import org.springframework.stereotype.Repository;

import qdh.dao.entity.systemConfig.SystemConfig;
import qdh.dao.impl.BaseDAO;

@Repository
public class SystemConfigDaoImpl extends BaseDAO<SystemConfig>{
	
	
	public SystemConfig getSystemConfig() {
		SystemConfig systemConfig = this.get(SystemConfig.DEFAULT_KEY, true);
		if (systemConfig == null){
			systemConfig = new SystemConfig();
			this.save(systemConfig, true);
		}
		return systemConfig;
	}

	public boolean canUpdateCust() {
		SystemConfig systemConfig = this.getSystemConfig();
		if (systemConfig.getLockUpdateCust() == SystemConfig.LOCK)
			return false;
		return true;
	}
	
	public boolean canUpdateProduct(){
		SystemConfig systemConfig = this.getSystemConfig();
		if (systemConfig.getLockUpdateProduct() == SystemConfig.LOCK)
			return false;
		return true;
	}

}
