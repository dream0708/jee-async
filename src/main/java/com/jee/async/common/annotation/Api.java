package com.jee.async.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 接口描述 注解
 * @author yaomengke
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  Api{
	
	String desc() default  "";  //接口功能描述

	String response() default "SimpleResponse"; //接口返回值类型

	String author() default "" ; //作者 
	
    String version() default ""; //版本
}
