package com.jee.async.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {

	 //http://blog.chinaunix.net/uid-20577907-id-1758770.html
	
	 public static final String allReg = "[\\w\\W]*" ;
	 public static final String mobileReg = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$" ;
	 public static final String emailReg = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	 
	 public static final String complexPwdReg = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%_]).{6,20})" ;
	 public static final String simplePwdReg = "[0-9a-zA-Z_]{6,16}" ;
	 public static final String webReg = "^http://([w-]+.)+[w-]+(/[w-./?%&=]*)?$" ;
	 public static final String cerReg = "(\\d{15})|(\\d{14}[xX])|(\\d{18})|(\\d{17}[xX])" ;
	 public static final String chReg = "[\u4e00-\u9fa5]{0,}" ;
	 public static final String ipReg = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";
	 public static final String urlReg = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?" ;
	 public static final String doubleCodeReg = "[^\\x00-\\xff]" ; //双字节;
	 public static final String negetiveReg = "^-[1-9]\\d*$" ;
	 
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
		 System.out.println(checkLegal("和26个英文字母组成的"
		 		+ "  字符串" , allReg));
	 }

}
