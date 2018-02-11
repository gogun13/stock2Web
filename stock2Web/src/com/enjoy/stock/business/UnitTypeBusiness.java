package com.enjoy.stock.business;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.stock.bean.ManageUnitTypeBean;

public interface UnitTypeBusiness {
	public ArrayList<ManageUnitTypeBean> getUnitTypeList(String tin) throws Exception;
	public ArrayList<ComboBean> unitNameList(String unitName, String tin) throws Exception;
	public String getUnitCode(String unitName, String tin) throws Exception;
	public void save(ArrayList<ManageUnitTypeBean> voList, String tin, String unitCodeForDelete)throws Exception;
	public int countUnitCodeInProduct(String unitCode, String tin) throws Exception;
}
