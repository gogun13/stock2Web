package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.poi.ss.usermodel.Row;

import com.enjoy.core.bean.BaseDTO;
import com.enjoy.core.utils.ExcelField;


@Data
@EqualsAndHashCode(callSuper = true)
public class ManageProductTypeBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productTypeCode;
	private String productTypeName;
	private String productTypeStatus;
	private String rowStatus;
	private String seq;
	private String tin;
	private String productTypeCodeDis;
	private ExcelField colA;
	private ExcelField colB;
	
	
	public ManageProductTypeBean(){
		this.productTypeCode 		= "";
		this.productTypeName 		= "";
		this.productTypeStatus 		= "";
		this.rowStatus 				= "";
		this.seq 					= "";
		this.tin					= "";
		this.productTypeCodeDis		= "";
	}
	
	public ManageProductTypeBean(Row row){
		this.colA			= new ExcelField(row, ".*", 0);
		this.colB			= new ExcelField(row, ".*", 1);
	}
}
