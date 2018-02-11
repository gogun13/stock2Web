package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;


@Data
@EqualsAndHashCode(callSuper = true)
public class HistoryPurchasedByDealerReportBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tin;
	private String companyName;
	private String vendorCode;
	private String vendorName;
	private String branchName;
	private String reciveDate;
	private String reciveDateFrom;
	private String reciveDateTo;
	private String reciveTotal;
	private String reciveDiscount;
	private String reciveNo;
	
	public HistoryPurchasedByDealerReportBean(){
		this.tin 				= "";
		this.companyName 		= "";
		this.vendorCode 		= "";
		this.vendorName 		= "";
		this.branchName 		= "";
		this.reciveDate 		= "";
		this.reciveDateFrom 	= "";
		this.reciveDateTo 		= "";
		this.reciveTotal		= "0.00";
		this.reciveDiscount 	= "0.00";
		this.reciveNo			= "";
	}
}
