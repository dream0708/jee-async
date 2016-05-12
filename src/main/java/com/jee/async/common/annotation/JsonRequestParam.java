package com.jee.async.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target(ElementType.PARAMETER)  
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRequestParam {
	
    String name() default "" ;
	
    boolean required() default false ;
	
	String  errorMsg() default "" ;
	
	String defaultValue() default "" ;

}
