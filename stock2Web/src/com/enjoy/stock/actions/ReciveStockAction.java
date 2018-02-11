package com.enjoy.stock.actions;

import java.util.ArrayList;
import java.util.Date;

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
import com.enjoy.core.main.ConfigFile;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.CompanyVendorBean;
import com.enjoy.stock.bean.ComparePriceBean;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.bean.ReciveOrdeDetailBean;
import com.enjoy.stock.bean.ReciveOrderMasterBean;
import com.enjoy.stock.business.CompanyVendorBusiness;
import com.enjoy.stock.business.ComparePriceBusiness;
import com.enjoy.stock.business.ProductBusiness;
import com.enjoy.stock.business.ProductquantityBusiness;
import com.enjoy.stock.business.ReciveStockBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReciveStockAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ReciveStockAction.class);
	
	private CompanyVendorBean 				companyVendorBean;
	private ReciveOrderMasterBean 			reciveOrderMasterBean;
	private ArrayList<ReciveOrdeDetailBean> reciveOrdeDetailList;
	private String							pageMode;
	private String							titlePage;
	private String							indexRow;
	private String							hidReciveNo;
	private ArrayList<ComboBean> 			statusCombo;
	private String							showBackFlag;
	private String							currReciveStatus;
	private String							systemVat;
	private ArrayList<ComboBean> 			reciveTypeList;
	private ArrayList<ComboBean> 			priceTypeList;
	private String							hidProductCodeSelect;
	private String							productCode;
	
	@Autowired
	ReciveStockBusiness reciveStockBusiness;
	@Autowired
	ProductBusiness productBusiness;
	@Autowired
	CompanyVendorBusiness companyVendorBusiness;
	@Autowired
	ComparePriceBusiness comparePriceBusiness;
	@Autowired
	ProductquantityBusiness productquantityBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	companyVendorBean		= new CompanyVendorBean();
    	reciveOrderMasterBean 	= new ReciveOrderMasterBean();
    	reciveOrdeDetailList	= new ArrayList<ReciveOrdeDetailBean>();
    	titlePage 				= "เพิ่มรายการในสต๊อกสินค้า";
    	pageMode				= Constants.NEW;
    	hidReciveNo				= "";
    	showBackFlag			= "N";
    	systemVat				= ConfigFile.getVat();
    	hidProductCodeSelect	= "";
    	
    	/*Begin set default value*/
		reciveOrderMasterBean.setReciveStatus("1");//สร้างใบสั่งซื้อ
		reciveOrderMasterBean.setReciveType("M");//เงินสด
		reciveOrderMasterBean.setPriceType("V");//มี VAT
		/*End set default value*/
    	
		currReciveStatus = reciveOrderMasterBean.getReciveStatus();
		
		setReffernce();
    	
		return SUCCESS;
	}
    
    public String getDetail() throws Exception{
		logger.info("[getDetail][Begin]");
		
		try{
			logger.info("[getDetail] hidReciveNo 	:: " + hidReciveNo);
			logger.info("[getDetail] showBackFlag 	:: " + showBackFlag);
			
			titlePage 				= "แก้ไขรายการในสต๊อกสินค้า";
			pageMode				= Constants.UPDATE;
	    	systemVat				= ConfigFile.getVat();
	    	hidProductCodeSelect 	= "";
			
			reciveOrderMasterBean 	= reciveStockBusiness.getReciveOrderMaster(hidReciveNo, getCurrentUser().getTin());
			
			if(reciveOrderMasterBean==null){
				throw new EnjoyException("เกิดข้อผิดพลาดในการดึงรายการในสต๊อกสินค้า");
			}
			
			currReciveStatus 		= reciveOrderMasterBean.getReciveStatus();
			companyVendorBean		= companyVendorBusiness.getCompanyVendor(reciveOrderMasterBean.getVendorCode(), getCurrentUser().getTin());
			reciveOrdeDetailList 	= reciveStockBusiness.getReciveOrdeDetailList(hidReciveNo, getCurrentUser().getTin());
			
			setReffernce();
			
		}catch(Exception e){
			logger.error("getDetail :: ", e);
			throw e;
		}finally{
			logger.info("[getDetail][End]");
		}
		
		return SUCCESS;
		
	}
    
    private void setReffernce() throws Exception{
		
		logger.info("[setReffernce][Begin]");
		
		try{
			
			statusCombo = reciveStockBusiness.getRefReciveOrderStatusCombo();
			
			reciveTypeList = new ArrayList<ComboBean>();
			reciveTypeList.add(new ComboBean("M","เงินสด"));
			reciveTypeList.add(new ComboBean("C","เครดิต"));
			
			priceTypeList = new ArrayList<ComboBean>();
			priceTypeList.add(new ComboBean("V","มี VAT"));
			priceTypeList.add(new ComboBean("N","ไม่มี VAT"));
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[setReffernce][End]");
		}
	}
    
	public String onSave() throws Exception{
		logger.info("[onSave][Begin]");
		
		JSONObject 			obj 				= new JSONObject();
		CompanyVendorBean 	companyVendorDb 	= null;
		String				reciveNo			= null;
		String				vendorCode			= null;
		String				branchName			= null;
		
		try{
			logger.info("[onSave] pageMode 		:: " + pageMode);
			logger.info("[onSave] vendorName 	:: " + companyVendorBean.getVendorName());
			logger.info("[onSave] branchName 	:: " + companyVendorBean.getBranchName());
			
			if(currReciveStatus.equals("") || currReciveStatus.equals("1") || currReciveStatus.equals("2")){
				companyVendorDb = companyVendorBusiness.getCompanyVendorByName(companyVendorBean.getVendorName()
																				, companyVendorBean.getBranchName()
																				, getCurrentUser().getTin());
				
				if(companyVendorDb==null){
					throw new EnjoyException("ระบุบริษัทผู้จำหน่ายผิด");
				}
				
				vendorCode 	= companyVendorDb.getVendorCode();
				branchName	= companyVendorDb.getBranchName();
			}
			
			reciveOrderMasterBean.setTin(getCurrentUser().getTin());
			reciveOrderMasterBean.setReciveNo(hidReciveNo);
			reciveOrderMasterBean.setUserUniqueId(String.valueOf(getCurrentUser().getUserUniqueId()));
			reciveOrderMasterBean.setVendorCode(vendorCode);
			reciveOrderMasterBean.setBranchName(branchName);
			
			reciveNo = reciveStockBusiness.save(reciveOrderMasterBean, reciveOrdeDetailList, pageMode, currReciveStatus);
			
			logger.info("[onSave] reciveNo :: " + reciveNo);
		   
		  	obj.put("reciveNo", reciveNo);
		   
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
	
	public String ctrlCreditDay()throws Exception {
	   logger.info("[ctrlCreditDay][Begin]");
	   
	   String		creditExpire	= null;
	   JSONObject 	obj		 		= new JSONObject();
	
	   try{
		   creditExpire = EnjoyUtils.increaseDate(new Date(), EnjoyUtils.parseInt(reciveOrderMasterBean.getCreditDay()));
		   
		   obj.put("creditExpire", 		creditExpire);
		   
		   logger.info("[ctrlCreditDay] obj :: " + obj.toString());
		   
		   writeMSG(obj.toString());
		   
	   }catch(Exception e){
		   throw e;
	   }finally{
		   logger.info("[ctrlCreditDay][End]");
	   }
	   
	   return null;
	}
	
	public String getProductDetailByName() throws Exception{
	   logger.info("[getProductDetailByName][Begin]");
	   
	   String							productName				= null;
	   JSONObject 						obj		 				= new JSONObject();
	   ProductmasterBean				productmasterBean		= null;
	   String							price					= "0.00";
	   String							discountRate			= "0";
	   ComparePriceBean 				comparePriceBean 		= null;
	   String							vendorCode				= null;
	   String							quantity				= null;
	   String							tin						= null;
	   String							inventory				= "";
	   ComparePriceBean					discountDetail			= null;
	   ReciveOrdeDetailBean				vo						= null;
	
	   try{
		   logger.info("[getProductDetailByName] indexRow 	:: " + indexRow);
			
		   vo = reciveOrdeDetailList.get(EnjoyUtils.parseInt(indexRow));
		   
		   
		   productName		= vo.getProductName();
		   vendorCode		= companyVendorBean.getVendorCode();
		   quantity			= vo.getQuantity();
		   tin				= getCurrentUser().getTin();
		   
		   logger.info("[getProductDetailByName] productName 	:: " + productName);
		   logger.info("[getProductDetailByName] vendorCode 	:: " + vendorCode);
		   logger.info("[getProductDetailByName] quantity 		:: " + quantity);
		   
		   productmasterBean 		= productBusiness.getProductDetailByName(productName, tin);
		   
		   if(productmasterBean!=null && !tin.equals("")){
			   comparePriceBean = new ComparePriceBean();
			   comparePriceBean.setProductCode(productmasterBean.getProductCode());
			   comparePriceBean.setVendorCode(vendorCode);
			   comparePriceBean.setQuantity(quantity);
			   comparePriceBean.setTin(tin);
			   
			   discountDetail = comparePriceBusiness.getPrice(comparePriceBean);
			   if(discountDetail!=null){
				   price 		= discountDetail.getPrice();
				   discountRate = discountDetail.getDiscountRate();
			   }
			   
			   obj.put("productCode"	,productmasterBean.getProductCode());
			   obj.put("productName"	,productmasterBean.getProductName());
			   obj.put("price"			,price);
			   obj.put("discountRate"	,discountRate);
			   
			   inventory = EnjoyUtils.convertNumberToDisplay(productquantityBusiness.getProductquantity(productmasterBean.getProductCode(),tin));
			   
			   obj.put("inventory"		,inventory);
			   obj.put("unitCode"		,productmasterBean.getUnitCode());
			   obj.put("unitName"		,productmasterBean.getUnitName());
		   }else{
			   obj.put("productCode"	,"");
			   obj.put("productName"	,"");
			   obj.put("price"			,price);
			   obj.put("discountRate"	,discountRate);
			   obj.put("inventory"		,inventory);
			   obj.put("unitCode"		,"");
			   obj.put("unitName"		,"");
		   }
		   
		   writeMSG(obj.toString());
		   
	   }catch(Exception e){
		   throw e;
	   }finally{
		   logger.info("[getProductDetailByName][End]");
	   }
	   
	   return null;
	}
	
	public String getCompanyVendorDetail() throws Exception{
	   logger.info("[getCompanyVendorDetail][Begin]");
	   
	   String							vendorName	= null;
	   String							branchName	= null;
	   JSONObject 						obj		 	= null;
	   CompanyVendorBean				vo			= null;
	   String							tin			= null;
	
	   try{
		   obj 				= new JSONObject();
		   vendorName		= companyVendorBean.getVendorName();
		   branchName		= companyVendorBean.getBranchName();
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
	
	public String getPrice() throws Exception {
		logger.info("[getPrice][Begin]");

		JSONObject 				obj		 			= new JSONObject();
		String					price				= "0.00";
		String					discountRate		= "0";
		ComparePriceBean 		comparePriceBean 	= null;
		ComparePriceBean		discountDetail		= null;
		ReciveOrdeDetailBean	vo					= null;
		
		try {
			logger.info("[getPrice] indexRow 	:: " + indexRow);
			
			vo = reciveOrdeDetailList.get(EnjoyUtils.parseInt(indexRow));
			
			comparePriceBean = new ComparePriceBean();
			comparePriceBean.setProductCode(vo.getProductCode());
			comparePriceBean.setTin(getCurrentUser().getTin());
			comparePriceBean.setQuantity(vo.getQuantity());
			comparePriceBean.setVendorCode(companyVendorBean.getVendorCode());
			
			discountDetail = comparePriceBusiness.getPrice(comparePriceBean);
		    if(discountDetail!=null){
		    	price 			= discountDetail.getPrice();
				discountRate 	= discountDetail.getDiscountRate();
			}
			
			obj.put("price"			,price);
			obj.put("discountRate"	,discountRate);
			
			writeMSG(obj.toString());
			
		} catch (Exception e) {
			throw e;
		} finally {
			logger.info("[getPrice][End]");
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
    		
    		if("companyVendorBean.vendorName".equals(getAutoCompleteName())){
    			list = companyVendorBusiness.vendorNameList(getAutoCompParamter(), getCurrentUser().getTin());
    		}else if("companyVendorBean.branchName".equals(getAutoCompleteName())){
    			list = companyVendorBusiness.branchNameList(companyVendorBean.getVendorName(), getAutoCompParamter(), getCurrentUser().getTin());
    		}else if(getAutoCompleteName().indexOf("productName") > -1){
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
	
	public String newRecord() throws Exception{
		logger.info("[newRecord][Begin]");
		
		ReciveOrdeDetailBean	bean	= new ReciveOrdeDetailBean();
		
		try{
			
			if(reciveOrdeDetailList==null){
				reciveOrdeDetailList = new ArrayList<ReciveOrdeDetailBean>();
			}
			
			bean.setRowStatus(Constants.NEW);
			
			reciveOrdeDetailList.add(bean);
			
			logger.info("[newRecord] size :: " + reciveOrdeDetailList.size());
			
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
			logger.info("[deleteRecord] size 		:: " + reciveOrdeDetailList.size());
			
			reciveOrdeDetailList.remove(EnjoyUtils.parseInt(indexRow));
			
		}catch(Exception e){
			logger.error("",e);
			throw e;
		}finally{
			logger.info("[deleteRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String openProductLoogUp()throws Exception {
    	hidProductCodeSelect 	= "";
    	
    	return "openProductLoogUp";
    }
    
    public String newRecordForLookUp() throws Exception{
		logger.info("[newRecordForLookUp][Begin]");
		
		ReciveOrdeDetailBean	bean;
		String[]				productCodeArray;
		ProductmasterBean		productmasterBean;
		String					tin;
		String					inventory				= "";
		ComparePriceBean		discountDetail;
		ComparePriceBean		comparePriceBean;
		String					price					= "0.00";
		String					discountRate			= "0";
		
		try{
			
			tin = getCurrentUser().getTin();
			
			if(reciveOrdeDetailList==null){
				reciveOrdeDetailList = new ArrayList<ReciveOrdeDetailBean>();
			}
			
			if(EnjoyUtils.chkNull(hidProductCodeSelect)){
				productCodeArray = hidProductCodeSelect.split("\\|");
				for(String productCode:productCodeArray){
					productmasterBean 	= productBusiness.getProductDetail(productCode, tin);
					bean				= new ReciveOrdeDetailBean();
					
					bean.setRowStatus(Constants.NEW);
					
					comparePriceBean = new ComparePriceBean();
					comparePriceBean.setProductCode(productmasterBean.getProductCode());
					comparePriceBean.setVendorCode(companyVendorBean.getVendorCode());
					comparePriceBean.setQuantity("0");
					comparePriceBean.setTin(tin);
				   
					discountDetail = comparePriceBusiness.getPrice(comparePriceBean);
					if(discountDetail!=null){
					   price 		= discountDetail.getPrice();
					   discountRate = discountDetail.getDiscountRate();
					}
					
					inventory = EnjoyUtils.convertNumberToDisplay(productquantityBusiness.getProductquantity(productmasterBean.getProductCode(),tin));
					
					bean.setProductCode(productmasterBean.getProductCode());
					bean.setProductName(productmasterBean.getProductName());
					bean.setPrice(price);
					bean.setDiscountRate(discountRate);
					bean.setInventory(inventory);
					bean.setUnitCode(productmasterBean.getUnitCode());
					bean.setUnitName(productmasterBean.getUnitName());
					
					reciveOrdeDetailList.add(bean);
				}
			}
			
			logger.info("[newRecordForLookUp] size :: " + reciveOrdeDetailList.size());
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[newRecordForLookUp][End]");
		}
		
		return SEARCH;
	}
    
    public String vendorDetailPopUp()throws Exception {
    	CompanyVendorBean				vo			= null;
    	
    	try{
    		vo = companyVendorBusiness.getCompanyVendorByName(companyVendorBean.getVendorName(), companyVendorBean.getBranchName(), getCurrentUser().getTin());
    		if(vo!=null){
    			companyVendorBean.setVendorCode(vo.getVendorCode());
 		   	}else{
 		   		throw new EnjoyException("ระบุบริษัทหรือสาขาไม่ถูกต้อง");
 		   	}
    	}catch(Exception e){
    		throw e;
    	}
    	
    	return "vendorDetailPopUp";
    }
    
    public String comparePricePopUp()throws Exception {
    	logger.info("[comparePricePopUp][Begin]");
    	
    	try{
    		logger.info("[comparePricePopUp] indexRow :: " + indexRow);
    		
    		productCode = reciveOrdeDetailList.get(EnjoyUtils.parseInt(indexRow)).getProductCode();
    		
    		logger.info("[comparePricePopUp] productName :: " + productCode);
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
			logger.info("[comparePricePopUp][End]");
		}
    	
    	return "comparePricePopUp";
    }

}







