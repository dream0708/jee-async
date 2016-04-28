package com.jee.async.common.response;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.annotation.JSONField;
import com.jee.async.common.enums.IBusinessResponse;
import com.jee.async.common.enums.ResponseCode;
import com.jee.async.common.exception.BusinessException;

public class PageResponse<T> extends RtnSuper implements Serializable{

	private static final Logger logger = LogManager.getLogger("async-exception") ;
    /**
	 * 
	 */
	private static final long serialVersionUID = 7091888109473242688L;
	 
    @JSONField(name="list" , ordinal = 7 )
	private List<T> list;
    /**
     * 当前页
     */
	@JSONField(name="totalPage" , ordinal = 6 )
    private int curPage = 1;

    /**
     * 每页几条
     */
    @JSONField(name="totalPage" , ordinal = 5 )
    private int numPerPage = 10;

    /**
     * 总页数
     */
    @JSONField(name="totalPage" , ordinal = 4 )
    private int totalPage = 0;

    /**
     * 记录条数
     */
    @JSONField(name="totalCount" , ordinal = 3 )
    private int totalCount = 0;

    /**
	 * 身份验证
	 */
	public PageResponse<T> hash(String hash){
		super.setHash(hash);
		return this ;
	}
	public PageResponse<T> token(String token){
		super.setToken(token);
		return this ;
	}
    
    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(int numPerPage) {
        this.numPerPage = numPerPage;
    }

    public int getTotalPage() {
    	if(numPerPage == 0)
    		return 0 ;
    	
    	return (int)totalCount/numPerPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    
    public PageResponse<T> totalPage(int totalPage){
    	this.totalPage = totalPage;
    	return this ;
    }

    public List<T> getList() {
        return list;
    }
    

    public void setList(List<T> list) {
        this.list = list;
    }

    public PageResponse<T> list(List<T> list){
    	this.list = list ;
    	return this ;
    }
    
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public PageResponse<T> totalCount(int totalCount) {
		this.totalCount = totalCount;
		return this ;
	}

	@SuppressWarnings("rawtypes")
	public static void pageResultWrapper(PageResponse pageResult , int rtnCode , String[] msg)
	{
		if(pageResult == null)
			pageResult = new PageResponse();
		
		pageResult.setRtnCode(rtnCode);
		pageResult.setRtnMsg(msg);
	}
	
	
	 /**
     * 成功
     * @return
     */
    
	public  PageResponse<T> success(){
    	this.setRtnCode(ResponseCode.SUCCESS_0.getCode());
    	this.setRtnMsg(new String[]{ResponseCode.SUCCESS_0.getMsg()});
    	return this ;
    }
    
	public  PageResponse<T> success(String msg){
		this.setRtnCode(ResponseCode.SUCCESS_0.getCode());
    	this.setRtnMsg(new String[]{msg});
    	return this ;
    }
	public  PageResponse<T> success(String[] msg){
		this.setRtnCode(ResponseCode.SUCCESS_0.getCode());
    	this.setRtnMsg(msg);
    	return this ;
    }
	
    /**
     * 失败
     * @return
     */
	public  PageResponse<T> failure(BusinessException ex){
		this.setRtnCode(ex.getErrorCode().getCode());
        this.setRtnMsg(new String[]{ex.getMessage()});
       	return this ;
    }
	public PageResponse<T> failure(IBusinessResponse code){
    	this.setRtnCode(code.getCode());
    	this.setRtnMsg(new String[]{ code.getMsg() });
    	return this ;
    }
	
	public PageResponse<T> failure(){
    	return failure(ResponseCode.FAILUER_1) ;
    }
	
	public  PageResponse<T> failure(String msg){
		this.setRtnCode(ResponseCode.FAILUER_1.getCode());
    	this.setRtnMsg(new String[]{ msg });
    	return this ;
    }
	public  PageResponse<T> failure(String[] msg){
		this.setRtnCode(ResponseCode.FAILUER_1.getCode());
    	this.setRtnMsg(msg);
    	return this ;
    }
   	public  PageResponse<T> failure(int code , String msg){
       	this.setRtnCode(code);
    	this.setRtnMsg(new String[]{msg});
       	return this ;
   }
   public  PageResponse<T> failure(int code , String[] msg){
       	this.setRtnCode(code);
    	this.setRtnMsg(msg);
       	return this ;
   }
   
   
   /**
	 * Async异常 
	 */
	public  PageResponse<T>  exception(Throwable ex , String hash , String... defaultMsg ){
		logger.error(ex.getMessage() , ex);
		Throwable cause = ex.getCause();
		logger.error(cause.getMessage() , cause);
		if (cause instanceof BusinessException) {
			BusinessException be = (BusinessException) cause;
			this.failure(be) ;
			this.setHash(hash);
		} else {
			if(defaultMsg == null || defaultMsg.length == 0){
				defaultMsg = new String[]{"系统异常::未知原因"} ;
			}
			this.failure(defaultMsg) ;
			this.setHash(hash);
		}
		return this ;
	}
	public  PageResponse<T> excpetionWithHash (Throwable ex , String hash , String ...defaultMsg ){
		return exception(ex , hash , defaultMsg ) ;
	}
	public  PageResponse<T> excpetionWithOutHash (Throwable ex , String ...defaultMsg ){
		return exception(ex , null , defaultMsg ) ;
	}
   
}
