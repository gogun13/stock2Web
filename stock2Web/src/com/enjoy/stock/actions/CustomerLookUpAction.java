package com.enjoy.stock.actions;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.actions.ActionControl;
import com.enjoy.core.actions.BaseAction;
import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.stock.bean.CustomerDetailsBean;
import com.enjoy.stock.business.CustomerBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerLookUpAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CustomerLookUpAction.class);
	
	private CustomerDetailsBean 		beanForLookUp;
	private PaginatedListBean			paginatedListForLookUp;
	
	@Autowired
	CustomerBusiness customerBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	beanForLookUp = new CustomerDetailsBean();
    	
    	setPageIndex("1");
    	search();
    	
		return SUCCESS;
	}
    
    public String search()throws Exception {
    	logger.info("[search][Begin]");
    	
    	try{
    		if (paginatedListForLookUp == null) {
    			paginatedListForLookUp = createPaginate(10);
            }
    		
    		paginatedListForLookUp.setIndex(Integer.parseInt(getPageIndex()));
    		
    		beanForLookUp.setTin(getCurrentUser().getTin());
    		beanForLookUp.setCusStatus("A");
    		
    		customerBusiness.getListCustomerDetails(paginatedListForLookUp, beanForLookUp);
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[search][End]");
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
    		
    		if("beanForLookUp.fullName".equals(getAutoCompleteName())){
    			list = customerBusiness.getCusFullName(getAutoCompParamter(),getCurrentUser().getTin());
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
