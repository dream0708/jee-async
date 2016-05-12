package com.jee.async.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable {

	String prefix() ;  //key前缀
	
	String key() ;     //redis key
	
	int expire() default -1 ;  // 过期时间 默认不过期
	
	String condition() default ""; // el表达式 
}
