package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.core.business.RefconstantcodeBusiness;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.ComparePriceBean;
import com.enjoy.stock.bean.ProductQuanHistoryBean;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.bean.ProductquantityBean;
import com.enjoy.stock.bean.ReciveOrdeDetailBean;
import com.enjoy.stock.bean.ReciveOrderMasterBean;
import com.enjoy.stock.services.ProductQuanHistoryService;
import com.enjoy.stock.services.ProductquantityService;
import com.enjoy.stock.services.ReciveStockService;

@Service
public class ReciveStockBusinessImpl extends AbstractBusiness implements ReciveStockBusiness{
	private static final Logger logger = Logger.getLogger(ReciveStockBusinessImpl.class);
	
	@Autowired
	ReciveStockService reciveStockService;
	@Autowired
	RefconstantcodeBusiness refconstantcodeBusiness;
	@Autowired
	ComparePriceBusiness comparePriceBusiness;
	@Autowired
	ProductquantityService productquantityService;
	@Autowired
	ProductQuanHistoryService productQuanHistoryService;
	@Autowired
	ProductBusiness productBusiness;

	@Override
	public void searchByCriteria(PaginatedListBean paginatedList,ReciveOrderMasterBean reciveOrderMasterBean, String tin)throws Exception {
		reciveStockService.searchByCriteria(paginatedList, reciveOrderMasterBean, tin);
	}

	@Override
	public ReciveOrderMasterBean getReciveOrderMaster(String reciveNo,String tin) throws Exception {
		return reciveStockService.getReciveOrderMaster(reciveNo, tin);
	}

	@Override
	public ArrayList<ReciveOrdeDetailBean> getReciveOrdeDetailList(String reciveNo, String tin) throws Exception {
		return reciveStockService.getReciveOrdeDetailList(reciveNo, tin);
	}

	@Override
	public ArrayList<ComboBean> getRefReciveOrderStatusCombo() throws Exception {
		return reciveStockService.getRefReciveOrderStatusCombo();
	}

