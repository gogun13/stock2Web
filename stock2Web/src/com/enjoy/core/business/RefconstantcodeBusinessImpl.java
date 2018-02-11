package com.enjoy.core.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.RefconstantcodeBean;
import com.enjoy.core.services.RefconstantcodeService;

@Service
public class RefconstantcodeBusinessImpl extends AbstractBusiness implements RefconstantcodeBusiness{
	private static final Logger logger = Logger.getLogger(RefconstantcodeBusinessImpl.class);
	
	@Autowired
	RefconstantcodeService refconstantcodeService;

	@Override
	public ArrayList<RefconstantcodeBean> searchByCriteria(RefconstantcodeBean refconstantcodeBean) throws Exception {
		return refconstantcodeService.searchByCriteria(refconstantcodeBean);
	}

	@Override
	public void updateRefconstantcode(ArrayList<RefconstantcodeBean> list) throws Exception {
		logger.info("[updateRefconstantcode][Begin]");
		
		try{
			if(list!=null){
				for(RefconstantcodeBean vo:list){
					refconstantcodeService.updateRefconstantcode(vo);
				}
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[updateRefconstantcode][End]");
		}
	}

	@Override
	public void updateCodeDisplay(ArrayList<RefconstantcodeBean> list) throws Exception {
		logger.info("[updateCodeDisplay][Begin]");
		
		try{
			if(list!=null){
				for(RefconstantcodeBean vo:list){
					refconstantcodeService.updateCodeDisplay(vo);
				}
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[updateCodeDisplay][End]");
		}
		
	}

	@Override
	public String getCodeDisplay(String id, String tin) throws Exception {
		return refconstantcodeService.getCodeDisplay(id, tin);
	}

	

}
