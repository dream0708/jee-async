package com.jee.async.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER,ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME) 
public @interface RequestParam {
	
	String name() default "" ;
	
	boolean required() default false ;
	
	String defaultValue() default "";

}
