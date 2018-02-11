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
import com.enjoy.core.bean.CompanyDetailsBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.business.CompanyDetailsBusiness;
import com.enjoy.core.business.UserdetailsBusiness;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.SummarySaleByEmployeeReportBean;
import com.enjoy.stock.business.SummarySaleReportBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class SummarySaleByEmployeeReportAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SummarySaleByEmployeeReportAction.class);
	
	private SummarySaleByEmployeeReportBean summarySaleByEmployeeReportBean;
	private String							userUniqueId;
	
	@Autowired
	SummarySaleReportBusiness summarySaleReportBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	@Autowired
	UserdetailsBusiness userdetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	summarySaleByEmployeeReportBean = new SummarySaleByEmployeeReportBean();
    	userUniqueId					= "";
    	
		return SUCCESS;
	}
    
    public String showData() throws Exception{
		logger.info("[showData][Begin]");
		
		JSONObject 									jsonObject 			= new JSONObject();
		JSONArray 									jSONArray 			= new JSONArray();
		JSONObject 									objDetail 			= null;
		String										tin 				= null;
		CompanyDetailsBean 							companyDetailsDb 	= null;
		ArrayList<SummarySaleByEmployeeReportBean> 	resultList 			= null;
		
		try{
			tin = getCurrentUser().getTin();
			
			logger.info("[showData] invoiceDateFrom :: " + summarySaleByEmployeeReportBean.getInvoiceDateFrom());
			logger.info("[showData] invoiceDateTo 	:: " + summarySaleByEmployeeReportBean.getInvoiceDateTo());
			logger.info("[showData] saleName 		:: " + summarySaleByEmployeeReportBean.getSaleName());
			
			jsonObject.put("tin"			,tin);
			jsonObject.put("invoiceDateFrom",summarySaleByEmployeeReportBean.getInvoiceDateFrom());
			jsonObject.put("invoiceDateTo"	,summarySaleByEmployeeReportBean.getInvoiceDateTo());
			jsonObject.put("saleName"		,summarySaleByEmployeeReportBean.getSaleName());
			
			/*Begin รายละเอียดบริษัท*/
			companyDetailsDb = companyDetailsBusiness.getCompanyDetails(tin);
			objDetail = new JSONObject();
			objDetail.put("tin"			, companyDetailsDb.getTin());
			objDetail.put("companyName"	, companyDetailsDb.getCompanyName());
			objDetail.put("address"		, companyDetailsDb.getAddress());
			objDetail.put("tel"			, companyDetailsDb.getTel());
			objDetail.put("fax"			, companyDetailsDb.getFax());
			objDetail.put("email"		, companyDetailsDb.getEmail());
			objDetail.put("remark"		, companyDetailsDb.getRemark());
			
			jsonObject.put("companyDetails"	,objDetail);
			/*End รายละเอียดบริษัท*/
			
			/*Begin รายละเอียดรายงาน*/
			summarySaleByEmployeeReportBean.setTin(tin);
			
			resultList = summarySaleReportBusiness.searchByEmployee(summarySaleByEmployeeReportBean);
			
			if(resultList!=null){
				for(SummarySaleByEmployeeReportBean vo:resultList){
					objDetail = new JSONObject();
					
					objDetail.put("invoiceCode"		,vo.getInvoiceCode());
					objDetail.put("invoiceDate"		,vo.getInvoiceDate());
					objDetail.put("cusName"			,vo.getCusName());
					objDetail.put("invoiceTotal"	,vo.getInvoiceTotal());
					objDetail.put("saleCommission"	,vo.getSaleCommission());
					
					jSONArray.add(objDetail);
				}
			}
			
			jsonObject.put("resultList"	,jSONArray);
			/*End รายละเอียดรายงาน*/
			
			writePDF("SummarySaleByEmployeePdfForm", jsonObject, "รายงานยอดขายของพนักงาน");
			
		}catch(Exception e){
			logger.error("genPdf :: ", e);
			throw e;
		}finally{
			logger.info("[genPdf][End]");
		}
		
		return null;
	}
    
    public String openUserLoogUp()throws Exception {
    	
    	return "openUserLoogUp";
    }
    
    public String getSaleNameDetailByCode() throws Exception{
 	   logger.info("[getSaleNameDetailByCode][Begin]");
 	   
 	   JSONObject 		obj		 			= new JSONObject();
 	   UserDetailsBean 	userDetailsBeanDb	= null;
 	   String			userFullName		= "";
 	   
 	   try{
 		   
 		   userDetailsBeanDb = userdetailsBusiness.getUserdetail(EnjoyUtils.parseInt(userUniqueId));
 		   
 		   if(userDetailsBeanDb!=null){
 			   userFullName = userDetailsBeanDb.getUserFullName();
 		   }
 		   
 		   obj.put("userFullName"	,userFullName);
 		   
 		   writeMSG(obj.toString());
 		   
 	   }catch(Exception e){
 		   throw e;
 	   }finally{
 		   logger.info("[getSaleNameDetailByCode][End]");
 	   }
 	   return null;
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
    		
    		if("summarySaleByEmployeeReportBean.saleName".equals(getAutoCompleteName())){
    			list = userdetailsBusiness.userFullNameList(getAutoCompParamter(), getCurrentUser().getTin(),getCurrentUser().getUserUniqueId());
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
