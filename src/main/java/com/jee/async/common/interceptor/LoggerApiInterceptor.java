package com.jee.async.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jee.async.common.util.LogUtils;

public class LoggerApiInterceptor extends  HandlerInterceptorAdapter{

	private static final Logger logger = LoggerFactory.getLogger(LoggerApiInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		MDC.clear();
	    LogUtils.logAccessEnter(request);
        return super.preHandle(request, response, handler);
	}


	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	
	}


	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		LogUtils.logAccessCompletion(request);
		if(null != ex){
			logger.error(ex.getMessage() , ex);
		}
		super.afterCompletion(request, response, handler, ex);
		
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
	}




}
