package org.yixun.support.exception;

public class BusinessException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5854290764079428208L;
	private String msg;
	public BusinessException(String msg){
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return msg;
	}
}
