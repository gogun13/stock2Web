package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.stock.bean.CompanyVendorBean;

public interface CompanyVendorBusiness {
	public void searchByCriteria(PaginatedListBean paginatedList,CompanyVendorBean vo) throws Exception;
	public CompanyVendorBean getCompanyVendor(String vendorCode,String tinCompany) throws Exception;
	public int checkDupVendorName(String vendorName, String branchName, String vendorCode, String tinCompany) throws Exception;
	public ArrayList<ComboBean> vendorNameList(String vendorName, String tinCompany) throws Exception;
	public ArrayList<ComboBean> branchNameList(String vendorName, String branchName, String tinCompany) throws Exception;
	public CompanyVendorBean getCompanyVendorByName(String vendorName, String branchName, String tinCompany) throws Exception;
	public void save(CompanyVendorBean vo, String pageMode, String tinCompany)throws Exception;
}
