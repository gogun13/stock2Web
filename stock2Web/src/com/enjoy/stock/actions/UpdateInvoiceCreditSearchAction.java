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
import com.enjoy.stock.bean.InvoiceCreditMasterBean;
import com.enjoy.stock.business.CustomerBusiness;
import com.enjoy.stock.business.InvoiceCreditBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateInvoiceCreditSearchAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UpdateInvoiceCreditSearchAction.class);
	
	private InvoiceCreditMasterBean 	invoiceCreditMasterBean;
	private ArrayList<ComboBean> 		priceTypeList;
	private PaginatedListBean			paginatedList;
	private String						hidInvoiceCode;
	
	@Autowired
	InvoiceCreditBusiness invoiceCreditBusiness;
	@Autowired
	CustomerBusiness customerBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	if (request.getSession(false) != null) {
            request.getSession(false).removeAttribute(Constants.SESSION_OBJ);
            request.getSession(false).removeAttribute(Constants.PAGE_INDEX);
        }
    	
    	invoiceCreditMasterBean = new InvoiceCreditMasterBean();
    	
    	invoiceCreditMasterBean.setTin(getCurrentUser().getTin());
    	
    	setStatusCombo();
    	
    	setPageIndex("1");
    	search();
    	
		return SUCCESS;
	}
    
    private void setStatusCombo() throws Exception {
		
		priceTypeList = new ArrayList<ComboBean>();
		priceTypeList.add(new ComboBean("","ทั้งหมด"));
		priceTypeList.add(new ComboBean("V","มี VAT"));
		priceTypeList.add(new ComboBean("N","ไม่มี VAT"));
    	
    }
    
    public String search()throws Exception {
    	logger.info("[search][Begin]");
    	
    	try{
    		if (paginatedList == null) {
    			paginatedList = createPaginate(10);
            }
    		
    		paginatedList.setIndex(Integer.parseInt(getPageIndex()));
    		
    		invoiceCreditBusiness.searchByCriteriaForCredit(paginatedList, invoiceCreditMasterBean);
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
    		request.getSession(false).setAttribute(Constants.SESSION_OBJ, invoiceCreditMasterBean);
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
    		
    		invoiceCreditMasterBean = (InvoiceCreditMasterBean) request.getSession(false).getAttribute(Constants.SESSION_OBJ);
        	setStatusCombo();
        	
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
    	logger.info("[autoComplete][Begin]");
    	
    	ArrayList<ComboBean> 	list 		= null;
    	JSONArray 				jSONArray 	= new JSONArray();
        JSONObject 				objDetail 	= new JSONObject();
    	
    	try{
    		logger.info("[autoComplete] autoCompleteName :: " + getAutoCompleteName());
    		logger.info("[autoComplete] autoCompParamter :: " + getAutoCompParamter());
    		
    		if("invoiceCreditMasterBean.cusFullName".equals(getAutoCompleteName())){
    			list = customerBusiness.getCusFullName(getAutoCompParamter(), getCurrentUser().getTin());
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
