package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class HistoryPurchasedByProductReportBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productName;
	private String vendorName;
	private String reciveNo;
	private String reciveDate;
	private String reciveDateFrom;
	private String reciveDateTo;
	private String costPrice;
	private String discountRate;
	private String tin;
	private String productCode;
	
	public HistoryPurchasedByProductReportBean(){
		this.productName 		= "";
		this.vendorName 		= "";
		this.reciveNo 			= "";
		this.reciveDate 		= "";
		this.reciveDateFrom 	= "";
		this.reciveDateTo 		= "";
		this.reciveDateFrom 	= "";
		this.costPrice 			= "0.00";
		this.discountRate		= "0.00";
		this.tin 				= "";
		this.productCode		= "";
	}
}
