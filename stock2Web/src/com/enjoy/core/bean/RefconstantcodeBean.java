package com.enjoy.core.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RefconstantcodeBean extends BaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String tin;
	private String codeDisplay;
	private String codeNameTH;
	private String codeNameEN;
	private String rowStatus;
	private String flagYear;
	private String flagEdit;
	private String typeTB;
	private boolean flagYearBoolean;
	
	public RefconstantcodeBean(){
		this.id 				= "";
		this.tin 				= "";
		this.codeDisplay 		= "";
		this.codeNameTH			= "";
		this.codeNameEN 		= "";
		this.rowStatus			= "";
		this.flagYear			= "N";
		this.flagEdit			= "N";
		this.typeTB				= "";
		this.flagYearBoolean	= false;
	}
}
