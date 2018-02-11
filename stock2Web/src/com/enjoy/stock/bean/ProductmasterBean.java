package com.enjoy.stock.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.poi.ss.usermodel.Row;

import com.enjoy.core.bean.BaseDTO;
import com.enjoy.core.utils.ExcelField;


@Data
@EqualsAndHashCode(callSuper = true)
public class ProductmasterBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productCode;
	private String productName;
	private String productTypeCode;
	private String productTypeName;
	private String productGroupCode;
	private String productGroupName;
	private String unitCode;
	private String unitName;
	private String minQuan;
	private String costPrice;
	private String salePrice1;
	private String salePrice2;
	private String salePrice3;
	private String salePrice4;
	private String salePrice5;
	private String tin;
	private String rowStatus;
	private String seq;
	private String quantity;
	private ExcelField colA;
	private ExcelField colB;
	private ExcelField colC;
	private ExcelField colD;
	private ExcelField colE;
	private ExcelField colF;
	private ExcelField colG;
	private ExcelField colH;
	private ExcelField colI;
	private ExcelField colJ;
	private String seqDis;
	private String productCodeDis;
	private String productStatus;
	
	public ProductmasterBean(){
		this.productCode 		= "";
		this.productName 		= "";
		this.productTypeCode 	= "";
		this.productTypeName 	= "";
		this.productGroupCode 	= "";
		this.productGroupName 	= "";
		this.unitCode 			= "";
		this.unitName 			= "";
		this.minQuan 			= "0";
		this.costPrice 			= "0.00";
		this.salePrice1 		= "0.00";
		this.salePrice2 		= "0.00";
		this.salePrice3 		= "0.00";
		this.salePrice4 		= "0.00";
		this.salePrice5 		= "0.00";
		this.tin				= "";
		this.rowStatus			= "";
		this.seq				= "0";
		this.quantity			= "0";
		this.seqDis				= "1";
		this.productCodeDis		= "";
		this.productStatus		= "";
	}
	
	public ProductmasterBean(Row row){
		this.colA			= new ExcelField(row, ".*", 0);
		this.colB			= new ExcelField(row, ".*", 1);
		this.colC			= new ExcelField(row, ".*", 2);
		this.colD			= new ExcelField(row, ".*", 3);
		this.colE			= new ExcelField(row, ".*", 4);
		this.colF			= new ExcelField(row, ".*", 5);
		this.colG			= new ExcelField(row, ".*", 6);
		this.colH			= new ExcelField(row, ".*", 7);
		this.colI			= new ExcelField(row, ".*", 8);
		this.colJ			= new ExcelField(row, ".*", 9);
	}
}
