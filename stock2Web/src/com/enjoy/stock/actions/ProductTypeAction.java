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
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.core.utils.ExcelUtil;
import com.enjoy.stock.bean.ManageProductTypeBean;
import com.enjoy.stock.business.ProductTypeBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductTypeAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProductTypeAction.class);
	
	private String								indexRow;
	private ArrayList<ManageProductTypeBean> 	productTypeList;
	private File uploadedFile;
	private String hidProductTypeJsonList;
	private String productTypeCodeForDelete;
	
	@Autowired
	ProductTypeBusiness productTypeBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	productTypeCodeForDelete 	= "";
    	productTypeList 			= productTypeBusiness.getProductTypeList(getCurrentUser().getTin());
    	hidProductTypeJsonList		= "";
    	
		return SUCCESS;
	}
    
    public String newRecord() throws Exception{
		logger.info("[newRecord][Begin]");
		
		ManageProductTypeBean	bean	= new ManageProductTypeBean();
		
		try{
			
			if(productTypeList==null){
				productTypeList = new ArrayList<ManageProductTypeBean>();
			}
			bean.setProductTypeStatus("A");
			bean.setRowStatus(Constants.NEW);
			
			productTypeList.add(bean);
			
			logger.info("[newRecord] size :: " + productTypeList.size());
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[newRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String newRecordFromExcelFile() throws Exception{
		logger.info("[newRecordFromExcelFile][Begin]");
		
		ManageProductTypeBean	bean				= null;
		JSONParser 				parser 				= new JSONParser();
		JSONObject 				jsonObject 			= null;
		JSONArray				productTypeJsonList	= null;
		JSONObject 				productTypeJson 	= null;
		
		try{
			
			logger.info("[newRecordFromExcelFile] hidProductTypeJsonList 	:: " + hidProductTypeJsonList);
			
			jsonObject 			= (JSONObject) parser.parse(hidProductTypeJsonList);
			productTypeJsonList	= (JSONArray) jsonObject.get("productTypeList");
			
			if(productTypeList==null){
				productTypeList = new ArrayList<ManageProductTypeBean>();
			}
			
			for(int i=0;i<productTypeJsonList.size();i++){
				productTypeJson = (JSONObject) productTypeJsonList.get(i);
				bean			= new ManageProductTypeBean();
				
				bean.setProductTypeStatus("A");
				bean.setRowStatus(Constants.NEW);
				bean.setProductTypeCodeDis((String)productTypeJson.get("productTypeCodeDis"));
				bean.setProductTypeName((String)productTypeJson.get("productTypeName"));
				
				productTypeList.add(bean);
			}
			
			logger.info("[newRecordFromExcelFile] size :: " + productTypeList.size());
			
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
			
			for(int i=0;i<productTypeList.size();i++){
				ManageProductTypeBean vo = productTypeList.get(i);
				
				if(vo.getSeq().equals(indexRow)){
					if(!Constants.NEW.equals(vo.getRowStatus())){
						if(!EnjoyUtils.chkNull(productTypeCodeForDelete)){
							productTypeCodeForDelete = vo.getProductTypeCode();
						}else{
							productTypeCodeForDelete += "," + vo.getProductTypeCode();
						}
					}
					productTypeList.remove(i);
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
		
    	ManageProductTypeBean		bean		= null;
 	   	ManageProductTypeBean		beanTemp	= null;
    	
		try{
			
			for(int i=0;i<productTypeList.size();i++){
				bean = productTypeList.get(i);
				for(int j=(i+1);j<productTypeList.size();j++){
					beanTemp = productTypeList.get(j);
					if(bean.getProductTypeCodeDis().equals(beanTemp.getProductTypeCodeDis())){
						throw new EnjoyException("รหัสหมวดสินค้าห้ามซ้ำ");
					}
					
					if(bean.getProductTypeName().equals(beanTemp.getProductTypeName())){
						throw new EnjoyException("ชื่อหมวดสินค้าห้ามซ้ำ");
					}
				}
			}
			
			productTypeBusiness.save(productTypeList, getCurrentUser().getTin(),productTypeCodeForDelete);
			
			success();
			
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
		ManageProductTypeBean	manageProductTypeBean	= null;
		String					productTypeCodeDis		= null;
		String					productTypeName			= null;
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
				manageProductTypeBean 	= new ManageProductTypeBean(rowArray[j]);
				productTypeCodeDis		= manageProductTypeBean.getColA().getValue();
				productTypeName			= manageProductTypeBean.getColB().getValue();
				objDetail 				= new JSONObject();
				
				logger.info("[uploadExcelFileFile] productTypeCodeDis :: " + productTypeCodeDis);
				logger.info("[uploadExcelFileFile] productTypeName 	:: " + productTypeName);
				
				objDetail.put("productTypeCodeDis"	, productTypeCodeDis);
				objDetail.put("productTypeName"		, productTypeName);
				
				jSONArray.add(objDetail);
				
			}
			
			uploadedFile.delete();
			
			obj.put(Constants.STATUS	, Constants.SUCCESS);
			obj.put("productTypeList"	, jSONArray);
			
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
    	return null;
    }
    
    
}
