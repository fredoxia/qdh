package qdh.pageModel;

import qdh.dao.impl.Response;

/**
 * 
 * JSON模型
 * 
 * 用户后台向前台返回的JSON对象
 * 
 * @author 孙宇
 * 
 */
public class Json implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8035480926731148678L;

	private boolean success = false;
	
	private boolean warning = false;
	
	private int returnCode = 0;

	private String msg = "";

	private Object obj = null;
	
	public Json(){
		
	}
	
	public Json(Response response){
		msg = response.getMessage();
		obj = response.getReturnValue();
		if (response.isSuccess())
			success = true;
		else if (response.getReturnCode() == Response.WARNING){
			warning = true;
		}
		
		returnCode = response.getReturnCode();
	}

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isWarning() {
		return warning;
	}

	public void setWarning(boolean warning) {
		this.warning = warning;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
