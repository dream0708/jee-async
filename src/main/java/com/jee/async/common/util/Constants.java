package com.jee.async.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 统一管理常量 
 * @author yaomengke
 *
 */
public class Constants {
	
	//public static final String REQUEST_FLAG = "request_uuid" ;
	
	public static final String REQEUST_UUID_FLAG = "uuid" ;
	
	public static final String MDC_UUID_FLAG = "uuid" ;
	
	public static final String REQUEST_USER_FLAG = "userid" ;
	
	public static final String RQEUST_TIME_FLAG = "request_begin" ;
	
	public static final int MAX_LOG_LENGTH = 12 ;
	
	public static final String REQUEST_TOKEN = "sessionid" ;
	
	public static final String REQUEST_HASH = "hash" ;
	
	public static final String REQUEST_ERROR = "error" ;
	
	public static final List<String> sensitives = Arrays.asList(  //敏感字段过滤规则
			"password" , "pwd" , "secret" , "u-keys"
    );
	
	
	
	 //http://blog.chinaunix.net/uid-20577907-id-1758770.html
	
	 public static String mobileReg = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$" ;
	 public static  String emailReg = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	 
	 public static String complexPwdReg = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%_]).{6,20})" ;
	 public static String simplePwdReg = "[0-9a-zA-Z_]{6,16}" ;
	 public static String webReg = "^http://([w-]+.)+[w-]+(/[w-./?%&=]*)?$" ;
	 public static String cerReg = "(\\d{15})|(\\d{14}[xX])|(\\d{18})|(\\d{17}[xX])" ;
	 public static String chReg = "[\u4e00-\u9fa5]{0,}" ;
	 public static String ipReg = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
	 public static String urlReg = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?" ;
	 public static String doubleCodeReg = "[^\\x00-\\xff]" ; //双字节;
	 public static String negetiveReg = "^-[1-9]\\d*$" ;
	 
	 /**
	 匹配特定数字：
	 ^[1-9]\\d*$ //匹配正整数
	 ^-[1-9]\\d*$ //匹配负整数
	 ^-?[1-9]\\d*$ //匹配整数
	 ^[1-9]\\d*|0$ //匹配非负整数（正整数 + 0）
	 ^-[1-9]\\d*|0$ //匹配非正整数（负整数 + 0）
	 ^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$ //匹配正浮点数
	 ^-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)$ //匹配负浮点数
	 ^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$ //匹配浮点数
	 ^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0$ //匹配非负浮点数（正浮点数 + 0）
	 ^(-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*))|0?\\.0+|0$ //匹配非正浮点数（负浮点数 + 0）
	 评注：处理大量数据时有用，具体应用时注意修正

	 匹配特定字符串：
	 ^[A-Za-z]+$ //匹配由26个英文字母组成的字符串
	 ^[A-Z]+$ //匹配由26个英文字母的大写组成的字符串
	 ^[a-z]+$ //匹配由26个英文字母的小写组成的字符串
	 ^[A-Za-z0-9]+$ //匹配由数字和26个英文字母组成的字符串
	 */
	 
	 /**
	  * 检查合法性
	  * @param subject
	  * @param reg
	  * @return
	  */
	 public static boolean checkLegal(String subject , String reg){
		 Pattern p =  Pattern.compile(reg);//复杂匹配  
		 Matcher m = p.matcher(subject);  
		 return m.matches();
	 }
	 
	 public static void main(String args[]){
		 System.out.println(checkLegal("" , mobileReg));
	 }

}
