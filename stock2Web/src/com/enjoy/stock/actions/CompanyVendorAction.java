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
import com.enjoy.core.bean.AddressBean;
import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.business.AddressBusiness;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.CompanyVendorBean;
import com.enjoy.stock.business.CompanyVendorBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyVendorAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CompanyVendorAction.class);
	
	private CompanyVendorBean 			companyVendorBean;
	private String						pageMode;
	private String						titlePage;
	private String						hidVendorCode;
	private String						showBackFlag;
	
	@Autowired
	AddressBusiness addressBusiness;
	
	@Autowired
	CompanyVendorBusiness companyVendorBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	companyVendorBean 	= new CompanyVendorBean();
    	titlePage 			= "บันทึกบริษัทสั่งซื้อ";
    	pageMode			= Constants.NEW;
    	
		return SUCCESS;
	}
    
    public String getDetail() throws Exception{
		logger.info("[getDetail][Begin]");
		
		try{
			logger.info("[getDetail] hidVendorCode :: " + hidVendorCode);
			
			titlePage 		= "แก้ไขบริษัทสั่งซื้อ";
			pageMode		= Constants.UPDATE;
			
			companyVendorBean = companyVendorBusiness.getCompanyVendor(hidVendorCode, getCurrentUser().getTin());
			
			if(companyVendorBean==null){
				throw new EnjoyException("เกิดข้อผิดพลาดในการดึงรายละเอียดบริษัทสั่งซื้อ");
			}
			
		}catch(Exception e){
			logger.error("getDetail :: ", e);
			throw e;
		}finally{
			logger.info("[getDetail][End]");
		}
		
		return SUCCESS;
		
	}
    
	public String onSave() throws Exception{
		logger.info("[onSave][Begin]");
		
		JSONObject 	obj 					= new JSONObject();
		AddressBean addressBean;
		String		branchName = "";
		
		try{
			logger.info("[onSave] hidVendorCode :: " + hidVendorCode);
			
			companyVendorBean.setVendorCode(hidVendorCode);
			
			if(EnjoyUtils.chkNull(companyVendorBean.getProvinceName()) && EnjoyUtils.chkNull(companyVendorBean.getDistrictName()) 
					&& EnjoyUtils.chkNull(companyVendorBean.getSubdistrictName())){
				addressBean = addressBusiness.getAddressId(companyVendorBean.getProvinceName()
															, companyVendorBean.getDistrictName()
															, companyVendorBean.getSubdistrictName());
				
				
				companyVendorBean.setProvinceCode(addressBean.getProvinceId());
				companyVendorBean.setDistrictCode(addressBean.getDistrictId());
				companyVendorBean.setSubdistrictCode(addressBean.getSubdistrictId());
			}else{
				companyVendorBean.setProvinceCode("");
				companyVendorBean.setDistrictCode("");
				companyVendorBean.setSubdistrictCode("");
				companyVendorBean.setProvinceName("");
				companyVendorBean.setDistrictName("");
				companyVendorBean.setSubdistrictName("");
			}
			
			if(companyVendorBusiness.checkDupVendorName(companyVendorBean.getVendorName()
														, companyVendorBean.getBranchName()
														, companyVendorBean.getVendorCode()
														, getCurrentUser().getTin()) > 0){
				if(EnjoyUtils.chkNull(companyVendorBean.getBranchName())){
					branchName = " สาขา " + companyVendorBean.getBranchName();
				}
			   
			   throw new EnjoyException(companyVendorBean.getVendorName() + branchName + " มีอยู่แล้วในระบบ");
		   }
			
			
			companyVendorBusiness.save(companyVendorBean, pageMode, getCurrentUser().getTin());
			
			
			obj.put("vendorCode", companyVendorBean.getVendorCode());
			
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
    		
    		if("companyVendorBean.provinceName".equals(getAutoCompleteName())){
    			list = addressBusiness.provinceList(getAutoCompParamter());
    		}else if("companyVendorBean.districtName".equals(getAutoCompleteName())){
    			list = addressBusiness.districtList(companyVendorBean.getProvinceName(),getAutoCompParamter());
    		}else if("companyVendorBean.subdistrictName".equals(getAutoCompleteName())){
    			list = addressBusiness.subdistrictList(companyVendorBean.getProvinceName(),companyVendorBean.getDistrictName(),getAutoCompParamter());
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
