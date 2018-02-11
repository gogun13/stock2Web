package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.ManageProductGroupBean;
import com.enjoy.stock.services.ProductGroupService;
import com.enjoy.stock.services.ProductService;

@Service
public class ProductGroupBusinessImpl extends AbstractBusiness implements ProductGroupBusiness{
	private static final Logger logger = Logger.getLogger(ProductGroupBusinessImpl.class);
	
	@Autowired
	ProductGroupService productGroupService;
	@Autowired
	ProductService productService;

	@Override
	public ArrayList<ManageProductGroupBean> getProductGroupList(String tin,String productTypeCode) throws Exception {
		return productGroupService.getProductGroupList(tin, productTypeCode);
	}

	@Override
	public ArrayList<ComboBean> productGroupNameList(String productTypeName,String productGroupName, String tin, boolean flag) throws Exception {
		return productGroupService.productGroupNameList(productTypeName, productGroupName, tin, flag);
	}

	@Override
	public String getProductGroupCode(String productTypeCode,String productGroupName, String tin) throws Exception {
		return productGroupService.getProductGroupCode(productTypeCode, productGroupName, tin);
	}

	@Override
	public void cancelProductgroupByProductTypeCode(String productTypeCode,String tin) throws Exception {
		productGroupService.cancelProductgroupByProductTypeCode(productTypeCode, tin);
		
	}

	@Override
	public void save(ArrayList<ManageProductGroupBean> productGroupList,String tin,String productTypeCode,String productGroupCodeForDelete) throws Exception {
		logger.info("[save][Begin]");
		
		boolean	chkFlag					= true;
		int		productGroupCode		= 0;
		String[] productGroupCodeArray;
		
		try{
			if(EnjoyUtils.chkNull(productGroupCodeForDelete)){
				productGroupCodeArray = productGroupCodeForDelete.split(",");
				for(String code:productGroupCodeArray){
					productGroupService.productGroupSuspended(tin, productTypeCode, code);
					productService.cancelProductmaster(tin, productTypeCode, code, "");
				}
			}
			
			if(productGroupList!=null){
				for(ManageProductGroupBean bean:productGroupList){
					bean.setTin(tin);
					bean.setProductTypeCode(productTypeCode);
					if(bean.getRowStatus().equals(Constants.NEW)){
						if(chkFlag==true){
							productGroupCode 	= productGroupService.genId(tin, productTypeCode);
							chkFlag  			= false;
						}else{
							productGroupCode++;
						}
						
						logger.info("[onSave] productGroupCode :: " + productGroupCode);
						
						bean.setProductGroupCode(EnjoyUtils.nullToStr(productGroupCode));
						
						productGroupService.insertProducGroup(bean);
						
					}else{
						productGroupService.updateProductgroup(bean);
					}
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][end]");
		}
		
	}

	

}
