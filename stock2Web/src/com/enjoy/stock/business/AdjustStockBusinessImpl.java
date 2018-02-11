package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.AdjustStockBean;
import com.enjoy.stock.bean.ProductQuanHistoryBean;
import com.enjoy.stock.bean.ProductmasterBean;
import com.enjoy.stock.bean.ProductquantityBean;
import com.enjoy.stock.services.AdjustStockService;
import com.enjoy.stock.services.ProductQuanHistoryService;
import com.enjoy.stock.services.ProductquantityService;

@Service
public class AdjustStockBusinessImpl extends AbstractBusiness implements AdjustStockBusiness{
	private static final Logger logger = Logger.getLogger(AdjustStockBusinessImpl.class);
	
	@Autowired
	AdjustStockService adjustStockService;
	@Autowired
	ProductquantityService productquantityService;
	@Autowired
	ProductQuanHistoryService productQuanHistoryService;
	@Autowired
	ProductBusiness productBusiness;

	@Override
	public void getAdjustHistoryList(PaginatedListBean paginatedList,AdjustStockBean adjustStockBean) throws Exception {
		adjustStockService.getAdjustHistoryList(paginatedList, adjustStockBean);
		
	}

	@Override
	public void insertAdjustHistory(AdjustStockBean adjustStockBean)throws Exception {
		adjustStockService.insertAdjustHistory(adjustStockBean);
	}

	@Override
	public void save(ArrayList<AdjustStockBean> adjustStockList, String tin)throws Exception {
		logger.info("[save][Begin]");
		
		String					quantityDb				= null;
		ProductquantityBean 	productquantityBean 	= null;
		ProductQuanHistoryBean 	productQuanHistoryBean 	= null;
		ProductmasterBean 		productmasterBeanDb 	= null;
		int						hisCode					= 1;
		boolean					chkFlag					= true;
		
		try{
			for(AdjustStockBean bean:adjustStockList){
				quantityDb = productquantityService.getProductquantity(bean.getProductCode(),tin);
				
				productquantityBean = new ProductquantityBean();
				
				productquantityBean.setProductCode(bean.getProductCode());
				productquantityBean.setTin(tin);
				productquantityBean.setQuantity(bean.getQuantity());
				
				if(quantityDb==null){
					productquantityService.insertProductquantity(productquantityBean);
				}else{
					productquantityService.updateProductquantity(productquantityBean);
				}
				
				/*Begin ส่วนประวัตเพิ่มลดสินค้า*/
				productQuanHistoryBean = new ProductQuanHistoryBean();
				productQuanHistoryBean.setFormRef("adjuststock");
				
				productmasterBeanDb = productBusiness.getProductDetail(bean.getProductCode(), tin);
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
				
				if(EnjoyUtils.parseDouble(bean.getQuanNew()) > 0){
					productQuanHistoryBean.setQuantityPlus(bean.getQuanNew());
					productQuanHistoryBean.setQuantityMinus("0.00");
				}else{
					productQuanHistoryBean.setQuantityPlus("0.00");
					productQuanHistoryBean.setQuantityMinus(EnjoyUtils.nullToStr(Math.abs(EnjoyUtils.parseDouble(bean.getQuanNew()))));
				}
				productQuanHistoryBean.setQuantityTotal(String.valueOf(bean.getQuantity()));
				
				/*Begin หา hisCode*/
				if(chkFlag==true){
					hisCode		= productQuanHistoryService.genId(tin);
					chkFlag  	= false;
				}else{
					hisCode++;
				}
				/*End หา hisCode*/
				productQuanHistoryService.insert(productQuanHistoryBean, hisCode);
				/*End ส่วนประวัตเพิ่มลดสินค้า*/
				
				bean.setTin(tin);
				adjustStockService.insertAdjustHistory(bean);
			}
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
	}
}
