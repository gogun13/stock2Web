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
import com.enjoy.core.main.Constants;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.business.ProductBusiness;
import com.enjoy.stock.business.ProductGroupBusiness;
import com.enjoy.stock.business.ProductTypeBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSearchAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ProductSearchAction.class);
	
	private static final String ITEM_PRODUCT_CODE = "ITEM_PRODUCT_CODE";
	
	private ProductmasterBean 				productmasterBean;
	private PaginatedListBean				paginatedList;
	private String							hidProductCode;
	private String							hidProductCodeForBarCode;
	private ArrayList<ProductmasterBean> 	productListForBarCode;
	private String							indexRow;
	private String							radPrint;
	
	@Autowired
	ProductTypeBusiness productTypeBusiness;
	@Autowired
	ProductGroupBusiness productGroupBusiness;
	@Autowired
	ProductBusiness productBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	if (request.getSession(false) != null) {
            request.getSession(false).removeAttribute(Constants.SESSION_OBJ);
            request.getSession(false).removeAttribute(Constants.PAGE_INDEX);
            request.getSession(false).removeAttribute(ITEM_PRODUCT_CODE);
        }
    	
    	productmasterBean 			= new ProductmasterBean();
    	hidProductCodeForBarCode 	= "";
    	
    	setPageIndex("1");
    	searchProductDetails();
    	
		return SUCCESS;
	}
    
    public String searchProductDetails()throws Exception {
    	logger.info("[searchProductDetails][Begin]");
    	
    	try{
    		if (paginatedList == null) {
    			paginatedList = createPaginate(10);
            }
    		
    		paginatedList.setIndex(Integer.parseInt(getPageIndex()));
    		
    		productBusiness.searchByCriteria(paginatedList, productmasterBean,getCurrentUser().getTin());
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[searchProductDetails][End]");
    	}
    	return SEARCH;
    }
    
    public String gotoDetail()throws Exception {
    	logger.info("[gotoDetail][Begin]");
    	
    	try{
    		request.getSession(false).setAttribute(Constants.SESSION_OBJ, productmasterBean);
    		request.getSession(false).setAttribute(Constants.PAGE_INDEX, getPageIndex());
    		request.getSession(false).setAttribute(ITEM_PRODUCT_CODE,hidProductCodeForBarCode);
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[gotoDetail][End]");
    	}
    	return "gotoDetail";
    }
    
    public String onBack() throws Exception{
    	logger.info("[onBack][Begin]");
    	
    	try{
    		
    		productmasterBean = (ProductmasterBean) request.getSession(false).getAttribute(Constants.SESSION_OBJ);
    		hidProductCodeForBarCode = (String) request.getSession(false).getAttribute(ITEM_PRODUCT_CODE);
        	
        	setPageIndex((String) request.getSession(false).getAttribute(Constants.PAGE_INDEX));
        	searchProductDetails();
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[onBack][End]");
    	}
    	return SUCCESS;
    }
    
    public String gotoProductBarCode()throws Exception {
    	logger.info("[gotoProductBarCode][Begin]");
    	
    	try{
    		logger.info("[gotoProductBarCode] hidProductCodeForBarCode :: " + hidProductCodeForBarCode);
    		
    		productListForBarCode = productBusiness.getProductmasterForBarCode(hidProductCodeForBarCode, getCurrentUser().getTin());
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[gotoProductBarCode][End]");
    	}
    	return "gotoProductBarCode";
    }
    
    public String deleteRecord() throws Exception{
		logger.info("[deleteRecord][Begin]");
		
		try{
			logger.info("[deleteRecord] indexRow :: " + indexRow);
			
			for(int i=0;i<productListForBarCode.size();i++){
				ProductmasterBean vo = productListForBarCode.get(i);
				
				if(vo.getSeq().equals(indexRow)){
					productListForBarCode.remove(i);
					break;
				}
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[deleteRecord][End]");
		}
		
		return "productBarCodeList";
	}
    
    public String genPdf() throws Exception{
		logger.info("[genPdf][Begin]");
		
		JSONObject 				obj 				= new JSONObject();
		JSONArray 				detailJSONArray 	= new JSONArray();
		JSONObject 				objDetail 			= null;
		
		try{
			logger.info("[gotoProductBarCode] hidProductCodeForBarCode 	:: " + hidProductCodeForBarCode);
			logger.info("[gotoProductBarCode] radPrint 					:: " + radPrint);
    		
    		productListForBarCode = productBusiness.getProductmasterForBarCode(hidProductCodeForBarCode, getCurrentUser().getTin());
    		for(ProductmasterBean bean:productListForBarCode){
    			objDetail 		= new JSONObject();
    			objDetail.put("productCodeDis"		,bean.getProductCodeDis());
    			objDetail.put("productName"			,bean.getProductName());
			   
    			detailJSONArray.add(objDetail);
    		}
    		obj.put(Constants.STATUS, Constants.SUCCESS);
    		obj.put("printType"	, radPrint);
 		   	obj.put("detailList" , detailJSONArray);
			
			writePDF("ProductBarcodePdfForm", obj, "รหัสสินค้า");
			
		}catch(Exception e){
			logger.error("genPdf :: ", e);
			throw e;
		}finally{
			logger.info("[genPdf][End]");
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
    			list = productGroupBusiness.productGroupNameList(productmasterBean.getProductTypeName(), getAutoCompParamter(), getCurrentUser().getTin(), true);
    		}else if("productmasterBean.productName".equals(getAutoCompleteName())){
    			list = productBusiness.productNameList(getAutoCompParamter()
    												, productmasterBean.getProductTypeName()
    												, productmasterBean.getProductGroupName()
    												, getCurrentUser().getTin()
    												, true);
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
