package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.core.business.AddressBusiness;
import com.enjoy.core.business.RefconstantcodeBusiness;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.CustomerDetailsBean;
import com.enjoy.stock.services.CustomerService;

@Service
public class CustomerBusinessImpl extends AbstractBusiness implements CustomerBusiness{
	private static final Logger logger = Logger.getLogger(CustomerBusinessImpl.class);
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	AddressBusiness addressBusiness;
	
	@Autowired
	RefconstantcodeBusiness refconstantcodeBusiness;

	@Override
	public void getListCustomerDetails(PaginatedListBean paginatedList	,CustomerDetailsBean customerDetailsBean) throws Exception {
		logger.info("[getListCustomerDetails][Begin]");
		
		ArrayList<CustomerDetailsBean>	customerDetailsList 	= null;
		String							provinceName			= null;
		String							districtName			= null;
		String							subdistrictName			= null;
		
		try{
			
			customerService.getListCustomerDetails(paginatedList, customerDetailsBean);
			
			customerDetailsList = (ArrayList<CustomerDetailsBean>) paginatedList.getList();
			
			logger.info("[getListCustomerDetails] customerDetailsList :: " + customerDetailsList.size());
			
			for(CustomerDetailsBean vo:customerDetailsList){
				provinceName		= addressBusiness.getProvinceName(vo.getProvinceCode());
				districtName		= addressBusiness.getDistrictName(vo.getDistrictCode());
				subdistrictName		= addressBusiness.getSubdistrictName(vo.getSubdistrictCode());
				
				if (EnjoyUtils.chkNull(provinceName))vo.setProvinceName(provinceName);
				if (EnjoyUtils.chkNull(districtName))vo.setDistrictName(districtName);
				if (EnjoyUtils.chkNull(subdistrictName))vo.setSubdistrictName(subdistrictName);
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getListCustomerDetails][End]");
		}
	}

	@Override
	public CustomerDetailsBean getCustomerDetail(String cusCode,String tin) throws Exception {
		logger.info("[getCustomerDetail][Begin]");
		
		CustomerDetailsBean	customerDetails 	= null;
		String				provinceName		= null;
		String				districtName		= null;
		String				subdistrictName		= null;
		
		try{
			
			customerDetails = customerService.getCustomerDetail(cusCode,tin);
			
			if(customerDetails!=null){
				provinceName		= addressBusiness.getProvinceName(customerDetails.getProvinceCode());
				districtName		= addressBusiness.getDistrictName(customerDetails.getDistrictCode());
				subdistrictName		= addressBusiness.getSubdistrictName(customerDetails.getSubdistrictCode());
				
				if (EnjoyUtils.chkNull(provinceName))customerDetails.setProvinceName(provinceName);
				if (EnjoyUtils.chkNull(districtName))customerDetails.setDistrictName(districtName);
				if (EnjoyUtils.chkNull(subdistrictName))customerDetails.setSubdistrictName(subdistrictName);
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getCustomerDetail][End]");
		}
		
		return customerDetails;
	}

	@Override
	public ArrayList<ComboBean> getStatusCombo() throws Exception {
		return customerService.getStatusCombo();
	}

	@Override
	public int checkDupCusName(String cusName, String cusSurname, String branchName, String cusCode, String tin) throws Exception {
		return customerService.checkDupCusName(cusName, cusSurname, branchName, cusCode, tin);
	}

	@Override
	public ArrayList<ComboBean> getCusFullName(String fullName,String tin) throws Exception {
		return customerService.getCusFullName(fullName, tin);
	}

	@Override
	public void save(CustomerDetailsBean vo, String pageMode, String tin)throws Exception {
		logger.info("[save][Begin]");
		
		String codeDisplay 	= null;
		String cusCode 		= null;
		
		try{
			logger.info("[save] pageMode 	:: " + pageMode);
			
			vo.setTin(tin);
			
			if(pageMode.equals(Constants.NEW)){
				codeDisplay = refconstantcodeBusiness.getCodeDisplay("1", tin);
				cusCode		= customerService.genCusCode(codeDisplay, tin);
				
				vo.setCusCode(cusCode);
				
				customerService.insertCustomerDetails(vo);
			}else{
				customerService.updateCustomerDetail(vo);
			}
		}catch(Exception e){
			logger.error("[save] :: ",e);
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
	}

	

}
