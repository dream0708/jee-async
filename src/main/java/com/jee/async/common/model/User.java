package com.jee.async.common.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import com.alibaba.fastjson.annotation.JSONField;

public class User implements Serializable {
	
	private static final long serialVersionUID = 5321850310574368492L;
	private String usercode ;
	/**key**/
	private String userid ;
	private Long id ;
	private String username ;
	private String nickName ;
	private String loginIp ;
	
	@JSONField(name="pwd")
	private String password = "******" ;
	private List<String> roles ;
	private List<String> permssions ;
	
	/**用户登录标示**/
	private String token ;
	
	/**CSRF 随机码**/
	private String hash ;
	
	public User(){
		
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	
	public User(String name){
		this.username = name ;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Long getId() {
		return id;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getPermssions() {
		return permssions;
	}

	public void setPermssions(List<String> permssions) {
		this.permssions = permssions;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	
}
