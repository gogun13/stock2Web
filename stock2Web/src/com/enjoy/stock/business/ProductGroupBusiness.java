package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.stock.bean.ManageProductGroupBean;


public interface ProductGroupBusiness {
	public ArrayList<ManageProductGroupBean> getProductGroupList(String tin, String productTypeCode) throws Exception;
	public ArrayList<ComboBean> productGroupNameList(String productTypeName, String productGroupName, String tin, boolean flag) throws Exception;
	public String getProductGroupCode(String productTypeCode, String productGroupName, String tin) throws Exception;
	public void cancelProductgroupByProductTypeCode(final String productTypeCode, final String tin)throws Exception;
	public void save(ArrayList<ManageProductGroupBean> 	productGroupList, String tin,String productTypeCode,String productGroupCodeForDelete) throws Exception;
}
