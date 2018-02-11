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
import com.enjoy.stock.bean.SummarySaleByProductReportBean;
import com.enjoy.stock.business.ProductBusiness;
import com.enjoy.stock.business.SummarySaleReportBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class SummarySaleByProductReportAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SummarySaleByProductReportAction.class);
	
	private SummarySaleByProductReportBean 	summarySaleByProductReportBean;
	
	@Autowired
	SummarySaleReportBusiness summarySaleReportBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	@Autowired
	ProductBusiness productBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	summarySaleByProductReportBean 	= new SummarySaleByProductReportBean();
    	
		return SUCCESS;
	}
    
    public String showData() throws Exception{
		logger.info("[showData][Begin]");
		
		JSONObject 								jsonObject 			= new JSONObject();
		JSONArray 								jSONArray 			= new JSONArray();
		JSONObject 								objDetail 			= null;
		String									tin 				= null;
		CompanyDetailsBean 						companyDetailsDb 	= null;
		ArrayList<SummarySaleByProductReportBean> 	resultList 			= null;
		
		try{
			tin = getCurrentUser().getTin();
			
			logger.info("[showData] invoiceDateFrom :: " + summarySaleByProductReportBean.getInvoiceDateFrom());
			logger.info("[showData] invoiceDateTo 	:: " + summarySaleByProductReportBean.getInvoiceDateTo());
			logger.info("[showData] productName 	:: " + summarySaleByProductReportBean.getProductName());
			
			jsonObject.put("tin"			,tin);
			jsonObject.put("invoiceDateFrom",summarySaleByProductReportBean.getInvoiceDateFrom());
			jsonObject.put("invoiceDateTo"	,summarySaleByProductReportBean.getInvoiceDateTo());
			jsonObject.put("productName"	,summarySaleByProductReportBean.getProductName());
			
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
			summarySaleByProductReportBean.setTin(tin);
			
			resultList = summarySaleReportBusiness.searchByProduct(summarySaleByProductReportBean);
			
			if(resultList!=null){
				for(SummarySaleByProductReportBean vo:resultList){
					objDetail = new JSONObject();
					
					objDetail.put("invoiceDate"	,vo.getInvoiceDate());
					objDetail.put("cusName"		,vo.getCusName());
					objDetail.put("productName"	,vo.getProductName());
					objDetail.put("quantity"	,vo.getQuantity());
					objDetail.put("price"		,vo.getPrice());
					
					jSONArray.add(objDetail);
				}
			}
			
			jsonObject.put("resultList"	,jSONArray);
			/*End รายละเอียดรายงาน*/
			
			writePDF("SummarySaleByProductPdfForm", jsonObject, "รายงานสรุปยอดขายตามกลุ่มสินค้า");
			
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
    	logger.info("[autoComplete][Begin]");
    	
    	ArrayList<ComboBean> 	list 		= null;
    	JSONArray 				jSONArray 	= new JSONArray();
        JSONObject 				objDetail 	= new JSONObject();
    	
    	try{
    		logger.info("[autoComplete] autoCompleteName :: " + getAutoCompleteName());
    		logger.info("[autoComplete] autoCompParamter :: " + getAutoCompParamter());
    		
    		if("summarySaleByProductReportBean.productName".equals(getAutoCompleteName())){
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
