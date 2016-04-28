package com.jee.async.common.interceptor;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptorAdapter;

import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;

public class AsyncTimeOutInterceptor extends DeferredResultProcessingInterceptorAdapter {

	

	@Override
	public <T> void beforeConcurrentHandling(final NativeWebRequest request, final DeferredResult<T> deferredResult)
			throws Exception {
	
	}

	@Override
	public <T> void preProcess(final NativeWebRequest request, final DeferredResult<T> deferredResult)
			throws Exception {
		
	}

	@Override
	public <T> void postProcess(final NativeWebRequest request, final DeferredResult<T> deferredResult,
			final Object concurrentResult) throws Exception {
		
		
	}

	@Override
	public <T> boolean handleTimeout(final NativeWebRequest request, final DeferredResult<T> deferredResult)
			throws Exception {
		throw new BusinessException(ResponseCode.DEFERREDRESULT_TIMEOUT_503 , "异步数据返回超时") ;
		//return true; // 如果return true 那么后续的拦截器的handleTimeout将不执行
	}

	@Override
	public <T> void afterCompletion(final NativeWebRequest request, final DeferredResult<T> deferredResult)
			throws Exception {
		
		
	}
}