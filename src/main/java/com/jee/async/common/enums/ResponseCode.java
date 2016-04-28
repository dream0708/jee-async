package com.jee.async.common.enums;

/**
 * 接口状态码描述 
 * @author yaomengke
 *
 */
public enum ResponseCode implements IBusinessResponse {
	//http://www.restapitutorial.com/httpstatuscodes.html
	SUCCESS_0(0 , "SUCCESS" , "操作成功") , //成功统称
	FAILUER_1(1 , "FAILUER" , "操作失败") , //失败统称 不区分级别的错误
	
	// 20X系列  请求成功
	OK_200(200 , "OK" , "操作成功") ,  
	CREATED_201(201 , "CREATED" , "创建成功") , 
	ACCEPTED_202(202 , "ACCEPTED" , "接受请求") , 
	DELETE_203(203 , "DELETE" , "删除成功") ,
	QUERY_204(204 , "QUERY" , "查询成功") ,
	ADD_205(205 , "ADD" , "添加成功") ,
	INVOKE_206(206 , "INVOKE" , "调用成功") ,
	
	//30X系列 重定向
	MOVED_PERMANENTLY_301(301 , "MOVED_PERMANENTLY" , "被请求的资源已永久移动到新位置") ,
	FOUND_302(302 , "FOUND" , "请求的资源现在临时从不同的 URI 响应请求") ,
	
	
	//40x系列 客户端错误
    NULL_ARGUMENT_400(400 , "NULL_ARGUMENT", "空参数异常"),
    UNAUTHORIZED_401(401 , "UNAUTHORIZED" , "未授权的访问") ,
    FORBIDDEN_403(403 , "FORBIDDEN" ,  "服务器拒绝执行" ) ,
    NOT_FOUND_404(404 , "NOT_FOUND" , "无效资源") ,
    
    REQUEST_TIMEOUT_408(408 , "REQUEST_TIMEOUT" , "请求超时") ,
	
    ILLEGAL_PARAM_411(411 , "ILLEGAL_PARAM" , "非法参数传入") ,
    WRONG_PARAM_412(412 , "WRONG_PARAM", "错误参数传入"),
    RESULT_UN_WANTED_413(413 , "RESULT_UN_WANTED" , "结果不满足要求" ) ,
	
	//50x系列 服务端错误
	SERVER_ERROR_500(500 , "SERVER_ERROR" , "服务器异常") ,
	NOT_IMPLEMENTED_501(501 , "NOT_IMPLEMENTED" , "服务器不支持当前请求所需要的某个功能") ,
	SEVER_TIMEOUT_502(502 , "SEVER_TIMEOUT" , "系统超时") ,
	DEFERREDRESULT_TIMEOUT_503(503 , "DEFERREDRESULT_TIMEOUT" , "异步操作数据返回超时") ,
	SYSTEM_ERROR_504(504 , "SYSTEM_ERROR" , "系统异常") ,
	RESULT_NOT_RETURN(505 , "RESULT_NOT_RETURN" , "没有获得到相应的返回数据") ,
	
	NETWORK_ERROR_511(511 , "NETWORK_ERROR" , "网络异常") ,
	SERVER_REJECT_512(512 , "SERVER_REJECT" , "服务器拒绝访问") ,
	
	DB_CONNECTION_ERROR_520(520 , "DB_CONNECTION_ERROR" , "数据库连接异常") ,
	DB_SYSYTEM_ERROR_521(521 , "DB_SYSYTEM_ERROR" , "数据库异常") ,
	ZBUS_INVOKE_ERROR_531(531 , "ZBUS_INVOKE_ERROR" , "调用zbus发生异常") ,
	
	CONFIG_ERROR_540(540 , "CONFIG_ERROR" , "服务端配置错误") ,
	
    //60x系列  用户身份认证、权限判断 自定义错误
	NO_LOGIN_600(600 , "NO_LOGIN" , "没有登录系统,或者登录超时") ,
	
	TOKEN_INVALID_611(611 , "TOKEN_INVALID" , "令牌失效") ,
	TOKEN_TIMEOUT_612(612 ,  "TOKEN_TIMEOUT" , "令牌超时") ,
	TOKEN_CONFIG_ERROR_613(613 , "TOKEN_CONFIG_ERROR" , "token令牌服务器配置错误") ,
	
	ROLES_UNAUTHORIZED_621(621 , "ROLES_UNAUTHORIZED" , "接口访问角色非法") ,
	ROLES_CONFIG_ERROR_622(622 , "ROLES_CONFIG_ERROR" , "接口访问角色配置错误") ,
	
	
	HASH_CHECK_FAILDED_631(631 , "HASH_CHECK_FAILDED" , "hash校验失败") ,
	HASH_UNAUTHORIZED_632(632 , "HASH_UNAUTHORIZED" , "hash校验失败") ,
	
	IP_ERROR_641(641 , "IP_ERROR" , "ip地址异常") ,
	NETWORK_CHANGE_642(642 , "NETWORK_CHANGE" , "ip地址异常变化") ,
	
	TRADE_LOGIN_ERROR_651(651 , "TRADE_LOGIN_ERROR" , "交易登录异常") ,
	TRADE_TOKEN_ERROR_652(652 , "TRADE_TOKEN_ERROR" , "登录令牌获取发生异常") ,
	TRADE_VALID_ERROR_653(653 , "TRADE_VALID_ERROR" , "登录令牌验证发生异常") ,
    
	SSO_SYSTEM_ERROR_661(661 , "SSO_SYSTEM_ERROR" , "sso系统异常") ,
	SSO_VALIED_FAILED_662(662 , "SSO_VALIED_FAILED" , "sso验证失败") ,
	
	JSON_FORMAT_ERROR_671(671 , "JSON_FORMAT_ERROR" , "JSON转换异常") ,
	
	//70x 业务1 相关自定义异常
	NICK_NAME_INVALID_701(701 , "NICK_NAME_INVALID" , "昵称无效") ,
	PASSWORD_UN_SAFE_702(702 , "PASSWORD_UN_SAFE" , "密码安全级别不够") ,
	PASSWORD_NOT_SAME_703(703 , "PASSWORD_NOT_SAME" , "两次密码不一致") ,
	VERIFY_CODE_ERROR_704(704 , "VERIFY_CODE_ERROR" , "验证码错误" ) ;
	
	//80x 业务2 相关自定义异常
	
	
	
    private int code;
    private String name;
    private String msg ;
    
    private ResponseCode() {
    	
    }
    private ResponseCode(int code , String name, String msg ) {
    	this.code = code ;
    	this.name= name ;
    	this.msg = msg;
    }
    private ResponseCode(int code ,String msg ) {
    	this.code = code ;
    	this.msg = msg;
    }
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
    
    
}

