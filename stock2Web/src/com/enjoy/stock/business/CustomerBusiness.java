package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.stock.bean.CustomerDetailsBean;

public interface CustomerBusiness {
	public void getListCustomerDetails(PaginatedListBean paginatedList	,CustomerDetailsBean customerDetailsBean) throws Exception;
	public CustomerDetailsBean getCustomerDetail(String cusCode,String tin) throws Exception;
	public ArrayList<ComboBean> getStatusCombo() throws Exception;
	public int checkDupCusName(String cusName, String cusSurname, String branchName, String cusCode, String tin) throws Exception;
	public ArrayList<ComboBean> getCusFullName(String fullName,String tin) throws Exception;
	public void save(CustomerDetailsBean vo, String pageMode, String tin)throws Exception;
}
