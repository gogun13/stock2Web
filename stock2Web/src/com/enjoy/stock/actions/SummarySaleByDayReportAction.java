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
import com.enjoy.core.bean.CompanyDetailsBean;
import com.enjoy.core.business.CompanyDetailsBusiness;
import com.enjoy.stock.bean.SummarySaleByDayReportBean;
import com.enjoy.stock.business.SummarySaleReportBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class SummarySaleByDayReportAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SummarySaleByDayReportAction.class);
	
	private SummarySaleByDayReportBean 	summarySaleByDayReportBean;
	
	@Autowired
	SummarySaleReportBusiness summarySaleReportBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	summarySaleByDayReportBean 	= new SummarySaleByDayReportBean();
    	
		return SUCCESS;
	}
    
    public String showData() throws Exception{
		logger.info("[showData][Begin]");
		
		JSONObject 								jsonObject 			= new JSONObject();
		JSONArray 								jSONArray 			= new JSONArray();
		JSONObject 								objDetail 			= null;
		String									tin 				= null;
		CompanyDetailsBean 						companyDetailsDb 	= null;
		ArrayList<SummarySaleByDayReportBean> 	resultList 			= null;
		
		try{
			tin = getCurrentUser().getTin();
			
			logger.info("[showData] invoiceDateFrom :: " + summarySaleByDayReportBean.getInvoiceDateFrom());
			logger.info("[showData] invoiceDateTo 	:: " + summarySaleByDayReportBean.getInvoiceDateTo());
			
			jsonObject.put("tin"				,tin);
			jsonObject.put("invoiceDateFrom"	,summarySaleByDayReportBean.getInvoiceDateFrom());
			jsonObject.put("invoiceDateTo"		,summarySaleByDayReportBean.getInvoiceDateTo());
			
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
			summarySaleByDayReportBean.setTin(tin);
			
			resultList = summarySaleReportBusiness.searchByDay(summarySaleByDayReportBean);
			
			if(resultList!=null){
				for(SummarySaleByDayReportBean vo:resultList){
					objDetail = new JSONObject();
					
					objDetail.put("invoiceCode"		,vo.getInvoiceCode());
					objDetail.put("cusName"			,vo.getCusName());
					objDetail.put("invoiceDate"		,vo.getInvoiceDate());
					objDetail.put("invoiceTotal"	,vo.getInvoiceTotal());
					objDetail.put("remark"			,vo.getRemark());
					
					jSONArray.add(objDetail);
				}
			}
			
			jsonObject.put("resultList"	,jSONArray);
			/*End รายละเอียดรายงาน*/
			
			writePDF("SummarySaleByDayPdfForm", jsonObject, "รายงานสรุปยอดขายประจำวัน");
			
		}catch(Exception e){
			logger.error("genPdf :: ", e);
			throw e;
		}finally{
			logger.info("[genPdf][End]");
		}
		
		return null;
	}
    
    @Override
    public String autoComplete()throws Exception {
    	return null;
    }
    
    
}
