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
import com.enjoy.stock.bean.InvoiceCreditDetailBean;
import com.enjoy.stock.bean.InvoiceCreditMasterBean;
import com.enjoy.stock.bean.ProductQuanHistoryBean;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.bean.ProductquantityBean;
import com.enjoy.stock.services.InvoiceCashService;
import com.enjoy.stock.services.InvoiceCreditService;
import com.enjoy.stock.services.ProductQuanHistoryService;
import com.enjoy.stock.services.ProductquantityService;

@Service
public class InvoiceCreditBusinessImpl extends AbstractBusiness implements InvoiceCreditBusiness{
	private static final Logger logger = Logger.getLogger(InvoiceCreditBusinessImpl.class);
	
	@Autowired
	InvoiceCreditService invoiceCreditService;
	@Autowired
	RefconstantcodeBusiness refconstantcodeBusiness;
	@Autowired
	ProductquantityService productquantityService;
	@Autowired
	ProductQuanHistoryService productQuanHistoryService;
	@Autowired
	ProductBusiness productBusiness;
	@Autowired
	InvoiceCashService invoiceCashService;
	
	@Override
	public void searchByCriteria(PaginatedListBean paginatedList,InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception {
		invoiceCreditService.searchByCriteria(paginatedList, invoiceCreditMasterBean);
		
	}
	@Override
	public InvoiceCreditMasterBean getInvoiceCreditMaster(String invoiceCode,String tin) throws Exception {
		return invoiceCreditService.getInvoiceCreditMaster(invoiceCode, tin);
	}
	@Override
	public ArrayList<InvoiceCreditDetailBean> getInvoiceCreditDetailList(String invoiceCode, String tin) throws Exception {
		return invoiceCreditService.getInvoiceCreditDetailList(invoiceCode, tin);
	}
	
	@Override
	public String save(InvoiceCreditMasterBean invoiceCreditMasterBean,ArrayList<InvoiceCreditDetailBean> invoiceCreditDetailList, UserDetailsBean userData) throws Exception {
		logger.info("[save][Begin]");
		
		String 	invoiceCreditCode = null;
		String 	codeDisplayCredit;
//		String 	codeDisplayCash;
		String 	tin;
		int		seqDb	= 1;
		String	quantityDb;
		ProductquantityBean	productquantityBean;
		double	quantity = 0.00;
//		ProductQuanHistoryBean		productQuanHistoryBean;
//		ProductmasterBean 			productmasterBeanDb;
//		int	hisCode		= 1;
//		boolean	chkFlag	= true;
//		String invoiceCashCode;
//		InvoiceCashMasterBean invoiceCashMasterBean;
//		InvoiceCashDetailBean invoiceCashDetailBean;
		
		try{
			/*Begin Section รายละเอียดใบสั่งซื้อ*/
			tin					= userData.getTin();
			codeDisplayCredit 	= refconstantcodeBusiness.getCodeDisplay(invoiceCreditMasterBean.getInvoiceType().equals("V")?"3":"4", tin);
//			codeDisplayCash 	= refconstantcodeBusiness.getCodeDisplay(invoiceCreditMasterBean.getInvoiceType().equals("V")?"5":"6", tin);
			invoiceCreditCode 	= String.valueOf(invoiceCreditService.genId(codeDisplayCredit, tin));
//			invoiceCashCode 	= String.valueOf(invoiceCashService.genId(codeDisplayCash, tin));
			
			invoiceCreditMasterBean.setInvoiceCode			(invoiceCreditCode);
//			invoiceCreditMasterBean.setInvoiceCash			(invoiceCashCode);
			invoiceCreditMasterBean.setUserUniqueId			(String.valueOf(userData.getUserUniqueId()));
			invoiceCreditMasterBean.setInvoiceStatus		("A");
			invoiceCreditMasterBean.setTin					(tin);
			
			invoiceCreditService.insertInvoiceCreditMaster(invoiceCreditMasterBean);
			
			/*Begin manage รายการสินค้า*/
			invoiceCreditService.deleteInvoiceCreditDetail(invoiceCreditCode, tin);
			for(InvoiceCreditDetailBean bean:invoiceCreditDetailList){
				bean.setInvoiceCode	(invoiceCreditCode);
				bean.setTin			(tin);
				bean.setSeqDb		(String.valueOf(seqDb++));
				invoiceCreditService.insertInvoiceCreditDetail(bean);
				
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
				
				/*Begin ส่วนประวัตเพิ่มลดสินค้า*/
//				productQuanHistoryBean = new ProductQuanHistoryBean();
//				productQuanHistoryBean.setFormRef(invoiceCreditCode);
//				
//				productmasterBeanDb = productBusiness.getProductDetail(bean.getProductCode(), tin);
//				if(productmasterBeanDb!=null){
//					productQuanHistoryBean.setProductType(productmasterBeanDb.getProductTypeCode());
//					productQuanHistoryBean.setProductGroup(productmasterBeanDb.getProductGroupCode());
//					productQuanHistoryBean.setProductCode(productmasterBeanDb.getProductCode());
//				}else{
//					productQuanHistoryBean.setProductType("");
//					productQuanHistoryBean.setProductGroup("");
//					productQuanHistoryBean.setProductCode("");
//				}
//				productQuanHistoryBean.setTin(tin);
//				productQuanHistoryBean.setQuantityPlus("0");
//				productQuanHistoryBean.setQuantityMinus(bean.getQuantity());
//				productQuanHistoryBean.setQuantityTotal(String.valueOf(quantity));
//				
//				/*Begin หา hisCode*/
//				if(chkFlag==true){
//					hisCode		= productQuanHistoryService.genId(tin);
//					chkFlag  	= false;
//				}else{
//					hisCode++;
//				}
//				/*End หา hisCode*/
//				productQuanHistoryService.insert(productQuanHistoryBean, hisCode);
				/*End ส่วนประวัตเพิ่มลดสินค้า*/
			}
			/*End manage รายการสินค้า*/
			
			/*End Section รายละเอียดใบสั่งซื้อ*/
			
			/*Begin บันทึกลง table ขายเงิดสด*/
//			invoiceCashMasterBean = new InvoiceCashMasterBean();
//			
//			invoiceCashMasterBean.setInvoiceCode		(invoiceCashCode);
//			invoiceCashMasterBean.setInvoiceDate		(invoiceCreditMasterBean.getInvoiceDate());
//			invoiceCashMasterBean.setInvoiceType		(invoiceCreditMasterBean.getInvoiceType());
//			invoiceCashMasterBean.setCusCode			(invoiceCreditMasterBean.getCusCode());
//			invoiceCashMasterBean.setBranchName			(invoiceCreditMasterBean.getBranchName());
//			invoiceCashMasterBean.setSaleUniqueId		(invoiceCreditMasterBean.getSaleUniqueId());
//			invoiceCashMasterBean.setSaleCommission		(invoiceCreditMasterBean.getSaleCommission());
//			invoiceCashMasterBean.setInvoicePrice		(invoiceCreditMasterBean.getInvoicePrice());
//			invoiceCashMasterBean.setInvoicediscount	(invoiceCreditMasterBean.getInvoicediscount());
//			invoiceCashMasterBean.setInvoiceDeposit		(invoiceCreditMasterBean.getInvoiceDeposit());
//			invoiceCashMasterBean.setInvoiceVat			(invoiceCreditMasterBean.getInvoiceVat());
//			invoiceCashMasterBean.setInvoiceTotal		(invoiceCreditMasterBean.getInvoiceTotal());
//			invoiceCashMasterBean.setInvoiceCredit		(invoiceCreditCode);
//			invoiceCashMasterBean.setUserUniqueId		(String.valueOf(userData.getUserUniqueId()));
//			invoiceCashMasterBean.setInvoiceStatus		("W");
//			invoiceCashMasterBean.setTin				(tin);
//			invoiceCashMasterBean.setRemark				(invoiceCreditMasterBean.getRemark());
//			
//			invoiceCashService.insertInvoiceCashMaster(invoiceCashMasterBean);
//			
//			invoiceCashService.deleteInvoiceCashDetail(invoiceCashCode, tin);
//			seqDb = 1;
//			for(InvoiceCreditDetailBean bean:invoiceCreditDetailList){
//				invoiceCashDetailBean = new InvoiceCashDetailBean();
//				
//				invoiceCashDetailBean.setInvoiceCode		(invoiceCashCode);
//				invoiceCashDetailBean.setTin				(tin);
//				invoiceCashDetailBean.setSeqDb				(String.valueOf(seqDb++));
//				invoiceCashDetailBean.setProductCode		(bean.getProductCode());
//				invoiceCashDetailBean.setQuantity			(bean.getQuantity());
//				invoiceCashDetailBean.setPricePerUnit		(bean.getPricePerUnit());
//				invoiceCashDetailBean.setDiscount			(bean.getDiscount());
//				invoiceCashDetailBean.setPrice				(bean.getPrice());
//				
//				invoiceCashService.insertInvoiceCashDetail(invoiceCashDetailBean);
//			}
			/*End บันทึกลง table ขายเงิดสด*/
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		return invoiceCreditCode;
	}

	@Override
	public void updateInvoiceCreditMasterStatus(InvoiceCreditMasterBean vo)throws Exception {
		invoiceCreditService.updateInvoiceCreditMasterStatus(vo);
	}

	@Override
	public void cancel(String invoiceCode, UserDetailsBean userData)throws Exception {
		logger.info("[cancel][Begin]");
		
		InvoiceCreditMasterBean  			invoiceCreditMasterBean		= new InvoiceCreditMasterBean();
		ArrayList<InvoiceCreditDetailBean> 	invoiceCreditDetailList		= null;
		String 	tin;
		String	quantityDb;
		ProductquantityBean	productquantityBean;
		double	quantity = 0.00;
		
		try{
			tin = userData.getTin();
			
			invoiceCreditMasterBean.setInvoiceStatus		("C");
			invoiceCreditMasterBean.setUserUniqueId			(String.valueOf(userData.getUserUniqueId()));
			invoiceCreditMasterBean.setInvoiceCode			(invoiceCode);
			invoiceCreditMasterBean.setTin					(tin);
			
			invoiceCreditService.updateInvoiceCreditMasterStatus(invoiceCreditMasterBean);
			
			invoiceCreditDetailList = getInvoiceCreditDetailList(invoiceCode, tin);
			if(invoiceCreditDetailList!=null && !invoiceCreditDetailList.isEmpty()){
				for(InvoiceCreditDetailBean vo:invoiceCreditDetailList){
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
			
//			invoiceCashService.cancelInvoiceCashMasterByInvoiceCredit(invoiceCode,tin);//แอบยกเลิกฝั่งเงินสดด้วย ปล.ค้นหาด้วยสถานะยกเงิกฝั่งเงินสดก็จะไม่เจอ
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[cancel][End]");
		}
	}
	@Override
	public void searchByCriteriaForCredit(PaginatedListBean paginatedList,InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception {
		invoiceCreditService.searchByCriteriaForCredit(paginatedList, invoiceCreditMasterBean);
		
	}
	@Override
	public ArrayList<InvoiceCreditMasterBean> searchForBillingReport(InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception {
		return invoiceCreditService.searchForBillingReport(invoiceCreditMasterBean);
	}
	@Override
	public InvoiceCreditMasterBean sumTotalForBillingReport(InvoiceCreditMasterBean invoiceCreditMasterBean) throws Exception {
		return invoiceCreditService.sumTotalForBillingReport(invoiceCreditMasterBean);
	}
	
	@Override
	public void edit(InvoiceCreditMasterBean invoiceCreditMasterBean,ArrayList<InvoiceCreditDetailBean> invoiceCreditDetailList, UserDetailsBean userData,String invoiceCreditCode) throws Exception {
		logger.info("[edit][Begin]");
		
		String 	tin;
		int		seqDb	= 1;
		String	quantityDb;
		ProductquantityBean	productquantityBean;
		double	quantity = 0.00;
		ArrayList<InvoiceCreditDetailBean>	invoiceCreditDetailOldList;
		
		try{
			logger.info("[edit] invoiceCreditCode :: " + invoiceCreditCode);
			
			/*Begin Section รายละเอียดใบสั่งซื้อ*/
			tin					= userData.getTin();
			
			invoiceCreditMasterBean.setInvoiceCode			(invoiceCreditCode);
			invoiceCreditMasterBean.setUserUniqueId			(String.valueOf(userData.getUserUniqueId()));
			invoiceCreditMasterBean.setTin					(tin);
			
			invoiceCreditService.updateInvoiceCreditMaster(invoiceCreditMasterBean);
			
			/*Begin เลือกรายการสินค้าเก่าจาก table เพื่อมา update ยอด stock กลับก่อนลบ*/
			invoiceCreditDetailOldList = getInvoiceCreditDetailList(invoiceCreditCode, tin);
			if(invoiceCreditDetailOldList!=null && !invoiceCreditDetailOldList.isEmpty()){
				for(InvoiceCreditDetailBean vo:invoiceCreditDetailOldList){
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
			/*End เลือกรายการสินค้าเก่าจาก table เพื่อมา update ยอด stock กลับก่อนลบ*/
			
			/*Begin manage รายการสินค้า*/
			invoiceCreditService.deleteInvoiceCreditDetail(invoiceCreditCode, tin);
			for(InvoiceCreditDetailBean bean:invoiceCreditDetailList){
				bean.setInvoiceCode	(invoiceCreditCode);
				bean.setTin			(tin);
				bean.setSeqDb		(String.valueOf(seqDb++));
				invoiceCreditService.insertInvoiceCreditDetail(bean);
				
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

	

	

}
