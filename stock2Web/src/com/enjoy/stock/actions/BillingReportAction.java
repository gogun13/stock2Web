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
import com.enjoy.core.business.CompanyDetailsBusiness;
import com.enjoy.core.main.ConfigFile;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.CustomerDetailsBean;
import com.enjoy.stock.bean.InvoiceCreditMasterBean;
import com.enjoy.stock.business.CustomerBusiness;
import com.enjoy.stock.business.InvoiceCreditBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class BillingReportAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BillingReportAction.class);
	
	private InvoiceCreditMasterBean 	invoiceCreditMasterBean;
	private ArrayList<ComboBean> 		priceTypeList;
	private boolean						includeVat;
	
	@Autowired
	CustomerBusiness customerBusiness;
	@Autowired
	InvoiceCreditBusiness invoiceCreditBusiness;
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	invoiceCreditMasterBean 	= new InvoiceCreditMasterBean();
    	
    	priceTypeList = new ArrayList<ComboBean>();
		priceTypeList.add(new ComboBean(""	, "ทั้งหมด"));
		priceTypeList.add(new ComboBean("V","มี VAT"));
		priceTypeList.add(new ComboBean("N","ไม่มี VAT"));
    	
		return SUCCESS;
	}
    
    public String showData() throws Exception{
		logger.info("[showData][Begin]");
		
		JSONObject 							jsonObject 				= new JSONObject();
		JSONArray 							jSONArray 				= new JSONArray();
		JSONObject 							objDetail 				= null;
		String								tin 					= null;
		CompanyDetailsBean 					companyDetailsDb 		= null;
		CustomerDetailsBean 				customerDetailsDb		= null;
		ArrayList<InvoiceCreditMasterBean> 	invoiceCreditMasterList = null;
		InvoiceCreditMasterBean 			invoiceCreditMasterSum 	= null;
		double								sumvat					= 0;
		String								sumvatStr				= "";
		
		try{
			tin = getCurrentUser().getTin();
			
			logger.info("[showData] cusCode  		:: " + invoiceCreditMasterBean.getCusCode());
			logger.info("[showData] invoiceCode 	:: " + invoiceCreditMasterBean.getInvoiceCode());
			logger.info("[showData] invoiceType 	:: " + invoiceCreditMasterBean.getInvoiceType());
			logger.info("[showData] invoiceDateFrom :: " + invoiceCreditMasterBean.getInvoiceDateForm());
			logger.info("[showData] invoiceDateTo 	:: " + invoiceCreditMasterBean.getInvoiceDateTo());
			logger.info("[showData] includeVat 		:: " + includeVat);
			
			/*Begin รายการใบวางบิล*/
			invoiceCreditMasterBean.setTin(tin);
			invoiceCreditMasterList = invoiceCreditBusiness.searchForBillingReport(invoiceCreditMasterBean);
			
			for(InvoiceCreditMasterBean vo:invoiceCreditMasterList){
				objDetail = new JSONObject();
				objDetail.put("invoiceCode"		, vo.getInvoiceCode());
				objDetail.put("invoiceDate"		, vo.getInvoiceDate());
				objDetail.put("invoicePrice"	, vo.getInvoicePrice());
				objDetail.put("invoicediscount"	, vo.getInvoicediscount());
				objDetail.put("invoiceDeposit"	, vo.getInvoiceDeposit());
				objDetail.put("invoiceVat"		, vo.getInvoiceVat());
				objDetail.put("invoiceTotal"	, vo.getInvoiceTotal());
				
				jSONArray.add(objDetail);
			}
			jsonObject.put("invoiceCreditMasterList"	,jSONArray);
			/*End รายการใบวางบิล*/
			
			/*Begin รายละเอียดใบวางบิล*/
			objDetail = new JSONObject();
			invoiceCreditMasterSum = invoiceCreditBusiness.sumTotalForBillingReport(invoiceCreditMasterBean);
			objDetail.put("sumInvoicePrice"		,invoiceCreditMasterSum.getInvoicePrice());
			objDetail.put("sumInvoicediscount"	,invoiceCreditMasterSum.getInvoicediscount());
			objDetail.put("sumInvoiceDeposit"	,invoiceCreditMasterSum.getInvoiceDeposit());
			
			if(includeVat){
				sumvat = ((EnjoyUtils.parseDouble(ConfigFile.getVat()) + 100) * EnjoyUtils.parseDouble(invoiceCreditMasterSum.getInvoicePrice()))/100;
				logger.info("[showData] sumvat :: " + sumvat);
				sumvatStr = EnjoyUtils.convertFloatToDisplay(String.valueOf(sumvat), 2);
			}else{
				sumvatStr = invoiceCreditMasterSum.getInvoiceVat();
			}
			
			logger.info("[showData] sumvatStr :: " + sumvatStr);
			
			objDetail.put("sumInvoiceVat"		,sumvatStr);
			objDetail.put("sumInvoiceTotal"		,invoiceCreditMasterSum.getInvoiceTotal());
			objDetail.put("totalRecord"			,String.valueOf(invoiceCreditMasterList.size()));
			objDetail.put("bullingDate"			,EnjoyUtils.dateToThaiDisplay(EnjoyUtils.currDateThai()));
			objDetail.put("tin"					,tin);
			objDetail.put("vatDis"				,ConfigFile.getVat() + "%");
			
			jsonObject.put("billingDetail"	,objDetail);
			/*End รายละเอียดใบวางบิล*/
			
			/*Begin รายละเอียดบริษัท*/
			companyDetailsDb = companyDetailsBusiness.getCompanyDetails(tin);
			objDetail = new JSONObject();
			objDetail.put("tin"			, companyDetailsDb.getTin());
			objDetail.put("companyName"	, companyDetailsDb.getCompanyName());
			objDetail.put("address"		, companyDetailsDb.getAddress());
			objDetail.put("tel"			, companyDetailsDb.getTel());
			objDetail.put("fax"			, companyDetailsDb.getFax());
			objDetail.put("email"		, companyDetailsDb.getEmail());
			objDetail.put("remark"		, companyDetailsDb.getRemark());
			
			jsonObject.put("companyDetails"	,objDetail);
			/*ENd รายละเอียดบริษัท*/
			
			/*Begin รายละเอียดลูกค้า*/
			customerDetailsDb = customerBusiness.getCustomerDetail(invoiceCreditMasterBean.getCusCode(),invoiceCreditMasterBean.getTin());
			objDetail = new JSONObject();
			objDetail.put("cusCode"		, customerDetailsDb.getCusCode());
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
			/*End รายละเอียดลูกค้า*/
			
			writePDF("BillingPdfForm", jsonObject, "ใบวางบิล");
			
		}catch(Exception e){
			logger.error("showData :: ", e);
			throw e;
		}finally{
			logger.info("[showData][End]");
		}
		
		return null;
	}
    
    public String openCustomerLookUp()throws Exception {
    	
    	return "openCustomerLookUp";
    }
    
    @Override
    public String autoComplete()throws Exception {
    	return null;
    }
    
    
}
