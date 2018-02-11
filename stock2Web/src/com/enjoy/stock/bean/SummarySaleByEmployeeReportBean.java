package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SummarySaleByEmployeeReportBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String invoiceCode;
	private String invoiceDate;
	private String invoiceDateFrom;
	private String invoiceDateTo;
	private String cusName;
	private String invoiceTotal;
	private String saleCommission;
	private String tin;
	private String saleName;
	
	public SummarySaleByEmployeeReportBean(){
		this.invoiceCode 		= "";
		this.invoiceDate 		= "";
		this.invoiceDateFrom 	= "";
		this.invoiceDateTo 		= "";
		this.cusName 			= "";
		this.invoiceTotal 		= "0.00";
		this.saleCommission		= "0.00";
		this.tin 				= "";
		this.saleName			= "";
	}
}
