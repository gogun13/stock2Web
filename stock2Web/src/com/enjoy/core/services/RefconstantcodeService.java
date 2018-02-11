package com.enjoy.core.services;

import java.util.ArrayList;

import com.enjoy.core.bean.RefconstantcodeBean;

public interface RefconstantcodeService {
	public ArrayList<RefconstantcodeBean> searchByCriteria(RefconstantcodeBean refconstantcodeBean) throws Exception;
	public void insertRefconstantcode(RefconstantcodeBean vo) throws Exception;
	public void updateRefconstantcode(RefconstantcodeBean vo) throws Exception;
	public void updateCodeDisplay(final RefconstantcodeBean vo) throws Exception;
	public String getCodeDisplay(String id, String tin) throws Exception;
	public ArrayList<RefconstantcodeBean> templateDefaultList() throws Exception;
	public RefconstantcodeBean getDetail(String id, String tin) throws Exception;
}
