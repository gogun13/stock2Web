package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SummarySaleByCustomerReportBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productName;
	private String invoiceDate;
	private String invoiceDateFrom;
	private String invoiceDateTo;
	private String quantity;
	private String price;
	private String discount;
	private String cusName;
	private String cusCode;
	private String tin;
	
	public SummarySaleByCustomerReportBean(){
		this.productName 		= "";
		this.invoiceDate 		= "";
		this.invoiceDateFrom 	= "";
		this.invoiceDateTo 		= "";
		this.quantity 			= "0";
		this.price				= "0.00";
		this.discount 			= "0.00";
		this.cusName 			= "";
		this.cusCode			= "";
		this.tin				= "";
	}
}
