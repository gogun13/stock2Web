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
import com.enjoy.stock.bean.CompanyVendorBean;
import com.enjoy.stock.bean.ComparePriceBean;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.business.CompanyVendorBusiness;
import com.enjoy.stock.business.ComparePriceBusiness;
import com.enjoy.stock.business.ProductBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComparePriceAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ComparePriceAction.class);
	
	private String							productName;
	private String							productCode;
	private String							indexRow;
	private ArrayList<ComparePriceBean> 	comparePriceList;
	private String							comparePriceRemark;
	private String							comparePriceFlagPopUp;
	private String	comparePriceFlagPopUpRepeat = "N";
	
	@Autowired
	ComparePriceBusiness comparePriceBusiness;
	@Autowired
	ProductBusiness productBusiness;
	@Autowired
	CompanyVendorBusiness companyVendorBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	productName				= "";
    	productCode				= "";
    	indexRow				= "";
    	comparePriceList 		= null;
    	comparePriceRemark		= "";
    	comparePriceFlagPopUp 	= "";
    	comparePriceFlagPopUpRepeat = "Y";
    	
		return SUCCESS;
	}
    
    public String comparePricePopUp() throws Exception {
    	logger.info("[comparePricePopUp][Begin]");
    	
    	ProductmasterBean		productmasterBean;
    	
    	try{
    		logger.info("[comparePricePopUp] productCode 			:: " + productCode);
    		logger.info("[comparePricePopUp] comparePriceFlagPopUp 	:: " + comparePriceFlagPopUp);
    		
    		productmasterBean 		= productBusiness.getProductDetail(productCode,getCurrentUser().getTin());
    		if(productmasterBean==null){
				throw new EnjoyException("สินค้านี้ไม่มีในระบบกรุณาตรวจสอบ");
			}
    		
    		productName = productmasterBean.getProductName();
    		
    		logger.info("[comparePricePopUp] productName 	:: " + productName);
			
    		comparePriceList 	= comparePriceBusiness.searchByCriteria(getCurrentUser().getTin(), productCode);
    		comparePriceRemark	= comparePriceBusiness.getRemark(productCode, getCurrentUser().getTin());
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[comparePricePopUp][End]");
    	}
    	return SUCCESS;
    }
    
    public String resetPopUp() throws Exception {
    	logger.info("[resetPopUp][Begin]");
    	
    	try{
    		logger.info("[comparePricePopUp] productCode 			:: " + productCode);
			
    		comparePriceList 	= comparePriceBusiness.searchByCriteria(getCurrentUser().getTin(), productCode);
    		comparePriceRemark	= comparePriceBusiness.getRemark(productCode, getCurrentUser().getTin());
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[resetPopUp][End]");
    	}
    	return SEARCH;
    }
    
    public String onSearch() throws Exception {
    	logger.info("[onSearch][Begin]");
    	
    	ProductmasterBean		productmasterBean;
    	
    	try{
    		logger.info("[onSearch] productName 			:: " + productName);
    		
    		productmasterBean 		= productBusiness.getProductDetailByName(productName, getCurrentUser().getTin());
    		if(productmasterBean==null){
				throw new EnjoyException("ชื่อสินค้านี้ไม่มีในระบบกรุณาตรวจสอบ");
			}
    		
    		productCode = productmasterBean.getProductCode();
    		
    		logger.info("[onSearch] productCode 	:: " + productCode);
			
    		comparePriceList 	= comparePriceBusiness.searchByCriteria(getCurrentUser().getTin(), productCode);
    		comparePriceRemark	= comparePriceBusiness.getRemark(productCode, getCurrentUser().getTin());
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[onSearch][End]");
    	}
    	return SUCCESS;
    }
    
    public String newRecord() throws Exception{
		logger.info("[newRecord][Begin]");
		
		ComparePriceBean	bean	= new ComparePriceBean();
		
		try{
			
			if(comparePriceList==null){
				comparePriceList = new ArrayList<ComparePriceBean>();
			}
			
			bean.setProductCode(productCode);
			bean.setTin(getCurrentUser().getTin());
			bean.setRowStatus(Constants.NEW);
			
			comparePriceList.add(bean);
			
			logger.info("[newRecord] size :: " + comparePriceList.size());
			
			
			
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
			logger.info("[deleteRecord] size 		:: " + comparePriceList.size());
			
			comparePriceList.remove(EnjoyUtils.parseInt(indexRow));
			
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
		
    	ComparePriceBean		bean		= null;
    	ComparePriceBean		beanTemp	= null;
    	
		try{
			
			for(int i=0;i<comparePriceList.size();i++){
				bean = comparePriceList.get(i);
				for(int j=(i+1);j<comparePriceList.size();j++){
					beanTemp = comparePriceList.get(j);
					
					if((bean.getVendorCode().equals(beanTemp.getVendorCode())) && (bean.getQuantity().equals(beanTemp.getQuantity()))){
						throw new EnjoyException("บริษัท สาขา และปริมาณห้ามซ้ำ");
					}
				}
		   }
			
			comparePriceBusiness.save(comparePriceList, getCurrentUser().getTin(),productCode,comparePriceRemark);
			
			onSearch();
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
		return SUCCESS;
    }
    
    public String getCompanyVendorDetail() throws Exception{
 	   logger.info("[getCompanyVendorDetail][Begin]");
 	   
 	   String						vendorName			= null;
 	   String						branchName			= null;
 	   JSONObject 					obj		 			= null;
 	   CompanyVendorBean			vo					= null;
 	   String						tin					= null;
 	   ComparePriceBean				comparePriceBean 	= null;
 	
 	   try{
 		  logger.info("[getCompanyVendorDetail] indexRow 	:: " + indexRow);
 		  
 		 comparePriceBean	= comparePriceList.get(EnjoyUtils.parseInt(indexRow));
 		  
 		  obj 				= new JSONObject();
 		  vendorName		= comparePriceBean.getVendorName();
 		  branchName		= comparePriceBean.getBranchName();
 		  tin				= getCurrentUser().getTin();
 		   
 		  logger.info("[getCompanyVendorDetail] vendorName 		:: " + vendorName);
 		  logger.info("[getCompanyVendorDetail] branchName 		:: " + branchName);
 		  logger.info("[getCompanyVendorDetail] tin 				:: " + tin);
 		   
 		  vo = companyVendorBusiness.getCompanyVendorByName(vendorName, branchName, tin);
 		   
 		  if(vo!=null){
 			  obj.put("vendorCode", 		vo.getVendorCode());
 			  obj.put("vendorName", 		vo.getVendorName());
 			  obj.put("branchName", 		vo.getBranchName());
 		  }else{
 			  obj.put("vendorCode", 		"");
 			  obj.put("vendorName", 		vendorName);
 			  obj.put("branchName", 		branchName);
 		  }
 		   
 		  writeMSG(obj.toString());
 		   
 	   }catch(Exception e){
 		   throw e;
 	   }finally{
 		   logger.info("[getCompanyVendorDetail][End]");
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
    		
    		if("productName".equals(getAutoCompleteName())){
    			list = productBusiness.productNameList(getAutoCompParamter(), "", "", getCurrentUser().getTin(), true);
    		}else if(getAutoCompleteName().indexOf("vendorName") > -1){
    			list = companyVendorBusiness.vendorNameList(getAutoCompParamter(), getCurrentUser().getTin());
    		}else if(getAutoCompleteName().indexOf("branchName") > -1){
    			logger.info("[autoComplete] indexRow 	:: " + indexRow);
    			
    			ComparePriceBean vo = comparePriceList.get(EnjoyUtils.parseInt(indexRow));
    			if(vo!=null){
    				list = companyVendorBusiness.branchNameList(vo.getVendorName(), getAutoCompParamter(), getCurrentUser().getTin());
    			}else{
    				list = new ArrayList<ComboBean>();
    			}
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
