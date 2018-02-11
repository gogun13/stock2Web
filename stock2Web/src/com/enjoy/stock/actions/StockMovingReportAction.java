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
import com.enjoy.stock.bean.ProductQuanHistoryBean;
import com.enjoy.stock.business.ProductBusiness;
import com.enjoy.stock.business.ProductGroupBusiness;
import com.enjoy.stock.business.ProductQuanHistoryBusiness;
import com.enjoy.stock.business.ProductTypeBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class StockMovingReportAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(StockMovingReportAction.class);
	
	private ProductQuanHistoryBean 	productQuanHistoryBean;
	
	@Autowired
	ProductTypeBusiness productTypeBusiness;
	@Autowired
	ProductGroupBusiness productGroupBusiness;
	@Autowired
	ProductBusiness productBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	@Autowired
	ProductQuanHistoryBusiness productQuanHistoryBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	productQuanHistoryBean 	= new ProductQuanHistoryBean();
    	
		return SUCCESS;
	}
    
    public String showData() throws Exception{
		logger.info("[showData][Begin]");
		
		JSONObject 							jsonObject 			= new JSONObject();
		JSONArray 							jSONArray 			= new JSONArray();
		JSONObject 							objDetail 			= null;
		String								tin 				= null;
		CompanyDetailsBean 					companyDetailsDb 	= null;
		ArrayList<ProductQuanHistoryBean> 	resultList 			= null;
		
		try{
			tin = getCurrentUser().getTin();
			
			logger.info("[showData] productTypeName  	:: " + productQuanHistoryBean.getProductTypeName());
			logger.info("[showData] productGroupName 	:: " + productQuanHistoryBean.getProductGroupName());
			logger.info("[showData] productName 		:: " + productQuanHistoryBean.getProductName());
			logger.info("[showData] hisDateFrom 		:: " + productQuanHistoryBean.getHisDateFrom());
			logger.info("[showData] hisDateTo 			:: " + productQuanHistoryBean.getHisDateTo());
			
			jsonObject.put("tin"				,tin);
			jsonObject.put("hisDateFrom"		,productQuanHistoryBean.getHisDateFrom());
			jsonObject.put("hisDateTo"			,productQuanHistoryBean.getHisDateTo());
			jsonObject.put("productTypeName"	,productQuanHistoryBean.getProductTypeName());
			jsonObject.put("productGroupName"	,productQuanHistoryBean.getProductGroupName());
			jsonObject.put("productName"		,productQuanHistoryBean.getProductName());
			
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
			productQuanHistoryBean.setTin(tin);
			
			resultList = productQuanHistoryBusiness.searchByCriteria(productQuanHistoryBean);
			
			if(resultList!=null){
				for(ProductQuanHistoryBean vo:resultList){
					objDetail = new JSONObject();
					
					objDetail.put("formRef"			,vo.getFormRef());
					objDetail.put("hisDate"			,vo.getHisDate());
					objDetail.put("productName"		,vo.getProductName());
					objDetail.put("quantityPlus"	,vo.getQuantityPlus());
					objDetail.put("quantityMinus"	,vo.getQuantityMinus());
					objDetail.put("quantityTotal"	,vo.getQuantityTotal());
					
					jSONArray.add(objDetail);
				}
			}
			
			jsonObject.put("resultList"	,jSONArray);
			/*End รายละเอียดรายงาน*/
			
			writePDF("StockMovingPdfForm", jsonObject, "รายงานเคลื่อนไหว Stock สินค้า");
			
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
    		
    		if("productQuanHistoryBean.productTypeName".equals(getAutoCompleteName())){
    			list = productTypeBusiness.productTypeNameList(getAutoCompParamter(), getCurrentUser().getTin());
    		}else if("productQuanHistoryBean.productGroupName".equals(getAutoCompleteName())){
    			list = productGroupBusiness.productGroupNameList(productQuanHistoryBean.getProductTypeName(), getAutoCompParamter(), getCurrentUser().getTin(), true);
    		}else if("productQuanHistoryBean.productName".equals(getAutoCompleteName())){
    			list = productBusiness.productNameList(getAutoCompParamter()
						, productQuanHistoryBean.getProductTypeName()
						, productQuanHistoryBean.getProductGroupName()
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
