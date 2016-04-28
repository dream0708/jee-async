package com.jee.async.common.enums;
/**
 * 接口入参类型枚举
 * @author yaomengke
 *
 */
public enum ParamType {
	Int("int" , "整型") ,
	Long("Long" , "长整型") ,
	Boolean("Boolean" , "布尔类型") ,
	Ignore("ignore" , "忽略,默认") ,
	List("List" , "列表类型") ,
	Array("Array" , "数组类型") ,
	Float("Float" , "长浮点型") ,
	String("String" , "字符串类型") ,
	Double("Double" , "长浮点型") ,
	Object("Object" , "实体类") ,
	Request("Request" , "Http请求对象") ,
	Response("Response" , "Http相应对象") ;
	
	String code ;
	String desc ;
	ParamType(){
		
	}
	private ParamType(String code , String desc){
		this.code = code ;
		this.desc = desc ;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}

}
