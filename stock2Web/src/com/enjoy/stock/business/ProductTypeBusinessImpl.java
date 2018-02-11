package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.ManageProductTypeBean;
import com.enjoy.stock.services.ProductGroupService;
import com.enjoy.stock.services.ProductService;
import com.enjoy.stock.services.ProductTypeService;

@Service
public class ProductTypeBusinessImpl extends AbstractBusiness implements ProductTypeBusiness{
	private static final Logger logger = Logger.getLogger(ProductTypeBusinessImpl.class);
	
	@Autowired
	ProductTypeService productTypeService;
	@Autowired
	ProductGroupService productGroupService;
	@Autowired
	ProductService productService;

	@Override
	public ArrayList<ManageProductTypeBean> getProductTypeList(String tin)throws Exception {
		return productTypeService.getProductTypeList(tin);
	}

	@Override
	public ArrayList<ComboBean> productTypeNameList(String productTypeName,String tin) throws Exception {
		return productTypeService.productTypeNameList(productTypeName, tin);
	}

	@Override
	public String getProductTypeCode(String productTypeName, String tin)throws Exception {
		return productTypeService.getProductTypeCode(productTypeName, tin);
	}

	@Override
	public void save(ArrayList<ManageProductTypeBean> voList ,String tin,String productTypeCodeForDelete) throws Exception {
		logger.info("[save][Begin]");
		
		boolean	chkFlag					= true;
		int		productTypeCode			= 0;
		String[] productTypeCodeArray;
		
		try{
			
			if(EnjoyUtils.chkNull(productTypeCodeForDelete)){
				productTypeCodeArray = productTypeCodeForDelete.split(",");
				for(String code:productTypeCodeArray){
					productTypeService.producTypeSuspended(code, tin);
					productGroupService.cancelProductgroupByProductTypeCode(code, tin);
					productService.cancelProductmaster(tin, code, "", "");
				}
			}
			
			if(voList!=null){
				for(ManageProductTypeBean bean:voList){
					bean.setTin(tin);
					if(bean.getRowStatus().equals(Constants.NEW)){
						if(chkFlag==true){
							productTypeCode = productTypeService.genId(tin);
							chkFlag  = false;
						}else{
							productTypeCode++;
						}
						
						logger.info("[onSave] productTypeCode :: " + productTypeCode);
						
						bean.setProductTypeCode(EnjoyUtils.nullToStr(productTypeCode));
						productTypeService.insertProductype(bean);
					}else{
						productTypeService.updateProductype(bean);
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
