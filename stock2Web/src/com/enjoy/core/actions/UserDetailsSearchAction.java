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
import com.enjoy.core.main.Constants;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDetailsSearchAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserDetailsSearchAction.class);
	
	private UserDetailsBean 			userDetailsBean;
	private ArrayList<ComboBean> 		statusCombo;
	private PaginatedListBean			paginatedList;
	private String						userUniqueId;
	
	@Autowired
	UserdetailsBusiness userdetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	if (request.getSession(false) != null) {
            request.getSession(false).removeAttribute(Constants.SESSION_OBJ);
            request.getSession(false).removeAttribute(Constants.PAGE_INDEX);
        }
    	
    	userDetailsBean = new UserDetailsBean();
    	setStatusCombo();
    	
    	setPageIndex("1");
    	searchUserDetail();
    	
		return SUCCESS;
	}
    
    private void setStatusCombo() throws Exception {
    	statusCombo = new ArrayList<ComboBean>();
    	
    	statusCombo.add(new ComboBean("", "ทุกสถานะ"));
    	for(ComboBean vo:userdetailsBusiness.getRefuserstatusCombo()){
    		statusCombo.add(new ComboBean(vo.getCode(), vo.getDesc()));
    	}
    	
    }
    
    public String searchUserDetail()throws Exception {
    	logger.info("[searchUserDetail][Begin]");
    	
    	try{
    		if (paginatedList == null) {
    			paginatedList = createPaginate(10);
            }
    		
    		paginatedList.setIndex(Integer.parseInt(getPageIndex()));
    		
    		userDetailsBean.setTin(getCurrentUser().getTin());
    		userDetailsBean.setUserUniqueId(getCurrentUser().getUserUniqueId());
    		
    		userdetailsBusiness.getListUserdetail(paginatedList, userDetailsBean);
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[searchUserDetail][End]");
    	}
    	return SEARCH;
    }
    
    public String gotoUserDetail()throws Exception {
    	logger.info("[gotoUserDetail][Begin]");
    	
    	try{
    		request.getSession(false).setAttribute(Constants.SESSION_OBJ, userDetailsBean);
    		request.getSession(false).setAttribute(Constants.PAGE_INDEX, getPageIndex());
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[gotoUserDetail][End]");
    	}
    	return "gotoUserDetail";
    }
    
    public String onBack() throws Exception{
    	logger.info("[onBack][Begin]");
    	
    	try{
    		
    		userDetailsBean = (UserDetailsBean) request.getSession(false).getAttribute(Constants.SESSION_OBJ);
        	setStatusCombo();
        	
        	setPageIndex((String) request.getSession(false).getAttribute(Constants.PAGE_INDEX));
        	searchUserDetail();
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[onBack][End]");
    	}
    	return SUCCESS;
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
    		
    		if("userDetailsBean.userName".equals(getAutoCompleteName())){
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
