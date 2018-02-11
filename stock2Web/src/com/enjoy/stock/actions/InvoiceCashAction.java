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
import com.enjoy.core.bean.CompanyDetailsBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.business.CompanyDetailsBusiness;
import com.enjoy.core.business.UserdetailsBusiness;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.ConfigFile;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.CustomerDetailsBean;
import com.enjoy.stock.bean.InvoiceCashDetailBean;
import com.enjoy.stock.bean.InvoiceCashMasterBean;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.business.CustomerBusiness;
import com.enjoy.stock.business.InvoiceCashBusiness;
import com.enjoy.stock.business.ProductBusiness;
import com.enjoy.stock.business.ProductquantityBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceCashAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InvoiceCashAction.class);
	private static final String 	CASH 				= "CA";
	
	private InvoiceCashMasterBean 			invoiceCashMasterBean;
	private CustomerDetailsBean 			customerDetailsBean;
	private UserDetailsBean					userDetailsBean;
	private ArrayList<InvoiceCashDetailBean>invoiceCashDetailList;
	private String							pageMode;
	private String							titlePage;
	private String							indexRow;
	private String							hidInvoiceCode;
	private ArrayList<ComboBean> 			statusCombo;
	private String							showBackFlag;
	private String							systemVat;
	private ArrayList<ComboBean> 			priceTypeList;
	private String							hidProductCodeSelect;
	private String							updateCredit;
	private String							productCodeDis;
	private String							cusCodeDis;
	
	@Autowired
	InvoiceCashBusiness invoiceCashBusiness;
	@Autowired
	ProductBusiness productBusiness;
	@Autowired
	CustomerBusiness customerBusiness;
	@Autowired
	ProductquantityBusiness productquantityBusiness;
	@Autowired
	UserdetailsBusiness userdetailsBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	invoiceCashMasterBean	= new InvoiceCashMasterBean();
    	customerDetailsBean		= new CustomerDetailsBean();
    	userDetailsBean			= new UserDetailsBean();
    	invoiceCashDetailList	= new ArrayList<InvoiceCashDetailBean>();
    	titlePage 				= "บันทึกการขายเงินสด";
    	pageMode				= Constants.NEW;
    	hidInvoiceCode			= "";
    	showBackFlag			= "N";
    	systemVat				= ConfigFile.getVat();
    	hidProductCodeSelect	= "";
    	updateCredit			= "";
    	productCodeDis			= "";
    	cusCodeDis				= "";
    	
    	/*Begin set default value*/
    	invoiceCashMasterBean.setInvoiceDate(EnjoyUtils.dateToThaiDisplay(EnjoyUtils.currDateThai()));
		invoiceCashMasterBean.setInvoiceType("N");
		invoiceCashMasterBean.setInvoiceStatus("A");
		invoiceCashMasterBean.setSaleName(getCurrentUser().getUserFullName());
		invoiceCashMasterBean.setSaleUniqueId(String.valueOf(getCurrentUser().getUserUniqueId()));
		
		userDetailsBean  = getCurrentUser();
		/*End set default value*/
    	
		setReffernce();
    	
		return SUCCESS;
	}
    
    public String getDetail() throws Exception{
		logger.info("[getDetail][Begin]");
		
		try{
			logger.info("[getDetail] hidInvoiceCode :: " + hidInvoiceCode);
			logger.info("[getDetail] showBackFlag 	:: " + showBackFlag);
			logger.info("[getDetail] updateCredit 	:: " + updateCredit);
			
			if(updateCredit.equals("Y")){
				titlePage = "ปรับปรุงงบการขายเงินเชื่อ";
			}else{
				titlePage = "แก้ไขรายการการขายเงินสด";
			}
			
			if(!Constants.EDIT_PAGE.equals(pageMode)){
				pageMode = Constants.UPDATE;
			}
			
	    	systemVat				= ConfigFile.getVat();
	    	hidProductCodeSelect 	= "";
	    	invoiceCashMasterBean 	= invoiceCashBusiness.getInvoiceCashMaster(hidInvoiceCode, getCurrentUser().getTin());
	    	productCodeDis			= "";
			
			if(invoiceCashMasterBean==null){
				throw new EnjoyException("เกิดข้อผิดพลาดในการดึงรายการบันทึกการขายเงินสด");
			}
			
			invoiceCashDetailList = invoiceCashBusiness.getInvoiceCashDetailList(hidInvoiceCode, getCurrentUser().getTin());
			
			if(EnjoyUtils.chkNull(invoiceCashMasterBean.getCusCode())){
				cusCodeDis			= invoiceCashMasterBean.getCusCode();
				customerDetailsBean = customerBusiness.getCustomerDetail(invoiceCashMasterBean.getCusCode(), getCurrentUser().getTin());
			}else{
				cusCodeDis			= "";
				customerDetailsBean = new CustomerDetailsBean();
			}
			
			if(EnjoyUtils.chkNull(invoiceCashMasterBean.getSaleUniqueId())){
				userDetailsBean  = userdetailsBusiness.getUserdetail(EnjoyUtils.parseInt(invoiceCashMasterBean.getSaleUniqueId()));
			}else{
				userDetailsBean = new UserDetailsBean();
			}
			
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
			
			statusCombo = new ArrayList<ComboBean>();
			statusCombo.add(new ComboBean(""	, "กรุณาระบุ"));
			statusCombo.add(new ComboBean("A"	, "ใช้งานอยู่"));
			statusCombo.add(new ComboBean("C"	, "ยกเลิกการใช้งาน"));
			statusCombo.add(new ComboBean("W"	, "รอสร้างใบ Invoice"));
			
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
		String				invoiceCode			= null;
		double 				commission 			= 0.00;
		
		try{
			logger.info("[onSave] pageMode 		:: " + pageMode);
			
			if(userDetailsBean!=null){
				logger.info("[onSave] UserUniqueId 		:: " + userDetailsBean.getUserUniqueId());
				
				if(userDetailsBean.getUserUniqueId() > 0){
					invoiceCashMasterBean.setSaleUniqueId(EnjoyUtils.nullToStr(userDetailsBean.getUserUniqueId()));
					
					if("Y".equals(userDetailsBean.getFlagSalesman())){
						commission = (EnjoyUtils.parseDouble(invoiceCashMasterBean.getInvoiceTotal()) * EnjoyUtils.parseDouble(userDetailsBean.getCommission()))/100;
					}
					
					invoiceCashMasterBean.setSaleCommission(String.valueOf(commission).replaceAll(",", ""));
				}else{
					invoiceCashMasterBean.setSaleUniqueId("0");
					invoiceCashMasterBean.setSaleCommission("0");
				}
			}
			
			if(EnjoyUtils.chkNull(customerDetailsBean.getCusCode())){
				invoiceCashMasterBean.setCusCode(customerDetailsBean.getCusCode());
				invoiceCashMasterBean.setBranchName(customerDetailsBean.getBranchName());
			}
			
			if(Constants.EDIT_PAGE.equals(pageMode)){
				logger.info("[onSave] hidInvoiceCode 		:: " + hidInvoiceCode);
				
				invoiceCode = hidInvoiceCode;
				pageMode	= Constants.UPDATE;
				invoiceCashBusiness.edit(invoiceCashMasterBean, invoiceCashDetailList, getCurrentUser(), hidInvoiceCode);
			}else{
				invoiceCode = invoiceCashBusiness.save(invoiceCashMasterBean, invoiceCashDetailList, getCurrentUser());
			}
			
//			invoiceCode = invoiceCashBusiness.save(invoiceCashMasterBean, invoiceCashDetailList, pageMode, getCurrentUser());
			
			logger.info("[onSave] invoiceCode :: " + invoiceCode);
		   
		  	obj.put("invoiceCode", invoiceCode);
		   
		  	writeMSG(obj.toString());
		   
		}catch(Exception e){
			logger.error("onSave :: ", e);
			throw e;
		}finally{
			logger.info("[onSave][End]");
		}
		
		return null;
	}
	
	public String onCancel() throws Exception{
		logger.info("[onCancel][Begin]");
		
		JSONObject 	obj = new JSONObject();
		
		try{
			logger.info("[onSave] hidInvoiceCode 		:: " + hidInvoiceCode);
			logger.info("[onSave] invoiceCredit 		:: " + invoiceCashMasterBean.getInvoiceCredit());
			
			invoiceCashBusiness.cancel(hidInvoiceCode, getCurrentUser(), invoiceCashMasterBean.getInvoiceCredit());
		   
		  	obj.put("invoiceCode", hidInvoiceCode);
		   
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
	
	public String onBackUpdateCredit()throws Exception {
		return "onBackUpdateCredit";
	}
	
	public String getProductDetailByName() throws Exception{
	   logger.info("[getProductDetailByName][Begin]");
	   
	   String							productName				= null;
	   JSONObject 						obj		 				= new JSONObject();
	   ProductmasterBean				productmasterBean		= null;
	   String							vendorCode				= null;
	   String							quantity				= null;
	   String							tin						= null;
	   String							inventory				= "";
	   InvoiceCashDetailBean			vo						= null;
	   String				 			groupSalePrice			= null;
	   String							pricePerUnit			= "";
	   String							discount				= "";
	
	   try{
		   logger.info("[getProductDetailByName] indexRow 	:: " + indexRow);
			
		   vo = invoiceCashDetailList.get(EnjoyUtils.parseInt(indexRow));
		   
		   productName		= vo.getProductName();
		   quantity			= vo.getQuantity();
		   tin				= getCurrentUser().getTin();
		   groupSalePrice	= customerDetailsBean.getGroupSalePrice();
		   
		   logger.info("[getProductDetailByName] productName 	:: " + productName);
		   logger.info("[getProductDetailByName] vendorCode 	:: " + vendorCode);
		   logger.info("[getProductDetailByName] quantity 		:: " + quantity);
		   
		   if(EnjoyUtils.parseDouble(quantity)==0){
			   quantity = "1";
		   }
		   
		   productmasterBean 		= productBusiness.getProductDetailByName(productName, tin);
		   
		   if(productmasterBean!=null && !tin.equals("")){
			   if(groupSalePrice.equals("2")){
				   pricePerUnit = productmasterBean.getSalePrice2();
			   }else if(groupSalePrice.equals("3")){
				   pricePerUnit = productmasterBean.getSalePrice3();
			   }else if(groupSalePrice.equals("4")){
				   pricePerUnit = productmasterBean.getSalePrice4();
			   }else if(groupSalePrice.equals("5")){
				   pricePerUnit = productmasterBean.getSalePrice5();
			   }else{
				   pricePerUnit = productmasterBean.getSalePrice1();
			   }
			   
			   discount = productBusiness.getQuanDiscount(productmasterBean.getProductCode(), quantity, invoiceCashMasterBean.getInvoiceDate(), tin, CASH);
			   
			   inventory = EnjoyUtils.convertNumberToDisplay(productquantityBusiness.getProductquantity(productmasterBean.getProductCode(),tin));
			   
			   obj.put("productCode"	,productmasterBean.getProductCode());
			   obj.put("productName"	,productmasterBean.getProductName());
			   obj.put("pricePerUnit"	,pricePerUnit);
			   obj.put("inventory"		,inventory);
			   obj.put("unitCode"		,productmasterBean.getUnitCode());
			   obj.put("unitName"		,productmasterBean.getUnitName());
			   obj.put("quantity"		,quantity);
			   obj.put("discount"		,discount);
		   }else{
			   obj.put("productCode"	,"");
			   obj.put("productName"	,"");
			   obj.put("pricePerUnit"	,"");
			   obj.put("inventory"		,"");
			   obj.put("unitCode"		,"");
			   obj.put("unitName"		,"");
			   obj.put("quantity"		,"0");
			   obj.put("discount"		,"0");
		   }
		   
		   writeMSG(obj.toString());
		   
	   }catch(Exception e){
		   throw e;
	   }finally{
		   logger.info("[getProductDetailByName][End]");
	   }
	   
	   return null;
	}
	
	public String getCustomerDetail() throws Exception{
	   logger.info("[getCustomerDetail][Begin]");
	   
	   JSONObject 			obj		 				= new JSONObject();
	   CustomerDetailsBean	customerDetailsBeanDb	= null;
	
	   try{
		   
		   logger.info("[getCustomerDetail] cusCodeDis :: " + cusCodeDis);
		   
		   customerDetailsBeanDb 		= customerBusiness.getCustomerDetail(cusCodeDis, getCurrentUser().getTin());
		   
		   if(customerDetailsBeanDb!=null){
			   obj.put("fullName"		,customerDetailsBeanDb.getFullName());
			   obj.put("cusCode"		,customerDetailsBeanDb.getCusCode());
			   obj.put("cusGroupCode"	,customerDetailsBeanDb.getCusGroupCode());
			   obj.put("groupSalePrice"	,customerDetailsBeanDb.getGroupSalePrice());
			   obj.put("branchName"		,customerDetailsBeanDb.getBranchName());
		   }else{
			   obj.put("fullName"		,"");
			   obj.put("cusCode"		,"");
			   obj.put("cusGroupCode"	,"");
			   obj.put("groupSalePrice"	,"");
			   obj.put("branchName"		,"");
		   }
		   
		   writeMSG(obj.toString());
		   
	   }catch(Exception e){
		   throw e;
	   }finally{
		   logger.info("[getCustomerDetail][End]");
	   }
	   
	   return null;
	}
	
	public String getSaleNameDetail() throws Exception{
	   logger.info("[getSaleNameDetail][Begin]");
	   
	   JSONObject 		obj		 			= new JSONObject();
	   String			userUniqueId		= "";
	   String			flagSalesman		= "";
	   String			commission			= "";
	   UserDetailsBean 	userDetailsBeanDb	= null;
	   
	   try{
		   
		   userDetailsBeanDb = userdetailsBusiness.getUserdetailByTin(userDetailsBean.getUserFullName(),getCurrentUser().getTin());
		   
		   if(userDetailsBeanDb!=null){
			   userUniqueId = EnjoyUtils.nullToStr(userDetailsBeanDb.getUserUniqueId());
			   flagSalesman = userDetailsBeanDb.getFlagSalesman();
			   commission 	= userDetailsBeanDb.getCommission();
		   }
		   
		   obj.put("userUniqueId"	,userUniqueId);
		   obj.put("flagSalesman"	,flagSalesman);
		   obj.put("commission"		,commission);
		   
		   writeMSG(obj.toString());
		   
	   }catch(Exception e){
		   throw e;
	   }finally{
		   logger.info("[getSaleNameDetail][End]");
	   }
	   return null;
	}
	
	public String getSaleNameDetailByCode() throws Exception{
	   logger.info("[getSaleNameDetailByCode][Begin]");
	   
	   JSONObject 		obj		 			= new JSONObject();
	   String			userUniqueId		= "";
	   String			flagSalesman		= "";
	   String			commission			= "";
	   UserDetailsBean 	userDetailsBeanDb	= null;
	   String			userFullName		= "";
	   
	   try{
		   
		   userDetailsBeanDb = userdetailsBusiness.getUserdetail(EnjoyUtils.parseInt(userDetailsBean.getUserUniqueId()));
		   
		   if(userDetailsBeanDb!=null){
			   userUniqueId = EnjoyUtils.nullToStr(userDetailsBeanDb.getUserUniqueId());
			   flagSalesman = userDetailsBeanDb.getFlagSalesman();
			   commission 	= userDetailsBeanDb.getCommission();
			   userFullName = userDetailsBeanDb.getUserFullName();
		   }
		   
		   obj.put("userUniqueId"	,userUniqueId);
		   obj.put("flagSalesman"	,flagSalesman);
		   obj.put("commission"		,commission);
		   obj.put("userFullName"	,userFullName);
		   
		   writeMSG(obj.toString());
		   
	   }catch(Exception e){
		   throw e;
	   }finally{
		   logger.info("[getSaleNameDetailByCode][End]");
	   }
	   return null;
	}
	
	public String getDiscount() throws Exception {
		logger.info("[getDiscount][Begin]");

		JSONObject 		obj		 		= new JSONObject();
		String 			discount;
		InvoiceCashDetailBean bean;
		
		try {
			logger.info("[getDiscount] indexRow 	:: " + indexRow);
			
			bean 		= invoiceCashDetailList.get(EnjoyUtils.parseInt(indexRow));
			discount 	= productBusiness.getQuanDiscount(bean.getProductCode(), bean.getQuantity(), invoiceCashMasterBean.getInvoiceDate(), getCurrentUser().getTin(), CASH);
			
			obj.put("discount"	,discount);
			
			writeMSG(obj.toString());
			
		} catch (Exception e) {
			throw e;
		} finally {
			logger.info("[getDiscount][End]");
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
    		
    		if("userDetailsBean.userFullName".equals(getAutoCompleteName())){
    			list = userdetailsBusiness.userFullNameList(getAutoCompParamter(), getCurrentUser().getTin(),getCurrentUser().getUserUniqueId());
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
		
		InvoiceCashDetailBean	bean	= new InvoiceCashDetailBean();
		
		try{
			
			if(invoiceCashDetailList==null){
				invoiceCashDetailList = new ArrayList<InvoiceCashDetailBean>();
			}
			
			bean.setRowStatus(Constants.NEW);
			
			invoiceCashDetailList.add(bean);
			
			logger.info("[newRecord] size :: " + invoiceCashDetailList.size());
			
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
			logger.info("[deleteRecord] size 		:: " + invoiceCashDetailList.size());
			
			invoiceCashDetailList.remove(EnjoyUtils.parseInt(indexRow));
			
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
    
    public String openUserLoogUp()throws Exception {
    	
    	return "openUserLoogUp";
    }
    
    public String openCustomerLookUp()throws Exception {
    	
    	return "openCustomerLookUp";
    }
    
    public String newRecordForLookUp() throws Exception{
		logger.info("[newRecordForLookUp][Begin]");
		
		InvoiceCashDetailBean	bean;
		String[]				productCodeArray;
		ProductmasterBean		productmasterBean;
		String					tin;
		String					inventory				= "";
		String				 	groupSalePrice			= null;
		String					pricePerUnit			= "";
		String					discount				= "";
		
		try{
			
			tin 			= getCurrentUser().getTin();
			groupSalePrice	= customerDetailsBean.getGroupSalePrice();
			
			if(invoiceCashDetailList==null){
				invoiceCashDetailList = new ArrayList<InvoiceCashDetailBean>();
			}
			
			if(EnjoyUtils.chkNull(hidProductCodeSelect)){
				productCodeArray = hidProductCodeSelect.split("\\|");
				for(String productCode:productCodeArray){
					productmasterBean 	= productBusiness.getProductDetail(productCode, tin);
					bean				= new InvoiceCashDetailBean();
					
					bean.setRowStatus(Constants.NEW);
					
					if(groupSalePrice.equals("2")){
					   pricePerUnit = productmasterBean.getSalePrice2();
					}else if(groupSalePrice.equals("3")){
					   pricePerUnit = productmasterBean.getSalePrice3();
					}else if(groupSalePrice.equals("4")){
					   pricePerUnit = productmasterBean.getSalePrice4();
					}else if(groupSalePrice.equals("5")){
					   pricePerUnit = productmasterBean.getSalePrice5();
					}else{
					   pricePerUnit = productmasterBean.getSalePrice1();
					}
					   
					discount = productBusiness.getQuanDiscount(productmasterBean.getProductCode(), "1", invoiceCashMasterBean.getInvoiceDate(), tin, CASH);
					   
					inventory = EnjoyUtils.convertNumberToDisplay(productquantityBusiness.getProductquantity(productmasterBean.getProductCode(),tin));
					
					bean.setProductCode(productmasterBean.getProductCode());
					bean.setProductName(productmasterBean.getProductName());
					bean.setPricePerUnit(pricePerUnit);
					bean.setInventory(inventory);
					bean.setUnitCode(productmasterBean.getUnitCode());
					bean.setUnitName(productmasterBean.getUnitName());
					bean.setQuantity("1");
					bean.setDiscount(discount);
					
					invoiceCashDetailList.add(bean);
				}
			}
			
			logger.info("[newRecordForLookUp] size :: " + invoiceCashDetailList.size());
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[newRecordForLookUp][End]");
		}
		
		return SEARCH;
	}
    
    public String getProductDetailByCodeDis() throws Exception{
		logger.info("[getProductDetailByCodeDis][Begin]");
		
		InvoiceCashDetailBean	bean = new InvoiceCashDetailBean();
		ProductmasterBean		productmasterBean;
		String					tin;
		String					inventory				= "";
		String				 	groupSalePrice			= null;
		String					pricePerUnit			= "";
		String					discount				= "";
		
		try{
			
			tin 			= getCurrentUser().getTin();
			groupSalePrice	= customerDetailsBean.getGroupSalePrice();
			
			if(invoiceCashDetailList==null){
				invoiceCashDetailList = new ArrayList<InvoiceCashDetailBean>();
			}
			
			productmasterBean 	= productBusiness.getProductDetailByProductCodeDis(tin, productCodeDis);
			
			if(productmasterBean==null){
				throw new EnjoyException("รหัสสินค้า "+ productCodeDis + " ไม่มีอยู่ในระบบ");
			}
			
			bean.setRowStatus(Constants.NEW);
			
			if(groupSalePrice.equals("2")){
			   pricePerUnit = productmasterBean.getSalePrice2();
			}else if(groupSalePrice.equals("3")){
			   pricePerUnit = productmasterBean.getSalePrice3();
			}else if(groupSalePrice.equals("4")){
			   pricePerUnit = productmasterBean.getSalePrice4();
			}else if(groupSalePrice.equals("5")){
			   pricePerUnit = productmasterBean.getSalePrice5();
			}else{
			   pricePerUnit = productmasterBean.getSalePrice1();
			}
			   
			discount = productBusiness.getQuanDiscount(productmasterBean.getProductCode(), "1", invoiceCashMasterBean.getInvoiceDate(), tin, CASH);
			   
			inventory = EnjoyUtils.convertNumberToDisplay(productquantityBusiness.getProductquantity(productmasterBean.getProductCode(),tin));
			
			bean.setProductCode(productmasterBean.getProductCode());
			bean.setProductName(productmasterBean.getProductName());
			bean.setPricePerUnit(pricePerUnit);
			bean.setInventory(inventory);
			bean.setUnitCode(productmasterBean.getUnitCode());
			bean.setUnitName(productmasterBean.getUnitName());
			bean.setQuantity("1");
			bean.setDiscount(discount);
			
			invoiceCashDetailList.add(bean);
			
			logger.info("[getProductDetailByCodeDis] size :: " + invoiceCashDetailList.size());
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getProductDetailByCodeDis][End]");
		}
		
		return SEARCH;
	}
    
    public String getMultiProductDetailFromList() throws Exception{
		logger.info("[getMultiProductDetailFromList][Begin]");
		
		ProductmasterBean		productmasterBean;
		String					tin;
		String				 	groupSalePrice			= null;
		String					pricePerUnit			= "";
		String					discount				= "";
		
		try{
			
			tin 			= getCurrentUser().getTin();
			groupSalePrice	= customerDetailsBean.getGroupSalePrice();
			
			if(invoiceCashDetailList!=null && !invoiceCashDetailList.isEmpty()){
				for(InvoiceCashDetailBean bean:invoiceCashDetailList){
					if(EnjoyUtils.chkNull(bean.getProductCode())){
						productmasterBean 	= productBusiness.getProductDetail(bean.getProductCode(),tin);
						
						if(groupSalePrice.equals("2")){
						   pricePerUnit = productmasterBean.getSalePrice2();
						}else if(groupSalePrice.equals("3")){
						   pricePerUnit = productmasterBean.getSalePrice3();
						}else if(groupSalePrice.equals("4")){
						   pricePerUnit = productmasterBean.getSalePrice4();
						}else if(groupSalePrice.equals("5")){
						   pricePerUnit = productmasterBean.getSalePrice5();
						}else{
						   pricePerUnit = productmasterBean.getSalePrice1();
						}
						
						discount = productBusiness.getQuanDiscount(productmasterBean.getProductCode(), "1", invoiceCashMasterBean.getInvoiceDate(), tin, CASH);
						
						bean.setPricePerUnit(pricePerUnit);
						bean.setDiscount(discount);
					}
				}
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getMultiProductDetailFromList][End]");
		}
		
		return SEARCH;
	}
    
    public String genPdf() throws Exception{
		logger.info("[genPdf][Begin]");
		
		JSONObject 							jsonObject 			= new JSONObject();
		JSONArray 							detailJSONArray 	= new JSONArray();
		JSONArray 							listJSONArray 		= new JSONArray();
		JSONObject 							objDetail 			= null;
		InvoiceCashMasterBean				invoiceCashMasterDb	= null;
		String								tin					= null;
		ArrayList<InvoiceCashDetailBean> 	voList 				= null;
		CompanyDetailsBean 					companyDetailsDb 	= null;
		CustomerDetailsBean 				customerDetailsDb	= null;
		int									cou					= 0;
		int									couTmp				= 0;
		
		try{
			logger.info("[genPdf] hidInvoiceCode 	:: " + hidInvoiceCode);
			
			tin = getCurrentUser().getTin();
    		
			/*Begin รายละเอียดใบกำกับภาษี*/
			invoiceCashMasterDb 	= invoiceCashBusiness.getInvoiceCashMaster(hidInvoiceCode, tin);
			objDetail = new JSONObject();
			objDetail.put("invoiceCode"		, invoiceCashMasterDb.getInvoiceCode());
			objDetail.put("invoiceDate"		, invoiceCashMasterDb.getInvoiceDate());
			objDetail.put("invoiceType"		, invoiceCashMasterDb.getInvoiceType());
			objDetail.put("cusCode"			, invoiceCashMasterDb.getCusCode());
			objDetail.put("branchName"		, invoiceCashMasterDb.getBranchName());
			objDetail.put("saleUniqueId"	, invoiceCashMasterDb.getSaleUniqueId());
			objDetail.put("saleName"		, invoiceCashMasterDb.getSaleName());
			objDetail.put("saleCommission"	, invoiceCashMasterDb.getSaleCommission());
			objDetail.put("invoicePrice"	, invoiceCashMasterDb.getInvoicePrice());
			objDetail.put("invoicediscount"	, invoiceCashMasterDb.getInvoicediscount());
			objDetail.put("invoiceDeposit"	, invoiceCashMasterDb.getInvoiceDeposit());
			objDetail.put("invoiceVat"		, invoiceCashMasterDb.getInvoiceVat());
			objDetail.put("invoiceTotal"	, invoiceCashMasterDb.getInvoiceTotal());
			objDetail.put("userUniqueId"	, invoiceCashMasterDb.getUserUniqueId());
			objDetail.put("invoiceCredit"	, invoiceCashMasterDb.getInvoiceCredit());
			objDetail.put("invoiceStatus"	, invoiceCashMasterDb.getInvoiceStatus());
			objDetail.put("invoiceTypeDesc"	, invoiceCashMasterDb.getInvoiceTypeDesc());
			objDetail.put("remark"			, invoiceCashMasterDb.getRemark());
			objDetail.put("tin"				, tin);
			objDetail.put("vatDis"			, ConfigFile.getVat() + "%");
			
			jsonObject.put("invoiceCashMaster"	,objDetail);
			/*End รายละเอียดใบกำกับภาษี*/
	
			/*Begin รายละเอียดบริษัท*/
			if(tin!=null && !"".equals(tin)){
				companyDetailsDb 	= companyDetailsBusiness.getCompanyDetails(tin);
				objDetail = new JSONObject();
				objDetail.put("tin"			, companyDetailsDb.getTin());
				objDetail.put("companyName"	, companyDetailsDb.getCompanyName());
				objDetail.put("address"		, companyDetailsDb.getAddress());
				objDetail.put("tel"			, companyDetailsDb.getTel());
				objDetail.put("fax"			, companyDetailsDb.getFax());
				objDetail.put("email"		, companyDetailsDb.getEmail());
				objDetail.put("remark"		, companyDetailsDb.getRemark());
				
				jsonObject.put("companyDetails"	,objDetail);
			}
			/*ENd รายละเอียดบริษัท*/
	
			/*Begin รายละเอียดลูกค้า*/
			if(EnjoyUtils.chkNull(invoiceCashMasterDb.getCusCode())){
				customerDetailsDb 		= customerBusiness.getCustomerDetail(invoiceCashMasterDb.getCusCode(), getCurrentUser().getTin());
				
				objDetail = new JSONObject();
				objDetail.put("cusCode"		, customerDetailsDb.getCusCode());
				objDetail.put("tin"			, customerDetailsDb.getTin());
				objDetail.put("cusName"		, customerDetailsDb.getCusName());
				objDetail.put("cusSurname"	, customerDetailsDb.getCusSurname());
				objDetail.put("branchName"	, customerDetailsDb.getBranchName());
				objDetail.put("sex"			, customerDetailsDb.getSex());
				objDetail.put("idType"		, customerDetailsDb.getIdType());
				objDetail.put("idNumber"	, customerDetailsDb.getIdNumber());
				objDetail.put("birthDate"	, customerDetailsDb.getBirthDate());
				objDetail.put("religion"	, customerDetailsDb.getReligion());
				objDetail.put("job"			, customerDetailsDb.getJob());
				objDetail.put("address"		, customerDetailsDb.getAddress());
				objDetail.put("tel"			, customerDetailsDb.getTel());
				objDetail.put("fax"			, customerDetailsDb.getFax());
				objDetail.put("email"		, customerDetailsDb.getEmail());
				
				jsonObject.put("customerDetails",	objDetail);
			}
			/*End รายละเอียดลูกค้า*/
	
			jsonObject.put(Constants.STATUS, Constants.SUCCESS);
			
			logger.info(jsonObject.toJSONString());
			
			if("V".equals(invoiceCashMasterDb.getInvoiceType())){
				/*Begin รายละเอียดรายการที่ขาย*/
				voList = invoiceCashBusiness.getInvoiceCashDetailList(hidInvoiceCode, tin);
				for(InvoiceCashDetailBean vo:voList){
					objDetail = new JSONObject();
					objDetail.put("invoiceCode"		, vo.getInvoiceCode());
					objDetail.put("seq"				, vo.getSeq());
					objDetail.put("productCode"		, vo.getProductCode());
					objDetail.put("productName"		, vo.getProductName());
					objDetail.put("quantity"		, vo.getQuantity());
					objDetail.put("pricePerUnit"	, vo.getPricePerUnit());
					objDetail.put("discount"		, vo.getDiscount());
					objDetail.put("price"			, vo.getPrice());
					objDetail.put("unitCode"		, vo.getUnitCode());
					objDetail.put("unitName"		, vo.getUnitName());
					
					detailJSONArray.add(objDetail);
					
					if(voList.size()<=12){
						if(cou==(voList.size()-1)){
							listJSONArray.add(detailJSONArray);
						}else{
							cou++;
						}
					}else{
						if(cou == 11 || couTmp==(voList.size()-1)){
							listJSONArray.add(detailJSONArray);
							
							detailJSONArray 	= new JSONArray();
							cou					= 0;
						}else{
							cou++;
						}
					}
					
					
					++couTmp;
				}
				
				jsonObject.put("invoiceCashDetailList"	,listJSONArray);
				/*End รายละเอียดรายการที่ขาย*/
				
//				writePDFWithPadding("FullSlipCashWithPatternPdfForm", jsonObject, "ใบเสร็จรับเงิน", 40f, 5f, 5f, 10f);
				writePDFWithPadding("FullSlipCashWithPatternPdfForm", jsonObject, "ใบเสร็จรับเงิน", 5f, 5f, 5f, 5f);
			}else{
				/*Begin รายละเอียดรายการที่ขาย*/
				voList = invoiceCashBusiness.getInvoiceCashDetailList(hidInvoiceCode, tin);
				for(InvoiceCashDetailBean vo:voList){
					objDetail = new JSONObject();
					objDetail.put("invoiceCode"		, vo.getInvoiceCode());
					objDetail.put("seq"				, vo.getSeq());
					objDetail.put("productCode"		, vo.getProductCode());
					objDetail.put("productName"		, vo.getProductName());
					objDetail.put("quantity"		, vo.getQuantity());
					objDetail.put("pricePerUnit"	, vo.getPricePerUnit());
					objDetail.put("discount"		, vo.getDiscount());
					objDetail.put("price"			, vo.getPrice());
					objDetail.put("unitCode"		, vo.getUnitCode());
					objDetail.put("unitName"		, vo.getUnitName());
					
					listJSONArray.add(objDetail);
				}
				
				jsonObject.put("invoiceCashDetailList"	,listJSONArray);
				
				/*End รายละเอียดรายการที่ขาย*/
				
				writePDFA5("FullSlipCashNoVatPdfForm", jsonObject, "ใบเสร็จรับเงิน");
			}
			
		}catch(Exception e){
			logger.error("genPdf :: ", e);
			throw e;
		}finally{
			logger.info("[genPdf][End]");
		}
		
		return null;
	}
    
    public String onUpdateCredit() throws Exception{
		logger.info("[onUpdateCredit][Begin]");
		
		JSONObject 			obj 				= new JSONObject();
		
		try{
			logger.info("[onUpdateCredit] hidInvoiceCode 	:: " + hidInvoiceCode);
			logger.info("[onUpdateCredit] invoiceCredit 	:: " + invoiceCashMasterBean.getInvoiceCredit());
			
			invoiceCashBusiness.onUpdateCredit(hidInvoiceCode, getCurrentUser(),invoiceCashMasterBean.getInvoiceCredit());
			
		  	obj.put("invoiceCode", hidInvoiceCode);
		   
		  	writeMSG(obj.toString());
		   
		}catch(Exception e){
			logger.error("onUpdateCredit :: ", e);
			throw e;
		}finally{
			logger.info("[onUpdateCredit][End]");
		}
		
		return null;
	}
    
}







