package com.jee.async.common.handler;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;
import com.jee.async.common.response.SimpleResponse;
import com.jee.async.common.util.Constants;

/**
 * 全局错误异常处理 只要是Servlet容器线程中出现异常 都会在这里得到直接返回 异步业务线程出现的异常 要程序封装显式返回
 * 
 * @author yaomengke
 *
 */

// @ControllerAdvice
public class RestExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

	// 自定义依次
	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public SimpleResponse<String> handleBusinessException(HttpServletRequest request, BusinessException ex) {
		String hash = (String) request.getAttribute(Constants.REQUEST_HASH);
		// String token = (String)request.getAttribute("token") ;
		// String userid =
		// (String)request.getAttribute(Constants.REQUEST_USER_FLAG) ;
		// String uuid =
		// (String)request.getAttribute(Constants.REQEUST_UUID_FLAG) ;
		logger.error(ex.getMessage() , ex);
		logger.error("Exception:{}, URL:{},ErrorCode:{} , Message:{}", ex.getClass().getSimpleName(),
				request.getRequestURL(), ex.getErrorCode().getCode(), ex.getMessage());
		return new SimpleResponse<String>().failure(ex).hash(hash).result(ex.getErrorCode().getName());
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class })
	@ResponseBody
	public SimpleResponse<String> handleHttpMessageNotReadableException(HttpServletRequest request,
			HttpMessageNotReadableException ex) {
		String hash = (String) request.getAttribute(Constants.REQUEST_HASH);
		// String token = (String)request.getAttribute("token") ;
		// String userid =
		// (String)request.getAttribute(Constants.REQUEST_USER_FLAG) ;
		// String uuid =
		// (String)request.getAttribute(Constants.REQEUST_UUID_FLAG) ;
		logger.error("Exception:{}, URL:{} , Message:{}", ex.getClass().getSimpleName(), request.getRequestURL(),
				ex.getMessage());
		return new SimpleResponse<String>()
				.failure(ResponseCode.JSON_FORMAT_ERROR_671.getCode(), "请求入参格式有误,请按照接口文档组织参数").hash(hash);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseBody
	public SimpleResponse<String> requestHandlingNoHandlerFound(HttpServletRequest request,
			NoHandlerFoundException ex) {
		String hash = (String) request.getAttribute(Constants.REQUEST_HASH);
		// String token = (String)request.getAttribute("token") ;
		// String uuid =
		// (String)request.getAttribute(Constants.REQUEST_USER_FLAG) ;
		logger.error("Exception:{}, URL:{}, Message:{}", ex.getClass().getSimpleName(), request.getContextPath(),
				ex.getMessage());
		return new SimpleResponse<String>().failure(ResponseCode.NOT_FOUND_404).hash(hash);
	}

	@ExceptionHandler({BindException.class })
    @ResponseBody
    public  SimpleResponse<List<String>> handleBindException(HttpServletRequest request, BindException ex){
		String hash = (String)request.getAttribute(Constants.REQUEST_HASH) ;
        //String token = (String)request.getAttribute("token") ;
        // String userid = (String)request.getAttribute(Constants.REQUEST_USER_FLAG) ;
        // String uuid = (String)request.getAttribute(Constants.REQEUST_UUID_FLAG) ;
		logger.error("Exception:{} , URL:{} , Message:{}",ex.getClass().getSimpleName() ,request.getRequestURL(), ex.getMessage());
		List<String> errors = new LinkedList<String>();
		for(ObjectError error : ex.getFieldErrors()){
			errors.add(error.getDefaultMessage());
		}
        return new SimpleResponse<List<String>>().failure(ResponseCode.JSON_FORMAT_ERROR_671.getCode(), "数据转换错误").hash(hash).result(errors) ;
    }
	
	
	@ExceptionHandler({ Throwable.class, RuntimeException.class, Exception.class })
	@ResponseBody
	public SimpleResponse<String> handleException(HttpServletRequest request, Exception ex) {
		String hash = (String) request.getAttribute(Constants.REQUEST_HASH);
		// String token = (String)request.getAttribute("token") ;
		// String userid =
		// (String)request.getAttribute(Constants.REQUEST_USER_FLAG) ;
		// String uuid =
		// (String)request.getAttribute(Constants.REQEUST_UUID_FLAG) ;
		logger.error(ex.getMessage(), ex);
		logger.error("Exception:{} , URL:{}, Message:{}", ex.getClass().getSimpleName(), request.getContextPath(),
				ex.getMessage());
		return new SimpleResponse<String>().failure(ResponseCode.SYSTEM_ERROR_504).hash(hash);
	}
}