package com.jee.async.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jee.async.common.response.SimpleResponse;


/***
 * XSS\SQL注入的工具类
 * 
 * @author Administrator
 * 
 */
public class RequestParameterFilter implements Filter {
	// 此处是不过滤的参数
	private List<String> excludeNames;
	/**
	 * header中不需要检测的项目
	 */
	private String[] excludeHeaderName = new String[] { "accept", "referer", "content-type" };
	private String[] sqlKeys = new String[] { "select ", "select*", "update ", "insert ", "drop ", "exec", "delete ",
			"truncate ", " and ", " union ", " or ", "‘", "'", ">", "<", "onerror", "onload", "onclick", "oncomplete",
			"javascript", "<script", "</script", "<iframe", "</iframe", "<frame", "</frame", "set-cookie", "%3cscript",
			"%3c/script", "%3ciframe", "%3c/iframe", "%3cframe", "%3c/frame", "src=\"javascript:", "<body", "</body",
			"%3cbody", "%3c/body", "</", "/>", "%3c", "%3e", "%3c/", "/%3e", "alert(" };
	private String[] serviceUrlKey = new String[] { "callback", "service", "targetUrl" };
	private String[] validServiceUrl = new String[] { 
			"127.0.0.1", "localhost", 
	};
	/**
	 * 不用检测的参数名
	 */
	private String[] excludeParamnames = new String[] { "logoutRequest" };
	// private static Logger logger =
	// Logger.getLogger(RequestParameterFilter.class);
	private static Logger logger = LoggerFactory.getLogger(RequestParameterFilter.class);

	public List<String> getExcludeNames() {
		return excludeNames;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		// 特殊处理,如果是sso的cs客户端调用类型，则只判断path是否正常,因为如果调用了getParamter，会使RestletFrameworkServlet调用request中参数失败
		if (httpRequest.getRequestURI().indexOf("/v1/") >= 0) {
			if (checkSql(httpRequest.getRequestURI())) {
				logger.debug("find invalid request-v1 ,redirect to error page!");
				if (StringUtils.isNotBlank(httpRequest.getRequestURI()))
					logger.debug("error url-v1:" + httpRequest.getRequestURI());
				//httpResponse.sendRedirect(httpRequest.getContextPath() + "/403.jsp");
				//httpRequest.getRequestDispatcher("/exception/403").forward(httpRequest, httpResponse);
				SimpleResponse<String> simple = new SimpleResponse<String>().failure("传入参数非法!") ;
				write(httpResponse , JSON.toJSONString(simple)) ;
			} else {
				chain.doFilter(request, response);
			}
		} else if (!checkRequestValid(request)) {
			logger.debug("find invalid request ,redirect to error page!");
			if (StringUtils.isNotBlank(httpRequest.getRequestURI()))
				logger.debug("error url:" + httpRequest.getRequestURI());
			//httpRequest.getRequestDispatcher("/exception/403").forward(httpRequest, httpResponse);
			//throw new BusinessException(ResponseCode.FORBIDDEN , "参数非法!") ;
			SimpleResponse<String> simple = new SimpleResponse<String>().failure("传入参数非法!") ;
			write(httpResponse , JSON.toJSONString(simple)) ;
		} else {
			chain.doFilter(request, response);
		}
	}

