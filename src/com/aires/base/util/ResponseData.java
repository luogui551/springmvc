package com.aires.base.util;
/**
 * JSON响应数据
 * @author User
 *
 */
public class ResponseData {
	/*
	 * 成功/失败
	 */
	private final boolean success;
	/*
	 * 一般用于前端提示的德信息
	 */
	private String msg;
	/*
	 * 返回到前端的额外数据（如新增时返回数据id）
	 */
	private Object value;
	/**
	 * 返回成功的对象（不包含消息和数据）
	 * @return
	 */
	public static ResponseData success(){
		return new ResponseData(true, "", "");
	}
	/**
	 * 返回成功的对象（不包含消息， 包含指定数据）
	 * @param value 数据对象
	 * @return
	 */
//	public static ResponseData success(Object value){
//		return new ResponseData(true, "", value);
//	}
	/**
	 * 返回失败的对象（不包含消息和数据）
	 * @return
	 */
	public static ResponseData fail(){
		return new ResponseData(false, "", "");
	}
	/**
	 * 返回失败的对象（包含消息，不包含数据）
	 * @param msg
	 * @return
	 */
	public static ResponseData fail(String msg){
		return new ResponseData(false, msg, "");
	}
	/**
	 * 为当前设置消息
	 * @param msg
	 * @return
	 */
	public ResponseData msg(String msg){
		this.msg = msg;
		return this;
	}
	/**
	 * 为当前对象设置数据
	 * @param value
	 * @return
	 */
	public ResponseData val(Object value){
		this.value = value;
		return this;
	}
	
	/**
	 * 
	 * @param success 是否成功
	 * @param msg  返回信息
	 */
	public ResponseData(boolean success, String msg) {
		this(success, msg, "");
	}
	/**
	 * 
	 * @param success 是否成功
	 * @param msg  返回信息
	 * @param value  返回数据
	 */
	public ResponseData(boolean success, String msg, Object value) {
		this.success = success;
		this.msg = msg;
		this.value = value;
	}
	public boolean isSuccess() {
		return success;
	}
	public String getMsg() {
		return msg;
	}
	public Object getValue() {
		return value;
	}	
}
