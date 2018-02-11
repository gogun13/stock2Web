package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SummarySaleByDayReportBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String invoiceCode;
	private String cusName;
	private String invoiceDate;
	private String invoiceDateFrom;
	private String invoiceDateTo;
	private String invoiceTotal;
	private String remark;
	private String tin;
	
	public SummarySaleByDayReportBean(){
		this.invoiceCode 		= "";
		this.cusName 			= "";
		this.invoiceDate 		= "";
		this.invoiceDateFrom 	= "";
		this.invoiceDateTo 		= "";
		this.invoiceTotal 		= "0.00";
		this.remark				= "";
		this.tin 				= "";
	}
}
