package com.enjoy.core.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.CompanyDetailsBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.bean.RefconstantcodeBean;
import com.enjoy.core.main.Constants;
import com.enjoy.core.services.CompanyDetailsService;
import com.enjoy.core.services.RefconstantcodeService;
import com.enjoy.core.utils.EnjoyUtils;

@Service
public class CompanyDetailsBusinessImpl extends AbstractBusiness implements CompanyDetailsBusiness{
	private static final Logger logger = Logger.getLogger(CompanyDetailsBusinessImpl.class);
	
	@Autowired
	CompanyDetailsService companyDetailsService;
	
	@Autowired
	AddressBusiness addressBusiness;
	
	@Autowired
	RefconstantcodeService refconstantcodeService;

	@Override
	public void getListCompanyDetails(PaginatedListBean paginatedList,CompanyDetailsBean companyDetailsBean) throws Exception {
		logger.info("[getListCompanyDetails][Begin]");
		
		ArrayList<CompanyDetailsBean>	companyDetailsList 		= null;
		String							provinceName			= null;
		String							districtName			= null;
		String							subdistrictName			= null;
		
		try{
			
			companyDetailsService.getListCompanyDetails(paginatedList, companyDetailsBean);
			
			companyDetailsList = (ArrayList<CompanyDetailsBean>) paginatedList.getList();
			
			logger.info("[getListCompanyDetails] companyDetailsList :: " + companyDetailsList.size());
			
			for(CompanyDetailsBean vo:companyDetailsList){
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
			logger.info("[getListCompanyDetails][End]");
		}
	}

	@Override
	public CompanyDetailsBean getCompanyDetails(String tin) throws Exception {
		logger.info("[getCompanyDetails][Begin]");
		
		CompanyDetailsBean	companyDetails 		= null;
		String				provinceName		= null;
		String				districtName		= null;
		String				subdistrictName		= null;
		
		try{
			
			companyDetails = companyDetailsService.getCompanyDetails(tin);
			
			if(companyDetails!=null){
				provinceName		= addressBusiness.getProvinceName(companyDetails.getProvinceCode());
				districtName		= addressBusiness.getDistrictName(companyDetails.getDistrictCode());
				subdistrictName		= addressBusiness.getSubdistrictName(companyDetails.getSubdistrictCode());
				
				if (EnjoyUtils.chkNull(provinceName))companyDetails.setProvinceName(provinceName);
				if (EnjoyUtils.chkNull(districtName))companyDetails.setDistrictName(districtName);
				if (EnjoyUtils.chkNull(subdistrictName))companyDetails.setSubdistrictName(subdistrictName);
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getCompanyDetails][End]");
		}
		
		return companyDetails;
	}

	@Override
	public ArrayList<ComboBean> getCompanystatusCombo() throws Exception {
		return companyDetailsService.getCompanystatusCombo();
	}

	@Override
	public int checkDupTin(String tin) throws Exception {
		return companyDetailsService.checkDupTin(tin);
	}

	@Override
	public ArrayList<ComboBean> companyNameList(String companyName,int userUniqueId) throws Exception {
		return companyDetailsService.companyNameList(companyName, userUniqueId);
	}

	@Override
	public String getTin(String companyName) throws Exception {
		return companyDetailsService.getTin(companyName);
	}

	@Override
	public ArrayList<ComboBean> getCompanyCombo() throws Exception {
		return companyDetailsService.getCompanyCombo();
	}

	@Override
	public ArrayList<ComboBean> tinListForAutoComplete(String tin)throws Exception {
		return companyDetailsService.tinListForAutoComplete(tin);
	}

	@Override
	public ArrayList<ComboBean> companyNameListForAutoComplete(String companyName) throws Exception {
		return companyDetailsService.companyNameListForAutoComplete(companyName);
	}

	@Override
	public void saveCompany(CompanyDetailsBean vo, String pageMode)throws Exception {
		logger.info("[saveCompany][Begin]");
		
		ArrayList<RefconstantcodeBean> 	templateDefaultList 	= null;
		
		try{
			logger.info("[saveCompany] pageMode 	:: " + pageMode);
			
			if(pageMode.equals(Constants.NEW)){
				companyDetailsService.insertCompanyDetail(vo);
				
				templateDefaultList = refconstantcodeService.templateDefaultList();
				
				for(RefconstantcodeBean bean:templateDefaultList){
					bean.setTin(vo.getTin());
					refconstantcodeService.insertRefconstantcode(bean);
				}
				
			}else{
				companyDetailsService.updateCompanyDetail(vo);
			}
		}catch(Exception e){
			logger.error("[saveCompany] :: ",e);
			throw e;
		}finally{
			logger.info("[saveCompany][End]");
		}
		
	}

	@Override
	public String getCompanyName(String tin) throws Exception {
		return companyDetailsService.getCompanyName(tin);
	}

	

}
