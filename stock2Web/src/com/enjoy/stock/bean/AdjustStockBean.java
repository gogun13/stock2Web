package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdjustStockBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String adjustNo;
	private String adjustDate;
	private String productCode;
	private String quanOld;
	private String quanNew;
	private String remark;
	private String productName;
	private String quantity;
	private String balanceVolume;
	private String unitName;
	private int lastOrder;
	private String tin;
	private String seq;
	private String rowStatus;
	
	public AdjustStockBean(){
		this.adjustNo 			= "";
		this.adjustDate 		= "";
		this.productCode 		= "";
		this.quanOld 			= "0";
		this.quanNew 			= "0";
		this.remark 			= "";
		this.productName 		= "";
		this.quantity 			= "0";
		this.balanceVolume 		= "0";
		this.unitName			= "";
		this.lastOrder			= 0;
		this.tin				= "";
		this.seq 				= "";
		this.rowStatus			= "";
	}
}
