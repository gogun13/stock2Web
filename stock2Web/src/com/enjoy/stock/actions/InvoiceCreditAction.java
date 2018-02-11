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
import com.enjoy.stock.bean.InvoiceCreditDetailBean;
import com.enjoy.stock.bean.InvoiceCreditMasterBean;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.business.CustomerBusiness;
import com.enjoy.stock.business.InvoiceCreditBusiness;
import com.enjoy.stock.business.ProductBusiness;
import com.enjoy.stock.business.ProductquantityBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceCreditAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InvoiceCreditAction.class);
	private static final String 	CREDIT 				= "CR";
	
	private InvoiceCreditMasterBean 			invoiceCreditMasterBean;
	private CustomerDetailsBean 				customerDetailsBean;
	private UserDetailsBean						userDetailsBean;
	private ArrayList<InvoiceCreditDetailBean>	invoiceCreditDetailList;
	private String								pageMode;
	private String								titlePage;
	private String								indexRow;
	private String								hidInvoiceCode;
	private ArrayList<ComboBean> 				statusCombo;
	private String								showBackFlag;
	private String								systemVat;
	private ArrayList<ComboBean> 				priceTypeList;
	private String								hidProductCodeSelect;
	private String								productCodeDis;
	private String								cusCodeDis;
	
	@Autowired
	InvoiceCreditBusiness invoiceCreditBusiness;
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
    	
    	invoiceCreditMasterBean	= new InvoiceCreditMasterBean();
    	customerDetailsBean		= new CustomerDetailsBean();
    	userDetailsBean			= new UserDetailsBean();
    	invoiceCreditDetailList	= new ArrayList<InvoiceCreditDetailBean>();
    	titlePage 				= "บันทึกการขายเงินเชื่อ";
    	pageMode				= Constants.NEW;
    	hidInvoiceCode			= "";
    	showBackFlag			= "N";
    	systemVat				= ConfigFile.getVat();
    	hidProductCodeSelect	= "";
    	productCodeDis			= "";
    	cusCodeDis				= "";
    	
    	/*Begin set default value*/
    	invoiceCreditMasterBean.setInvoiceDate(EnjoyUtils.dateToThaiDisplay(EnjoyUtils.currDateThai()));
		invoiceCreditMasterBean.setInvoiceType("N");
		invoiceCreditMasterBean.setInvoiceStatus("A");
		invoiceCreditMasterBean.setSaleName(getCurrentUser().getUserFullName());
		invoiceCreditMasterBean.setSaleUniqueId(String.valueOf(getCurrentUser().getUserUniqueId()));
		
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
			
			titlePage = "แก้ไขรายการการขายเงินเชื่อ";
			
			if(!Constants.EDIT_PAGE.equals(pageMode)){
				pageMode				= Constants.UPDATE;
			}
			
	    	systemVat				= ConfigFile.getVat();
	    	hidProductCodeSelect 	= "";
	    	invoiceCreditMasterBean = invoiceCreditBusiness.getInvoiceCreditMaster(hidInvoiceCode, getCurrentUser().getTin());
	    	productCodeDis			= "";
			
			if(invoiceCreditMasterBean==null){
				throw new EnjoyException("เกิดข้อผิดพลาดในการดึงรายการบันทึกการขายเงินเชื่อ");
			}
			
			invoiceCreditDetailList = invoiceCreditBusiness.getInvoiceCreditDetailList(hidInvoiceCode, getCurrentUser().getTin());
			
			if(EnjoyUtils.chkNull(invoiceCreditMasterBean.getCusCode())){
				cusCodeDis			= invoiceCreditMasterBean.getCusCode();
				customerDetailsBean = customerBusiness.getCustomerDetail(invoiceCreditMasterBean.getCusCode(), getCurrentUser().getTin());
			}else{
				cusCodeDis			= "";
				customerDetailsBean = new CustomerDetailsBean();
			}
			
			if(EnjoyUtils.chkNull(invoiceCreditMasterBean.getSaleUniqueId())){
				userDetailsBean  = userdetailsBusiness.getUserdetail(EnjoyUtils.parseInt(invoiceCreditMasterBean.getSaleUniqueId()));
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
			statusCombo.add(new ComboBean("S"	, "รับเงินเรียบร้อยแล้ว"));
			
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
					invoiceCreditMasterBean.setSaleUniqueId(EnjoyUtils.nullToStr(userDetailsBean.getUserUniqueId()));
					
					if("Y".equals(userDetailsBean.getFlagSalesman())){
						commission = (EnjoyUtils.parseDouble(invoiceCreditMasterBean.getInvoiceTotal()) * EnjoyUtils.parseDouble(userDetailsBean.getCommission()))/100;
					}
					
					invoiceCreditMasterBean.setSaleCommission(String.valueOf(commission).replaceAll(",", ""));
				}else{
					invoiceCreditMasterBean.setSaleUniqueId("0");
					invoiceCreditMasterBean.setSaleCommission("0");
				}
			}
			
			if(EnjoyUtils.chkNull(customerDetailsBean.getCusCode())){
				invoiceCreditMasterBean.setCusCode(customerDetailsBean.getCusCode());
				invoiceCreditMasterBean.setBranchName(customerDetailsBean.getBranchName());
			}
			
			if(Constants.EDIT_PAGE.equals(pageMode)){
				logger.info("[onSave] hidInvoiceCode 		:: " + hidInvoiceCode);
				
				invoiceCode = hidInvoiceCode;
				pageMode	= Constants.UPDATE;
				invoiceCreditBusiness.edit(invoiceCreditMasterBean, invoiceCreditDetailList, getCurrentUser(), hidInvoiceCode);
			}else{
				invoiceCode = invoiceCreditBusiness.save(invoiceCreditMasterBean, invoiceCreditDetailList, getCurrentUser());
			}
			
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
			
			invoiceCreditBusiness.cancel(hidInvoiceCode, getCurrentUser());
		   
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
	
	public String getProductDetailByName() throws Exception{
	   logger.info("[getProductDetailByName][Begin]");
	   
	   String							productName				= null;
	   JSONObject 						obj		 				= new JSONObject();
	   ProductmasterBean				productmasterBean		= null;
	   String							vendorCode				= null;
	   String							quantity				= null;
	   String							tin						= null;
	   String							inventory				= "";
	   InvoiceCreditDetailBean			vo						= null;
	   String				 			groupSalePrice			= null;
	   String							pricePerUnit			= "";
	   String							discount				= "";
	
	   try{
		   logger.info("[getProductDetailByName] indexRow 	:: " + indexRow);
			
		   vo = invoiceCreditDetailList.get(EnjoyUtils.parseInt(indexRow));
		   
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
			   
			   discount = productBusiness.getQuanDiscount(productmasterBean.getProductCode(), quantity, invoiceCreditMasterBean.getInvoiceDate(), tin, CREDIT);
			   
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
		InvoiceCreditDetailBean bean;
		
		try {
			logger.info("[getDiscount] indexRow 	:: " + indexRow);
			
			bean 		= invoiceCreditDetailList.get(EnjoyUtils.parseInt(indexRow));
			discount 	= productBusiness.getQuanDiscount(bean.getProductCode(), bean.getQuantity(), invoiceCreditMasterBean.getInvoiceDate(), getCurrentUser().getTin(), CREDIT);
			
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
		
		InvoiceCreditDetailBean	bean	= new InvoiceCreditDetailBean();
		
		try{
			
			if(invoiceCreditDetailList==null){
				invoiceCreditDetailList = new ArrayList<InvoiceCreditDetailBean>();
			}
			
			bean.setRowStatus(Constants.NEW);
			
			invoiceCreditDetailList.add(bean);
			
			logger.info("[newRecord] size :: " + invoiceCreditDetailList.size());
			
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
			logger.info("[deleteRecord] size 		:: " + invoiceCreditDetailList.size());
			
			invoiceCreditDetailList.remove(EnjoyUtils.parseInt(indexRow));
			
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
		
		InvoiceCreditDetailBean	bean;
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
			
			if(invoiceCreditDetailList==null){
				invoiceCreditDetailList = new ArrayList<InvoiceCreditDetailBean>();
			}
			
			if(EnjoyUtils.chkNull(hidProductCodeSelect)){
				productCodeArray = hidProductCodeSelect.split("\\|");
				for(String productCode:productCodeArray){
					productmasterBean 	= productBusiness.getProductDetail(productCode, tin);
					bean				= new InvoiceCreditDetailBean();
					
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
					   
					discount = productBusiness.getQuanDiscount(productmasterBean.getProductCode(), "1", invoiceCreditMasterBean.getInvoiceDate(), tin, CREDIT);
					   
					inventory = EnjoyUtils.convertNumberToDisplay(productquantityBusiness.getProductquantity(productmasterBean.getProductCode(),tin));
					
					bean.setProductCode(productmasterBean.getProductCode());
					bean.setProductName(productmasterBean.getProductName());
					bean.setPricePerUnit(pricePerUnit);
					bean.setInventory(inventory);
					bean.setUnitCode(productmasterBean.getUnitCode());
					bean.setUnitName(productmasterBean.getUnitName());
					bean.setQuantity("1");
					bean.setDiscount(discount);
					
					invoiceCreditDetailList.add(bean);
				}
			}
			
			logger.info("[newRecordForLookUp] size :: " + invoiceCreditDetailList.size());
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[newRecordForLookUp][End]");
		}
		
		return SEARCH;
	}
    
    public String getProductDetailByCodeDis() throws Exception{
		logger.info("[getProductDetailByCodeDis][Begin]");
		
		InvoiceCreditDetailBean	bean = new InvoiceCreditDetailBean();
		ProductmasterBean		productmasterBean;
		String					tin;
		String					inventory				= "";
		String				 	groupSalePrice			= null;
		String					pricePerUnit			= "";
		String					discount				= "";
		
		try{
			
			tin 			= getCurrentUser().getTin();
			groupSalePrice	= customerDetailsBean.getGroupSalePrice();
			
			if(invoiceCreditDetailList==null){
				invoiceCreditDetailList = new ArrayList<InvoiceCreditDetailBean>();
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
			   
			discount = productBusiness.getQuanDiscount(productmasterBean.getProductCode(), "1", invoiceCreditMasterBean.getInvoiceDate(), tin, CREDIT);
			   
			inventory = EnjoyUtils.convertNumberToDisplay(productquantityBusiness.getProductquantity(productmasterBean.getProductCode(),tin));
			
			bean.setProductCode(productmasterBean.getProductCode());
			bean.setProductName(productmasterBean.getProductName());
			bean.setPricePerUnit(pricePerUnit);
			bean.setInventory(inventory);
			bean.setUnitCode(productmasterBean.getUnitCode());
			bean.setUnitName(productmasterBean.getUnitName());
			bean.setQuantity("1");
			bean.setDiscount(discount);
			
			invoiceCreditDetailList.add(bean);
			
			logger.info("[getProductDetailByCodeDis] size :: " + invoiceCreditDetailList.size());
			
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
			
			if(invoiceCreditDetailList!=null && !invoiceCreditDetailList.isEmpty()){
				for(InvoiceCreditDetailBean bean:invoiceCreditDetailList){
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
						
						discount = productBusiness.getQuanDiscount(productmasterBean.getProductCode(), "1", invoiceCreditMasterBean.getInvoiceDate(), tin, CREDIT);
						
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
		JSONObject 							objDetail 			= null;
		InvoiceCreditMasterBean				invoiceCreditMasterDb	= null;
		String								tin					= null;
		ArrayList<InvoiceCreditDetailBean> 	voList 				= null;
		CompanyDetailsBean 					companyDetailsDb 	= null;
		CustomerDetailsBean 				customerDetailsDb	= null;
		
		try{
			logger.info("[genPdf] hidInvoiceCode 	:: " + hidInvoiceCode);
			
			tin = getCurrentUser().getTin();
    		
			/*Begin รายละเอียดใบกำกับภาษี*/
			invoiceCreditMasterDb 	= invoiceCreditBusiness.getInvoiceCreditMaster(hidInvoiceCode, tin);
			objDetail = new JSONObject();
			objDetail.put("invoiceCode"		, invoiceCreditMasterDb.getInvoiceCode());
			objDetail.put("invoiceDate"		, invoiceCreditMasterDb.getInvoiceDate());
			objDetail.put("invoiceType"		, invoiceCreditMasterDb.getInvoiceType());
			objDetail.put("cusCode"			, invoiceCreditMasterDb.getCusCode());
			objDetail.put("branchName"		, invoiceCreditMasterDb.getBranchName());
			objDetail.put("saleUniqueId"	, invoiceCreditMasterDb.getSaleUniqueId());
			objDetail.put("saleName"		, invoiceCreditMasterDb.getSaleName());
			objDetail.put("saleCommission"	, invoiceCreditMasterDb.getSaleCommission());
			objDetail.put("invoicePrice"	, invoiceCreditMasterDb.getInvoicePrice());
			objDetail.put("invoicediscount"	, invoiceCreditMasterDb.getInvoicediscount());
			objDetail.put("invoiceDeposit"	, invoiceCreditMasterDb.getInvoiceDeposit());
			objDetail.put("invoiceVat"		, invoiceCreditMasterDb.getInvoiceVat());
			objDetail.put("invoiceTotal"	, invoiceCreditMasterDb.getInvoiceTotal());
			objDetail.put("userUniqueId"	, invoiceCreditMasterDb.getUserUniqueId());
			objDetail.put("invoiceCash"		, invoiceCreditMasterDb.getInvoiceCash());
			objDetail.put("invoiceStatus"	, invoiceCreditMasterDb.getInvoiceStatus());
			objDetail.put("invoiceTypeDesc"	, invoiceCreditMasterDb.getInvoiceTypeDesc());
			objDetail.put("remark"			, invoiceCreditMasterDb.getRemark());
			objDetail.put("tin"				, tin);
			objDetail.put("vatDis"			, ConfigFile.getVat() + "%");
			
			jsonObject.put("invoiceCreditMaster"	,objDetail);
			/*End รายละเอียดใบกำกับภาษี*/
	
			/*Begin รายละเอียดรายการที่ขาย*/
			voList = invoiceCreditBusiness.getInvoiceCreditDetailList(hidInvoiceCode, tin);
			for(InvoiceCreditDetailBean vo:voList){
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
			}
			
			jsonObject.put("invoiceCreditDetailList"	,detailJSONArray);
			/*End รายละเอียดรายการที่ขาย*/
	
	
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
			if(EnjoyUtils.chkNull(invoiceCreditMasterDb.getCusCode())){
				customerDetailsDb 		= customerBusiness.getCustomerDetail(invoiceCreditMasterDb.getCusCode(), getCurrentUser().getTin());
				
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
			
			String pdfName = "FullSlipCreditPdfForm";//หน้าตาบิลเครดิตแบบมี vat และไม่มี vat หน้าตาเหมือนกัน
			
			writePDFA5(pdfName, jsonObject, "ใบเสร็จรับเงิน");
			
		}catch(Exception e){
			logger.error("genPdf :: ", e);
			throw e;
		}finally{
			logger.info("[genPdf][End]");
		}
		
		return null;
	}
    
}







