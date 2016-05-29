package com.jee.async.common.response;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.annotation.JSONField;
import com.jee.async.common.enums.IBusinessResponse;
import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;
import com.jee.async.common.model.User;

public class SimpleResponse<T> extends RtnSuper implements Serializable{

	private static final Logger logger = LogManager.getLogger("async-exception") ;
	
	private static final long serialVersionUID = -5479905266283048694L;
	
	@JSONField( ordinal = 5)
	private List<T> result;
   
    public List<T> getResult() {
	    return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	
	@SuppressWarnings("unchecked")
	public SimpleResponse<T> result(T data){
		
		if(data instanceof Collection){
			this.result = new LinkedList<T>((Collection<? extends T>)data);
			return this ;
		}
		this.result = new LinkedList<T>();
		result.add(data);
		return this ;
	}
	
	@SuppressWarnings("unchecked")
	public SimpleResponse<T> result(Collection<T> data){
		this.result = new LinkedList<T>(data);
		return this ;
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
       	if(StringUtils.isBlank(ex.getMessage())){
       		this.setRtnMsg(new String[]{ex.getErrorCode().getMsg()});
       	}else{
       		this.setRtnMsg(new String[]{ex.getMessage()});
       	}
       	return this ;
    }	
   	public	SimpleResponse<T> success(User user){
    	return failure(ResponseCode.SUCCESS_0).hash(user.getHash()).session(user.getSessionid()) ;
    }
    public	SimpleResponse<T> failure(User user){
    	return failure(ResponseCode.FAILUER_1).hash(user.getHash()).session(user.getSessionid()) ;
    }
   	
   	
   	/**
	 * 身份验证
	 */
	public SimpleResponse<T> hash(String hash){
		super.setHash(hash);
		return this ;
	}
	public SimpleResponse<T> session(String token){
		super.setSessionid(token);
		return this ;
	}
	
	
	
	/**
	 * Async异常 
	 */
	public  SimpleResponse<T>  exception(Throwable ex , String hash , String... defaultMsg ){
		Throwable cause = ex.getCause();
		if (cause instanceof BusinessException) {
			BusinessException be = (BusinessException) cause;
			logger.error(" Catch BusinessException and Return ErrorCode: {} , ErrorMsg: {} " , be.getErrorCode().getCode() , be.getMessage());
			this.failure(be) ;
			this.setHash(hash);
		} else {
			logger.error(ex.getMessage() , ex);
			//logger.error(cause.getMessage() , cause);
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
	
	
	
   
}
