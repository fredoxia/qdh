package qdh.dao.impl;

import java.io.Serializable;

/**
 * This is the reponse for the function returned
 * @author fredo
 *
 */
public class Response implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -657015437997770821L;
	public static final int IN_USE = 1;
	public static final int SUCCESS = 2;
	public static final int FAIL = 3;
	public static final int NO_AUTHORITY = 4;
	public static final int ERROR = 5;
	public static final int WARNING = 6;
	
	public static final int ACTION_ADD = 1;
	public static final int ACTION_UPDATE = 2;
	public static final int ACTION_DELETE = 3;

	/**
	 * the type of the return code like success/fail
	 */
    private int returnCode = SUCCESS;
    
    /**
     * 这次的动作
     */
    private int action;
    
    /**
     * returned message
     */
    private String message;
    
    /**
     * returned value
     */
    private Object returnValue;
    
    public Response(){
    	
    }
    
    public Response(int code){
    	super();
    	if (code == this.SUCCESS)
    		this.setReturnCode(Response.SUCCESS);
    	else if (code == this.FAIL)
    		this.setReturnCode(Response.FAIL);
    }

	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
    
	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	/**
	 * the quick function to set the success
	 */
    public void setQuickValue(int returnCode, String msg){
    	this.setReturnCode(returnCode);
    	this.setMessage(msg);
    }
    
    public boolean isSuccess(){
    	if (this.getReturnCode() == SUCCESS)
    		return true;
    	else {
			return false;
		}
    }
    
    public void setSuccess(String msg){
    	this.setReturnCode(SUCCESS);
    	this.setMessage(msg);
    }
    
    public void setFail(String msg){
    	this.setReturnCode(FAIL);
    	this.setMessage(msg);
    }

	@Override
	public String toString() {
		return "Response [returnCode=" + returnCode + ", action=" + action
				+ ", message=" + message + ", returnValue=" + returnValue + "]";
	}
    
}
