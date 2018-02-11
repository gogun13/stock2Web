package com.enjoy.core.actions;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.bean.AddressBean;
import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.CompanyDetailsBean;
import com.enjoy.core.business.AddressBusiness;
import com.enjoy.core.business.CompanyDetailsBusiness;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.Constants;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyDetailsMaintananceAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CompanyDetailsMaintananceAction.class);
	
	private CompanyDetailsBean 				companyDetailsBean;
	private ArrayList<ComboBean> 			statusCombo;
	private String							pageMode;
	private String							titlePage;
	private String							hidTin;
	private String							companyStatusDis;
	private String							userUniqueId;
	private String							showBackFlag;
	
	@Autowired
	AddressBusiness addressBusiness;
	
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	userUniqueId = String.valueOf(getCurrentUser().getUserUniqueId());
    	
    	if(userUniqueId.equals("1")){
	    	companyDetailsBean 	= new CompanyDetailsBean();
	    	titlePage 			= "เพิ่มรายละเอียดบริษัท";
	    	pageMode			= Constants.NEW;
	    	
	    	companyStatusDis 	= "A";
	    	companyDetailsBean.setCompanyStatus(companyStatusDis);
	    	
	    	setRefference();
    	}else{
    		hidTin = getCurrentUser().getTin();
    		getDetail();
    	}
    	
		return SUCCESS;
	}
    
    private void setRefference() throws Exception{
		
		logger.info("[setRefference][Begin]");
		
		try{
			statusCombo 		= companyDetailsBusiness.getCompanystatusCombo();
			
		}catch(Exception e){
			logger.error("setRefference :: ", e);
			throw e;
		}finally{
			logger.info("[setRefference][End]");
		}
	}
    
    public String getDetail() throws Exception{
		logger.info("[getDetail][Begin]");
		
		try{
			logger.info("[getDetail] hidTin :: " + hidTin);
			
			titlePage 		= "แก้ไขรายละเอียดบริษัท";
			pageMode		= Constants.UPDATE;
			userUniqueId 	= String.valueOf(getCurrentUser().getUserUniqueId());
			
			setRefference();
			
			companyDetailsBean 	= companyDetailsBusiness.getCompanyDetails(hidTin);
			
			if(companyDetailsBean==null){
				throw new EnjoyException("เกิดข้อผิดพลาดในการดึงรายละเอียดบริษัท");
			}
			
			companyStatusDis 	= companyDetailsBean.getCompanyStatus();
			
		}catch(Exception e){
			logger.error("getDetail :: ", e);
			throw e;
		}finally{
			logger.info("[getDetail][End]");
		}
		
		return SUCCESS;
		
	}
    
	public String checkDupTin() throws Exception{
		logger.info("[checkDupTin][Begin]");
		
		int 				cou				= 0;
		JSONObject 			obj 			= null;
		
		try{
			logger.info("[checkDupTin] hidTin 	:: " + hidTin);
			
			
			cou	= companyDetailsBusiness.checkDupTin(hidTin);
			
			obj = new JSONObject();
			obj.put("COU",cou);
			
			logger.info("[checkDupTin] cou :: " + cou);
		}catch(Exception e){
			logger.error("checkDupTin :: ", e);
			throw e;
		}finally{
			writeMSG(obj.toString());
			logger.info("[checkDupTin][End]");
		}
		return null;
	}

	
	public String onSave() throws Exception{
		logger.info("[onSave][Begin]");
		
		String		flagChkCompany			= null;
		JSONObject 	obj 					= new JSONObject();
		AddressBean addressBean;
		
		try{
			logger.info("[onSave] hidTin :: " + hidTin);
			
			companyDetailsBean.setTin(hidTin);
			
			addressBean = addressBusiness.getAddressId(companyDetailsBean.getProvinceName()
														, companyDetailsBean.getDistrictName()
														, companyDetailsBean.getSubdistrictName());
			
			
			companyDetailsBean.setProvinceCode(addressBean.getProvinceId());
			companyDetailsBean.setDistrictCode(addressBean.getDistrictId());
			companyDetailsBean.setSubdistrictCode(addressBean.getSubdistrictId());
			
			companyDetailsBusiness.saveCompany(companyDetailsBean, pageMode);
			
			flagChkCompany = getCurrentUser().getFlagChkCompany();
			if(flagChkCompany.equals("Y") && getCurrentUser().getFlagChangePassword().equals("N")){
				getCurrentUser().setFlagChkCompany("N");
			}
			
			obj.put("tin"				, hidTin);
			obj.put("flagChkCompany"	, flagChkCompany);
			obj.put("FlagChange"		, getCurrentUser().getFlagChangePassword());
			
			writeMSG(obj.toString());
			
		}catch(Exception e){
			logger.error("onSave :: ", e);
			throw e;
		}finally{
			logger.info("[onSave][End]");
		}
		
		return null;
	}
	
	public String onBack()throws Exception {
		return "onBack";
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
    		
    		if("companyDetailsBean.provinceName".equals(getAutoCompleteName())){
    			list = addressBusiness.provinceList(getAutoCompParamter());
    		}else if("companyDetailsBean.districtName".equals(getAutoCompleteName())){
    			list = addressBusiness.districtList(companyDetailsBean.getProvinceName(),getAutoCompParamter());
    		}else if("companyDetailsBean.subdistrictName".equals(getAutoCompleteName())){
    			list = addressBusiness.subdistrictList(companyDetailsBean.getProvinceName(),companyDetailsBean.getDistrictName(),getAutoCompParamter());
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
