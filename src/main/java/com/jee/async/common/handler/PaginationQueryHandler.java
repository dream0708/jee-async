package com.jee.async.common.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.jee.async.common.annotation.Pagination;
import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;
import com.jee.async.common.model.Page;

public class PaginationQueryHandler implements HandlerMethodArgumentResolver{

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(Pagination.class)) {
            return true;
        }
        return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		Pagination pagination = parameter.getParameterAnnotation(Pagination.class) ;
		if(null == pagination){
			throw new BusinessException(ResponseCode.CONFIG_ERROR_540 , "分页操作配置错误") ;
		}
		String no = pagination.no() ;
		if(StringUtils.isBlank(no)){
			throw new BusinessException(ResponseCode.CONFIG_ERROR_540 , "分页查询(pageNo)参数配置错误") ;
		}
		String size = pagination.size() ;
		if(StringUtils.isBlank(size)){
			throw new BusinessException(ResponseCode.CONFIG_ERROR_540 , "分页查询(pageSize)参数配置错误") ;
		}
		String pNo = webRequest.getParameter(no);
		if(StringUtils.isBlank(pNo) || !StringUtils.isNumeric(pNo)){
			throw new BusinessException(ResponseCode.ILLEGAL_PARAM_411 , "分页查询(" + no +")参数格式有误,请重新输入") ;
		}
		String pSize = webRequest.getParameter(size);
		if(StringUtils.isBlank(pSize) || !StringUtils.isNumeric(pSize)){
			throw new BusinessException(ResponseCode.ILLEGAL_PARAM_411 , "分页查询(" + size +")参数格式有误,请重新输入") ;
		}
		Integer pageNo = Integer.valueOf(pNo) ;
		if(pageNo <= 0){
			throw new BusinessException(ResponseCode.ILLEGAL_PARAM_411 , "分页查询(" + no +")数字不符合分页要求") ;
		}
		Integer pageSize = Integer.valueOf(pSize) ;
		if(pageSize <= 0){
			throw new BusinessException(ResponseCode.ILLEGAL_PARAM_411 , "分页查询(" + size +")数字不符合分页要求") ;
		}
		return new Page(pageNo , pageSize) ;
	}

}
