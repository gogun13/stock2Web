package com.enjoy.stock.bean;

import com.enjoy.core.bean.BaseDTO;
import com.enjoy.core.utils.EnjoyUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDetailsBean extends BaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String cusCode;
	private String cusName;
	private String cusSurname;
	private String branchName;
	private String cusGroupCode;
	private String sex;
	private String idType;
	private String idNumber;
	private String birthDate;
	private String religion;
	private String job;
	private String buildingName;
	private String houseNumber;
	private String mooNumber;
	private String soiName;
	private String streetName;
	private String subdistrictName;
	private String subdistrictCode;
	private String districtName;
	private String districtCode;
	private String provinceName;
	private String provinceCode;
	private String postCode;
	private String tel;
	private String fax;
	private String email;
	private String cusStatus;
	private String startDate;
	private String expDate;
	private String point;
	private String remark;
	private String fullName;
	private String customerStatusName;
	private String groupSalePrice;
	private String tin;
	private String seqDis;
	
	public CustomerDetailsBean(){
		this.cusCode 			= "";
		this.cusName 			= "";
		this.cusSurname 		= "";
		this.branchName			= "";
		this.cusGroupCode		= "";
		this.sex 				= "";
		this.idType 			= "";
		this.idNumber 			= "";
		this.birthDate 			= "";
		this.religion 			= "";
		this.job 				= "";
		this.buildingName 		= "";
		this.houseNumber 		= "";
		this.mooNumber 			= "";
		this.soiName 			= "";
		this.streetName 		= "";
		this.subdistrictName 	= "";
		this.subdistrictCode 	= "";
		this.districtName 		= "";
		this.districtCode 		= "";
		this.provinceName 		= "";
		this.provinceCode 		= "";
		this.postCode 			= "";
		this.tel 				= "";
		this.fax 				= "";
		this.email 				= "";
		this.cusStatus 			= "";
		this.startDate 			= "";
		this.expDate 			= "";
		this.point 				= "";
		this.remark 			= "";
		this.fullName 			= "";
		this.customerStatusName = "";
		this.groupSalePrice		= "";
		this.tin				= "";
		this.seqDis				= "1";
	}
	
	public String getAddress() throws Exception{
		
		StringBuilder address         = new StringBuilder();
		
		try{
			
			if(EnjoyUtils.chkNull(houseNumber)){
				address.append(houseNumber);
			}
			
			if(EnjoyUtils.chkNull(mooNumber)){
				address.append(" หมู่ที่ " + mooNumber);
			}
			
			if(EnjoyUtils.chkNull(buildingName)){
				address.append(" อาคาร " + buildingName);
			}
			
			if(EnjoyUtils.chkNull(soiName)){
				address.append(" ซอย" + soiName);
			}
			
			if(EnjoyUtils.chkNull(streetName)){
				address.append(" ถนน" + streetName);
			}
			
			if(EnjoyUtils.chkNull(subdistrictName)){
				if(provinceCode.equals("10")){
					address.append(" แขวง " + subdistrictName);
				}else{
					address.append(" ตำบล " + subdistrictName);
				}
			}
			
			if(EnjoyUtils.chkNull(districtName)){
				if(provinceCode.equals("10")){
					address.append(" เขต " + districtName);
				}else{
					address.append(" อำเภอ " + districtName);
				}
			}
			
			if(EnjoyUtils.chkNull(provinceName)){
				address.append(" " + provinceName);
			}
			
			if(EnjoyUtils.chkNull(postCode)){
				address.append(" " + postCode);
			}
			
		}catch(Exception e){
			throw e;
		}
		
		return address.toString();
	}
}
