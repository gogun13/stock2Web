package com.enjoy.stock.bean;

import com.enjoy.core.bean.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceCreditMasterBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String invoiceCode;
	private String invoiceDate;
	private String invoiceDateForm;
	private String invoiceDateTo;
	private String invoiceType;
	private String cusCode;
	private String branchName;
	private String saleUniqueId;
	private String saleName;
	private String saleCommission;
	private String invoicePrice;
	private String invoicediscount;
	private String invoiceDeposit;
	private String invoiceVat;
	private String invoiceTotal;
	private String userUniqueId;
	private String invoiceCash;
	private String invoiceStatus;
	private String invoiceStatusDesc;
	private String cusFullName;
	private String invoiceTypeDesc;
	private String tin;
	private String remark;
	private String seqDis;
	
	public InvoiceCreditMasterBean(){
		this.invoiceCode 		= "";
		this.invoiceDate 		= "";
		this.invoiceDateForm 	= "";
		this.invoiceDateTo 		= "";
		this.invoiceType 		= "";
		this.cusCode 			= "";
		this.branchName 		= "";
		this.saleUniqueId 		= "";
		this.saleName			= "";
		this.saleCommission 	= "0.00";
		this.invoicePrice 		= "0.00";
		this.invoicediscount 	= "0.00";
		this.invoiceDeposit 	= "0.00";
		this.invoiceVat 		= "0.00";
		this.invoiceTotal 		= "0.00";
		this.userUniqueId 		= "";
		this.invoiceCash 		= "";
		this.invoiceStatus 		= "";
		this.invoiceStatusDesc	= "";
		this.cusFullName		= "";
		this.invoiceTypeDesc	= "";
		this.tin				= "";
		this.remark				= "";
		this.seqDis				= "1";
	}
}
