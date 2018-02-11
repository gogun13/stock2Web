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
import com.enjoy.core.business.CompanyDetailsBusiness;
import com.enjoy.stock.bean.CustomerDetailsBean;
import com.enjoy.stock.bean.SummarySaleByCustomerReportBean;
import com.enjoy.stock.business.CustomerBusiness;
import com.enjoy.stock.business.ProductBusiness;
import com.enjoy.stock.business.SummarySaleReportBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class SummarySaleByCustomerReportAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SummarySaleByCustomerReportAction.class);
	
	private SummarySaleByCustomerReportBean summarySaleByCustomerReportBean;
	
	@Autowired
	SummarySaleReportBusiness summarySaleReportBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	@Autowired
	ProductBusiness productBusiness;
	@Autowired
	CustomerBusiness customerBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	summarySaleByCustomerReportBean 	= new SummarySaleByCustomerReportBean();
    	
		return SUCCESS;
	}
    
    public String showData() throws Exception{
		logger.info("[showData][Begin]");
		
		JSONObject 									jsonObject 			= new JSONObject();
		JSONArray 									jSONArray 			= new JSONArray();
		JSONObject 									objDetail 			= null;
		String										tin 				= null;
		CompanyDetailsBean 							companyDetailsDb 	= null;
		ArrayList<SummarySaleByCustomerReportBean> 	resultList 			= null;
		CustomerDetailsBean 						customerDetailsDb	= null;
		
		try{
			tin = getCurrentUser().getTin();
			
			logger.info("[showData] invoiceDateFrom :: " + summarySaleByCustomerReportBean.getInvoiceDateFrom());
			logger.info("[showData] invoiceDateTo 	:: " + summarySaleByCustomerReportBean.getInvoiceDateTo());
			logger.info("[showData] productName 	:: " + summarySaleByCustomerReportBean.getProductName());
			logger.info("[showData] cusCode 		:: " + summarySaleByCustomerReportBean.getCusCode());
			
			jsonObject.put("cusCode"		,summarySaleByCustomerReportBean.getCusCode());
			jsonObject.put("invoiceDateFrom",summarySaleByCustomerReportBean.getInvoiceDateFrom());
			jsonObject.put("invoiceDateTo"	,summarySaleByCustomerReportBean.getInvoiceDateTo());
			jsonObject.put("productName"	,summarySaleByCustomerReportBean.getProductName());
			jsonObject.put("tin"			,tin);
			
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
			
			/*Begin รายละเอียดลูกค้า*/
			customerDetailsDb = customerBusiness.getCustomerDetail(summarySaleByCustomerReportBean.getCusCode(), tin);
			objDetail = new JSONObject();
			objDetail.put("cusCode"		, customerDetailsDb.getCusCode());
			objDetail.put("fullName"	, customerDetailsDb.getFullName());
			objDetail.put("address"		, customerDetailsDb.getAddress());
			objDetail.put("tel"			, customerDetailsDb.getTel());
			objDetail.put("fax"			, customerDetailsDb.getFax());
			objDetail.put("email"		, customerDetailsDb.getEmail());
			objDetail.put("remark"		, customerDetailsDb.getRemark());
			
			jsonObject.put("customerDetails"	,objDetail);
			/*ENd รายละเอียดลูกค้า*/
			
			/*Begin รายละเอียดรายงาน*/
			summarySaleByCustomerReportBean.setTin(tin);
			
			resultList = summarySaleReportBusiness.searchByCustomer(summarySaleByCustomerReportBean);
			
			if(resultList!=null){
				for(SummarySaleByCustomerReportBean vo:resultList){
					objDetail 	= new JSONObject();
					
					objDetail.put("productName"	,vo.getProductName());
					objDetail.put("invoiceDate"	,vo.getInvoiceDate());
					objDetail.put("quantity"	,vo.getQuantity());
					objDetail.put("price"		,vo.getPrice());
					objDetail.put("discount"	,vo.getDiscount());
					
					jSONArray.add(objDetail);
				}
			}
			
			jsonObject.put("resultList"	,jSONArray);
			/*End รายละเอียดรายงาน*/
			
			writePDF("SummarySaleByCustomerPdfForm", jsonObject, "รายงานประวัติการซื้อของลูกค้า");
			
		}catch(Exception e){
			logger.error("genPdf :: ", e);
			throw e;
		}finally{
			logger.info("[genPdf][End]");
		}
		
		return null;
	}
    
    public String openCustomerLookUp()throws Exception {
    	
    	return "openCustomerLookUp";
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
    		
    		if("summarySaleByCustomerReportBean.productName".equals(getAutoCompleteName())){
    			list = productBusiness.productNameList(getAutoCompParamter()
						, ""
						, ""
						, getCurrentUser().getTin()
						, true);
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
