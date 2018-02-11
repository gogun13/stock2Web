package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.enjoy.core.bean.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class RelationGroupCustomerBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String cusGroupCode;
	private String cusGroupName;
	private String groupSalePrice;
	private String cusGroupStatus;
	private String seq;
	private String rowStatus;
	private String tin;
	
	public RelationGroupCustomerBean(){
		this.cusGroupCode 		= "";
		this.cusGroupName 		= "";
		this.groupSalePrice 	= "";
		this.cusGroupStatus		= "";
		this.seq 				= "";
		this.rowStatus			= "";
		this.tin				= "";
	}
}
