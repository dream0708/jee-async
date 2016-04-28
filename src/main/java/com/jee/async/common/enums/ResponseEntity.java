package com.jee.async.common.enums;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jee.async.common.util.SpringContextUtil;

public class ResponseEntity implements IBusinessResponse{

	    private static Logger logger = LogManager.getLogger(ResponseEntity.class) ;    
	

	    private int code;
	    private String name;
	    private String msg ;
	    
	    private ResponseEntity() {
	    	
	    }
	    private ResponseEntity(int code , String name, String msg ) {
	    	this.code = code ;
	    	this.name= name ;
	    	this.msg = msg;
	    }
	    private ResponseEntity(int code ,String msg ) {
	    	this.code = code ;
	    	this.msg = msg;
	    }
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		
		public static ResponseEntity get(String beanname){
			ResponseEntity entity = null ;
			try{
				entity =  SpringContextUtil.getBean(beanname);
			}catch(Exception ex){
				logger.error(ex.getMessage() , ex);
				entity = new  ResponseEntity(ResponseCode.FAILUER_1.getCode() , "ERROR_CODE_CONFIG_ERROR" , "错误码配置错误") ;
			}
			return entity ;
		}
}
