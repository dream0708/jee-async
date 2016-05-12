package com.jee.async.common.aop;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.jee.async.common.annotation.CacheEvict;
import com.jee.async.common.annotation.CacheEvicts;
import com.jee.async.common.annotation.Cacheable;
import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;
import com.jee.async.common.future.CompletableFutureUtils;
import com.jee.async.common.session.SessionUtil;

public class CacheAspect {
	
	private Logger logger = LogManager.getLogger(CacheAspect.class) ;
	
	
	private SessionUtil sessionUtil ;
    
	@Around("@annotation(com.jee.async.common.annotation.Cacheable)")
	public Object cache(ProceedingJoinPoint pjp ) {
		 Object result = null ;
         try{
        	 Method method = getMethod(pjp);
             Cacheable cacheable= method.getAnnotation(Cacheable.class);
    		 if(cacheable == null || StringUtils.isBlank(cacheable.key()) || StringUtils.isBlank(cacheable.prefix())){
    			result = pjp.proceed() ;
    			return result ;
    		 }
    		 
    		 String key = parseKey(cacheable.key() , method , pjp.getArgs());
             if(StringUtils.isBlank(key)){
            	result = pjp.proceed() ;
      			return result ;
             }
    		 
    		 boolean condition = parseCondition(cacheable.condition(), method , pjp.getArgs()) ; //满足要求
    		 if(!condition){
    			result = pjp.proceed() ;
     			return result ;
    		 }
    		 
             //获取方法的返回类型,让缓存可以返回正确的类型
             Class<?> returnType = ((MethodSignature)pjp.getSignature()).getReturnType();
             Type returnTypeG = method.getGenericReturnType();
             if(returnType == Void.class || returnType == void.class){
            	 result = pjp.proceed() ;
      			 return result ;
             }
             Type type = getGenericReturnType(returnType , returnTypeG) ;
        	 result = sessionUtil.getObject(cacheable.prefix() + key ,  type ) ;
        	 if(null == result){
        		 result = pjp.proceed();
        		 if(returnType == CompletableFuture.class){
        			 CompletableFuture<?> cf = (CompletableFuture<?>)result ;
        			 return  cf.thenApply(data -> {
        				 try{
        					 sessionUtil.setObject(cacheable.prefix() + key , data);
            				 int expire = cacheable.expire() ;
                             if(expire > 0){
                            	 sessionUtil.expire(cacheable.prefix() + key, expire);
                             } 
                             return data ; 
        				 }catch(Exception ex){
        					 logger.error(ex.getMessage() , ex);
        					 return data ;
        				 }
        				 
        			 })  ;
            	 }else{
            		  sessionUtil.setObject(cacheable.prefix() + key , result);
                      int expire = cacheable.expire() ;
                      if(expire > 0){
                     	 sessionUtil.expire(cacheable.prefix() + key, expire);
                      }
                      return result ;
            	 }
               
        	 }
        	 if(returnType == CompletableFuture.class){
        		 return CompletableFutureUtils.one(result) ;
        	 }
             return result ;
         }catch(BusinessException e){
        	 logger.error(e.getMessage() , e);
        	 throw e ;
         }catch (Throwable e) {
             logger.error(e.getMessage() , e);
             throw new BusinessException(ResponseCode.SYSTEM_ERROR_504 , "系统缓存出现异常");
         }
        
	}
	
	
	
	@Around("@annotation(com.jee.async.common.annotation.CacheEvict)")
	public Object cacheEvict(ProceedingJoinPoint pjp ) {
		 Object result = null ;
     	 
         try{
        	 Method method= getMethod(pjp);
        	 CacheEvict cacheable=  method.getAnnotation(CacheEvict.class);
        	
    		 if(cacheable == null || StringUtils.isBlank(cacheable.key()) || StringUtils.isBlank(cacheable.prefix())){
    			result = pjp.proceed() ;
    			return result ;
    		 }
           
        	 String key = parseKey(cacheable.key(), method , pjp.getArgs());
             if(StringUtils.isBlank(key)){
            	result = pjp.proceed() ;
      			return result ;
             }
             
             boolean condition = parseCondition(cacheable.condition(), method , pjp.getArgs()) ; //满足要求
    		 if(!condition){
    			result = pjp.proceed() ;
     			return result ;
    		 }
             
        	 sessionUtil.delete(cacheable.prefix() + key ) ;
        	 
        	 result = pjp.proceed();
             return result ;
         }catch(BusinessException e){
        	 logger.error(e.getMessage() , e);
        	 throw e ;
         }catch (Throwable e) {
             logger.error(e.getMessage() , e);
             throw new BusinessException(ResponseCode.SYSTEM_ERROR_504 , "系统缓存出现异常");
         }
        
	}
	
