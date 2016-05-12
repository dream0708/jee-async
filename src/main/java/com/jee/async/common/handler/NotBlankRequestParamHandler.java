package com.jee.async.common.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jee.async.common.annotation.NotBlankRequestParam;
import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;

public class NotBlankRequestParamHandler implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(NotBlankRequestParam.class)) {
            return true;
        }
        return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		NotBlankRequestParam info = parameter.getParameterAnnotation(NotBlankRequestParam.class);
		
		String name = info.name() ;
		if(StringUtils.isBlank(name)){
			name = parameter.getParameterName() ;
		}
		String value = webRequest.getParameter(name) ;
		Object arg = null ;
		
		if(StringUtils.isNotBlank(value)){
			try {
				WebDataBinder binder = binderFactory.createBinder(webRequest, null, name);
				arg =  binder.convertIfNecessary(value, parameter.getParameterType(), parameter);
				return arg ;
			}catch(Exception ex){
				throw new BusinessException(ResponseCode.ILLEGAL_PARAM_411 , showConvertError(parameter , name , value)) ;
			}
		}
		String defaultValue = info.defaultValue() ;
		
		if(StringUtils.isBlank(value) && StringUtils.isBlank(defaultValue)){
			throw new BusinessException(ResponseCode.ILLEGAL_PARAM_411 , showInputError(info , name)) ;
		}
		
		if(StringUtils.isBlank(value) && StringUtils.isNotBlank(defaultValue)){
			try {
				WebDataBinder binder = binderFactory.createBinder(webRequest, null, name);
				arg = binder.convertIfNecessary(defaultValue, parameter.getParameterType(), parameter);
			    return arg ;
			}catch(Exception ex){
				//转化错误
				throw new BusinessException(ResponseCode.ILLEGAL_PARAM_411 , showConvertDefaultError(parameter , name , value)) ;
			}
		}
		
		return arg ;
	}
	
	public String showConvertDefaultError(MethodParameter parameters , String name , String value){
		 return  "参数 " + name + " 默认值配置有误 , 错误原因  ( 期待传入参数类型:" + parameters.getParameterType() + " , 实际配置默认值: '" + value + "' ) " ;
	}
	
	public String showConvertError(MethodParameter parameters , String name , String value){
		 return  "参数 " + name + " 转化有误 , 错误原因  ( 期待传入参数类型:" + parameters.getParameterType() + " , 实际传入数据: '" + value + "' ) " ;
	}
	
	public String showInputError(NotBlankRequestParam info , String name){
		String errorMsg = info.errorMsg() ;
		if(StringUtils.isBlank(errorMsg)){
			errorMsg = "参数: " +name + " 不为空！ " ;
		}
		return errorMsg ;
	}

}
