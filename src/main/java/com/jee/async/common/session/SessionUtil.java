package com.jee.async.common.session;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.TypeReference;
import com.jee.async.common.model.User;



public interface SessionUtil {
	
	
	public  boolean exists(String key) ;
	
	public  void set(String key , String value) ;
	
	public <T>  void  setObject(String key , T value) ;
	
	public  void setex(String key , String value , int expires) ;
	
	public  String get(String key) ;
	public  <T> T getObject(String key , Class<T> clazz) ;
	public  <T> T getObject(String key , TypeReference<T> type) ;
	public  <T> T getObject(String key , Type type) ;
	public  void expire(String key , int seconds) ;

	
	public  void set(String key , Object value) ;
	public  void set(String key , Object value , int expire) ;
	public  void setAttribute(String sessionid , String key , Object value) ;
	public  void setAttribute(String sessionid , String key , String value) ;    
	public  void setAttribute(User user ,String key , Object value ) ;
	
	public String getAttribute(String sessionid , String key )  ;
	public void setAttribute(String sessionid , Map<String , String> map) ;
	public <T> T getAttribute(String sessionid , String key , Class<T> clazz) ;
	public <T> T getAttribute(String sessionid , String key , TypeReference<T> type)  ;
	
	public Map<String , String> getAll(String sessionid) ;
	
	public void delete(String key) ;
	public void removeAttribute(String sessionid , String key) ;
		
    public void removeAttribute(User user , String key)  ;
    public Set<String> keys(String pattern) ;
    public Long ttl(String key);
}
