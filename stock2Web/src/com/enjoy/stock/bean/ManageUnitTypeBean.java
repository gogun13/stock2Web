package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ManageUnitTypeBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitCode;
	private String unitName;
	private String unitStatus;
	private String rowStatus;
	private String seq;
	private String tin;
	
	public ManageUnitTypeBean(){
		this.unitCode 		= "";
		this.unitName 		= "";
		this.unitStatus 	= "";
		this.rowStatus 		= "";
		this.seq 			= "";
		this.tin			= "";
	}
}
