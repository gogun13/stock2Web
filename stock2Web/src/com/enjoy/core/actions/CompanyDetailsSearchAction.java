package com.enjoy.core.actions;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.CompanyDetailsBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.business.CompanyDetailsBusiness;
import com.enjoy.core.main.Constants;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyDetailsSearchAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CompanyDetailsSearchAction.class);
	
	private CompanyDetailsBean 			companyDetailsBean;
	private ArrayList<ComboBean> 		statusCombo;
	private PaginatedListBean			paginatedList;
	private String						hidTin;
	
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	if (request.getSession(false) != null) {
            request.getSession(false).removeAttribute(Constants.SESSION_OBJ);
            request.getSession(false).removeAttribute(Constants.PAGE_INDEX);
        }
    	
    	companyDetailsBean = new CompanyDetailsBean();
    	setStatusCombo();
    	
    	setPageIndex("1");
    	searchCompanyDetails();
    	
		return SUCCESS;
	}
    
    private void setStatusCombo() throws Exception {
    	statusCombo = new ArrayList<ComboBean>();
    	
    	statusCombo.add(new ComboBean("", "ทุกสถานะ"));
    	for(ComboBean vo:companyDetailsBusiness.getCompanystatusCombo()){
    		statusCombo.add(new ComboBean(vo.getCode(), vo.getDesc()));
    	}
    	
    }
    
    public String searchCompanyDetails()throws Exception {
    	logger.info("[searchCompanyDetails][Begin]");
    	
    	try{
    		if (paginatedList == null) {
    			paginatedList = createPaginate(10);
            }
    		
    		paginatedList.setIndex(Integer.parseInt(getPageIndex()));
    		
    		companyDetailsBusiness.getListCompanyDetails(paginatedList, companyDetailsBean);
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[searchCompanyDetails][End]");
    	}
    	return SEARCH;
    }
    
    public String gotoDetail()throws Exception {
    	logger.info("[gotoDetail][Begin]");
    	
    	try{
    		request.getSession(false).setAttribute(Constants.SESSION_OBJ, companyDetailsBean);
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
    		
    		companyDetailsBean = (CompanyDetailsBean) request.getSession(false).getAttribute(Constants.SESSION_OBJ);
        	setStatusCombo();
        	
        	setPageIndex((String) request.getSession(false).getAttribute(Constants.PAGE_INDEX));
        	searchCompanyDetails();
    		
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
    		
    		if("companyDetailsBean.companyName".equals(getAutoCompleteName())){
    			list = companyDetailsBusiness.companyNameListForAutoComplete(getAutoCompParamter());
    		}else if("companyDetailsBean.tin".equals(getAutoCompleteName())){
    			list = companyDetailsBusiness.tinListForAutoComplete(getAutoCompParamter());
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
