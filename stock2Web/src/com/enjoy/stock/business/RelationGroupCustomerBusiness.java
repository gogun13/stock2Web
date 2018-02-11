package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.stock.bean.RelationGroupCustomerBean;

public interface RelationGroupCustomerBusiness {
	public ArrayList<RelationGroupCustomerBean> searchByCriteria(String tin) throws Exception;
	public void save(ArrayList<RelationGroupCustomerBean> voList,String tin,String cusGroupCodeForDelete) throws Exception;
	public int countCusGroupCodeInCustomer(String cusGroupCode, String tin) throws Exception;
}
