package com.enjoy.core.business;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.RelationUserAndCompanyBean;
import com.enjoy.core.main.Constants;
import com.enjoy.core.services.RelationUserAndCompanyService;
import com.enjoy.core.utils.EnjoyUtils;

@Service
public class RelationUserAndCompanyBusinessImpl extends AbstractBusiness implements RelationUserAndCompanyBusiness{
	private static final Logger logger = Logger.getLogger(RelationUserAndCompanyBusinessImpl.class);
	
	@Autowired
	RelationUserAndCompanyService relationUserAndCompanyService;

	@Override
	public ArrayList<RelationUserAndCompanyBean> searchByCriteria(RelationUserAndCompanyBean vo) throws Exception {
		return relationUserAndCompanyService.searchByCriteria(vo);
	}

	@Override
	public void save(ArrayList<RelationUserAndCompanyBean> relationUserAndCompanyList,String tin, String userUniqueIdForDelete) throws Exception {
		logger.info("[save][Begin]");
		
		String[] userUniqueIdArray;
		
		try{
			logger.info("[save] tin :: " + tin);
			
			if(EnjoyUtils.chkNull(userUniqueIdForDelete)){
				userUniqueIdArray = userUniqueIdForDelete.split(",");
				for(String userUniqueId:userUniqueIdArray){
					relationUserAndCompanyService.deleteRelationUserAndCompany(userUniqueId, tin);
				}
			}
			
			if(relationUserAndCompanyList!=null){
				for(RelationUserAndCompanyBean vo:relationUserAndCompanyList){
					if(vo.getRowStatus().equals(Constants.NEW)){
						vo.setTin(tin);
						relationUserAndCompanyService.insertRelationUserAndCompany(vo);
					}
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
	}

}
