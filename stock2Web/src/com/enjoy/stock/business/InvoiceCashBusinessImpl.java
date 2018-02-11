package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.core.business.RefconstantcodeBusiness;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.InvoiceCashDetailBean;
import com.enjoy.stock.bean.InvoiceCashMasterBean;
import com.enjoy.stock.bean.InvoiceCreditMasterBean;
import com.enjoy.stock.bean.ProductquantityBean;
import com.enjoy.stock.services.InvoiceCashService;
import com.enjoy.stock.services.InvoiceCreditService;
import com.enjoy.stock.services.ProductQuanHistoryService;
import com.enjoy.stock.services.ProductquantityService;

@Service
public class InvoiceCashBusinessImpl extends AbstractBusiness implements InvoiceCashBusiness{
	private static final Logger logger = Logger.getLogger(InvoiceCashBusinessImpl.class);
	
	@Autowired
	InvoiceCashService invoiceCashService;
	@Autowired
	RefconstantcodeBusiness refconstantcodeBusiness;
	@Autowired
	ProductquantityService productquantityService;
	@Autowired
	ProductQuanHistoryService productQuanHistoryService;
	@Autowired
	ProductBusiness productBusiness;
	@Autowired
	InvoiceCreditService invoiceCreditService;

	@Override
	public void searchByCriteria(PaginatedListBean paginatedList,InvoiceCashMasterBean invoiceCashMasterBean) throws Exception {
		invoiceCashService.searchByCriteria(paginatedList, invoiceCashMasterBean);
		
	}

	@Override
	public InvoiceCashMasterBean getInvoiceCashMaster(String invoiceCode,String tin) throws Exception {
		return invoiceCashService.getInvoiceCashMaster(invoiceCode, tin);
	}

	@Override
	public ArrayList<InvoiceCashDetailBean> getInvoiceCashDetailList(String invoiceCode, String tin) throws Exception {
		return invoiceCashService.getInvoiceCashDetailList(invoiceCode, tin);
	}

