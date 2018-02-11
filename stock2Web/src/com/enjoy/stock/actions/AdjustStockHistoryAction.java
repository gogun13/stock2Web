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
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.stock.bean.AdjustStockBean;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.business.AdjustStockBusiness;
import com.enjoy.stock.business.ProductBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdjustStockHistoryAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AdjustStockHistoryAction.class);
	
	private PaginatedListBean			paginatedList;
	private AdjustStockBean 			adjustStockBean;
	
	@Autowired
	AdjustStockBusiness adjustStockBusiness;
	@Autowired
	ProductBusiness productBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	adjustStockBean 	= new AdjustStockBean();
    	paginatedList		= null;
    	
    	setPageIndex("1");
    	
		return SUCCESS;
	}
    
    public String search() throws Exception {
    	logger.info("[search][Begin]");
    	
    	ProductmasterBean	productmasterBean;
    	String				tin;
    	
    	try{
    		tin						= getCurrentUser().getTin();
			productmasterBean		= productBusiness.getProductDetailByName(adjustStockBean.getProductName(), tin);
    		
			if(productmasterBean==null){
				throw new EnjoyException("ระบุชื่อสินค้าผิดกรุณาตรวจสอบ");
			}
    		
			if (paginatedList == null) {
    			paginatedList = createPaginate(10);
            }
			
			paginatedList.setIndex(Integer.parseInt(getPageIndex()));
			
			adjustStockBean.setProductCode(productmasterBean.getProductCode());
			adjustStockBean.setTin(tin);
			
			adjustStockBusiness.getAdjustHistoryList(paginatedList, adjustStockBean);
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[search][End]");
    	}
    	return SEARCH;
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
    		
    		if("adjustStockBean.productName".equals(getAutoCompleteName())){
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
