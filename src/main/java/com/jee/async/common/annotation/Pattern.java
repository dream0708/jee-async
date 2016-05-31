package com.jee.async.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jee.async.common.util.RegularUtil;

@Target({ElementType.PARAMETER,ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME) 
public @interface Pattern {

	String regex() default RegularUtil.allReg;
	
	String message() default "" ;
}
