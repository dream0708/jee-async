package com.jee.async.common.response;
import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class RtnSuper implements Serializable {

	private static final long serialVersionUID = -1518404045147038306L;

	/**
     * 返回代码
     */
	@JSONField(name="code" , ordinal = 0 )
    private Integer rtnCode;

    /**
     * 成功消息
     */
	@JSONField(name="msg" , ordinal = 1 )
    private String[] rtnMsg;
    
	/**
	 * 返回用户token/sessionid值 用户登录标示
	 */
	@JSONField(name="sessionid" , ordinal = 2 )
	private String sessionid ;
	
	/**
	 * 防止CSRF攻击 重复提交的随机码
	 */
	@JSONField(name="hash" , ordinal = 3 )
	private String hash ;
	

    
	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
    public Integer getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(Integer rtnCode) {
		this.rtnCode = rtnCode;
	}

	public String[] getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String[] rtnMsg) {
        this.rtnMsg = rtnMsg;
    }
    
   
}
