package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductQuanHistoryBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String hisCode;
	private String formRef;
	private String hisDate;
	private String hisDateFrom;
	private String hisDateTo;
	private String productType;
	private String productGroup;
	private String productCode;
	private String tin;
	private String quantityPlus;
	private String quantityMinus;
	private String quantityTotal;
	private String productName;
	private String productTypeName;
	private String productGroupName;
	
	public ProductQuanHistoryBean(){
		
		this.hisCode 			= "";
		this.formRef 			= "";
		this.hisDate 			= "";
		this.hisDateFrom 		= "";
		this.hisDateTo 			= "";
		this.productType 		= "";
		this.productGroup 		= "";
		this.productCode 		= "";
		this.tin 				= "";
		this.quantityPlus 		= "0.00";
		this.quantityMinus 		= "0.00";
		this.quantityTotal 		= "0.00";
		this.productName 		= "";
		this.productTypeName 	= "";
		this.productGroupName 	= "";
	}
}
