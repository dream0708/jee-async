package com.jee.async.common.session;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;
import com.jee.async.common.model.User;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

public class RedisSessionUtil implements SessionUtil {

	private static Logger logger = LogManager.getLogger(RedisSessionUtil.class) ;    
    
	private JedisPool jedisPool ;
	//private String prefixToken ; 
	public boolean exists(String key){
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			return handler.exists(key) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	public String get(String key){
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			return handler.get(key) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	public <T>  void  setObject(String key , T value) {
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
		    handler.set(key, JSON.toJSONString(value)) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	
	public void expire(String key , int times){
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			handler.expire(key, times) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	public  <T> T getObject(String key , Class<T> clazz) {
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			String content =  handler.get(key) ;
			return JSON.parseObject(content, clazz) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	public  <T> T getObject(String key , TypeReference<T> type) {
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			String content =  handler.get(key) ;
			return JSON.parseObject(content, type) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	public  <T> T getObject(String key , Type type) {
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			String content =  handler.get(key) ;
			return JSON.parseObject(content, type) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	public  void set(String key , String value){
		if(StringUtils.isBlank(key)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "key不为空") ;
		}
		if(StringUtils.isBlank(value)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "value不为空") ;
		}
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			handler.set(key,value) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	public  void setex(String key , String value , int expires){
		if(StringUtils.isBlank(key)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "key不为空") ;
		}
		if(StringUtils.isBlank(value)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "value不为空") ;
		}
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			handler.setex(key, expires, value) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	
	public  void set(String key , Object value){
		if(StringUtils.isBlank(key)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "key不为空") ;
		}
		if(value == null){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "value不为空") ;
		}
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			handler.set(key,JSON.toJSONString(value)) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	public void set(String key , Object value , int expire) {
		
		if(StringUtils.isBlank(key)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "key不为空") ;
		}
		if(value == null){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "value不为空") ;
		}
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			handler.setex(key, expire,JSON.toJSONString(value)) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	public void setAttribute(String sessionid , String key , String value){
		if(StringUtils.isBlank(sessionid)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "sessionid不为空") ;
		}
		if(StringUtils.isBlank(key)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "key不为空") ;
		}
		if(StringUtils.isBlank(value)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "value不为空") ;
		}
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			handler.hset(sessionid, key, value) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	
	public void setAttribute(String sessionid , String key , Object value){
		setAttribute(sessionid , key , JSON.toJSON(value)) ;
	}
	    
	public void setAttribute(User user ,String key , Object value ) {
		setAttribute(user.getSessionid() , key , value) ;
	}
	
	public String getAttribute(String sessionid , String key ) {
		if(StringUtils.isBlank(sessionid)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "sessionid不为空") ;
		}
		if(StringUtils.isBlank(key)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "key不为空") ;
		}
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			String value = handler.hget(sessionid, key) ;
			return value ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	} 
	public void setAttribute(String sessionid , Map<String , String> map) {
		if(StringUtils.isBlank(sessionid)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "sessionid不为空") ;
		}
		if(map == null){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "map不为空") ;
		}
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			handler.hmset(sessionid, map) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	public <T> T getAttribute(String sessionid , String key , Class<T> clazz) {
		if(StringUtils.isBlank(sessionid)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "sessionid不为空") ;
		}
		if(StringUtils.isBlank(key)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "key不为空") ;
		}
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			String value = handler.hget(sessionid, key) ;
			return JSON.parseObject(value, clazz) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	
	public Map<String , String> getAll(String sessionid) {
		if(StringUtils.isBlank(sessionid)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "sessionid不为空") ;
		}
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			return handler.hgetAll(sessionid) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
	public <T> T getAttribute(String sessionid , String key , TypeReference<T> type) {
		if(StringUtils.isBlank(sessionid)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "sessionid不为空") ;
		}
		if(StringUtils.isBlank(key)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "key不为空") ;
		}
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			String value = handler.hget(sessionid, key) ;
			return JSON.parseObject(value, type) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}    
	
	public void removeAttribute(String sessionid , String key) {
		if(StringUtils.isBlank(sessionid)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "sessionid不为空") ;
		}
		if(StringUtils.isBlank(key)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "key不为空") ;
		}
		
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			handler.hdel(sessionid, key) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
	}
    public void removeAttribute(User user , String key) {
    	removeAttribute(user.getSessionid() , key) ;
    }
    
    public void delete(String key){
    	
		if(StringUtils.isBlank(key)){
			throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , "key不为空") ;
		}
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			handler.del(key) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
    }
    public Set<String> keys(String pattern){
		Jedis handler = null ;
		try{
			handler = jedisPool.getResource() ;
			return handler.keys(pattern) ;
		}catch(JedisException ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
		}finally{
			release(jedisPool, handler);
		}
    }
    public Long ttl(String key){
  		Jedis handler = null ;
  		try{
  			handler = jedisPool.getResource() ;
  			return handler.ttl(key) ;
  		}catch(JedisException ex){
  			logger.error(ex.getMessage() , ex);
  			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "session服务器发生异常") ;
  		}catch(Exception ex){
  			logger.error(ex.getMessage() , ex);
  			throw new BusinessException(ResponseCode.SERVER_ERROR_500 , "系统发生异常") ;
  		}finally{
  			release(jedisPool, handler);
  		}
    }
	private void release(JedisPool jedisPool , Jedis jedis){
		if(null == jedis){
			return ;
		}
		jedis.close();
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
}
