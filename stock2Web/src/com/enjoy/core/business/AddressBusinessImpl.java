package com.enjoy.core.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.AddressBean;
import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.services.AddressService;
import com.enjoy.core.utils.EnjoyUtils;

@Service
public class AddressBusinessImpl extends AbstractBusiness implements AddressBusiness{
	private static final Logger logger = Logger.getLogger(AddressBusinessImpl.class);
	
	@Autowired
	AddressService addressService;

	@Override
	public ArrayList<ComboBean> provinceList(String provinceName)throws Exception {
		return addressService.provinceList(provinceName);
	}

	@Override
	public ArrayList<ComboBean> districtList(String provinceName,String districtName) throws Exception {
		return addressService.districtList(provinceName, districtName);
	}

	@Override
	public ArrayList<ComboBean> subdistrictList(String provinceName,String districtName, String subdistrictName)throws Exception {
		return addressService.subdistrictList(provinceName, districtName, subdistrictName);
	}

	@Override
	public String getProvinceName(String provinceId) throws Exception {
		return addressService.getProvinceName(provinceId);
	}

	@Override
	public String getDistrictName(String districtId) throws Exception {
		return addressService.getDistrictName(districtId);
	}

	@Override
	public String getSubdistrictName(String subdistrictId) throws Exception {
		return addressService.getSubdistrictName(subdistrictId);
	}

	@Override
	public AddressBean getAddressId(String provinceName, String districtName,String subdistrictName) throws Exception {
		logger.info("[getAddressId][Begin]");
		
		AddressBean addressBean = new AddressBean();
		String provinceId;
		String districtId;
		String subdistrictId;
		
		try{
			logger.info("[getAddressId] provinceName 	:: " + provinceName);
			logger.info("[getAddressId] districtName 	:: " + districtName);
			logger.info("[getAddressId] subdistrictName :: " + subdistrictName);
			
			provinceId 		= addressService.getProvinceId(provinceName);
			if(!EnjoyUtils.chkNull(provinceId))throw new EnjoyException("ระบุจังหวัดผิด");
			
			districtId 		= addressService.getDistrictId(provinceName, districtName);
			if(!EnjoyUtils.chkNull(districtId))throw new EnjoyException("ระบุอำเภอ/เขตผิด");
			
			subdistrictId 	= addressService.getSubdistrictId(provinceName, districtName, subdistrictName);
			if(!EnjoyUtils.chkNull(subdistrictId))throw new EnjoyException("ระบุตำบล/แขวงผิด");
			
			addressBean.setProvinceId(provinceId);
			addressBean.setDistrictId(districtId);
			addressBean.setSubdistrictId(subdistrictId);
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getAddressId][End]");
		}
		return addressBean;
	}

}
