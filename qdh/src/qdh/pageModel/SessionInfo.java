package qdh.pageModel;

import java.util.List;

/**
 * session信息模型
 * 
 * @author 孙宇
 * 
 */
public class SessionInfo implements java.io.Serializable {

	private String userId;// 用户ID
	private String userName;// 用户登录名
	private String ip;// 用户IP

	private List<String> resourceList;// 用户可以访问的资源地址列表

	public List<String> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<String> resourceList) {
		this.resourceList = resourceList;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
