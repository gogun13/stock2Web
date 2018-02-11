package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.stock.bean.RelationGroupCustomerBean;

public interface RelationGroupCustomerService {
	public ArrayList<RelationGroupCustomerBean> searchByCriteria(String tin) throws Exception;
	public void insertRelationGroupCustomer(RelationGroupCustomerBean relationGroupCustomerBean) throws Exception;
	public int genId(String tin) throws Exception;
	public void updateRelationGroupCustomer(RelationGroupCustomerBean vo) throws Exception;
	public void rejectRelationGroupCustomer(String cusGroupCode ,String tin)throws Exception;
}
