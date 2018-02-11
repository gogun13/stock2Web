package com.enjoy.stock.actions;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.actions.ActionControl;
import com.enjoy.core.actions.BaseAction;
import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.ProductdetailBean;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.business.ProductBusiness;
import com.enjoy.stock.business.ProductGroupBusiness;
import com.enjoy.stock.business.ProductTypeBusiness;
import com.enjoy.stock.business.UnitTypeBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProductAction.class);
	
	private ProductmasterBean 				productmasterBean;
	private ArrayList<ProductdetailBean> 	productdetailList;
	private String							pageMode;
	private String							titlePage;
	private String							indexRow;
	private String							hidProductCode;
	private ArrayList<ComboBean> 			availPageFlagCombo;
	private String							showBackFlag;
	
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
    	
    	productmasterBean 	= new ProductmasterBean();
    	productdetailList	= new ArrayList<ProductdetailBean>();
    	titlePage 			= "เพิ่มรายละเอียดสินค้า";
    	pageMode			= Constants.NEW;
    	hidProductCode		= "";
    	showBackFlag		= "N";
    	
    	setAvailPageFlagCombo();
    	
		return SUCCESS;
	}
    
    public String getDetail() throws Exception{
		logger.info("[getDetail][Begin]");
		
		try{
			logger.info("[getDetail] hidProductCode :: " + hidProductCode);
			logger.info("[getDetail] showBackFlag 	:: " + showBackFlag);
			
			titlePage 		= "แก้ไขรายละเอียดสินค้า";
			pageMode		= Constants.UPDATE;
			
			productmasterBean 	= productBusiness.getProductDetail(hidProductCode, getCurrentUser().getTin());
			
			if(productmasterBean==null){
				throw new EnjoyException("เกิดข้อผิดพลาดในการดึงรายละเอียดสินค้า");
			}
			
			productdetailList = productBusiness.getProductdetailList(hidProductCode, getCurrentUser().getTin());
			
			setAvailPageFlagCombo();
			
		}catch(Exception e){
			logger.error("getDetail :: ", e);
			throw e;
		}finally{
			logger.info("[getDetail][End]");
		}
		
		return SUCCESS;
		
	}
    
    public String cancel() throws Exception{
		logger.info("[cancel][Begin]");
		
		try{
			logger.info("[cancel] productCode :: " + productmasterBean.getProductCode());
			
			productBusiness.cancelProductmaster(getCurrentUser().getTin(), "", "", productmasterBean.getProductCode());
			
		}catch(Exception e){
			logger.error("cancel :: ", e);
			throw e;
		}finally{
			logger.info("[cancel][End]");
		}
		return null;
	}
    
    private void setAvailPageFlagCombo() throws Exception{
		
		logger.info("[setAvailPageFlagCombo][Begin]");
		
		String[]	idArray 	= {"AL", "CA", "CR"};
		String[]	descArray 	= {"ทั้งหมด", "เงินสด", "เงินเชื่อ"};
		
		try{
			
			availPageFlagCombo = new ArrayList<ComboBean>();
			
			for(int i=0;i<idArray.length;i++){
				availPageFlagCombo.add(new ComboBean(idArray[i]	, descArray[i]));
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[setAvailPageFlagCombo][End]");
		}
	}
    
	public String onSave() throws Exception{
		logger.info("[onSave][Begin]");
		
		int			cou						= 0;
		String		productTypeCode			= null;
		String		productGroupCode		= null;
		String		unitCode				= null;
		JSONObject 	obj 					= new JSONObject();
		
		try{
			logger.info("[onSave] pageMode :: " + pageMode);
			
			cou = productBusiness.checkDupProductCode(productmasterBean.getProductCodeDis()
													, getCurrentUser().getTin()
													, productmasterBean.getProductCode()
													, pageMode);
			if(cou > 0){
				throw new EnjoyException("รหัสสินค้า " + productmasterBean.getProductCodeDis() + " มีในระบบแล้ว");
			}
		   
			cou = productBusiness.checkDupProductName(productmasterBean.getProductName()
				   									, productmasterBean.getProductCode()
				   									, getCurrentUser().getTin()
				   									, pageMode);
			if(cou > 0){
			   throw new EnjoyException("ชื่อสินค้า " + productmasterBean.getProductName() + " มีในระบบแล้ว");
			}
		   
			productTypeCode 		= productTypeBusiness.getProductTypeCode(productmasterBean.getProductTypeName(), getCurrentUser().getTin());
			if(!EnjoyUtils.chkNull(productTypeCode)){
			   throw new EnjoyException("หมวดสินค้าไม่มีอยู่ในระบบ");
			}
		   
			productGroupCode = productGroupBusiness.getProductGroupCode(productTypeCode
				   														, productmasterBean.getProductGroupName()
				   														, getCurrentUser().getTin());
			if(!EnjoyUtils.chkNull(productGroupCode)){
			   throw new EnjoyException("หมู่สินค้าไม่มีอยู่ในระบบ");
			}
		   
			unitCode = unitTypeBusiness.getUnitCode(productmasterBean.getUnitName(), getCurrentUser().getTin());
			if(!EnjoyUtils.chkNull(unitCode)){
			   throw new EnjoyException("หน่วยสินค้าไม่มีอยู่ในระบบ");
			}
		   
		  	productmasterBean.setProductTypeCode(productTypeCode);
		  	productmasterBean.setProductGroupCode(productGroupCode);
		  	productmasterBean.setUnitCode(unitCode);
		  	productmasterBean.setTin(getCurrentUser().getTin());
		   
		  	hidProductCode = productBusiness.save(productmasterBean, productdetailList, pageMode);
		   
		  	obj.put("productCode", hidProductCode);
		   
		  	writeMSG(obj.toString());
		   
		}catch(Exception e){
			logger.error("onSave :: ", e);
			throw e;
		}finally{
			logger.info("[onSave][End]");
		}
		
		return null;
	}
	
	public String onBack()throws Exception {
		return "onBack";
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
	
	public String newRecord() throws Exception{
		logger.info("[newRecord][Begin]");
		
		ProductdetailBean	bean	= new ProductdetailBean();
		
		try{
			
			if(productdetailList==null){
				productdetailList = new ArrayList<ProductdetailBean>();
			}
			
			bean.setAvailPageFlag("AL");
			bean.setRowStatus(Constants.NEW);
			
			productdetailList.add(bean);
			
			setAvailPageFlagCombo();
			
			logger.info("[newRecord] size :: " + productdetailList.size());
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[newRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String deleteRecord() throws Exception{
		logger.info("[deleteRecord][Begin]");
		
		try{
			logger.info("[deleteRecord] indexRow 	:: " + indexRow);
			logger.info("[deleteRecord] size 		:: " + productdetailList.size());
			
			productdetailList.remove(EnjoyUtils.parseInt(indexRow));
			
			setAvailPageFlagCombo();
			
		}catch(Exception e){
			logger.error("",e);
			throw e;
		}finally{
			logger.info("[deleteRecord][End]");
		}
		
		return SEARCH;
	}

}
