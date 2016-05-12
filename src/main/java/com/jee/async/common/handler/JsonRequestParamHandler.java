package com.jee.async.common.handler;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSON;
import com.jee.async.common.annotation.JsonRequestParam;
import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;

public class JsonRequestParamHandler implements HandlerMethodArgumentResolver {

	private static Logger logger = LoggerFactory.getLogger(RestRejectedExecutionHandler.class);

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(JsonRequestParam.class)) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		JsonRequestParam info = parameter.getParameterAnnotation(JsonRequestParam.class);
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		// content-type不是json的不处理
		
		if (!request.getContentType().contains("application/json")) {
			return null;
		}

		// 把reqeust的body读取到StringBuilder
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();

		char[] buf = new char[1024];
		int rd;
		while ((rd = reader.read(buf)) != -1) {
			sb.append(buf, 0, rd);
		}
		
		try {
			Class<?> clazz = parameter.getParameterType();
			return JSON.parseObject(sb.toString(), clazz);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new BusinessException(ResponseCode.JSON_FORMAT_ERROR_671, "数据转换错误");

		}
	}

	

}