	public void write(HttpServletResponse  resp , String str){
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json; charset=utf-8");
		PrintWriter out  = null ;
		try{
			out = resp.getWriter() ;
			out.write(str);
			
		}catch(Exception e){
			logger.error("resp error " , e) ;
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	public void init(FilterConfig config) throws ServletException {

		String exclude = config.getInitParameter("exclude");
		if (exclude != null && exclude.length() > 0) {
			excludeNames = Arrays.asList(exclude.split(","));
		}
	}

	public void destroy() {

	};

	private class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
		public MyHttpServletRequestWrapper(HttpServletRequest request) {

			super(request);
		}

		@Override
		public String getParameter(String name) {

			try {
				if (excludeNames != null && excludeNames.contains(name)) {
					return super.getParameter(name);
				}
				return replaceSql(replaceXss(super.getParameter(name)));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return super.getParameter(name);
			}
		}

		@Override
		public String[] getParameterValues(String name) {

			try {
				if (excludeNames != null && excludeNames.contains(name)) {
					return super.getParameterValues(name);
				}
				String[] params = super.getParameterValues(name);
				if (params != null && params.length > 0)
					for (int i = 0; i < params.length; i++) {
						params[i] = replaceXss(params[i]);
						params[i] = replaceSql(params[i]);
					}
				return params;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return super.getParameterValues(name);
			}
		}
	}

	protected String replaceSql(String value) {

		try {
			if (StringUtils.isNotBlank(value)) {
				for (String sqlKey : sqlKeys) {
					if (value.indexOf(sqlKey) >= 0) {
						logger.debug("发现Sql注入关键词(" + sqlKey + ")，直接替换掉!");
						value = value.replace(sqlKey, "");
					}
				}
				return value;
			}
			return value;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	protected String replaceXss(String value) {

		try {
			if (value != null && value.length() > 0) {
				// 此处还能加更多的过滤规则
				value = value.replace("<", "&lt;");
				value = value.replace(">", "&gt;");
				value = value.replace("script", "&gt;");
				return value;
			}
			return value;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	public String[] getSqlKeys() {

		return sqlKeys;
	}

	public void setSqlKeys(String[] sqlKeys) {

		this.sqlKeys = sqlKeys;
	}

	protected Boolean checkSql(String value) {

		try {
			if (StringUtils.isNotBlank(value)) {
				for (String sqlKey : sqlKeys) {
					if (value.toLowerCase().indexOf(sqlKey) >= 0) {
						logger.debug("发现Sql注入关键词(" + sqlKey + ")，直接返回错误页面!");
						return true;
					}
				}
				return false;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 增加容错性
			return false;
		}
	}

	protected Boolean checkRequestValid(ServletRequest request) {

		try {
			// 遍历参数
			Enumeration paramNames = request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				if (excludeParamnames != null && ArrayUtils.contains(excludeParamnames, paramName)) {
					logger.debug("略过该参数(paramName:" + paramName + ")的检测.");
					continue;
				}
				// 包含单个和多个值
				String[] paramValues = request.getParameterValues(paramName);
				if (paramValues != null && paramValues.length > 0)
					for (int i = 0; i < paramValues.length; i++) {
						String paramValue = paramValues[i];
						logger.debug("[paramName=" + paramName + ",paramValue=" + String.valueOf(paramValue) + "]");
						if (checkSql(paramValue)) {
							// 设置错误标识
							logger.error("[paramName=" + paramName + ",paramValue=" + String.valueOf(paramValue)
									+ "],find abnormal data in paramter,return false ");
							return false;
						}
						// 如果是service参数，就需要检查域名是否在白名单里面
						if (ArrayUtils.contains(serviceUrlKey, paramName)) {
							logger.debug("begin check serviceUrl:" + paramValue);
							String checkValue = paramValue.replace("http://", "");
							checkValue = checkValue.replace("https://", "").trim();
							if (validServiceUrl != null && validServiceUrl.length > 0) {
								boolean checkResult = false;
								for (String validUrl : validServiceUrl)
									if (checkValue.startsWith(validUrl))
										checkResult = true;
								if (!checkResult) {
									logger.error("含有非法的转向地址.[paramName=" + paramName + ",paramValue="
											+ String.valueOf(paramValue) + "]");
									return checkResult;
								}
							}
						}
					}
			}
			// 遍历header
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			Enumeration headerNames = httpRequest.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				String headerValue = httpRequest.getHeader(headerName);
				headerValue = excludeHeaderValue(headerName, headerValue);
				if (!ArrayUtils.contains(excludeHeaderName, headerName.toLowerCase())) {
					// if(!headerName.toLowerCase().equals("accept")){
					if (checkSql(headerValue)) {
						// 设置错误标识
						logger.error("[headerName=" + headerName + ",headerValue=" + String.valueOf(headerValue)
								+ "],find abnormal data in header,return false ");
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 增加容错性
			return true;
		}
	}

	public String excludeHeaderValue(String headerName, String headerValue) {

		try {
			// 过滤掉一些正常的内容
			// 1.过滤掉AutoUpdate
			headerValue = headerValue.replace("AutoUpdate", "");
			// 2.如果是user-agent的话
			if (headerName.toLowerCase().equalsIgnoreCase("user-agent") && headerValue.startsWith("Mozilla")) {
				int start = headerValue.indexOf("(");
				int end = headerValue.indexOf(")");
				if (start > 0 && end > 0 && end > start)
					headerValue = headerValue.replace(headerValue.substring(start, end), "");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return headerValue;
	}

	public String[] getExcludeHeaderName() {

		return excludeHeaderName;
	}

	public void setExcludeHeaderName(String[] excludeHeaderName) {

		this.excludeHeaderName = excludeHeaderName;
	}

	public void setExcludeNames(List<String> excludeNames) {

		this.excludeNames = excludeNames;
	}

	public String[] getValidServiceUrl() {

		return validServiceUrl;
	}

	public void setValidServiceUrl(String[] validServiceUrl) {

		this.validServiceUrl = validServiceUrl;
	}

	public String[] getExcludeParamnames() {

		return excludeParamnames;
	}

	public void setExcludeParamnames(String[] excludeParamnames) {

		this.excludeParamnames = excludeParamnames;
	}

	public String[] getServiceUrlKey() {

		return serviceUrlKey;
	}

	public void setServiceUrlKey(String[] serviceUrlKey) {

		this.serviceUrlKey = serviceUrlKey;
	}
}
