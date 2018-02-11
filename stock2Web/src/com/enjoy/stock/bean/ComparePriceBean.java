package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComparePriceBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productCode;
	private String productName;
	private String vendorCode;
	private String vendorName;
	private String branchName;
	private String seq;
	private String seqDb;
	private String quantity;
	private String price;
	private String rowStatus;
	private String tin;
	private String discountRate;
	
	public ComparePriceBean(){
		this.productCode 		= "";
		this.productName 		= "";
		this.vendorCode 		= "";
		this.vendorName 		= "";
		this.branchName 		= "";
		this.seq 				= "";
		this.seqDb				= "";
		this.quantity 			= "0";
		this.price 				= "0.00";
		this.rowStatus			= "";
		this.tin				= "";
		this.discountRate		= "0";
	}
}
