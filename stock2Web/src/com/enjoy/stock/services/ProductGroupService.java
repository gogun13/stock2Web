package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.stock.bean.ManageProductGroupBean;

public interface ProductGroupService {
	public ArrayList<ManageProductGroupBean> getProductGroupList(String tin, String productTypeCode) throws Exception;
	public void insertProducGroup(ManageProductGroupBean manageProductGroupBean) throws Exception;
	public void updateProductgroup(ManageProductGroupBean vo)throws Exception;
	public ArrayList<ComboBean> productGroupNameList(String productTypeName, String productGroupName, String tin, boolean flag) throws Exception;
	public String getProductGroupCode(String productTypeCode, String productGroupName, String tin) throws Exception;
	public int genId(String tin, String productTypeCode) throws Exception;
	public void cancelProductgroupByProductTypeCode(final String productTypeCode, final String tin)throws Exception;
	public void productGroupSuspended(String tin ,String productTypeCode ,String productGroupCode)throws Exception;
}
