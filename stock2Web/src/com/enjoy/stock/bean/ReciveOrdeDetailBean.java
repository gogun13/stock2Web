package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReciveOrdeDetailBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String reciveNo;
	private String seqDb;
	private String productCode;
	private String productName;
	private String inventory;
	private String quantity;
	private String unitCode;
	private String unitName;
	private String price;
	private String discountRate;
	private String costPrice;
	private String seq;
	private String rowStatus;
	private String tin;
	
	public ReciveOrdeDetailBean(){
		this.reciveNo 		= "";
		this.seqDb 			= "";
		this.productCode 	= "";
		this.productName	= "";
		this.inventory		= "";
		this.quantity 		= "0";
		this.unitCode 		= "";
		this.unitName 		= "";
		this.price 			= "0.00";
		this.discountRate 	= "0";
		this.costPrice 		= "0.00";
		this.seq 			= "";
		this.rowStatus		= "";
		this.tin			= "";
	}
}
