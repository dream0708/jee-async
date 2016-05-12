package com.jee.async.common.util;

import java.util.Arrays;
import java.util.List;
/**
 * 统一管理常量 
 * @author yaomengke
 *
 */
public class Constants {
	
	//public static final String REQUEST_FLAG = "request_uuid" ;
	
	public static final String REQEUST_UUID_FLAG = "uuid" ;
	
	public static final String MDC_UUID_FLAG = "uuid" ;
	
	public static final String REQUEST_USER_FLAG = "userid" ;
	
	public static final String RQEUST_TIME_FLAG = "request_begin" ;
	
	public static final int MAX_LOG_LENGTH = 12 ;
	
	public static final String REQUEST_TOKEN = "sessionid" ;
	
	public static final String REQUEST_HASH = "hash" ;
	
	public static final String REQUEST_ERROR = "error" ;
	
	public static final List<String> sensitives = Arrays.asList(  //敏感字段过滤规则
			"password" , "pwd" , "secret" , "u-keys"
    );

}
