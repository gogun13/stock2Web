package com.enjoy.core.services;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.RelationUserAndCompanyBean;

public interface RelationUserAndCompanyService {
	public ArrayList<RelationUserAndCompanyBean> searchByCriteria(RelationUserAndCompanyBean vo) throws Exception;
	public void insertRelationUserAndCompany(RelationUserAndCompanyBean vo) throws Exception;
	public void deleteRelationUserAndCompany(String tin) throws Exception;
	public void deleteRelationUserAndCompany(String userUniqueId, String tin) throws Exception;
	public int countForCheckLogin(int userUniqueId) throws Exception;
	public ArrayList<ComboBean> getCompanyList(int userUniqueId) throws Exception;
	public ComboBean getCompanyForAdminEnjoy(int userUniqueId) throws Exception;
}
