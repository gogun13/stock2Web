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
import com.enjoy.stock.bean.HistoryPurchasedByDealerReportBean;
import com.enjoy.stock.business.CompanyVendorBusiness;
import com.enjoy.stock.business.HistoryPurchasedReportBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class HistoryPurchasedByDealerReportAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HistoryPurchasedByDealerReportAction.class);
	
	private HistoryPurchasedByDealerReportBean 	historyPurchasedByDealerReportBean;
	
	@Autowired
	HistoryPurchasedReportBusiness historyPurchasedReportBusiness;
	@Autowired
	CompanyVendorBusiness companyVendorBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	historyPurchasedByDealerReportBean 	= new HistoryPurchasedByDealerReportBean();
    	
		return SUCCESS;
	}
    
    public String showData() throws Exception{
		logger.info("[showData][Begin]");
		
		JSONObject 										jsonObject 			= new JSONObject();
		JSONArray 										jSONArray 			= new JSONArray();
		JSONObject 										objDetail 			= null;
		String											tin 				= null;
		CompanyDetailsBean 								companyDetailsDb 	= null;
		ArrayList<HistoryPurchasedByDealerReportBean> 	resultList 			= null;
		
		try{
			tin = getCurrentUser().getTin();
			
			logger.info("[showData] vendorName  	:: " + historyPurchasedByDealerReportBean.getVendorName());
			logger.info("[showData] reciveDateFrom 	:: " + historyPurchasedByDealerReportBean.getReciveDateFrom());
			logger.info("[showData] reciveDateTo 	:: " + historyPurchasedByDealerReportBean.getReciveDateTo());
			
			jsonObject.put("tin"			,tin);
			jsonObject.put("reciveDateFrom"	,historyPurchasedByDealerReportBean.getReciveDateFrom());
			jsonObject.put("reciveDateTo"	,historyPurchasedByDealerReportBean.getReciveDateTo());
			jsonObject.put("vendorName"		,historyPurchasedByDealerReportBean.getVendorName());
			
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
			historyPurchasedByDealerReportBean.setTin(tin);
			
			resultList = historyPurchasedReportBusiness.searchByDealer(historyPurchasedByDealerReportBean);
			
			if(resultList!=null){
				for(HistoryPurchasedByDealerReportBean vo:resultList){
					objDetail = new JSONObject();
					
					objDetail.put("vendorName"		,vo.getVendorName());
					objDetail.put("reciveNo"		,vo.getReciveNo());
					objDetail.put("reciveDate"		,vo.getReciveDate());
					objDetail.put("reciveTotal"		,vo.getReciveTotal());
					objDetail.put("reciveDiscount"	,vo.getReciveDiscount());
					
					jSONArray.add(objDetail);
				}
			}
			
			jsonObject.put("historyPurchasedByDealerReportList"	,jSONArray);
			/*End รายละเอียดรายงาน*/
			/*End รายละเอียดรายงาน*/
			
			writePDF("HistoryPurchasedByDealerPdfForm", jsonObject, "รายงานประวัติการซื้อตามผู้จำหน่าย");
			
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
    		
    		if("historyPurchasedByDealerReportBean.vendorName".equals(getAutoCompleteName())){
    			list = companyVendorBusiness.vendorNameList(getAutoCompParamter(), getCurrentUser().getTin());
    		}else if("historyPurchasedByDealerReportBean.branchName".equals(getAutoCompleteName())){
    			list = companyVendorBusiness.branchNameList(historyPurchasedByDealerReportBean.getVendorName(), getAutoCompParamter(), getCurrentUser().getTin());
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
