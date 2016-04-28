package com.jee.async.common.util ;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class SpringContextUtil implements ApplicationContextAware {
	// Spring应用上下文环境  
    private static ApplicationContext applicationContext;  

    /** 
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境 
     *
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /** 
     * {@link ApplicationContext#getBean(String)}
     * 获取对象 
     * 这里重写了bean方法，起主要作用 
     * @param name 
     * @return Object 一个以所给名字注册的bean的实例 
     * @throws BeansException 
     */
    @SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException {  
        return (T)applicationContext.getBean(name);  
    }
    
    /**
     * 
     * {@link ApplicationContext#getBean(Class)}
     */
    public static <T> T getBean(Class<T> clazz) {
    	return applicationContext.getBean(clazz);
    }
    
    /**
     * {@link ApplicationContext#containsBean(String)}
     * @param id
     * @return
     */
    public static boolean containsBean(String id) {
    	return applicationContext.containsBean(id);
    }
    
}
