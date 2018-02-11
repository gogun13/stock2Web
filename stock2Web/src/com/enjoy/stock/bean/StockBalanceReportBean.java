package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class StockBalanceReportBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productTypeName;
	private String productGroupName;
	private String productName;
	private String quantity;
	private String productType;
	private String productGroup;
	private String tin;
	
	public StockBalanceReportBean(){
		this.productTypeName 	= "";
		this.productGroupName 	= "";
		this.productName 		= "";
		this.quantity 			= "0";
		this.productType 		= "";
		this.productGroup 		= "";
		this.tin 				= "";
	}
}