	@Override
	public String save(InvoiceCashMasterBean invoiceCashMasterBean,ArrayList<InvoiceCashDetailBean> invoiceCashDetailList, UserDetailsBean userData) throws Exception {
		logger.info("[save][Begin]");
		
		String 	invoiceCode = null;
		String 	codeDisplay;
		String 	tin;
		int		seqDb	= 1;
		String	quantityDb;
		ProductquantityBean	productquantityBean;
		double	quantity = 0.00;
		
		try{
			/*Begin Section รายละเอียดใบสั่งซื้อ*/
			tin			= userData.getTin();
			codeDisplay = refconstantcodeBusiness.getCodeDisplay(invoiceCashMasterBean.getInvoiceType().equals("V")?"3":"4", tin);
			invoiceCode = String.valueOf(invoiceCashService.genId(codeDisplay, tin));
			
			invoiceCashMasterBean.setInvoiceCode			(invoiceCode);
			invoiceCashMasterBean.setInvoiceCredit			("");
			invoiceCashMasterBean.setUserUniqueId			(String.valueOf(userData.getUserUniqueId()));
			invoiceCashMasterBean.setInvoiceStatus			("A");
			invoiceCashMasterBean.setTin					(tin);
			
			invoiceCashService.insertInvoiceCashMaster(invoiceCashMasterBean);
			
			/*Begin manage รายการสินค้า*/
			invoiceCashService.deleteInvoiceCashDetail(invoiceCode, tin);
			for(InvoiceCashDetailBean bean:invoiceCashDetailList){
				bean.setInvoiceCode	(invoiceCode);
				bean.setTin			(tin);
				bean.setSeqDb		(String.valueOf(seqDb++));
				invoiceCashService.insertInvoiceCashDetail(bean);
				
				/*Begin Section รายการสินค้า*/
				quantityDb = productquantityService.getProductquantity(bean.getProductCode(),tin);
				
				productquantityBean = new ProductquantityBean();
				
				productquantityBean.setProductCode(bean.getProductCode());
				productquantityBean.setTin(tin);
				if(quantityDb==null){
					quantity =  EnjoyUtils.parseDouble("0");
					productquantityBean.setQuantity(String.valueOf(quantity));
					productquantityService.insertProductquantity(productquantityBean);
				}else{
					quantity = EnjoyUtils.parseDouble(quantityDb) - EnjoyUtils.parseDouble(bean.getQuantity());
					productquantityBean.setQuantity(String.valueOf(quantity));
					productquantityService.updateProductquantity(productquantityBean);
				}
				/*End Section รายการสินค้า*/
			}
			/*End manage รายการสินค้า*/
			
			/*End Section รายละเอียดใบสั่งซื้อ*/
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		return invoiceCode;
	}
	
	@Override
	public void edit(InvoiceCashMasterBean invoiceCashMasterBean,ArrayList<InvoiceCashDetailBean> invoiceCashDetailList, UserDetailsBean userData, String invoiceCode) throws Exception {
		logger.info("[save][Begin]");
		
		String 	tin;
		int		seqDb	= 1;
		String	quantityDb;
		ProductquantityBean	productquantityBean;
		double	quantity = 0.00;
		ArrayList<InvoiceCashDetailBean> invoiceCashDetailDbList;
		
		try{
			/*Begin Section รายละเอียดใบสั่งซื้อ*/
			tin			= userData.getTin();
			
			invoiceCashMasterBean.setInvoiceCode			(invoiceCode);
			invoiceCashMasterBean.setUserUniqueId			(String.valueOf(userData.getUserUniqueId()));
			invoiceCashMasterBean.setTin					(tin);
			
			invoiceCashService.updateInvoiceCashMaster(invoiceCashMasterBean);
			
			invoiceCashDetailDbList = getInvoiceCashDetailList(invoiceCode, tin);
			if(invoiceCashDetailDbList!=null && !invoiceCashDetailDbList.isEmpty()){
				for(InvoiceCashDetailBean vo:invoiceCashDetailDbList){
					/*Begin Section รายการสินค้า*/
					quantityDb = productquantityService.getProductquantity(vo.getProductCode(),tin);
					
					productquantityBean = new ProductquantityBean();
					productquantityBean.setProductCode(vo.getProductCode());
					productquantityBean.setTin(tin);
					
					if(quantityDb==null){
						quantity =  EnjoyUtils.parseDouble("0");
						productquantityBean.setQuantity(String.valueOf(quantity));
						productquantityService.insertProductquantity(productquantityBean);
					}else{
						quantity = EnjoyUtils.parseDouble(quantityDb) + EnjoyUtils.parseDouble(vo.getQuantity());
						productquantityBean.setQuantity(String.valueOf(quantity));
						productquantityService.updateProductquantity(productquantityBean);
					}
					/*End Section รายการสินค้า*/
				}
			}
			
			/*Begin manage รายการสินค้า*/
			invoiceCashService.deleteInvoiceCashDetail(invoiceCode, tin);
			for(InvoiceCashDetailBean bean:invoiceCashDetailList){
				bean.setInvoiceCode	(invoiceCode);
				bean.setTin			(tin);
				bean.setSeqDb		(String.valueOf(seqDb++));
				invoiceCashService.insertInvoiceCashDetail(bean);
				
				/*Begin Section รายการสินค้า*/
				quantityDb = productquantityService.getProductquantity(bean.getProductCode(),tin);
				
				productquantityBean = new ProductquantityBean();
				
				productquantityBean.setProductCode(bean.getProductCode());
				productquantityBean.setTin(tin);
				if(quantityDb==null){
					quantity =  EnjoyUtils.parseDouble("0");
					productquantityBean.setQuantity(String.valueOf(quantity));
					productquantityService.insertProductquantity(productquantityBean);
				}else{
					quantity = EnjoyUtils.parseDouble(quantityDb) - EnjoyUtils.parseDouble(bean.getQuantity());
					productquantityBean.setQuantity(String.valueOf(quantity));
					productquantityService.updateProductquantity(productquantityBean);
				}
				/*End Section รายการสินค้า*/
			}
			/*End manage รายการสินค้า*/
			
			/*End Section รายละเอียดใบสั่งซื้อ*/
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
	}

	@Override
	public void updateInvoiceCashMasterStatus(InvoiceCashMasterBean vo)throws Exception {
		invoiceCashService.updateInvoiceCashMasterStatus(vo);
	}

	@Override
	public void cancel(String invoiceCode, UserDetailsBean userData, String invoiceCredit)throws Exception {
		logger.info("[cancel][Begin]");
		
		InvoiceCashMasterBean  				invoiceCashMasterBean		= new InvoiceCashMasterBean();
		ArrayList<InvoiceCashDetailBean> 	invoiceCashDetailList		= null;
		String 	tin;
		String	quantityDb;
		ProductquantityBean	productquantityBean;
		double	quantity = 0.00;
		
		try{
			tin = userData.getTin();
			
			invoiceCashMasterBean.setInvoiceStatus			("C");
			invoiceCashMasterBean.setUserUniqueId			(String.valueOf(userData.getUserUniqueId()));
			invoiceCashMasterBean.setInvoiceCode			(invoiceCode);
			invoiceCashMasterBean.setTin					(tin);
			
			invoiceCashService.updateInvoiceCashMasterStatus(invoiceCashMasterBean);
			
			invoiceCashDetailList = getInvoiceCashDetailList(invoiceCode, tin);
			if(invoiceCashDetailList!=null && !invoiceCashDetailList.isEmpty()){
				for(InvoiceCashDetailBean vo:invoiceCashDetailList){
					/*Begin Section รายการสินค้า*/
					quantityDb = productquantityService.getProductquantity(vo.getProductCode(),tin);
					
					productquantityBean = new ProductquantityBean();
					productquantityBean.setProductCode(vo.getProductCode());
					productquantityBean.setTin(tin);
					
					if(quantityDb==null){
						quantity =  EnjoyUtils.parseDouble("0");
						productquantityBean.setQuantity(String.valueOf(quantity));
						productquantityService.insertProductquantity(productquantityBean);
					}else{
						quantity = EnjoyUtils.parseDouble(quantityDb) + EnjoyUtils.parseDouble(vo.getQuantity());
						productquantityBean.setQuantity(String.valueOf(quantity));
						productquantityService.updateProductquantity(productquantityBean);
					}
					/*End Section รายการสินค้า*/
				}
			}
			
//			if(EnjoyUtils.chkNull(invoiceCredit)){
//				InvoiceCreditMasterBean invoiceCreditMasterBean		= new InvoiceCreditMasterBean();
//				invoiceCreditMasterBean.setInvoiceStatus("C");
//				invoiceCreditMasterBean.setInvoiceCode	(invoiceCredit);
//				invoiceCreditMasterBean.setTin			(tin);
//				
//				invoiceCreditService.updateInvoiceCreditMasterStatus(invoiceCreditMasterBean);
//			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[cancel][End]");
		}
	}

	@Override
	public void onUpdateCredit(String invoiceCode, UserDetailsBean userData	,String invoiceCredit) throws Exception {
		logger.info("[onUpdateCredit][Begin]");
		
		try{
			//จะไม่มา gen record ใบกำกับภาษีจากบันทึกเงินเชื่อแล้วเนื่องจากเลขใบกำกับภาษีสำคัญกับการยื่ให้กรมสรรพากร
//			InvoiceCashMasterBean invoiceCashMasterBean		= new InvoiceCashMasterBean();
//			invoiceCashMasterBean.setInvoiceCode			(invoiceCode);
//			invoiceCashMasterBean.setInvoiceStatus			("A");
//			invoiceCashMasterBean.setTin					(userData.getTin());
//			
//			invoiceCashService.updateInvoiceCashMasterStatus(invoiceCashMasterBean);
			
			InvoiceCreditMasterBean invoiceCreditMasterBean = new InvoiceCreditMasterBean();
			invoiceCreditMasterBean.setInvoiceCode			(invoiceCredit);
			invoiceCreditMasterBean.setInvoiceStatus		("S");
			invoiceCreditMasterBean.setTin					(userData.getTin());
			
			invoiceCreditService.updateInvoiceCreditMasterStatus(invoiceCreditMasterBean);
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[onUpdateCredit][End]");
		}
		
	}

	

	

}
