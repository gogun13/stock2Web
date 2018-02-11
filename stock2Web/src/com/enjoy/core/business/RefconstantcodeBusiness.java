package com.enjoy.core.business;

import java.util.ArrayList;

import com.enjoy.core.bean.RefconstantcodeBean;


public interface RefconstantcodeBusiness {
	public ArrayList<RefconstantcodeBean> searchByCriteria(RefconstantcodeBean refconstantcodeBean) throws Exception;
	public void updateRefconstantcode(ArrayList<RefconstantcodeBean> list) throws Exception;
	public void updateCodeDisplay(ArrayList<RefconstantcodeBean> list) throws Exception;
	public String getCodeDisplay(String id, String tin) throws Exception;
}
