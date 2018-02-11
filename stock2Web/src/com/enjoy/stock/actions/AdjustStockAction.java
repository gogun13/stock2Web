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
import com.enjoy.stock.bean.AdjustStockBean;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.business.AdjustStockBusiness;
import com.enjoy.stock.business.ProductBusiness;
import com.enjoy.stock.business.ProductquantityBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdjustStockAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AdjustStockAction.class);
	
	private String							indexRow;
	private ArrayList<AdjustStockBean> 		adjustStockList;
	private String							hidProductCodeSelect;
	
	@Autowired
	AdjustStockBusiness adjustStockBusiness;
	@Autowired
	ProductBusiness productBusiness;
	@Autowired
	ProductquantityBusiness productquantityBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	indexRow				= "";
    	adjustStockList 		= new ArrayList<AdjustStockBean>();
    	hidProductCodeSelect 	= "";
    	
		return SUCCESS;
	}
    
    public String newRecord() throws Exception{
		logger.info("[newRecord][Begin]");
		
		AdjustStockBean	bean	= new AdjustStockBean();
		
		try{
			
			if(adjustStockList==null){
				adjustStockList = new ArrayList<AdjustStockBean>();
			}
			
			bean.setRowStatus(Constants.NEW);
			
			adjustStockList.add(bean);
			
			logger.info("[newRecord] size :: " + adjustStockList.size());
			
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
			logger.info("[deleteRecord] size 		:: " + adjustStockList.size());
			
			adjustStockList.remove(EnjoyUtils.parseInt(indexRow));
			
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
    	
    	AdjustStockBean bean 		= null;
    	AdjustStockBean beanTemp 	= null;
    	
		try{
			if(adjustStockList!=null && !adjustStockList.isEmpty()){
				for(int i=0;i<adjustStockList.size();i++){
					bean = adjustStockList.get(i);
					for(int j=(i+1);j<adjustStockList.size();j++){
						beanTemp = adjustStockList.get(j);
						if(bean.getProductCode().equals(beanTemp.getProductCode())){
							throw new EnjoyException("มีสินค้า" + bean.getProductName() + "ในรายการแล้ว");
						}
					}
				}
				
				adjustStockBusiness.save(adjustStockList, getCurrentUser().getTin());
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
		return null;
    }
    
    public String openProductLoogUp()throws Exception {
    	hidProductCodeSelect 	= "";
    	
    	return "openProductLoogUp";
    }
    
    public String newRecordForLookUp() throws Exception{
		logger.info("[newRecordForLookUp][Begin]");
		
		AdjustStockBean			bean;
		String[]				productCodeArray;
		ProductmasterBean		productmasterBean;
		String					tin;
		String					inventory				= "";
		
		try{
			
			tin = getCurrentUser().getTin();
			
			if(adjustStockList==null){
				adjustStockList = new ArrayList<AdjustStockBean>();
			}
			
			if(EnjoyUtils.chkNull(hidProductCodeSelect)){
				productCodeArray = hidProductCodeSelect.split("\\|");
				for(String productCode:productCodeArray){
					productmasterBean 	= productBusiness.getProductDetail(productCode, tin);
					bean				= new AdjustStockBean();
					
					bean.setRowStatus(Constants.NEW);
					
					inventory = EnjoyUtils.convertNumberToDisplay(productquantityBusiness.getProductquantity(productmasterBean.getProductCode(),tin));
					
					bean.setProductCode(productmasterBean.getProductCode());
					bean.setProductName(productmasterBean.getProductName());
					bean.setQuanOld(inventory);
					bean.setQuantity(inventory);
					bean.setUnitName(productmasterBean.getUnitName());
					
					adjustStockList.add(bean);
				}
			}
			
			logger.info("[newRecordForLookUp] size :: " + adjustStockList.size());
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[newRecordForLookUp][End]");
		}
		
		return SEARCH;
	}
    
    public String getProductDetailByName() throws Exception{
 	   logger.info("[getProductDetailByName][Begin]");
 	   
 	   String						productName				= null;
 	   JSONObject 					obj		 				= new JSONObject();
 	   ProductmasterBean			productmasterBean		= null;
 	   String						tin						= null;
 	   String						inventory				= "";
 	   AdjustStockBean				vo						= null;
 	
 	   try{
 		   logger.info("[getProductDetailByName] indexRow 	:: " + indexRow);
 			
 		   vo 			= adjustStockList.get(EnjoyUtils.parseInt(indexRow));
 		   productName	= vo.getProductName();
 		   tin			= getCurrentUser().getTin();
 		   
 		   logger.info("[getProductDetailByName] productName 	:: " + productName);
 		   
 		   productmasterBean 		= productBusiness.getProductDetailByName(productName, tin);
 		   
 		   if(productmasterBean!=null && !tin.equals("")){
 			  obj.put("productCode"	,productmasterBean.getProductCode());
			  obj.put("productName"	,productmasterBean.getProductName());
			   
			  inventory = EnjoyUtils.convertNumberToDisplay(productquantityBusiness.getProductquantity(productmasterBean.getProductCode(),tin));
			   
			  obj.put("quanOld"		,inventory);
			  obj.put("quantity"	,inventory);
			  obj.put("unitName"	,productmasterBean.getUnitName());
 		   }else{
 			  obj.put("productCode"	,"");
 			  obj.put("productName"	,"");
			  obj.put("quanOld"		,inventory);
			  obj.put("quantity"	,inventory);
			  obj.put("unitName"	,"");
 		   }
 		   
 		   writeMSG(obj.toString());
 		   
 	   }catch(Exception e){
 		   throw e;
 	   }finally{
 		   logger.info("[getProductDetailByName][End]");
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
    		
    		if(getAutoCompleteName().indexOf("productName") > -1){
    			list = productBusiness.productNameList(getAutoCompParamter(), "", "", getCurrentUser().getTin(), true);
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
