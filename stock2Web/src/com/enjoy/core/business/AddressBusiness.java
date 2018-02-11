package com.enjoy.core.business;

import java.util.ArrayList;

import com.enjoy.core.bean.AddressBean;
import com.enjoy.core.bean.ComboBean;

public interface AddressBusiness {
	public ArrayList<ComboBean>provinceList(String provinceName)throws Exception;
	public ArrayList<ComboBean> districtList(String provinceName, String districtName)throws Exception;
	public ArrayList<ComboBean> subdistrictList(String provinceName, String districtName, String subdistrictName)throws Exception;
	public String getProvinceName(String provinceId) throws Exception;
	public String getDistrictName(String districtId) throws Exception;
	public String getSubdistrictName(String subdistrictId) throws Exception;
	public AddressBean getAddressId(String provinceName,String districtName,String subdistrictName) throws Exception;
}
