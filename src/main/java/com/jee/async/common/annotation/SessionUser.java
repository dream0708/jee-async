package com.jee.async.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jee.async.common.util.Constants;
/**
 * 会话管理控制可配置注解
 * @author yaomengke
 *
 */
@Target(ElementType.PARAMETER)  
@Retention(RetentionPolicy.RUNTIME) 
public @interface SessionUser {

	final String hashName  = Constants.REQUEST_HASH;  // 传入hash验证的字段
	
	String value() default Constants.REQUEST_TOKEN ; //用户身份认证字段
	
	boolean hash() default false ;  //随机码  防止重复提交和CSRF攻击
	
	String roles() default "all" ; //用户角色
	
	String permissions() default  "all" ; //用户权限
	
	String step() default "first" ; //步骤
}
