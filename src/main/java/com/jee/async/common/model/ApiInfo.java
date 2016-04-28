package com.jee.async.common.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.jee.async.common.annotation.ApiParam;

public class ApiInfo {

	@JSONField(ordinal = 0)
	private String desc;
	@JSONField(ordinal = 2)
	private String className;
	@JSONField(ordinal = 3)
	private String methodName;
	@JSONField(ordinal = 1)
	private String url;
	@JSONField(ordinal = 6)
	private String returnType;
	
	@JSONField(ordinal = 4)
	private AuthInfo authInfo;
	@JSONField(ordinal = 5)
	private List<ParamInfo> paramInfo;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public AuthInfo getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(AuthInfo authInfo) {
		this.authInfo = authInfo;
	}

	public List<ParamInfo> getParamInfo() {
		return paramInfo;
	}

	public void setParamInfo(List<ParamInfo> paramInfo) {
		this.paramInfo = paramInfo;
	}

	public class AuthInfo {

		@JSONField(ordinal = 0)
		private String token;
		@JSONField(ordinal = 2)
		private String roles;
		@JSONField(ordinal = 1)
		private boolean hash;

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getRoles() {
			return roles;
		}

		public void setRoles(String roles) {
			this.roles = roles;
		}

		public boolean isHash() {
			return hash;
		}

		public void setHash(boolean hash) {
			this.hash = hash;
		}

	}

   public class ParamInfo {
	    @JSONField(ordinal = 1)
		private String name;
		@JSONField(ordinal = 0)
		private String desc;
		@JSONField(ordinal = 2)
		private boolean requried;
		@JSONField(ordinal = 3)
		private String type;
		@JSONField(ordinal = 4)
		private String defaultValue;

		public ParamInfo(ApiParam apies) {
			this.desc = apies.desc();
			this.name = apies.name();
			this.defaultValue = apies.defaultValue();
			this.type = apies.type().getCode() ;
			this.requried = apies.required();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public boolean isRequried() {
			return requried;
		}

		public void setRequried(boolean requried) {
			this.requried = requried;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

	}

}
