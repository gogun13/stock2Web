package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.stock.bean.ManageUnitTypeBean;

public interface UnitTypeService {
	public ArrayList<ManageUnitTypeBean> getUnitTypeList(String tin) throws Exception;
	public void insertUnitType(ManageUnitTypeBean manageUnitTypeBean) throws Exception;
	public void updateUnitType(final ManageUnitTypeBean manageUnitTypeBean)throws Exception;
	public ArrayList<ComboBean> unitNameList(String unitName, String tin) throws Exception;
	public String getUnitCode(String unitName, String tin) throws Exception;
	public int genId(String tin) throws Exception;
	public void unitTypeSuspended(String unitCode,String tin)throws Exception;
}
