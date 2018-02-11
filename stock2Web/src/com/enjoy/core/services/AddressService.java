package com.enjoy.core.services;

import java.util.ArrayList;

import com.enjoy.core.bean.ComboBean;

public interface AddressService {
	public ArrayList<ComboBean>provinceList(String provinceName)throws Exception;
	public ArrayList<ComboBean> districtList(String provinceName, String districtName)throws Exception;
	public ArrayList<ComboBean> subdistrictList(String provinceName, String districtName, String subdistrictName) throws Exception;
	public String getProvinceName(String provinceId) throws Exception;
	public String getDistrictName(String districtId) throws Exception;
	public String getSubdistrictName(String subdistrictId) throws Exception;
	public String getProvinceId(String provinceName) throws Exception;
	public String getDistrictId(String provinceName,String districtName) throws Exception;
	public String getSubdistrictId(String provinceName,String districtName,String subdistrictName) throws Exception;
}
