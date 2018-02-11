package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReciveOrderMasterBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String reciveNo;
	private String reciveDate;
	private String reciveType;
	private String creditDay;
	private String creditExpire;
	private String vendorCode;
	private String branchName;
	private String billNo;
	private String priceType;
	private String reciveStatus;
	private String reciveStatusDesc;
	private String userUniqueId;
	private String reciveAmount;
	private String reciveDiscount;
	private String reciveVat;
	private String reciveTotal;
	private String reciveDateFrom;
	private String reciveDateTo;
	private String usrName;
	private String tin;
	private String seqDis;
	private String remark;
	private String vendorName;
	
	public ReciveOrderMasterBean(){
		this.reciveNo 			= "";
		this.reciveDate 		= "";
		this.reciveType 		= "";
		this.creditDay 			= "";
		this.creditExpire 		= "";
		this.vendorCode 		= "";
		this.branchName 		= "";
		this.billNo 			= "";
		this.priceType 			= "";
		this.reciveStatus 		= "";
		this.reciveStatusDesc 	= "";
		this.userUniqueId 		= "";
		this.reciveAmount 		= "0.00";
		this.reciveDiscount 	= "0.00";
		this.reciveVat 			= "0";
		this.reciveTotal 		= "0.00";
		this.reciveDateFrom 	= "";
		this.reciveDateTo		= "";
		this.usrName			= "";
		this.tin				= "";
		this.seqDis				= "";
		this.remark			 	= "";
		this.vendorName			= "";
	}
}
