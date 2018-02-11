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
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.SummarySaleByMonthReportBean;
import com.enjoy.stock.business.SummarySaleReportBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class SummarySaleByMonthReportAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SummarySaleByMonthReportAction.class);
	
	private SummarySaleByMonthReportBean 	summarySaleByMonthReportBean;
	
	@Autowired
	SummarySaleReportBusiness summarySaleReportBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	summarySaleByMonthReportBean 	= new SummarySaleByMonthReportBean();
    	
		return SUCCESS;
	}
    
    public String showData() throws Exception{
		logger.info("[showData][Begin]");
		
		JSONObject 								jsonObject 			= new JSONObject();
		JSONArray 								jSONArray 			= new JSONArray();
		JSONObject 								objDetail 			= null;
		String									tin 				= null;
		CompanyDetailsBean 						companyDetailsDb 	= null;
		ArrayList<SummarySaleByMonthReportBean> resultList 			= null;
		String									invoiceDateFrom		= "";
	    String									invoiceDateTo		= "";
		
		try{
			tin = getCurrentUser().getTin();
			
			logger.info("[showData] invoiceMonth :: " + summarySaleByMonthReportBean.getInvoiceMonth());
			
			invoiceDateFrom = "01/" + summarySaleByMonthReportBean.getInvoiceMonth();
			invoiceDateTo 	= EnjoyUtils.getLastDateOfMonth(invoiceDateFrom);
			
			logger.info("[showData] invoiceDateFrom :: " + invoiceDateFrom);
			logger.info("[showData] invoiceDateTo 	:: " + invoiceDateTo);
			
			jsonObject.put("tin"				,tin);
			jsonObject.put("invoiceDateFrom"	,invoiceDateFrom);
			jsonObject.put("invoiceDateTo"		,invoiceDateTo);
			
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
			summarySaleByMonthReportBean.setTin(tin);
			summarySaleByMonthReportBean.setInvoiceDateFrom(invoiceDateFrom);
			summarySaleByMonthReportBean.setInvoiceDateTo(invoiceDateTo);
			
			resultList = summarySaleReportBusiness.searchByMonth(summarySaleByMonthReportBean);
			
			if(resultList!=null){
				for(SummarySaleByMonthReportBean vo:resultList){
					objDetail = new JSONObject();
					
					objDetail.put("cusName"		,vo.getCusName());
					objDetail.put("productName"	,vo.getProductName());
					objDetail.put("quantity"	,vo.getQuantity());
					objDetail.put("price"		,vo.getPrice());
					
					jSONArray.add(objDetail);
				}
			}
			
			jsonObject.put("resultList"	,jSONArray);
			/*End รายละเอียดรายงาน*/
			
			writePDF("SummarySaleByMonthPdfForm", jsonObject, "รายงานสรุปยอดขายประจำเดือน");
			
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
