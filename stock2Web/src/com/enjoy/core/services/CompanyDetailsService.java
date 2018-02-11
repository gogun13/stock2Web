package com.enjoy.core.services;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.CompanyDetailsBean;
import com.enjoy.core.bean.PaginatedListBean;

public interface CompanyDetailsService {
	public void getListCompanyDetails(PaginatedListBean paginatedList,CompanyDetailsBean companyDetailsBean) throws Exception;
	public CompanyDetailsBean getCompanyDetails(String tin) throws Exception;
	public ArrayList<ComboBean> getCompanystatusCombo()throws Exception;
	public int checkDupTin(String tin) throws Exception;
	public void insertCompanyDetail(CompanyDetailsBean vo) throws Exception;
	public void updateCompanyDetail(final CompanyDetailsBean vo) throws Exception;
	public ArrayList<ComboBean> companyNameList(String companyName, int userUniqueId)throws Exception;
	public String getTin(String companyName)throws Exception;
	public ArrayList<ComboBean> getCompanyCombo()throws Exception;
	public ArrayList<ComboBean> tinListForAutoComplete(String tin)throws Exception;
	public ArrayList<ComboBean> companyNameListForAutoComplete(String companyName)throws Exception;
	public String getCompanyName(String tin) throws Exception;
}
