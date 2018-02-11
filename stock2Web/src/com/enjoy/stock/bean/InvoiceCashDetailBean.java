package com.enjoy.stock.bean;

import com.enjoy.core.bean.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceCashDetailBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String invoiceCode;
	private String seqDb;
	private String productCode;
	private String productName;
	private String inventory;
	private String quantity;
	private String pricePerUnit;
	private String discount;
	private String price;
	private String seq;
	private String rowStatus;
	private String unitCode;
	private String unitName;
	private String quanDiscount;
	private String tin;
	
	public InvoiceCashDetailBean(){
		this.invoiceCode 	= "";
		this.seqDb 			= "";
		this.productCode 	= "";
		this.productName	= "";
		this.inventory		= "0";
		this.quantity		= "0";
		this.pricePerUnit	= "";
		this.discount		= "0";
		this.price			= "0.00";
		this.seq 			= "";
		this.rowStatus		= "";
		this.unitCode		= "";
		this.unitName		= "";
		this.quanDiscount	= "0";
		this.tin			= "";
	}
}
