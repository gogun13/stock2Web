package com.enjoy.stock.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.business.AbstractBusiness;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.stock.bean.RelationGroupCustomerBean;
import com.enjoy.stock.services.CustomerService;
import com.enjoy.stock.services.RelationGroupCustomerService;

@Service
public class RelationGroupCustomerBusinessImpl extends AbstractBusiness implements RelationGroupCustomerBusiness{
	private static final Logger logger = Logger.getLogger(RelationGroupCustomerBusinessImpl.class);
	
	@Autowired
	RelationGroupCustomerService relationGroupCustomerService;
	@Autowired
	CustomerService customerService;

	@Override
	public ArrayList<RelationGroupCustomerBean> searchByCriteria(String tin)throws Exception {
		return relationGroupCustomerService.searchByCriteria(tin);
	}

	@Override
	public void save(ArrayList<RelationGroupCustomerBean> voList,String tin, String cusGroupCodeForDelete) throws Exception {
		logger.info("[save][Begin]");
		
		boolean		chkFlag					= true;
		int			cusGroupCode			= 0;
		String[] 	cusGroupCodeArray;
		
		try{
			
			if(EnjoyUtils.chkNull(cusGroupCodeForDelete)){
				cusGroupCodeArray = cusGroupCodeForDelete.split(",");
				for(String code:cusGroupCodeArray){
					relationGroupCustomerService.rejectRelationGroupCustomer(code, tin);
				}
			}
			
			if(voList!=null){
				for(RelationGroupCustomerBean bean:voList){
					bean.setTin(tin);
					if(bean.getRowStatus().equals(Constants.NEW)){
						if(chkFlag==true){
							cusGroupCode = relationGroupCustomerService.genId(tin);
							chkFlag  = false;
						}else{
							cusGroupCode++;
						}
						
						logger.info("[onSave] cusGroupCode :: " + cusGroupCode);
						
						bean.setCusGroupCode(EnjoyUtils.nullToStr(cusGroupCode));
						relationGroupCustomerService.insertRelationGroupCustomer(bean);
					}else{
						relationGroupCustomerService.updateRelationGroupCustomer(bean);
					}
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][end]");
		}
		
	}

	@Override
	public int countCusGroupCodeInCustomer(String cusGroupCode, String tin)throws Exception {
		return customerService.countCusGroupCodeInCustomer(cusGroupCode, tin);
	}

	
	

}
