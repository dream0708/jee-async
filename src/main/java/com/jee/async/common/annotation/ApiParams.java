package com.jee.async.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jee.async.common.enums.ParamType;

/**
 * 接口所有入参描述 
 * @author yaomengke
 *
 */
@Target({ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiParams {
	
	ApiParam[] value() default  {@ApiParam(desc= "" , name = "" , required = false , type = ParamType.Ignore, defaultValue = "") } ;
	          
}
