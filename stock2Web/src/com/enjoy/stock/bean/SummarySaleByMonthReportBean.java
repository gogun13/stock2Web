package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SummarySaleByMonthReportBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String invoiceCode;
	private String cusName;
	private String productName;
	private String quantity;
	private String price;
	private String tin;
	private String invoiceDate;
	private String invoiceDateFrom;
	private String invoiceDateTo;
	private String invoiceMonth;
	
	public SummarySaleByMonthReportBean(){
		this.invoiceCode 		= "";
		this.cusName 			= "";
		this.productName 		= "";
		this.quantity 			= "0";
		this.price 				= "0.00";
		this.tin 				= "";
		this.invoiceDate 		= "";
		this.invoiceDateFrom 	= "";
		this.invoiceDateTo 		= "";
		this.invoiceMonth		= "";
	}
}
