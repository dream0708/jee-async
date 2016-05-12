package com.jee.async.common.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.jee.async.common.session.SessionUtil;

public class User implements Serializable {
	
	private static final long serialVersionUID = 5321850310574368492L;
	
	/**key**/
	private String userid ;
	private String usercode ;
	private Long id ;
	private String username ;
	private String nickName ;
	private String loginIp ;
	@JSONField(name="pwd")
	private String password = "******" ;
	private List<String> roles ;
	private List<String> permssions ;
	
	/**用户登录标示**/
	private String sessionid ;
	
	/**CSRF 随机码**/
	private String hash ;
	
	@JSONField(serialize = false)
	private transient SessionUtil session ;
	@JSONField(serialize = false)
	private transient String prefixSession ;
	
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
	public String getPrefixSession() {
		return prefixSession;
	}
	public void setPrefixSession(String prefixSession) {
		this.prefixSession = prefixSession;
	}
	
	
	public SessionUtil getSession() {
		return session;
	}
	
	public void setSession(SessionUtil session) {
		this.session = session;
	}
	
	
	@JSONField(serialize = false)
	public  void setAttribute(String key , String value ){
		session.setAttribute(prefixSession + sessionid, key , value);
	}
	@JSONField(serialize = false)
	public  void setAttribute(Map<String , String> value ){
		session.setAttribute(prefixSession + sessionid, value);
	}
	@JSONField(serialize = false)
	public void setAttribute(String key , Object value){
		session.setAttribute(prefixSession + sessionid, key , value);
	}
	@JSONField(serialize = false)
	public String getAttribute(String key){
		return session.getAttribute(prefixSession + sessionid, key) ;
	}
	@JSONField(serialize = false)
	public Map<String ,String> getAll(){
		return session.getAll(prefixSession + sessionid) ;
	}
	@JSONField(serialize = false)
	public void removeAttribute(String key){
		session.removeAttribute(prefixSession + sessionid, key);
	}
	
}
