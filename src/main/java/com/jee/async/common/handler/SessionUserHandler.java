package com.jee.async.common.handler;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jee.async.common.annotation.SessionUser;
import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;
import com.jee.async.common.model.User;
import com.jee.async.common.session.SessionManager;
import com.jee.async.common.util.Constants;


/**
 * 封装 类似HttpSession的User对象  验证通过可以直接获取User
 * 可配置化 根据注解按到接口是否需要身份验证
 *    有@SessionUser   表示需要身份验证 
 *    hash默认false 当 hash = true 需要进行hash验证  防止CSRF和重复提交
 *    roles 配置接口角色  &并且关系 |或者关系
 *    根据SessionUser定义可以添加相应管理配置
 * 返回用户User对象 
 * 单例多线程模式
 * 
 * 示例:
 * @SessionUser() 
 *  需要身份认证 默认认证字段sessionid,默认不需要hash认证,默认不要角色认证
 * @SessionUser(value="sessionid" , hash = true, roles = "aa&&bb&&cc") 
 *  需要身份认证认证字段sessionid ,需要hash校验 , 接口需要同时具有aa,bb,cc角色
 * @SessionUser(value="token" , hash = false, roles = "aa||bb||dd") 
 *  需要身份认证认证字段token ,不需要hash校验 , 接口需要具有aa,bb,cc任何一个角色
 * 支持权限控制可扩展  
 * @author yaomengke
 *
 */

public class SessionUserHandler implements HandlerMethodArgumentResolver  {
	
	private Object lock = new Object();
	
