package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.poi.ss.usermodel.Row;

import com.enjoy.core.bean.BaseDTO;
import com.enjoy.core.utils.ExcelField;

@Data
@EqualsAndHashCode(callSuper = true)
public class ManageProductGroupBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productTypeCode;
	private String productGroupCode;
	private String productGroupCodeDis;
	private String productGroupName;
	private String productGroupStatus;
	private String rowStatus;
	private String seq;
	private String tin;
	private ExcelField colA;
	private ExcelField colB;
	
	public ManageProductGroupBean(){
		this.productTypeCode 		= "";
		this.productGroupCode 		= "";
		this.productGroupCodeDis	= "";
		this.productGroupName 		= "";
		this.productGroupStatus 	= "";
		this.rowStatus 				= "";
		this.seq 					= "";
		this.tin					= "";
	}
	
	public ManageProductGroupBean(Row row){
		this.colA			= new ExcelField(row, ".*", 0);
		this.colB			= new ExcelField(row, ".*", 1);
	}
}
