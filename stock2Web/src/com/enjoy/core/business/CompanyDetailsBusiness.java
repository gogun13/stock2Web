package com.enjoy.core.business;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.CompanyDetailsBean;
import com.enjoy.core.bean.PaginatedListBean;

public interface CompanyDetailsBusiness {
	public void getListCompanyDetails(PaginatedListBean paginatedList,CompanyDetailsBean companyDetailsBean) throws Exception;
	public CompanyDetailsBean getCompanyDetails(String tin) throws Exception;
	public ArrayList<ComboBean> getCompanystatusCombo()throws Exception;
	public int checkDupTin(String tin) throws Exception;
	public ArrayList<ComboBean> companyNameList(String companyName, int userUniqueId)throws Exception;
	public String getTin(String companyName)throws Exception;
	public ArrayList<ComboBean> getCompanyCombo()throws Exception;
	public ArrayList<ComboBean> tinListForAutoComplete(String tin)throws Exception;
	public ArrayList<ComboBean> companyNameListForAutoComplete(String companyName)throws Exception;
	public void saveCompany(CompanyDetailsBean vo, String pageMode) throws Exception;
	public String getCompanyName(String tin) throws Exception;
}
