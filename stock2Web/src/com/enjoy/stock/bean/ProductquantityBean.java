package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductquantityBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productCode;
	private String tin;
	private String quantity;
	
	public ProductquantityBean(){
		this.productCode 				= "";
		this.tin						= "";
		this.quantity 					= "";
	}
}
