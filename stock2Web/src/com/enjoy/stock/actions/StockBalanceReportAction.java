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
import com.enjoy.stock.bean.StockBalanceReportBean;
import com.enjoy.stock.business.ProductGroupBusiness;
import com.enjoy.stock.business.ProductTypeBusiness;
import com.enjoy.stock.business.StockBalanceReportBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class StockBalanceReportAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(StockBalanceReportAction.class);
	
	private StockBalanceReportBean 	stockBalanceReportBean;
	
	@Autowired
	ProductTypeBusiness productTypeBusiness;
	@Autowired
	ProductGroupBusiness productGroupBusiness;
	@Autowired
	StockBalanceReportBusiness stockBalanceReportBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	stockBalanceReportBean 	= new StockBalanceReportBean();
    	
		return SUCCESS;
	}
    
    public String showData() throws Exception{
		logger.info("[showData][Begin]");
		
		JSONObject 							jsonObject 			= new JSONObject();
		JSONArray 							jSONArray 			= new JSONArray();
		JSONObject 							objDetail 			= null;
		String								tin 				= null;
		CompanyDetailsBean 					companyDetailsDb 	= null;
		ArrayList<StockBalanceReportBean> 	resultList 			= null;
		
		try{
			tin = getCurrentUser().getTin();
			
			logger.info("[showData] productTypeName  :: " + stockBalanceReportBean.getProductTypeName());
			logger.info("[showData] productGroupName :: " + stockBalanceReportBean.getProductGroupName());
			
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
			stockBalanceReportBean.setTin(tin);
			
			resultList = stockBalanceReportBusiness.search(stockBalanceReportBean);
			
			if(resultList!=null){
				for(StockBalanceReportBean vo:resultList){
					objDetail = new JSONObject();
					
					objDetail.put("productTypeName"	,vo.getProductTypeName());
					objDetail.put("productGroupName",vo.getProductGroupName());
					objDetail.put("productName"		,vo.getProductName());
					objDetail.put("quantity"		,vo.getQuantity());
					
					jSONArray.add(objDetail);
				}
			}
			
			jsonObject.put("resultList"	,jSONArray);
			/*End รายละเอียดรายงาน*/
			
			writePDF("StockBalancePdfForm", jsonObject, "รายงานยอด Stock คงเหลือ");
			
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
    		
    		if("stockBalanceReportBean.productTypeName".equals(getAutoCompleteName())){
    			list = productTypeBusiness.productTypeNameList(getAutoCompParamter(), getCurrentUser().getTin());
    		}else if("stockBalanceReportBean.productGroupName".equals(getAutoCompleteName())){
    			list = productGroupBusiness.productGroupNameList(stockBalanceReportBean.getProductTypeName(), getAutoCompParamter(), getCurrentUser().getTin(), true);
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
