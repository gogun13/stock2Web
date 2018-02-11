package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.stock.bean.CustomerDetailsBean;

public interface CustomerService {
	public void getListCustomerDetails(PaginatedListBean paginatedList	,CustomerDetailsBean customerDetailsBean) throws Exception;
	public CustomerDetailsBean getCustomerDetail(String cusCode,String tin) throws Exception;
	public ArrayList<ComboBean> getStatusCombo() throws Exception;
	public int checkDupCusName(String cusName, String cusSurname, String branchName, String cusCode, String tin) throws Exception;
	public ArrayList<ComboBean> getCusFullName(String fullName,String tin) throws Exception;
	public void insertCustomerDetails(CustomerDetailsBean vo)throws Exception;
	public void updateCustomerDetail(final CustomerDetailsBean vo)throws Exception;
	public String genCusCode(String codeDisplay,String tin) throws Exception;
	public int countCusGroupCodeInCustomer(String cusGroupCode, String tin) throws Exception;
}
