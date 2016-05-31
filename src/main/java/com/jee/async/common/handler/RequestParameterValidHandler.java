package com.jee.async.common.handler;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jee.async.common.annotation.Length;
import com.jee.async.common.annotation.NotBlank;
import com.jee.async.common.annotation.Pattern;
import com.jee.async.common.annotation.RequestParam;
import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;
import com.jee.async.common.util.RegularUtil;

public class RequestParameterValidHandler implements HandlerMethodArgumentResolver {

	public Logger logger = LogManager.getLogger(RequestParameterValidHandler.class) ;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> type = parameter.getParameterType() ;
		return  
				(parameter.hasParameterAnnotation(RequestParam.class) ||
				parameter.hasParameterAnnotation(NotBlank.class) ||
				parameter.hasParameterAnnotation(Pattern.class) ||
				parameter.hasParameterAnnotation(Length.class)) &&
				(type.isPrimitive() || type == String.class || type == Number.class );
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Class<?> type = parameter.getParameterType() ;
		RequestParam rp = parameter.getParameterAnnotation(RequestParam.class) ;
		String name = parameter.getParameterName() ; 
		String defaultValue = null ;
		if(rp != null){
			name = rp.name() ;
			defaultValue = rp.defaultValue() ;
		}
		try{
			WebDataBinder binder = binderFactory.createBinder(webRequest, null, name);
			Object[] values = webRequest.getParameterValues(name) ;
			Object value = null ;
			if(values != null && values.length > 0 ){
				value = values[0] ;
			}
			if(value == null && StringUtils.isNotBlank(defaultValue)){
				value = defaultValue ;
			}
			Object arg =  binder.convertIfNecessary(value, parameter.getParameterType(), parameter);
			NotBlank notBlank = parameter.getParameterAnnotation(NotBlank.class) ;
			String errorMsg = "" ;
			if(notBlank != null ){
				errorMsg = name + "不为空" ;
				if(StringUtils.isNotBlank(notBlank.message())){
					errorMsg = notBlank.message() ;
				}
				if(arg == null){
					throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , errorMsg ) ;
				}
				if(arg instanceof String){
					if(StringUtils.isBlank((String)arg)){
						throw new BusinessException(ResponseCode.NULL_ARGUMENT_400 , errorMsg ) ;
					}
				}
			}
			if(type == String.class){
				Length length = parameter.getParameterAnnotation(Length.class) ;
				if(length != null){
					int min = length.min() ;
					int max = length.max() ;
					String args = (String)arg ;
					if(StringUtils.isNotBlank(args)){
						if(args.length() < min || args.length() > max){
							errorMsg =  length.message() ;
							if(StringUtils.isBlank(errorMsg)){
								errorMsg = name + "长度不正确,长度范围需在[" + min + " , " + max + " ] " ;
							}
							throw new BusinessException(ResponseCode.ILLEGAL_PARAM_411, errorMsg ) ;
						}
					}
					
				}
				Pattern pattern = parameter.getParameterAnnotation(Pattern.class) ;
				if(pattern != null ){
					String regxp = pattern.regex() ;
					String args = (String)arg ;
					if(StringUtils.isNotBlank(args) && StringUtils.isNotBlank(regxp)){
						boolean legal = RegularUtil.checkLegal(args, regxp) ;
						if(!legal){
							errorMsg = pattern.message() ;
							if(StringUtils.isBlank(errorMsg)){
								errorMsg = name + "不符合正则表达式要求" ;
							}
							throw new BusinessException(ResponseCode.ILLEGAL_PARAM_411, errorMsg ) ;
						}
					}
				}
				
			}
			
			return arg ;
			
		}catch(BusinessException ex){
			throw ex ;
		}catch(Exception ex){
			logger.error(ex.getMessage() , ex);
			throw new BusinessException(ResponseCode.ILLEGAL_PARAM_411 , name + "参数错误!") ;
		}
		
	}
	
	

}
