package com.enjoy.core.business;

import java.util.ArrayList;

import com.enjoy.core.bean.RelationUserAndCompanyBean;


public interface RelationUserAndCompanyBusiness {
	public ArrayList<RelationUserAndCompanyBean> searchByCriteria(RelationUserAndCompanyBean vo) throws Exception;
	public void save(ArrayList<RelationUserAndCompanyBean> 	relationUserAndCompanyList, String tin, String userUniqueIdForDelete) throws Exception;
}
