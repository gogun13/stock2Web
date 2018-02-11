package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductdetailBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productCode;
	private String quanDiscount;
	private String discountRate;
	private String seqDb;
	private String rowStatus;
	private String seq;
	private String startDate;
	private String expDate;
	private String tin;
	private String availPageFlag;
	
	public ProductdetailBean(){
		this.productCode 	= "";
		this.quanDiscount 	= "";
		this.discountRate 	= "";
		this.seqDb 			= "";
		this.rowStatus 		= "";
		this.seq 			= "";
		this.startDate 		= "";
		this.expDate 		= "";
		this.tin			= "";
		this.availPageFlag	= "";
	}
}
