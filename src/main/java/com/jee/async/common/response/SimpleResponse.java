package com.jee.async.common.response;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.annotation.JSONField;
import com.jee.async.common.enums.IBusinessResponse;
import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;

public class SimpleResponse<T> extends RtnSuper implements Serializable{

	private static final Logger logger = LogManager.getLogger("async-exception") ;
	
	private static final long serialVersionUID = -5479905266283048694L;
	
	@JSONField(name ="result" , ordinal = 5)
	private T r;
   
    public T getR() {
        return r;
    }
    public void setR(T r) {
    	this.r = r ;
    }
   
    public SimpleResponse<T> result(T r) {
    	this.r = r ;
    	return this ;
    }
    
	@SuppressWarnings("rawtypes")
	public static void simpleResultWrapper(SimpleResponse simpleResult , int rtnCode , String[] msg)
	{
		if(simpleResult == null)
			simpleResult = new SimpleResponse();
		simpleResult.setRtnCode(rtnCode);
		simpleResult.setRtnMsg(msg);
	}
	
	 /**
     * 成功
     * @return
     */
	public  SimpleResponse<T> success(){
    	this.setRtnCode(ResponseCode.SUCCESS_0.getCode());
    	this.setRtnMsg(new String[]{ResponseCode.SUCCESS_0.getMsg()});
    	return this ;
    }
    
	public  SimpleResponse<T> success(String msg){
		this.setRtnCode(ResponseCode.SUCCESS_0.getCode());
    	this.setRtnMsg(new String[]{msg});
    	return this ;
    }
	public  SimpleResponse<T> success(int code , String msg){
    	this.setRtnCode(code);
    	this.setRtnMsg(new String[]{msg});
    	return this ;
    }
    /**
     * 失败
     * @return
     */
	
	public SimpleResponse<T> failure(IBusinessResponse code){
    	this.setRtnCode(code.getCode());
    	this.setRtnMsg(new String[]{ code.getMsg() });
    	return this ;
    }
	
	public SimpleResponse<T> failure(){
    	return failure(ResponseCode.FAILUER_1) ;
    }
	
    
	public  SimpleResponse<T> failure(String msg){
    	this.setRtnCode(ResponseCode.FAILUER_1.getCode());
    	this.setRtnMsg(new String[]{msg});
    	return this ;
    }
	public  SimpleResponse<T> failure(String[] msg){
    	this.setRtnCode(ResponseCode.FAILUER_1.getCode());
    	this.setRtnMsg(msg);
    	return this ;
    }
	
   	public  SimpleResponse<T> failure(int code , String msg){
       	this.setRtnCode(code);
    	this.setRtnMsg(new String[]{msg});
       	return this ;
    }
   	public  SimpleResponse<T> failure(int code , String[] msg){
       	this.setRtnCode(code);
    	this.setRtnMsg(msg);
       	return this ;
    }
   	
   	public  SimpleResponse<T> failure(BusinessException ex){
       	this.setRtnCode(ex.getErrorCode().getCode());
        this.setRtnMsg(new String[]{ex.getMessage()});
       	return this ;
    }	
   	
   	
   	/**
	 * 身份验证
	 */
	public SimpleResponse<T> hash(String hash){
		super.setHash(hash);
		return this ;
	}
	public SimpleResponse<T> token(String token){
		super.setToken(token);
		return this ;
	}
	/**
	 * Async异常 
	 */
	public  SimpleResponse<T>  exception(Throwable ex , String hash , String... defaultMsg ){
		logger.error(ex.getMessage() , ex);
		Throwable cause = ex.getCause();
		logger.error(cause.getMessage() , cause);
		if (cause instanceof BusinessException) {
			BusinessException be = (BusinessException) cause;
			this.failure(be) ;
			this.setHash(hash);
		} else {
			if(defaultMsg == null || defaultMsg.length == 0){
				defaultMsg = new String[]{"系统异常::未知原因"} ;
			}
			this.failure(defaultMsg) ;
			this.setHash(hash);
		}
		return this ;
	}
	public  SimpleResponse<T> excpetionWithHash (Throwable ex , String hash , String ...defaultMsg ){
		return exception(ex , hash , defaultMsg ) ;
	}
	public  SimpleResponse<T> excpetionWithOutHash (Throwable ex , String ...defaultMsg ){
		return exception(ex , null , defaultMsg ) ;
	}
	
	@Override
	public String toString() {
		return "SimpleResponse [r=" + r + ", getToken()=" + getToken() + ", getHash()=" + getHash() + ", getRtnCode()="
				+ getRtnCode() + ", getRtnMsg()=" + Arrays.toString(getRtnMsg()) + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
	
	

	
	
   
}
