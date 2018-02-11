package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.ManageUnitTypeBean;
import com.enjoy.stock.services.ProductService;
import com.enjoy.stock.services.UnitTypeService;

@Service
public class UnitTypeBusinessImpl extends AbstractBusiness implements UnitTypeBusiness{
	private static final Logger logger = Logger.getLogger(UnitTypeBusinessImpl.class);
	
	@Autowired
	UnitTypeService unitTypeService;
	@Autowired
	ProductService productService;

	@Override
	public ArrayList<ManageUnitTypeBean> getUnitTypeList(String tin)throws Exception {
		return unitTypeService.getUnitTypeList(tin);
	}

	@Override
	public ArrayList<ComboBean> unitNameList(String unitName, String tin)throws Exception {
		return unitTypeService.unitNameList(unitName, tin);
	}

	@Override
	public String getUnitCode(String unitName, String tin) throws Exception {
		return unitTypeService.getUnitCode(unitName, tin);
	}

	@Override
	public void save(ArrayList<ManageUnitTypeBean> voList, String tin, String unitCodeForDelete)throws Exception {
		logger.info("[save][Begin]");
		
		int			unitCode				= 1;
		boolean		chkFlag					= true;
		String[] 	unitCodeArray;
		
		try{
			
			if(EnjoyUtils.chkNull(unitCodeForDelete)){
				unitCodeArray = unitCodeForDelete.split(",");
				for(String code:unitCodeArray){
					unitTypeService.unitTypeSuspended(code, tin);
				}
			}
			
			if(voList!=null){
				for(ManageUnitTypeBean bean:voList){
					bean.setTin(tin);
					if(bean.getRowStatus().equals(Constants.NEW)){
						if(chkFlag==true){
							unitCode = unitTypeService.genId(tin);
							chkFlag  = false;
						}else{
							unitCode++;
						}
						bean.setUnitCode(EnjoyUtils.nullToStr(unitCode));
						unitTypeService.insertUnitType(bean);
					}else{
						unitTypeService.updateUnitType(bean);
					}
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
	}

	@Override
	public int countUnitCodeInProduct(String unitCode, String tin)throws Exception {
		return productService.countUnitCodeInProduct(unitCode, tin);
	}

	

	

}
