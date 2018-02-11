package com.enjoy.core.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AddressBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String provinceId;
	private String districtId;
	private String subdistrictId;
	private String provinceName;
	private String districtName;
	private String subdistrictName;
	
	public AddressBean(){
		this.provinceId			= "";
		this.districtId			= "";
		this.subdistrictId		= "";
		this.provinceName		= "";
		this.districtName		= "";
		this.subdistrictName	= "";
		
	}
	
}
