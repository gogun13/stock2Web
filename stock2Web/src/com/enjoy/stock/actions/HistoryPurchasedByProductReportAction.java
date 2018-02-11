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
import com.enjoy.stock.bean.HistoryPurchasedByProductReportBean;
import com.enjoy.stock.business.HistoryPurchasedReportBusiness;
import com.enjoy.stock.business.ProductBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class HistoryPurchasedByProductReportAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HistoryPurchasedByProductReportAction.class);
	
	private HistoryPurchasedByProductReportBean 	historyPurchasedByProductReportBean;
	
	@Autowired
	HistoryPurchasedReportBusiness historyPurchasedReportBusiness;
	@Autowired
	ProductBusiness productBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	historyPurchasedByProductReportBean 	= new HistoryPurchasedByProductReportBean();
    	
		return SUCCESS;
	}
    
    public String showData() throws Exception{
		logger.info("[showData][Begin]");
		
		JSONObject 										jsonObject 			= new JSONObject();
		JSONArray 										jSONArray 			= new JSONArray();
		JSONObject 										objDetail 			= null;
		String											tin 				= null;
		CompanyDetailsBean 								companyDetailsDb 	= null;
		ArrayList<HistoryPurchasedByProductReportBean> 	resultList 			= null;
		
		try{
			tin = getCurrentUser().getTin();
			
			logger.info("[showData] productName  	:: " + historyPurchasedByProductReportBean.getProductName());
			logger.info("[showData] reciveDateFrom 	:: " + historyPurchasedByProductReportBean.getReciveDateFrom());
			logger.info("[showData] reciveDateTo 	:: " + historyPurchasedByProductReportBean.getReciveDateTo());
			
			jsonObject.put("tin"			,tin);
			jsonObject.put("reciveDateFrom"	,historyPurchasedByProductReportBean.getReciveDateFrom());
			jsonObject.put("reciveDateTo"	,historyPurchasedByProductReportBean.getReciveDateTo());
			jsonObject.put("productName"	,historyPurchasedByProductReportBean.getProductName());
			
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
			historyPurchasedByProductReportBean.setTin(tin);
			
			resultList = historyPurchasedReportBusiness.searchByProduct(historyPurchasedByProductReportBean);
			
			if(resultList!=null){
				for(HistoryPurchasedByProductReportBean vo:resultList){
					objDetail = new JSONObject();
					
					objDetail.put("productName"		,vo.getProductName());
					objDetail.put("vendorName"		,vo.getVendorName());
					objDetail.put("reciveNo"		,vo.getReciveNo());
					objDetail.put("reciveDate"		,vo.getReciveDate());
					objDetail.put("costPrice"		,vo.getCostPrice());
					objDetail.put("discountRate"	,vo.getDiscountRate());
					
					jSONArray.add(objDetail);
				}
			}
			
			jsonObject.put("resultList"	,jSONArray);
			/*End รายละเอียดรายงาน*/
			
			writePDF("HistoryPurchasedByProductPdfForm", jsonObject, "รายงานประวัติการซื้อตามสินค้า");
			
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
    		
    		if("historyPurchasedByProductReportBean.productName".equals(getAutoCompleteName())){
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
