package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SummarySaleByProductReportBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String invoiceDate;
	private String invoiceDateFrom;
	private String invoiceDateTo;
	private String cusName;
	private String productName;
	private String quantity;
	private String price;
	private String tin;
	
	public SummarySaleByProductReportBean(){
		this.invoiceDate 		= "";
		this.invoiceDateFrom 	= "";
		this.invoiceDateTo 		= "";
		this.cusName 			= "";
		this.productName 		= "";
		this.quantity 			= "0";
		this.price				= "0.00";
		this.tin 				= "";
	}

}
