package com.jee.async.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jee.async.common.enums.ParamType;

/**
 * 接口单个入参描述 
 * @author yaomengke
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiParam {
	
	String name() default "" ; //请求参数名称
	
	String desc() default "" ; //请求参数描述
	
	boolean required() default false ; //是否为必须传递
	
	ParamType type() default ParamType.Ignore ; //参数类型
	
	String defaultValue() default "" ;//默认值
	
}
