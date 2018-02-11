package com.enjoy.stock.actions;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.actions.ActionControl;
import com.enjoy.core.actions.BaseAction;
import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.main.Constants;
import com.enjoy.stock.bean.CompanyVendorBean;
import com.enjoy.stock.business.CompanyVendorBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyVendorSearchAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CompanyVendorSearchAction.class);
	
	private CompanyVendorBean 		companyVendorBean;
	private ArrayList<ComboBean> 	statusCombo;
	private PaginatedListBean		paginatedList;
	private String					hidVendorCode;
	
	@Autowired
	CompanyVendorBusiness companyVendorBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	if (request.getSession(false) != null) {
            request.getSession(false).removeAttribute(Constants.SESSION_OBJ);
            request.getSession(false).removeAttribute(Constants.PAGE_INDEX);
        }
    	
    	companyVendorBean = new CompanyVendorBean();
    	
    	companyVendorBean.setTinCompany(getCurrentUser().getTin());
    	
    	setPageIndex("1");
    	search();
    	
		return SUCCESS;
	}
    
    public String search()throws Exception {
    	logger.info("[search][Begin]");
    	
    	try{
    		if (paginatedList == null) {
    			paginatedList = createPaginate(10);
            }
    		
    		paginatedList.setIndex(Integer.parseInt(getPageIndex()));
    		
    		companyVendorBusiness.searchByCriteria(paginatedList, companyVendorBean);
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[search][End]");
    	}
    	return SEARCH;
    }
    
    public String gotoDetail()throws Exception {
    	logger.info("[gotoDetail][Begin]");
    	
    	try{
    		request.getSession(false).setAttribute(Constants.SESSION_OBJ, companyVendorBean);
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
    		
    		companyVendorBean = (CompanyVendorBean) request.getSession(false).getAttribute(Constants.SESSION_OBJ);
        	
        	setPageIndex((String) request.getSession(false).getAttribute(Constants.PAGE_INDEX));
        	search();
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[onBack][End]");
    	}
    	return SUCCESS;
    }
    
    @Override
    public String autoComplete()throws Exception {
    	return null;
    }
    
    
}
