package com.jee.async.common.exception;

import com.jee.async.common.enums.IBusinessResponse;
/**
 * 业务异常 
 * @author yaomengke
 *
 */
public class  BusinessException extends RuntimeException{
	
	private static final long serialVersionUID = -1003683355305462875L;
    /** 错误码 */
	private IBusinessResponse errorCode ;
	
	/** 保留字段 其他信息 方便log处理等用途 **/
	private Object reserved ;
	
	public BusinessException(IBusinessResponse errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }
	
    public BusinessException(IBusinessResponse errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public BusinessException(IBusinessResponse errorCode, String message , Object other) {
        super(message);
        this.errorCode = errorCode;
        this.reserved = other ;
    }
    
    
    

	public Object getReserved() {
		return reserved;
	}

	public void setReserved(Object reserved) {
		this.reserved = reserved;
	}

	/**

     * 构造方法

     * @param message 错误消息

     * @param errorCode 错误码

     * @param ex {@link Throwable}

     */
    public BusinessException(IBusinessResponse errorCode, String message, Throwable ex) {
        super(message, ex);
        this.errorCode = errorCode;
    }

  
    public IBusinessResponse getErrorCode() {
        return errorCode;
    }

  
    public void setErrorCode(IBusinessResponse errorCode) {
        this.errorCode = errorCode;
    }

}
