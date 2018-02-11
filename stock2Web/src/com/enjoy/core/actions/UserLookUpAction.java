package com.enjoy.core.actions;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.business.UserdetailsBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserLookUpAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserLookUpAction.class);
	
	private UserDetailsBean 			beanForLookUp;
	private PaginatedListBean			paginatedListForLookUp;
	
	@Autowired
	UserdetailsBusiness userdetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	beanForLookUp = new UserDetailsBean();
    	
    	setPageIndex("1");
    	searchUserDetail();
    	
		return SUCCESS;
	}
    
    public String searchUserDetail()throws Exception {
    	logger.info("[searchUserDetail][Begin]");
    	
    	try{
    		if (paginatedListForLookUp == null) {
    			paginatedListForLookUp = createPaginate(10);
            }
    		
    		paginatedListForLookUp.setIndex(Integer.parseInt(getPageIndex()));
    		
    		beanForLookUp.setTin(getCurrentUser().getTin());
    		beanForLookUp.setUserUniqueId(getCurrentUser().getUserUniqueId());
    		
    		userdetailsBusiness.getListUserdetailForLookUp(paginatedListForLookUp, beanForLookUp);
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[searchUserDetail][End]");
    	}
    	return SEARCH;
    }
    
    @Override
    public String autoComplete()throws Exception {
    	logger.info("[autoComplete][Begin]");
    	
    	ArrayList<ComboBean> 	list 		= null;
    	JSONArray 				jSONArray 	= new JSONArray();
        JSONObject 				objDetail 	= new JSONObject();
    	
    	try{
    		logger.info("[autoComplete] autoCompleteName :: " + getAutoCompleteName());
    		logger.info("[autoComplete] autoCompParamter :: " + getAutoCompParamter());
    		
    		if("beanForLookUp.userName".equals(getAutoCompleteName())){
    			list = userdetailsBusiness.userFullNameList(getAutoCompParamter(), getCurrentUser().getTin(), getCurrentUser().getUserUniqueId());
    		}
    		
    		if(list!=null && !list.isEmpty()){
	    		for(ComboBean bean:list){
	 			   objDetail 		= new JSONObject();
	 			   
	 			   objDetail.put("id"			,bean.getCode());
	 			   objDetail.put("value"		,bean.getDesc());
	 			   
	 			   jSONArray.add(objDetail);
	 		   	}
    		}
    		
    		writeMSG(jSONArray.toString());
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[autoComplete][End]");
    	}
    	return null;
    }
    
    
}
