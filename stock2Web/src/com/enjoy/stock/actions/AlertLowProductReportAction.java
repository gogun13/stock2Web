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
import com.enjoy.core.main.Constants;
import com.enjoy.stock.bean.AlertLowProductReportBean;
import com.enjoy.stock.business.AlertLowProductReportBusiness;
import com.enjoy.stock.business.ProductGroupBusiness;
import com.enjoy.stock.business.ProductTypeBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlertLowProductReportAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AlertLowProductReportAction.class);
	
	private AlertLowProductReportBean 				alertLowProductReportBean;
	
	@Autowired
	ProductTypeBusiness productTypeBusiness;
	@Autowired
	ProductGroupBusiness productGroupBusiness;
	@Autowired
	AlertLowProductReportBusiness alertLowProductReportBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	alertLowProductReportBean 	= new AlertLowProductReportBean();
    	
		return SUCCESS;
	}
    
    public String showData() throws Exception{
		logger.info("[showData][Begin]");
		
		JSONObject 								jsonObject 			= new JSONObject();
		JSONArray 								detailJSONArray 	= new JSONArray();
		JSONObject 								objDetail 			= null;
		String									tin 				= null;
		CompanyDetailsBean 						companyDetailsDb 	= null;
		ArrayList<AlertLowProductReportBean> 	resultList 			= null;
		AlertLowProductReportBean				bean				= new AlertLowProductReportBean();
		
		try{
			tin = getCurrentUser().getTin();
			
			logger.info("[showData] productTypeName  :: " + alertLowProductReportBean.getProductTypeName());
			logger.info("[showData] productGroupName :: " + alertLowProductReportBean.getProductGroupName());
			
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
			/*ENd รายละเอียดบริษัท*/
			
			/*Begin รายละเอียดรายงาน*/
			bean.setTin(tin);
			bean.setProductTypeName(alertLowProductReportBean.getProductTypeName());
			bean.setProductGroupName(alertLowProductReportBean.getProductGroupName());
			
			resultList = alertLowProductReportBusiness.search(bean);
			
			if(resultList!=null){
				for(AlertLowProductReportBean vo:resultList){
					objDetail = new JSONObject();
					
					objDetail.put("productTypeName"	,vo.getProductTypeName());
					objDetail.put("productGroupName",vo.getProductGroupName());
					objDetail.put("productName"		,vo.getProductName());
					objDetail.put("minQuan"			,vo.getMinQuan());
					objDetail.put("quantity"		,vo.getQuantity());
					
					detailJSONArray.add(objDetail);
				}
			}
			
			jsonObject.put("resultList"	,detailJSONArray);
			/*End รายละเอียดรายงาน*/
			
			writePDF("AlertLowProductPdfForm", jsonObject, "รายงานแจ้งเตือน Stock สินค้าใกล้หมด");
			
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
    		
    		if("alertLowProductReportBean.productTypeName".equals(getAutoCompleteName())){
    			list = productTypeBusiness.productTypeNameList(getAutoCompParamter(), getCurrentUser().getTin());
    		}else if("alertLowProductReportBean.productGroupName".equals(getAutoCompleteName())){
    			list = productGroupBusiness.productGroupNameList(alertLowProductReportBean.getProductTypeName(), getAutoCompParamter(), getCurrentUser().getTin(), true);
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
