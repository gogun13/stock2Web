package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;


@Data
@EqualsAndHashCode(callSuper = true)
public class AlertLowProductReportBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productTypeName;
	private String productGroupName;
	private String productName;
	private String minQuan;
	private String quantity;
	private String productType;
	private String productGroup;
	private String tin;
	
	public AlertLowProductReportBean(){
		this.productTypeName 	= "";
		this.productGroupName 	= "";
		this.productName 		= "";
		this.minQuan 			= "0";
		this.quantity 			= "0";
		this.productType 		= "";
		this.productGroup 		= "";
		this.tin 				= "";
	}
}
