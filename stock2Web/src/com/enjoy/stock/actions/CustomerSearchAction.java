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
import com.enjoy.core.main.Constants;
import com.enjoy.stock.bean.CustomerDetailsBean;
import com.enjoy.stock.business.CustomerBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerSearchAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CustomerSearchAction.class);
	
	private CustomerDetailsBean 		customerDetailsBean;
	private ArrayList<ComboBean> 		statusCombo;
	private PaginatedListBean			paginatedList;
	private String						hidCusCode;
	
	@Autowired
	CustomerBusiness customerBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	if (request.getSession(false) != null) {
            request.getSession(false).removeAttribute(Constants.SESSION_OBJ);
            request.getSession(false).removeAttribute(Constants.PAGE_INDEX);
        }
    	
    	customerDetailsBean = new CustomerDetailsBean();
    	
    	customerDetailsBean.setTin(getCurrentUser().getTin());
    	
    	setStatusCombo();
    	
    	setPageIndex("1");
    	searchCustomerDetails();
    	
		return SUCCESS;
	}
    
    private void setStatusCombo() throws Exception {
    	statusCombo = new ArrayList<ComboBean>();
    	
    	statusCombo.add(new ComboBean("", "ทุกสถานะ"));
    	for(ComboBean vo:customerBusiness.getStatusCombo()){
    		statusCombo.add(new ComboBean(vo.getCode(), vo.getDesc()));
    	}
    	
    }
    
    public String searchCustomerDetails()throws Exception {
    	logger.info("[searchCustomerDetails][Begin]");
    	
    	try{
    		if (paginatedList == null) {
    			paginatedList = createPaginate(10);
            }
    		
    		paginatedList.setIndex(Integer.parseInt(getPageIndex()));
    		
    		customerBusiness.getListCustomerDetails(paginatedList, customerDetailsBean);
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[searchCustomerDetails][End]");
    	}
    	return SEARCH;
    }
    
    public String gotoDetail()throws Exception {
    	logger.info("[gotoDetail][Begin]");
    	
    	try{
    		request.getSession(false).setAttribute(Constants.SESSION_OBJ, customerDetailsBean);
    		request.getSession(false).setAttribute(Constants.PAGE_INDEX, getPageIndex());
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[gotoDetail][End]");
    	}
    	return "gotoDetail";
    }
    
    public String onBack() throws Exception{
    	logger.info("[onBack][Begin]");
    	
    	try{
    		
    		customerDetailsBean = (CustomerDetailsBean) request.getSession(false).getAttribute(Constants.SESSION_OBJ);
        	setStatusCombo();
        	
        	setPageIndex((String) request.getSession(false).getAttribute(Constants.PAGE_INDEX));
        	searchCustomerDetails();
    		
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
    		
    		if("customerDetailsBean.fullName".equals(getAutoCompleteName())){
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