	@Around("@annotation(com.jee.async.common.annotation.CacheEvicts)")
	public Object cacheEvicts(ProceedingJoinPoint pjp ) {
		 Object result = null ;
     	 
         try{
        	 Method method= getMethod(pjp);
        	 CacheEvicts cacheables=  method.getAnnotation(CacheEvicts.class);
        	 
        	 if(cacheables == null || cacheables.value().length == 0){
    			result = pjp.proceed() ;
    			return result ;
    		 }
             for(CacheEvict cacheable : cacheables.value()){
            	 if(StringUtils.isBlank(cacheable.prefix())){
            		 continue ;
            	 }
            	 String key = parseKey(cacheable.key(), method , pjp.getArgs());
            	 if(StringUtils.isBlank(key)){
            		 continue ;
            	 }
            	 boolean condition = parseCondition(cacheable.condition(), method , pjp.getArgs()) ; //满足要求
            	 if(condition){
            		 sessionUtil.delete(cacheable.prefix() + key ) ;
            	 }
             }
        	 
        	 result = pjp.proceed();
             return result ;
         }catch(BusinessException e){
        	 logger.error(e.getMessage() , e);
        	 throw e ;
         }catch (Throwable e) {
             logger.error(e.getMessage() , e);
             throw new BusinessException(ResponseCode.SYSTEM_ERROR_504 , "系统缓存出现异常");
         }
        
	}
	
	/**
     *  获取被拦截方法对象
     *  
     *  MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象
     *    而缓存的注解在实现类的方法上
     *  所以应该使用反射获取当前对象的方法对象
     */
    public Method getMethod(ProceedingJoinPoint pjp){
        //获取参数的类型
		Object[] args = pjp.getArgs();
		Class<?>[] argTypes = new Class[pjp.getArgs().length];
		for (int i = 0; i < args.length; i++) {
			argTypes[i] = args[i].getClass();
		}
		Method method = null;
		try {
			method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return method;
        
    }
    
     private boolean parseCondition(String key,Method method, Object [] args){
        if(StringUtils.isBlank(key)){
        	return true ;
        }
        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u =   
            new LocalVariableTableParameterNameDiscoverer();  
        String [] paraNameArr=u.getParameterNames(method);
        
        //使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser(); 
        //SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        
        //把方法参数放入SPEL上下文中
        for(int i=0;i<paraNameArr.length ; i++){
            context.setVariable(paraNameArr[i], args[i]);
        }
        
        try{
        	return  parser.parseExpression(key).getValue(context , boolean.class);
        }catch(Exception e){
        	logger.error(e.getMessage() , e);
        	return false ;
        }
        
    }
     /**
      *    获取缓存的key 
      *    key 定义在注解上，支持SPEL表达式
      * @param pjp
      * @return
      */
    private String parseKey(String key,Method method,Object [] args){
        
        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u =   
            new LocalVariableTableParameterNameDiscoverer();  
        String [] paraNameArr=u.getParameterNames(method);
        
        //使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser(); 
        //SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SPEL上下文中
        for(int i=0;i<paraNameArr.length;i++){
            context.setVariable(paraNameArr[i], args[i]);
        }
        try{
        	return parser.parseExpression(key).getValue(context , String.class);
        }catch(Exception e){
        	logger.error(e.getMessage() , e);
        	return null ;
        }
        
    }
	
    
    public Type getGenericReturnType(Class<?> clazz , Type returnType){
    	if(clazz == CompletableFuture.class  && returnType instanceof ParameterizedType){
    		Type[] types = ((ParameterizedType) returnType)
                    .getActualTypeArguments();// 泛型类型列表
    		return types[0] ;
    	}
    	return clazz ;
    }
    
	public SessionUtil getSessionUtil() {
		return sessionUtil;
	}

	public void setSessionUtil(SessionUtil sessionUtil) {
		this.sessionUtil = sessionUtil;
	}
	

}
