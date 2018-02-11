package com.enjoy.core.services;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PagesDetailBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.bean.UserPrivilegeBean;

public interface UserdetailsService {
	public UserDetailsBean userSelect(String userEmail, String pass) throws Exception;
	public ArrayList<UserPrivilegeBean> userPrivilegeListSelect(String userPrivilege) throws Exception;
	public ArrayList<PagesDetailBean> pagesDetailListSelect(String pagesCode) throws Exception;
	public int updateUserPassword(UserDetailsBean userDetailsBean) throws Exception;
	public ArrayList<ComboBean> getRefuserstatusCombo() throws Exception;
	public ArrayList<UserPrivilegeBean> getUserprivilege() throws Exception;
	public UserDetailsBean getUserdetail(int userUniqueId) throws Exception;
	public int checkDupUserEmail(String userEmail, String pageMode, int userUniqueId) throws Exception;
	public void saveUserDetails(UserDetailsBean vo) throws Exception;
	public void updateUserDetail(UserDetailsBean vo) throws Exception;
	public int lastId() throws Exception;
	public void changePassword(final String newPassword, final int userUniqueId) throws Exception;
	public ArrayList<ComboBean> userFullNameList(String userFullName, String tin, int userUniqueId)throws Exception;
	public void getListUserdetail(PaginatedListBean paginatedList,UserDetailsBean userDetailsBean) throws Exception;
	public void getListUserdetailForLookUp(PaginatedListBean paginatedList,UserDetailsBean userDetailsBean) throws Exception;
	public void updateUserStatusByUserEmail(final String userEmail, final String userStatus) throws Exception;
	public UserDetailsBean getUserdetailByTin(String fullName ,String tin) throws Exception;
}
