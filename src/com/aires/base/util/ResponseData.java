package com.aires.base.util;
/**
 * JSON响应数据
 * @author User
 *
 */
public class ResponseData {
	private boolean success;
	private String msg;
	private Object value;
	
	public ResponseData() {
		this(null);
	}
	/**
	 * 成功
	 * @param msg 返回信息
	 */
	public ResponseData(String msg) {
		this(true, msg);
	}
	/**
	 * 成功
	 * @param msg  返回信息
	 * @param value  返回数据
	 */
	public ResponseData(String msg, Object value) {
		this(true, msg, value);
	}
	/**
	 * 
	 * @param success 是否成功
	 * @param msg  返回信息
	 */
	public ResponseData(boolean success, String msg) {
		this(success, msg, null);
	}
	/**
	 * 
	 * @param success 是否成功
	 * @param msg  返回信息
	 * @param value  返回数据
	 */
	public ResponseData(boolean success, String msg, Object value) {
		super();
		this.success = success;
		this.msg = msg;
		this.value = value;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}	
}
