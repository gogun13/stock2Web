package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.stock.bean.ManageProductTypeBean;

public interface ProductTypeService {
	public ArrayList<ManageProductTypeBean> getProductTypeList(String tin) throws Exception;
	public void insertProductype(ManageProductTypeBean manageProductTypeBean) throws Exception;
	public void updateProductype(final ManageProductTypeBean vo)throws Exception;
	public ArrayList<ComboBean> productTypeNameList(String productTypeName, String tin) throws Exception;
	public String getProductTypeCode(String productTypeName, String tin) throws Exception;
	public int genId(String tin) throws Exception;
	public void producTypeSuspended(String productTypeCode,String tin)throws Exception;
}