	@Override
	public String save(ReciveOrderMasterBean reciveOrderMasterBean,ArrayList<ReciveOrdeDetailBean> reciveOrdeDetailList,String pageMode, String currReciveStatus) throws Exception {
		logger.info("[save][Begin]");
		
		String					reciveNo				= null;
		String 					codeDisplay 			= null;
		String					reciveStatus			= null;
		String					tin						= null;
		int						seqDb					= 1;
		int						cou						= 0;
		String					vendorCode				= null;
		int						comparePriceSeq			= 1;
		boolean					chkFlag					= true;
		String					quantityDb				= null;
		ProductquantityBean 	productquantityBean 	= null;
		double					quantity				= 0;
		ProductQuanHistoryBean 	productQuanHistoryBean 	= null;
		ProductmasterBean 		productmasterBeanDb 	= null;
		int						hisCode					= 1;
		boolean					chkHisCodeFlag			= true;
		
		try{
			reciveStatus 	= reciveOrderMasterBean.getReciveStatus();
			tin			 	= reciveOrderMasterBean.getTin();
			vendorCode		= reciveOrderMasterBean.getVendorCode();
			
			logger.info("[save] tin 				:: " + tin);
			logger.info("[save] reciveStatus 		:: " + reciveStatus);
			logger.info("[save] currReciveStatus 	:: " + currReciveStatus);
			logger.info("[save] pageMode 			:: " + pageMode);
			logger.info("[save] vendorCode 			:: " + vendorCode);
			
			/*Begin Section รายละเอียดใบสั่งซื้อ*/
			if(pageMode.equals(Constants.NEW)){
				codeDisplay = refconstantcodeBusiness.getCodeDisplay("2", tin);
				reciveNo 	= reciveStockService.genReciveNo(codeDisplay,tin);
				
				reciveOrderMasterBean.setReciveNo(reciveNo);
				
				reciveStockService.insertReciveordermaster(reciveOrderMasterBean);
			}else{
				reciveNo = reciveOrderMasterBean.getReciveNo();
				
				if(reciveStatus.equals("") || reciveStatus.equals("1") || reciveStatus.equals("2")){
					reciveStockService.updateReciveOrderMaster(reciveOrderMasterBean);
				}else{
					reciveStockService.updateReciveOrderMasterSpecial(reciveOrderMasterBean);
				}
			}
			/*End Section รายละเอียดใบสั่งซื้อ*/
			
			/*Begin Section รายการสินค้า*/
			if(reciveStatus.equals("") || reciveStatus.equals("1") || reciveStatus.equals("2")){
				reciveStockService.deleteReciveordedetail(reciveNo, tin);
				
				for(ReciveOrdeDetailBean vo:reciveOrdeDetailList){
					vo.setReciveNo	(reciveNo);
					vo.setTin		(tin);
					vo.setSeqDb		(String.valueOf(seqDb++));
					
					reciveStockService.insertReciveOrdeDetail(vo);
					
					/*Begin Update เปรียบเทียบราคา*/
					cou = comparePriceBusiness.couVenderInThisProduct(vo.getProductCode(), vendorCode, tin);
					
					if(cou==0){
						ComparePriceBean comparePriceBean = new ComparePriceBean();
						
						if(chkFlag==true){
							comparePriceSeq = comparePriceBusiness.genId(vo.getProductCode(), tin);
							chkFlag  = false;
						}else{
							comparePriceSeq++;
						}
						
						comparePriceBean.setProductCode	(vo.getProductCode());
						comparePriceBean.setTin			(tin);
						comparePriceBean.setSeqDb		(EnjoyUtils.nullToStr(comparePriceSeq));
						comparePriceBean.setVendorCode	(vendorCode);
						comparePriceBean.setQuantity	(vo.getQuantity());
						comparePriceBean.setPrice		(vo.getPrice());
						comparePriceBean.setDiscountRate(vo.getDiscountRate());
						
						comparePriceBusiness.insertCompareprice(comparePriceBean);
					}
					/*End Update เปรียบเทียบราคา*/
				}
				
				
			}else{
				reciveOrdeDetailList = reciveStockService.getReciveOrdeDetailList(reciveNo,tin);
				
				for(ReciveOrdeDetailBean beanTemp:reciveOrdeDetailList){
					if(reciveStatus.equals("3")){
						
						quantityDb = productquantityService.getProductquantity(beanTemp.getProductCode(),tin);
						
						logger.info("[save] quantityDb :: " + quantityDb);
						
						productquantityBean = new ProductquantityBean();
						
						productquantityBean.setProductCode(beanTemp.getProductCode());
						productquantityBean.setTin(tin);
						
						if(quantityDb==null){
							quantity =  EnjoyUtils.parseDouble(beanTemp.getQuantity());
							productquantityBean.setQuantity(String.valueOf(quantity));
							productquantityService.insertProductquantity(productquantityBean);
						}else{
							quantity =  EnjoyUtils.parseDouble(quantityDb) +  EnjoyUtils.parseDouble(beanTemp.getQuantity());
							productquantityBean.setQuantity(String.valueOf(quantity));
							productquantityService.updateProductquantity(productquantityBean);
						}
						
						/*Begin ส่วนประวัตเพิ่มลดสินค้า*/
						productQuanHistoryBean = new ProductQuanHistoryBean();
						
						productmasterBeanDb = productBusiness.getProductDetail(beanTemp.getProductCode(), tin);
						if(productmasterBeanDb!=null){
							productQuanHistoryBean.setProductType(productmasterBeanDb.getProductTypeCode());
							productQuanHistoryBean.setProductGroup(productmasterBeanDb.getProductGroupCode());
							productQuanHistoryBean.setProductCode(productmasterBeanDb.getProductCode());
						}else{
							productQuanHistoryBean.setProductType("");
							productQuanHistoryBean.setProductGroup("");
							productQuanHistoryBean.setProductCode("");
						}
						productQuanHistoryBean.setFormRef(reciveNo);
						productQuanHistoryBean.setTin(tin);
						productQuanHistoryBean.setQuantityPlus(beanTemp.getQuantity());
						productQuanHistoryBean.setQuantityMinus("0");
						productQuanHistoryBean.setQuantityTotal(String.valueOf(quantity));
						
						/*Begin หา hisCode*/
						if(chkHisCodeFlag==true){
							hisCode			= productQuanHistoryService.genId(tin);
							chkHisCodeFlag  = false;
						}else{
							hisCode++;
						}
						/*End หา hisCode*/
						productQuanHistoryService.insert(productQuanHistoryBean, hisCode);
						/*End ส่วนประวัตเพิ่มลดสินค้า*/
						
					}else if(currReciveStatus.equals("3") && reciveStatus.equals("4")){
						quantityDb = productquantityService.getProductquantity(beanTemp.getProductCode(), tin);
						
						productquantityBean = new ProductquantityBean();
						
						productquantityBean.setProductCode(beanTemp.getProductCode());
						productquantityBean.setTin(tin);
						
						if(quantityDb==null){
							quantity =  EnjoyUtils.parseDouble("0.00");
							productquantityBean.setQuantity(String.valueOf(quantity));
							productquantityService.insertProductquantity(productquantityBean);
						}else{
							quantity	=  EnjoyUtils.parseDouble(quantityDb) -  EnjoyUtils.parseDouble(beanTemp.getQuantity());
							productquantityBean.setQuantity(String.valueOf(quantity));
							productquantityService.updateProductquantity(productquantityBean);
						}
						
						/*Begin ส่วนประวัตเพิ่มลดสินค้า*/
						productQuanHistoryBean = new ProductQuanHistoryBean();
						productQuanHistoryBean.setFormRef(reciveNo);
						
						productmasterBeanDb = productBusiness.getProductDetail(beanTemp.getProductCode(), tin);
						if(productmasterBeanDb!=null){
							productQuanHistoryBean.setProductType(productmasterBeanDb.getProductTypeCode());
							productQuanHistoryBean.setProductGroup(productmasterBeanDb.getProductGroupCode());
							productQuanHistoryBean.setProductCode(productmasterBeanDb.getProductCode());
						}else{
							productQuanHistoryBean.setProductType("");
							productQuanHistoryBean.setProductGroup("");
							productQuanHistoryBean.setProductCode("");
						}
						productQuanHistoryBean.setTin(tin);
						productQuanHistoryBean.setQuantityPlus("0.00");
						productQuanHistoryBean.setQuantityMinus(beanTemp.getQuantity());
						productQuanHistoryBean.setQuantityTotal(String.valueOf(quantity));
						
						/*Begin หา hisCode*/
						if(chkHisCodeFlag==true){
							hisCode			= productQuanHistoryService.genId(tin);
							chkHisCodeFlag  = false;
						}else{
							hisCode++;
						}
						/*End หา hisCode*/
						productQuanHistoryService.insert(productQuanHistoryBean, hisCode);
						/*End ส่วนประวัตเพิ่มลดสินค้า*/
					}
				}
			}
			/*End Section รายการสินค้า*/
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
		return reciveNo;
		
	}

	

}
