package com.enjoy.core.bean;

import com.enjoy.core.utils.EnjoyUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyDetailsBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tin;
	private String companyName;
	private String branchName;
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
	private String remark;
	private String companyStatus;
	private String companyStatusName;
	private String seqDis;
	
	public CompanyDetailsBean(){
		this.tin 				= "";
		this.companyName 		= "";
		this.branchName 		= "";
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
		this.remark 			= "";
		this.companyStatus 		= "";
		this.companyStatusName 	= "";
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
