package qdh.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import qdh.dao.entity.systemConfig.SystemConfig;
import qdh.dao.impl.Response;
import qdh.dao.impl.systemConfig.SystemConfigDaoImpl;

@Service
public class SystemConfigService {
	
	@Autowired
	private SystemConfigDaoImpl systemConfigDaoImpl;

	@Transactional
	public Response PreSystemConfigPage() {
		Response response = new Response();
		
		SystemConfig systemConfig = systemConfigDaoImpl.getSystemConfig();
		response.setReturnValue(systemConfig);
		return response;
	}

	public Response updateSystemConfig(SystemConfig systemConfig, String userName) {
		Response response = new Response();
		
		systemConfig.setId(SystemConfig.DEFAULT_KEY);
		
		systemConfigDaoImpl.merge(systemConfig);
		response.setMessage("成功更新配置信息");
		return response;
	}

}
