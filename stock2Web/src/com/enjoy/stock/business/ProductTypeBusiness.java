package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.stock.bean.ManageProductTypeBean;

public interface ProductTypeBusiness {
	public ArrayList<ManageProductTypeBean> getProductTypeList(String tin) throws Exception;
	public ArrayList<ComboBean> productTypeNameList(String productTypeName, String tin) throws Exception;
	public String getProductTypeCode(String productTypeName, String tin) throws Exception;
	public void save(ArrayList<ManageProductTypeBean> voList ,String tin,String productTypeCodeForDelete) throws Exception;
}