	private SessionManager sessionManager ;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		
		//如果参数类型是User并且有SessionUser注解则支持
        if (parameter.getParameterType().isAssignableFrom(User.class) &&
                parameter.hasParameterAnnotation(SessionUser.class)) {
            return true;
        }
        return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		SessionUser info = parameter.getParameterAnnotation(SessionUser.class);
		if(info != null){
			String value = info.value() ;
			if(StringUtils.isNotBlank(value)){
				String token = (String)webRequest.getParameter(value); //身份验证  前端有可能放在Hearder里面
				if(StringUtils.isNotBlank(token)){
					User user = validToken(token , webRequest) ;
					if( user != null ){
						MDC.put(Constants.REQUEST_USER_FLAG, user.getUserid()) ; //身份验证成功后
						webRequest.setAttribute(Constants.REQUEST_USER_FLAG, user.getUserid(), WebRequest.SCOPE_REQUEST);
						String roles = info.roles() ;
						validRoles(roles , user) ;
						//检查hash
						boolean hash = info.hash() ;
						if(hash){
							String hashName = SessionUser.hashName ; //传入hash验证的字段名
							String hstr = (String)webRequest.getParameter(hashName) ; // hash 防止重复提交，CSRF攻击 前端有可能放在Hearder里面
							if(StringUtils.isNotBlank(hstr)){ 
								synchronized(lock){  //跨JVM问题没有解决 需要使用分布式lock
									String newHash = validHash(token, hstr) ; 
									if(StringUtils.isNotBlank(newHash)){ //hash验证成功才会返回新的有效的hash 验证不成功 hash不变
										// 更新hash操作
										webRequest.setAttribute(Constants.REQUEST_HASH, newHash, WebRequest.SCOPE_REQUEST);
										user.setHash(newHash);
										//MDC.put(Constants.REQUEST_USER_FLAG, user.getUserid()) ; //用户标识
										return user ;
									}else{
										throw new BusinessException(ResponseCode.HASH_CHECK_FAILDED_631, "HASH验证未通过");
									}
								}
								
							}else{
								throw new BusinessException(ResponseCode.NULL_ARGUMENT_400, "需要HASH验证数据");
							}
							
						}else{ //不需要hash 比如查询等
							//webRequest.setAttribute(Constants.REQUEST_HASH, user.getHash(), WebRequest.SCOPE_REQUEST);
							//MDC.put(Constants.REQUEST_USER_FLAG, user.getUserid()) ;
							return user ;
						}
					
					}else{
						throw new BusinessException(ResponseCode.NO_LOGIN_600 , "没有登录系统,或者登录超时");
					}
				}
				//
				throw new BusinessException(ResponseCode.TOKEN_CONFIG_ERROR_613 , "用户身份验证码非法");
			}
			throw new BusinessException(ResponseCode.TOKEN_CONFIG_ERROR_613 , "令牌服务器配置错误");
		}else{
			throw new BusinessException(ResponseCode.UNAUTHORIZED_401 , "未授权访问,请登录");
		}
	}
	
	
	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	
	/**
	 * 验证用户身份
	 * @param token
	 * @return
	 */
	public User validToken(String token , String ip){ // redis
		try{
			User user = new User();
			user = sessionManager.checkToken(token , ip);
			
			return user ;
		}catch(BusinessException ex){
			throw  ex ;
		}catch(Exception ex){
			throw new BusinessException(ResponseCode.TOKEN_INVALID_611 , "用户身份失败!");
		}
	}
	/**
	 * 验证用户身份
	 * @param token
	 * @return
	 */
	public User validToken(String token , NativeWebRequest request){ // redis
		try{
			User user = new User();
			user = sessionManager.checkToken(token , request);
			
			return user ;
		}catch(BusinessException ex){
			throw  ex ;
		}catch(Exception ex){
			throw new BusinessException(ResponseCode.TOKEN_INVALID_611 , "用户身份验证失败!");
		}
	}
	/**
	 * 验证成功后,返回新hash
	 * @param token
	 * @param hash
	 * @return
	 */
	public String validHash(String  token, String hash){  // redis
		try{
			String nhash = sessionManager.validHash(token, hash) ;
			return nhash ;
		}catch(BusinessException ex){
			throw new BusinessException(ex.getErrorCode() , ex.getMessage());
		}catch(Exception ex){
			throw new BusinessException(ResponseCode.HASH_CHECK_FAILDED_631 , "HASH验证失败!");
		}
	}
	
	

	/**
	 * 验证用户权限
	 * @param roles
	 * @param user
	 */
	public void validRoles(String roles , final User user){
		if(StringUtils.isEmpty(roles)){
			throw new BusinessException(ResponseCode.ROLES_CONFIG_ERROR_622, "接口访问权限配置错误!");
		}
		if(!roles.equals("all")){ //非默认情况  默认情况直接通过
			//这里面需要根据userid 去查询用户角色信息
			user.setRoles(Arrays.asList(new String[]{"aa" , "bb" , "cc" , "dd" , "ee"}));
			//包装user

			
			if(user.getRoles() == null || user.getRoles().size() == 0){
				throw new BusinessException(ResponseCode.ROLES_UNAUTHORIZED_621, "用户角色为空,需要添加相应角色才可以访问该权限!");
			}
			if(roles.contains("|") && roles.contains("&")){
				throw new BusinessException(ResponseCode.ROLES_CONFIG_ERROR_622, "接口访问权限配置错误!");
			}
			
			if(! roles.contains("|") && !roles.contains("&")){  //单一权限
				if(! user.getRoles().contains(roles)){
					throw new BusinessException(ResponseCode.ROLES_UNAUTHORIZED_621, "访问该接口需要权限:" + roles);
				}
			}
						
			
			if(roles.contains("|")){ //或者关系
				String[] ors = roles.split("\\|+") ; 
				Long have = Stream.of(ors).filter( (o) -> {
					   return user.getRoles().contains(o) ;} ).count() ;
				if(have == 0){
					throw new BusinessException(ResponseCode.ROLES_UNAUTHORIZED_621, "访问该接口需要权限:" + roles);
				}
				/**
				boolean has = false ;
				for(String or : ors){
					if(user.getRoles().contains(or)){
						has = true ;
						break ;
					}
				}
				if(!has){ 
					throw new BusinessException(ResponseCode.ROLES_UNAUTHORIZED, "访问该接口需要权限:" + roles);
				}**/
			}
			if(roles.contains("&")){ //并且关系
				String[] ands = roles.split("\\&+") ; 
				List<String> lands = new ArrayList<String>(Arrays.asList(ands));
				if(! user.getRoles().containsAll(lands)){
					throw new BusinessException(ResponseCode.ROLES_UNAUTHORIZED_621, "访问该接口需要权限:" + roles);
				}
			}
		
		}
	}
	
}
