package com.enjoy.core.business;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.bean.UserPrivilegeBean;

public interface UserdetailsBusiness {
	public UserDetailsBean userSelect(String userEmail, String pass) throws Exception;
	public void changePassword(String oldPassword, String newPassword, UserDetailsBean userData) throws Exception;
	public ArrayList<ComboBean> getRefuserstatusCombo() throws Exception;
	public ArrayList<UserPrivilegeBean> getUserprivilege() throws Exception;
	public UserDetailsBean getUserdetail(int userUniqueId) throws Exception;
	public int checkDupUserEmail(String userEmail, String pageMode, int userUniqueId) throws Exception;
	public int saveUserDetailsBean(UserDetailsBean vo, String pageMode, UserDetailsBean userData, String sendMailFlag, String password) throws Exception;
	public String getSendMailFlag(String tin)throws Exception;
	public void changePassword(final String newPassword, final int userUniqueId) throws Exception;
	public ArrayList<ComboBean> userFullNameList(String userFullName, String tin, int userUniqueId)throws Exception;
	public void getListUserdetail(PaginatedListBean paginatedList,UserDetailsBean userDetailsBean) throws Exception;
	public ArrayList<ComboBean> getCompanyList(int userUniqueId) throws Exception;
	public ComboBean getCompanyForAdminEnjoy(int userUniqueId) throws Exception;
	public void getListUserdetailForLookUp(PaginatedListBean paginatedList,UserDetailsBean userDetailsBean) throws Exception;
	public void updateUserStatusByUserEmail(String userEmail, String userStatus) throws Exception;
	public UserDetailsBean getUserdetailByTin(String fullName ,String tin) throws Exception;
}
