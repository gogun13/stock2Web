package com.enjoy.stock.bean;

import com.enjoy.core.bean.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyVendorBean extends BaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String vendorCode;
	private String vendorName;
	private String tin;
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
	private String tinCompany;
	private String seqDis;
	
	public CompanyVendorBean(){
		this.vendorCode 				= "";
		this.vendorName 				= "";
		this.tin						= "";
		this.branchName 				= "";
		this.buildingName 				= "";
		this.houseNumber 				= "";
		this.mooNumber 					= "";
		this.soiName 					= "";
		this.streetName 				= "";
		this.subdistrictName 			= "";
		this.subdistrictCode 			= "";
		this.districtName 				= "";
		this.districtCode 				= "";
		this.provinceName 				= "";
		this.provinceCode 				= "";
		this.postCode 					= "";
		this.tel 						= "";
		this.fax 						= "";
		this.email 						= "";
		this.remark 					= "";
		this.tinCompany					= "";
		this.seqDis						= "1";
	}
	
	public String getAddress() {
		
		String address = "";
		
		try{
			
			if(!houseNumber.equals("")){
				address += houseNumber;
			}
			
			if(!mooNumber.equals("")){
				address += " หมู่ที่ " + mooNumber;
			}
			
			if(!buildingName.equals("")){
				address += " อาคาร " + buildingName;
			}
			
			if(!soiName.equals("")){
				address += " ซอย" + soiName;
			}
			
			if(!streetName.equals("")){
				address += " ถนน" + streetName;
			}
			
			if(subdistrictName!=null && !subdistrictName.equals("")){
				if(provinceCode.equals("10")){
					address += " แขวง " + subdistrictName;
				}else{
					address += " ตำบล " + subdistrictName;
				}
			}
			
			if(!districtName.equals("")){
				if(provinceCode.equals("10")){
					address += " เขต " + districtName;
				}else{
					address += " อำเภอ " + districtName;
				}
			}
			
			if(!provinceName.equals("")){
				address += " " + provinceName;
			}
			
			if(!postCode.equals("")){
				address += " " + postCode;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return address;
	}
}
