package com.jee.async.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.alibaba.fastjson.JSON;

public class LogUtils {

	private static String logAccessName = "access";
	private static String logErrorName = "error";
	
	
    public static final Logger ERROR_LOG = LoggerFactory.getLogger(logErrorName);
    public static final Logger ACCESS_LOG = LoggerFactory.getLogger(logAccessName);

    /**
     * 记录访问日志
     * [jsessionid][ip][accept][UserAgent][url][params][Referer]
     *
     * @param request
     */
    public static void logAccessEnter(HttpServletRequest request ) {
    	
       
        String method = request.getMethod();
        String ip = IpUtils.getIpAddr(request);
        String url = request.getRequestURI();
        String params = getParams(request);
       
        StringBuffer s = new StringBuffer();
	    //接口处理时间
		String uuid = (String) request.getAttribute(Constants.REQEUST_UUID_FLAG) ;
		if(StringUtils.isBlank(uuid)){ //第一次进入接口
			uuid = UUID.randomUUID().toString() ;
        	request.setAttribute(Constants.REQEUST_UUID_FLAG, uuid );
        	request.setAttribute(Constants.RQEUST_TIME_FLAG , System.currentTimeMillis());
        	MDC.put(Constants.MDC_UUID_FLAG, uuid);
        }else{ //异步有效 第二次进入接口
        	MDC.put(Constants.MDC_UUID_FLAG, uuid);
        	String userid =  (String) request.getAttribute(Constants.REQUEST_USER_FLAG) ;
        	MDC.put(Constants.REQUEST_USER_FLAG, userid);
        }
		
		//s.append(getBlock("进入接口前 ")) ;
        //s.append(getBlock(jsessionId));
        s.append(getBlock(method));
        s.append(getBlock(ip));
        s.append(getBlock(url));
        s.append(getBlock(params));
        
        
        //String accept = request.getHeader("accept");
        //String userAgent = request.getHeader("User-Agent");
        //s.append(getBlock(headers));
        //s.append(getBlock(request.getHeader("Referer")));
        //s.append(getBlock(accept));
        //s.append(getBlock(userAgent));
        getAccessLog().info(s.toString());
    }
    
    /**
     * 记录访问日志
     * [jsessionid][ip][accept][UserAgent][url][params][Referer]
     *
     * @param request
     */
    public static void logAccessCompletion(HttpServletRequest request ) {
       
        String method = request.getMethod();
        String ip = IpUtils.getIpAddr(request);
        String url = request.getRequestURI();
        String params = getParams(request);
        StringBuffer s = new StringBuffer();
    
        //s.append(getBlock("接口返回后 ")) ;
        //s.append(getBlock(jsessionId));
        s.append(getBlock(method));
        s.append(getBlock(ip));
        s.append(getBlock(url));
        s.append(getBlock(params));
        //String headers = getHeaders(request);
        //String accept = request.getHeader("accept");
        //String userAgent = request.getHeader("User-Agent");
        //s.append(getBlock(headers));
        //s.append(getBlock(accept));
        //s.append(getBlock(userAgent));
        //s.append(getBlock(request.getHeader("Referer")));
        
        Long end = System.currentTimeMillis() ;
        Long start = (Long)request.getAttribute(Constants.RQEUST_TIME_FLAG) ;
        Long consume = 0L ;
        if(null != start){
        	consume = end - start ;
        }
        s.append(getBlock(consume + ":ms")) ;
        getAccessLog().info(s.toString());
    }
    

    /**
     * 记录异常错误
     * 格式 [exception]
     *
     * @param message
     * @param e
     */
    public static void logError(String message, Throwable e) {
        StringBuffer s = new StringBuffer();
        s.append(getBlock("exception"));
        s.append(getBlock(message));
        ERROR_LOG.error(s.toString(), e);
    }

    /**
     * 记录页面错误
     * 错误日志记录 [page/eception][username][statusCode][errorMessage][servletName][uri][exceptionName][ip][exception]
     *
     * @param request
     */
    public static void logPageError(HttpServletRequest request) {
        
    }


    public static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }



    protected static String getParams(HttpServletRequest request) {
        final Map<String, String[]> params = request.getParameterMap();
        final Map<String, List<String>> temps = new HashMap<String , List<String>>();
        params.forEach( (k , v) -> {
        	if(Constants.sensitives.contains(k.toLowerCase())){
        		temps.put(k, Arrays.asList("***")) ;
        	}else{
        		List<String> cvs  = Arrays.stream(v).map(vx -> {
        			if(vx.length() > Constants.MAX_LOG_LENGTH){
        				return new StringBuilder(vx.substring(0 , 4)).append("***").append(vx.substring(vx.length() - 4 , vx.length())).toString();
            			//return vx.substring(0 , 4) + "***" + vx.substring(vx.length() - 4 , vx.length());
            		}else{
            			return vx ;
            		}
        		}).collect(Collectors.toList()) ;
        		temps.put(k, cvs) ;
        	}
        	
        });
        return JSON.toJSONString(temps);
    }


    private static String getHeaders(HttpServletRequest request) {
    	Map<String, List<String>> headers = new HashMap<String , List<String>>() ;
        Enumeration<String> namesEnumeration = request.getHeaderNames();
        while(namesEnumeration.hasMoreElements()) {
            String name = namesEnumeration.nextElement();
            Enumeration<String> valueEnumeration = request.getHeaders(name);
            List<String> values = new ArrayList<String>();
            while(valueEnumeration.hasMoreElements()) {
                values.add(valueEnumeration.nextElement());
            }
            headers.put(name, values);
        }
        return JSON.toJSONString(headers);
    }

    
    public static Logger getAccessLog() {
        return ACCESS_LOG;
    }

    public static Logger getErrorLog() {
        return ERROR_LOG;
    }

}
