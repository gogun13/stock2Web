package com.enjoy.stock.actions;

import java.io.File;
import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.actions.ActionControl;
import com.enjoy.core.actions.BaseAction;
import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.core.utils.ExcelUtil;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.business.ProductBusiness;
import com.enjoy.stock.business.ProductGroupBusiness;
import com.enjoy.stock.business.ProductTypeBusiness;
import com.enjoy.stock.business.UnitTypeBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultiManageProductAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MultiManageProductAction.class);
	
	private String								indexRow;
	private ArrayList<ProductmasterBean> 	productList;
	private ProductmasterBean productmasterBean;
	private File uploadedFile;
	private String productCodeForDelete;
	private String hidProductJsonList;
	
	@Autowired
	ProductTypeBusiness productTypeBusiness;
	@Autowired
	ProductGroupBusiness productGroupBusiness;
	@Autowired
	UnitTypeBusiness unitTypeBusiness;
	@Autowired
	ProductBusiness productBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	productCodeForDelete 	= "";
    	productList 			= null;
    	productmasterBean		= new ProductmasterBean();
    	hidProductJsonList		= "";
    	
		return SUCCESS;
	}
    
    public String onSearch() throws Exception {
    	logger.info("[onSearch][Begin]");
    	
    	String	productTypeCode			= null;
		String	productGroupCode		= null;
		String	unitCode				= null;
    	
    	try{
    		logger.info("[onSearch] productTypeName 	:: " + productmasterBean.getProductTypeName());
    		logger.info("[onSearch] productGroupName 	:: " + productmasterBean.getProductGroupName());
    		logger.info("[onSearch] unitName 			:: " + productmasterBean.getUnitName());
    		
    		productTypeCode		= productTypeBusiness.getProductTypeCode(productmasterBean.getProductTypeName(), getCurrentUser().getTin());
    		if(!EnjoyUtils.chkNull(productTypeCode)){
				throw new EnjoyException("หมวดสินค้า"+productmasterBean.getProductTypeName()+"ไม่มีอยู่ในระบบกรุณาตรวจสอบ");
			}
    		
    		productGroupCode	= productGroupBusiness.getProductGroupCode(productTypeCode, productmasterBean.getProductGroupName(), getCurrentUser().getTin());
    		if(!EnjoyUtils.chkNull(productGroupCode)){
				throw new EnjoyException("หมู่สินค้า"+productmasterBean.getProductGroupName()+"ไม่มีอยู่ในระบบกรุณาตรวจสอบ");
			}
    		
    		unitCode			= unitTypeBusiness.getUnitCode(productmasterBean.getUnitName(), getCurrentUser().getTin());
    		if(!EnjoyUtils.chkNull(unitCode)){
				throw new EnjoyException("หน่วยสินค้า"+productmasterBean.getUnitName()+"ไม่มีอยู่ในระบบกรุณาตรวจสอบ");
			}
			
    		productCodeForDelete 	= "";
    		hidProductJsonList		= "";
    		
    		productmasterBean.setTin(getCurrentUser().getTin());
    		productmasterBean.setProductTypeCode(productTypeCode);
    		productmasterBean.setProductGroupCode(productGroupCode);
    		productmasterBean.setUnitCode(unitCode);
    		
    		productList = productBusiness.getMultiManageProduct(productmasterBean);
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[onSearch][End]");
    	}
    	return SUCCESS;
    }
    
    public String newRecord() throws Exception{
		logger.info("[newRecord][Begin]");
		
		ProductmasterBean	bean	= new ProductmasterBean();
		
		try{
			
			if(productList==null){
				productList = new ArrayList<ProductmasterBean>();
			}
			bean.setProductStatus("A");
			bean.setRowStatus(Constants.NEW);
			
			productList.add(bean);
			
			logger.info("[newRecord] size :: " + productList.size());
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[newRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String newRecordFromExcelFile() throws Exception{
		logger.info("[newRecordFromExcelFile][Begin]");
		
		ProductmasterBean	bean			= null;
		JSONParser 			parser 			= new JSONParser();
		JSONObject 			jsonObject 		= null;
		JSONArray			productJsonList	= null;
		JSONObject 			productJson 	= null;
		
		try{
			
			logger.info("[newRecordFromExcelFile] hidProductJsonList :: " + hidProductJsonList);
			
			jsonObject 		= (JSONObject) parser.parse(hidProductJsonList);
			productJsonList	= (JSONArray) jsonObject.get("productList");
			
			if(productList==null){
				productList = new ArrayList<ProductmasterBean>();
			}
			
			for(int i=0;i<productJsonList.size();i++){
				productJson = (JSONObject) productJsonList.get(i);
				bean		= new ProductmasterBean();
				
				bean.setProductStatus("A");
				bean.setRowStatus(Constants.NEW);
				bean.setProductCodeDis((String)productJson.get("productCodeDis"));
				bean.setProductName((String)productJson.get("productName"));
				bean.setMinQuan((String)productJson.get("minQuan"));
				bean.setCostPrice((String)productJson.get("costPrice"));
				bean.setSalePrice1((String)productJson.get("salePrice1"));
				bean.setSalePrice2((String)productJson.get("salePrice2"));
				bean.setSalePrice3((String)productJson.get("salePrice3"));
				bean.setSalePrice4((String)productJson.get("salePrice4"));
				bean.setSalePrice5((String)productJson.get("salePrice5"));
				bean.setQuantity((String)productJson.get("quantity"));
				
				productList.add(bean);
			}
			
			logger.info("[newRecordFromExcelFile] size :: " + productList.size());
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[newRecordFromExcelFile][End]");
		}
		
		return SEARCH;
	}
    
    public String deleteRecord() throws Exception{
		logger.info("[deleteRecord][Begin]");
		
		try{
			logger.info("[deleteRecord] indexRow :: " + indexRow);
			
			for(int i=0;i<productList.size();i++){
				ProductmasterBean vo = productList.get(i);
				
				if(vo.getSeq().equals(indexRow)){
					if(!Constants.NEW.equals(vo.getRowStatus())){
						if(!EnjoyUtils.chkNull(productCodeForDelete)){
							productCodeForDelete = vo.getProductCode();
						}else{
							productCodeForDelete += "," + vo.getProductCode();
						}
					}
					productList.remove(i);
					break;
				}
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[deleteRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String save()throws Exception {
    	logger.info("[save][Begin]");
		
    	ProductmasterBean		bean		= null;
    	ProductmasterBean		beanTemp	= null;
    	int						cou			= 0;
    	
		try{
			
			for(int i=0;i<productList.size();i++){
				bean = productList.get(i);
				for(int j=(i+1);j<productList.size();j++){
					beanTemp = productList.get(j);
					if(bean.getProductCodeDis().equals(beanTemp.getProductCodeDis())){
						throw new EnjoyException("รหัสสินค้า " + bean.getProductCodeDis() + "ซ้ำ");
					}
					
					if(bean.getProductName().equals(beanTemp.getProductName())){
						throw new EnjoyException("ชื่อสินค้า " + bean.getProductName() + "ซ้ำ");
					}
				}
				
				cou = productBusiness.checkDupProductCode(bean.getProductCodeDis()
														, getCurrentUser().getTin()
														, bean.getProductCode()
														, bean.getRowStatus());
				if(cou > 0){
					throw new EnjoyException("รหัสสินค้า " + bean.getProductCodeDis() + " มีในระบบแล้ว");
				}
				
				cou = productBusiness.checkDupProductName(bean.getProductName()
														, bean.getProductCode()
														, getCurrentUser().getTin()
														, bean.getRowStatus());
				if(cou > 0){
					throw new EnjoyException("ชื่อสินค้า " + bean.getProductName() + " มีในระบบแล้ว");
				}
				
				
			}
			
			productBusiness.saveMultiProduct(productmasterBean, productList, getCurrentUser().getTin(), productCodeForDelete);
			
			onSearch();
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
		return SEARCH;
    }
    
    public String uploadExcelFileFile(){
    	logger.info("[uploadExcelFileFile][Begin]");
		
    	Workbook 				wb						= null;
    	Sheet 					sheet					= null;
		String 					sheetName 				= "";
		Row[]   				rowArray  				= null;
		ProductmasterBean		bean					= null;
		String					productCodeDis			= null;
		String					productName				= null;
		String					minQuan					= null;
		String					costPrice				= null;
		String					salePrice1				= null;
		String					salePrice2				= null;
		String					salePrice3				= null;
		String					salePrice4				= null;
		String					salePrice5				= null;
		String					quantity				= null;
		JSONObject 				obj 					= new JSONObject();
		JSONArray 				jSONArray 				= new JSONArray();
		JSONObject 				objDetail 				= null;
    	
		try{
			
			logger.info("[uploadExcelFileFile] uploadedFile :: " + uploadedFile);
			
			wb             = ExcelUtil.getWorkbook(uploadedFile);
    		
    		sheetName = wb.getSheetName(0);
			sheet     = wb.getSheetAt(0);
			rowArray  = ExcelUtil.getAllRows(sheet);
			
			logger.info("[uploadExcelFileFile] sheetName :: " + sheetName);
			
			for(int j=1;j<rowArray.length;j++){
				bean 				= new ProductmasterBean(rowArray[j]);
				productCodeDis		= bean.getColA().getValue();
				productName			= bean.getColB().getValue();
				minQuan				= bean.getColC().getValue();
				costPrice			= bean.getColD().getValue();
				salePrice1			= bean.getColE().getValue();
				salePrice2			= bean.getColF().getValue();
				salePrice3			= bean.getColG().getValue();
				salePrice4			= bean.getColH().getValue();
				salePrice5			= bean.getColI().getValue();
				quantity			= bean.getColJ().getValue();
				objDetail 			= new JSONObject();
				
				objDetail.put("productCodeDis"	, productCodeDis);
				objDetail.put("productName"		, productName);
				objDetail.put("minQuan"			, minQuan);
				objDetail.put("costPrice"		, costPrice);
				objDetail.put("salePrice1"		, salePrice1);
				objDetail.put("salePrice2"		, salePrice2);
				objDetail.put("salePrice3"		, salePrice3);
				objDetail.put("salePrice4"		, salePrice4);
				objDetail.put("salePrice5"		, salePrice5);
				objDetail.put("quantity"		, quantity);
				
				jSONArray.add(objDetail);
				
			}
			
			uploadedFile.delete();
			
			obj.put(Constants.STATUS	, Constants.SUCCESS);
			obj.put("productList"	, jSONArray);
			
		}catch(Exception e){
			logger.error("uploadExcelFileFile",e);
			
			obj.put(Constants.STATUS, Constants.ERROR);
			obj.put(Constants.MSG	, "uploadFile is error");
		}finally{
			writeMSG(obj.toString());
			
			logger.info("[uploadExcelFileFile][End]");
		}
    	return null;
    }
    
    @Override
	public String autoComplete()throws Exception {
    	logger.info("[autoComplete][Begin]");
    	
    	ArrayList<ComboBean> 	list 		= null;
    	JSONArray 				jSONArray 	= new JSONArray();
        JSONObject 				objDetail 	= new JSONObject();
    	
    	try{
    		logger.info("[autoComplete] autoCompleteName :: " + getAutoCompleteName());
    		logger.info("[autoComplete] autoCompParamter :: " + getAutoCompParamter());
    		
    		if("productmasterBean.productTypeName".equals(getAutoCompleteName())){
    			list = productTypeBusiness.productTypeNameList(getAutoCompParamter(), getCurrentUser().getTin());
    		}else if("productmasterBean.productGroupName".equals(getAutoCompleteName())){
    			list = productGroupBusiness.productGroupNameList(productmasterBean.getProductTypeName(), getAutoCompParamter(), getCurrentUser().getTin(), false);
    		}else if("productmasterBean.unitName".equals(getAutoCompleteName())){
    			list = unitTypeBusiness.unitNameList(getAutoCompParamter(), getCurrentUser().getTin());
    		}
    		
    		if(list!=null && !list.isEmpty()){
	    		for(ComboBean bean:list){
	 			   objDetail 		= new JSONObject();
	 			   
	 			   objDetail.put("id"			,bean.getCode());
	 			   objDetail.put("value"		,bean.getDesc());
	 			   
	 			   jSONArray.add(objDetail);
	 		   	}
    		}
    		
    		writeMSG(jSONArray.toString());
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[autoComplete][End]");
    	}
    	return null;
    }
    
    
}
