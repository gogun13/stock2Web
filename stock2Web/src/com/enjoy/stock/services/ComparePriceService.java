package com.enjoy.stock.services;

import java.util.ArrayList;

import com.enjoy.stock.bean.ComparePriceBean;
import com.enjoy.stock.bean.ComparePriceRemarkBean;

public interface ComparePriceService {
	public ArrayList<ComparePriceBean> searchByCriteria(String tin, String productCode) throws Exception;
	public void insertCompareprice(ComparePriceBean vo) throws Exception;
	public int couVenderInThisProduct(String productCode, String vendorCode, String tin) throws Exception;
	public int genId(String productCode, String tin) throws Exception;
	public void deleteCompareprice(String productCode, String tin) throws Exception;
	public ComparePriceBean getPrice(ComparePriceBean vo) throws Exception;
	public void insertComparepriceRemark(ComparePriceRemarkBean vo) throws Exception;
	public void updateComparepriceRemark(final ComparePriceRemarkBean vo)throws Exception;
	public int checkForInsertRemark(String productCode, String tin) throws Exception;
	public String getRemark(String productCode, String tin) throws Exception;
}
