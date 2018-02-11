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
import com.enjoy.stock.bean.ManageProductGroupBean;
import com.enjoy.stock.business.ProductGroupBusiness;
import com.enjoy.stock.business.ProductTypeBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductGroupAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProductGroupAction.class);
	
	private String								productTypeName;
	private String								productTypeCode;
	private String								indexRow;
	private ArrayList<ManageProductGroupBean> 	productGroupList;
	private File 								uploadedFile;
	private String								productGroupCodeForDelete;
	private String 								hidProductGroupJsonList;
	
	@Autowired
	ProductGroupBusiness productGroupBusiness;
	@Autowired
	ProductTypeBusiness productTypeBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	productTypeName				= "";
    	productTypeCode				= "";
    	indexRow					= "";
    	productGroupList 			= null;
    	hidProductGroupJsonList		= "";
    	productGroupCodeForDelete 	= "";
    	
    	
		return SUCCESS;
	}
    
    public String onSearch() throws Exception {
    	logger.info("[onSearch][Begin]");
    	
    	try{
    		productTypeCode	= productTypeBusiness.getProductTypeCode(productTypeName, getCurrentUser().getTin());
    		
    		logger.info("[onSearch] productTypeName 	:: " + productTypeName);
			
			if(!EnjoyUtils.chkNull(productTypeCode)){
				throw new EnjoyException("หมวดสินค้านี้ไม่มีในระบบกรุณาตรวจสอบ");
			}
			
			productGroupCodeForDelete 	= "";
			hidProductGroupJsonList		= "";
			productGroupList 			= productGroupBusiness.getProductGroupList(getCurrentUser().getTin(), productTypeCode);
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[onSearch][End]");
    	}
    	return SUCCESS;
    }
    
    public String newRecord() throws Exception{
		logger.info("[newRecord][Begin]");
		
		ManageProductGroupBean	bean	= new ManageProductGroupBean();
		
		try{
			
			if(productGroupList==null){
				productGroupList = new ArrayList<ManageProductGroupBean>();
			}
			
			bean.setProductGroupStatus("A");
			bean.setRowStatus(Constants.NEW);
			
			productGroupList.add(bean);
			
			logger.info("[newRecord] size :: " + productGroupList.size());
			
			
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[newRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String newRecordFromExcelFile() throws Exception{
		logger.info("[newRecordFromExcelFile][Begin]");
		
		ManageProductGroupBean	bean					= null;
		JSONParser 				parser 					= new JSONParser();
		JSONObject 				jsonObject 				= null;
		JSONArray				productGroupJsonList	= null;
		JSONObject 				productGroupJson 		= null;
		
		try{
			
			logger.info("[newRecordFromExcelFile] hidProductJsonList :: " + hidProductGroupJsonList);
			
			jsonObject 				= (JSONObject) parser.parse(hidProductGroupJsonList);
			productGroupJsonList	= (JSONArray) jsonObject.get("productGroupList");
			
			if(productGroupList==null){
				productGroupList = new ArrayList<ManageProductGroupBean>();
			}
			
			for(int i=0;i<productGroupJsonList.size();i++){
				productGroupJson 	= (JSONObject) productGroupJsonList.get(i);
				bean				= new ManageProductGroupBean();
				
				bean.setProductGroupStatus("A");
				bean.setRowStatus(Constants.NEW);
				bean.setProductGroupCodeDis((String)productGroupJson.get("productGroupCodeDis"));
				bean.setProductGroupName((String)productGroupJson.get("productGroupName"));
				
				productGroupList.add(bean);
			}
			
			logger.info("[newRecordFromExcelFile] size :: " + productGroupList.size());
			
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
			logger.info("[deleteRecord] indexRow 	:: " + indexRow);
			logger.info("[deleteRecord] size 		:: " + productGroupList.size());
			
			for(int i=0;i<productGroupList.size();i++){
				ManageProductGroupBean vo = productGroupList.get(i);
				
				if(indexRow.equals(vo.getSeq())){
					if(!Constants.NEW.equals(vo.getRowStatus())){
						if(!EnjoyUtils.chkNull(productGroupCodeForDelete)){
							productGroupCodeForDelete = vo.getProductGroupCode();
						}else{
							productGroupCodeForDelete += "," + vo.getProductGroupCode();
						}
					}
					
					productGroupList.remove(i);
					
					break;
				}
			}
			
		}catch(Exception e){
			logger.error("",e);
			throw e;
		}finally{
			logger.info("[deleteRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String save()throws Exception {
    	logger.info("[save][Begin]");
		
    	ManageProductGroupBean		bean						= null;
    	ManageProductGroupBean		beanTemp					= null;
    	
		try{
			for(int i=0;i<productGroupList.size();i++){
				bean = productGroupList.get(i);
				for(int j=(i+1);j<productGroupList.size();j++){
					beanTemp = productGroupList.get(j);
					
					if(bean.getProductGroupCodeDis().equals(beanTemp.getProductGroupCodeDis())){
						throw new EnjoyException("รหัสหมู่สินค้าห้ามซ้ำ");
					}
					
					if(bean.getProductGroupName().equals(beanTemp.getProductGroupName())){
						throw new EnjoyException("ชื่อหมู่สินค้าห้ามซ้ำ");
					}
					
				}
		   }
			
			productGroupBusiness.save(productGroupList, getCurrentUser().getTin(),productTypeCode,productGroupCodeForDelete);
			
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
		ManageProductGroupBean	manageProductGroupBean	= null;
		String					productGroupCodeDis		= null;
		String					productGroupName		= null;
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
				manageProductGroupBean 	= new ManageProductGroupBean(rowArray[j]);
				productGroupCodeDis		= manageProductGroupBean.getColA().getValue();
				productGroupName		= manageProductGroupBean.getColB().getValue();
				objDetail 				= new JSONObject();
				
				logger.info("[uploadExcelFileFile] productGroupCodeDis 	:: " + productGroupCodeDis);
				logger.info("[uploadExcelFileFile] productGroupName		:: " + productGroupName);
				
				objDetail.put("productGroupCodeDis"	, productGroupCodeDis);
				objDetail.put("productGroupName"	, productGroupName);
				
				jSONArray.add(objDetail);
				
			}
			
			uploadedFile.delete();
			
			obj.put(Constants.STATUS	, Constants.SUCCESS);
			obj.put("productGroupList"	, jSONArray);
			
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
    		
    		if("productTypeName".equals(getAutoCompleteName())){
    			list = productTypeBusiness.productTypeNameList(getAutoCompParamter(), getCurrentUser().getTin());
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
