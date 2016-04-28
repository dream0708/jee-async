package com.jee.async.common.session;

import org.springframework.web.context.request.NativeWebRequest;

import com.jee.async.common.model.User;
/**
 * 用户会话 控制模块
 * @author yaomengke
 *
 */
public interface SessionManager {

	/**
	 * 创建session
	 * @param user
	 * @return
	 */
    public User createToken(User user);

    /**
     * 检验用户session有效性
     * @param token
     * @param ip
     * @return
     */
    public User checkToken(String token , String ip);
    /**
     * 检验用户session有效性 重载方法
     * @param token
     * @param ip
     * @return
     */
    public User checkToken(String token , final NativeWebRequest request) ;

    /**
     * 删除session
     * @param token
     */
    public void deleteToken(String token);
    
    /**
     * 验证接口hash
     * @param token
     * @param hash
     * @return
     */
    public String validHash(String token , String hash) ;
}
