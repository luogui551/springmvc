/**
 * 
 */
package com.aires.base.exception;

/**
 * @author aires
 * 2016-9-13 下午4:58:05
 * 描述：非预期结果异常
 */
public class UnexpectedException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4570023111641733954L;

	public UnexpectedException(){
		super("结果与预期值不匹配！");
	}
	
	public UnexpectedException(String msg){
		super(msg);
	}
	
	public UnexpectedException(Throwable t){
		super("结果与预期值不匹配:", t);
	}
}
