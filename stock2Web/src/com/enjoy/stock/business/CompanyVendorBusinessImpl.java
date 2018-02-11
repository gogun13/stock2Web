package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.core.business.AddressBusiness;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.CompanyVendorBean;
import com.enjoy.stock.services.CompanyVendorService;

@Service
public class CompanyVendorBusinessImpl extends AbstractBusiness implements CompanyVendorBusiness{
	private static final Logger logger = Logger.getLogger(CompanyVendorBusinessImpl.class);
	
	@Autowired
	CompanyVendorService companyVendorService;
	@Autowired
	AddressBusiness addressBusiness;

	@Override
	public void searchByCriteria(PaginatedListBean paginatedList,CompanyVendorBean companyVendorBean) throws Exception {
		logger.info("[searchByCriteria][Begin]");
		
		ArrayList<CompanyVendorBean>	companyVendorList 		= null;
		String							provinceName			= null;
		String							districtName			= null;
		String							subdistrictName			= null;
		
		try{
			
			companyVendorService.searchByCriteria(paginatedList, companyVendorBean);
			
			companyVendorList = (ArrayList<CompanyVendorBean>) paginatedList.getList();
			
			logger.info("[searchByCriteria] companyVendorList :: " + companyVendorList.size());
			
			for(CompanyVendorBean vo:companyVendorList){
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
			logger.info("[searchByCriteria][End]");
		}
	}

	@Override
	public CompanyVendorBean getCompanyVendor(String vendorCode,String tinCompany) throws Exception {
		logger.info("[getCompanyVendor][Begin]");
		
		CompanyVendorBean	companyVendorBean 	= null;
		String				provinceName		= null;
		String				districtName		= null;
		String				subdistrictName		= null;
		
		try{
			
			companyVendorBean = companyVendorService.getCompanyVendor(vendorCode, tinCompany);
			
			if(companyVendorBean!=null){
				provinceName		= addressBusiness.getProvinceName(companyVendorBean.getProvinceCode());
				districtName		= addressBusiness.getDistrictName(companyVendorBean.getDistrictCode());
				subdistrictName		= addressBusiness.getSubdistrictName(companyVendorBean.getSubdistrictCode());
				
				if (EnjoyUtils.chkNull(provinceName))companyVendorBean.setProvinceName(provinceName);
				if (EnjoyUtils.chkNull(districtName))companyVendorBean.setDistrictName(districtName);
				if (EnjoyUtils.chkNull(subdistrictName))companyVendorBean.setSubdistrictName(subdistrictName);
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getCompanyVendor][End]");
		}
		
		return companyVendorBean;
	}

	@Override
	public int checkDupVendorName(String vendorName, String branchName,String vendorCode, String tinCompany) throws Exception {
		return companyVendorService.checkDupVendorName(vendorName, branchName, vendorCode, tinCompany);
	}

	@Override
	public ArrayList<ComboBean> vendorNameList(String vendorName,String tinCompany) throws Exception {
		return companyVendorService.vendorNameList(vendorName, tinCompany);
	}

	@Override
	public ArrayList<ComboBean> branchNameList(String vendorName,String branchName, String tinCompany) throws Exception {
		return companyVendorService.branchNameList(vendorName, branchName, tinCompany);
	}

	@Override
	public CompanyVendorBean getCompanyVendorByName(String vendorName,String branchName, String tinCompany) throws Exception {
		return companyVendorService.getCompanyVendorByName(vendorName, branchName, tinCompany);
	}
	
	@Override
	public void save(CompanyVendorBean vo, String pageMode, String tinCompany)throws Exception {
		logger.info("[save][Begin]");
		
		
		try{
			logger.info("[save] pageMode 	:: " + pageMode);
			
			vo.setTinCompany(tinCompany);
			
			if(pageMode.equals(Constants.NEW)){
				
				vo.setVendorCode(EnjoyUtils.nullToStr(companyVendorService.genId(tinCompany)));
				
				companyVendorService.insertCompanyVendor(vo);
			}else{
				companyVendorService.updateCompanyvendor(vo);
			}
		}catch(Exception e){
			logger.error("[save] :: ",e);
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
	}


}
