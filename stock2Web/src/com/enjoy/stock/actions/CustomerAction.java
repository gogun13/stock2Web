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
import com.enjoy.stock.bean.CustomerDetailsBean;
import com.enjoy.stock.bean.RelationGroupCustomerBean;
import com.enjoy.stock.business.CustomerBusiness;
import com.enjoy.stock.business.RelationGroupCustomerBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CustomerAction.class);
	
	private CustomerDetailsBean 			customerDetailsBean;
	private ArrayList<ComboBean> 			statusCombo;
	private ArrayList<ComboBean> 			sexCombo;
	private ArrayList<ComboBean> 			idTypeCombo;
	private ArrayList<ComboBean> 			groupSalePriceCombo;
	private String							pageMode;
	private String							titlePage;
	private String							hidCusCode;
	private String							cusStatusDis;
	private String							showBackFlag;
	
	@Autowired
	AddressBusiness addressBusiness;
	
	@Autowired
	CustomerBusiness customerBusiness;
	
	@Autowired
	RelationGroupCustomerBusiness relationGroupCustomerBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	customerDetailsBean = new CustomerDetailsBean();
    	titlePage 			= "เพิ่มรายละเอียดลูกค้า";
    	pageMode			= Constants.NEW;
    	
    	cusStatusDis 	= "A";
    	customerDetailsBean.setCusStatus(cusStatusDis);
    	
    	setRefference();
    	
		return SUCCESS;
	}
    
    private void setRefference() throws Exception{
		
		logger.info("[setRefference][Begin]");
		
		try{
			statusCombo 		= customerBusiness.getStatusCombo();
			setSexCombo();
			setIdTypeCombo();
			setGroupSalePriceCombo();
			
		}catch(Exception e){
			logger.error("setRefference :: ", e);
			throw e;
		}finally{
			logger.info("[setRefference][End]");
		}
	}
    
    private void setSexCombo() throws Exception{
		
		logger.info("[setCusStatus][Begin]");
		
		try{
			
			sexCombo = new ArrayList<ComboBean>();
			
			sexCombo.add(new ComboBean(""	, "กรุณาระบุ"));
			sexCombo.add(new ComboBean("M"	, "ชาย"));
			sexCombo.add(new ComboBean("F"	, "หญิง"));
		}
		catch(Exception e){
			throw e;
		}finally{
			logger.info("[setSexCombo][End]");
		}
	}
    
    private void setIdTypeCombo() throws EnjoyException{
		
		logger.info("[setIdTypeCombo][Begin]");
		
		try{
			
			idTypeCombo = new ArrayList<ComboBean>();
			
			idTypeCombo.add(new ComboBean("0"	, "ไม่ระบุ"));
			idTypeCombo.add(new ComboBean("1"	, "บุคคลธรรมดา"));
			idTypeCombo.add(new ComboBean("2"	, "นิติบุคคล"));
		}
		catch(Exception e){
			logger.info(e.getMessage());
			throw new EnjoyException("setIdTypeCombo is error");
		}finally{
			logger.info("[setIdTypeCombo][End]");
		}
	}
    
    private void setGroupSalePriceCombo() throws Exception{
		
		logger.info("[setGroupSalePriceCombo][Begin]");
		
		ArrayList<RelationGroupCustomerBean> list 					= null;
		
		try{
			list				= relationGroupCustomerBusiness.searchByCriteria(getCurrentUser().getTin());
			groupSalePriceCombo	= new ArrayList<ComboBean>();
			
			groupSalePriceCombo.add(new ComboBean(""	, "กรุณาเลือก"));
			for(RelationGroupCustomerBean bean:list){
				groupSalePriceCombo.add(new ComboBean(bean.getCusGroupCode()	, bean.getCusGroupName()));
			}
			
		}
		catch(Exception e){
			throw e;
		}finally{
			logger.info("[setGroupSalePriceCombo][End]");
		}
	}
    
    public String getDetail() throws Exception{
		logger.info("[getDetail][Begin]");
		
		try{
			logger.info("[getDetail] hidCusCode :: " + hidCusCode);
			
			titlePage 		= "แก้ไขรายละเอียดลูกค้า";
			pageMode		= Constants.UPDATE;
			
			setRefference();
			
			customerDetailsBean = customerBusiness.getCustomerDetail(hidCusCode, getCurrentUser().getTin());
			
			if(customerDetailsBean==null){
				throw new EnjoyException("เกิดข้อผิดพลาดในการดึงรายละเอียดลูกค้า");
			}
			
			cusStatusDis 		= customerDetailsBean.getCusStatus();
			
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
			logger.info("[onSave] hidCusCode :: " + hidCusCode);
			
			customerDetailsBean.setCusCode(hidCusCode);
			
			if(EnjoyUtils.chkNull(customerDetailsBean.getProvinceName()) && EnjoyUtils.chkNull(customerDetailsBean.getDistrictName()) 
					&& EnjoyUtils.chkNull(customerDetailsBean.getSubdistrictName())){
				addressBean = addressBusiness.getAddressId(customerDetailsBean.getProvinceName()
															, customerDetailsBean.getDistrictName()
															, customerDetailsBean.getSubdistrictName());
				
				
				customerDetailsBean.setProvinceCode(addressBean.getProvinceId());
				customerDetailsBean.setDistrictCode(addressBean.getDistrictId());
				customerDetailsBean.setSubdistrictCode(addressBean.getSubdistrictId());
			}else{
				customerDetailsBean.setProvinceCode("");
				customerDetailsBean.setDistrictCode("");
				customerDetailsBean.setSubdistrictCode("");
				customerDetailsBean.setProvinceName("");
				customerDetailsBean.setDistrictName("");
				customerDetailsBean.setSubdistrictName("");
			}
			
			if(customerBusiness.checkDupCusName(customerDetailsBean.getCusName()
												, customerDetailsBean.getCusSurname()
												, customerDetailsBean.getBranchName()
												, hidCusCode
												, getCurrentUser().getTin()) > 0){
				if(EnjoyUtils.chkNull(customerDetailsBean.getBranchName())){
				   branchName = " สาขา " + customerDetailsBean.getBranchName();
			   }
			   
			   throw new EnjoyException("ชื่อ"+customerDetailsBean.getCusName() + " " + customerDetailsBean.getCusSurname() + branchName + " มีอยู่แล้วในระบบ");
		   }
			
			
			customerBusiness.save(customerDetailsBean, pageMode, getCurrentUser().getTin());
			
			
			obj.put("cusCode", customerDetailsBean.getCusCode());
			
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
    		
    		if("customerDetailsBean.provinceName".equals(getAutoCompleteName())){
    			list = addressBusiness.provinceList(getAutoCompParamter());
    		}else if("customerDetailsBean.districtName".equals(getAutoCompleteName())){
    			list = addressBusiness.districtList(customerDetailsBean.getProvinceName(),getAutoCompParamter());
    		}else if("customerDetailsBean.subdistrictName".equals(getAutoCompleteName())){
    			list = addressBusiness.subdistrictList(customerDetailsBean.getProvinceName(),customerDetailsBean.getDistrictName(),getAutoCompParamter());
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
