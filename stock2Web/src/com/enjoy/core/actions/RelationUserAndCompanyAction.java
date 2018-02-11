package com.enjoy.core.actions;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.RelationUserAndCompanyBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.business.CompanyDetailsBusiness;
import com.enjoy.core.business.RelationUserAndCompanyBusiness;
import com.enjoy.core.business.UserdetailsBusiness;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class RelationUserAndCompanyAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RelationUserAndCompanyAction.class);
	
	private String									companyName;
	private String									tin;
	private String									indexRow;
	private ArrayList<RelationUserAndCompanyBean> 	relationUserAndCompanyList;
	private String									hidUserUniqueId;
	private String 									userUniqueIdForDelete;
	
	@Autowired
	RelationUserAndCompanyBusiness relationUserAndCompanyBusiness;
	
	@Autowired
	CompanyDetailsBusiness companyDetailsBusiness;
	
	@Autowired
	UserdetailsBusiness userdetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	companyName 				= "";
    	tin 						= "";
    	hidUserUniqueId				= "";
    	indexRow					= "";
    	relationUserAndCompanyList 	= null;
    	userUniqueIdForDelete		= "";
    	
		return SUCCESS;
	}
    
    public String onSearch()throws Exception {
    	logger.info("[onSearch][Begin]");
    	
    	RelationUserAndCompanyBean bean = new RelationUserAndCompanyBean();
    	
    	try{
    		tin	= companyDetailsBusiness.getTin(companyName);
    		
    		logger.info("[onSearch] companyName 	:: " + companyName);
			logger.info("[onSearch] tin 			:: " + tin);
			
			if(!EnjoyUtils.chkNull(tin)){
				throw new EnjoyException("ระบุชื่อบริษัทผิดกรุณาตรวจสอบ");
			}
			
			bean.setTin(tin);
			
			relationUserAndCompanyList = relationUserAndCompanyBusiness.searchByCriteria(bean);
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[onSearch][End]");
    	}
    	return SUCCESS;
    }
    
    public String newRecord() throws Exception{
		logger.info("[newRecord][Begin]");
		
		RelationUserAndCompanyBean	relationUserAndCompanyBean	= new RelationUserAndCompanyBean();
		UserDetailsBean userDetailsBean;
		
		try{
			
			logger.info("[newRecord] hidUserUniqueId :: " + hidUserUniqueId);
			
			if(relationUserAndCompanyList==null){
				relationUserAndCompanyList = new ArrayList<RelationUserAndCompanyBean>();
			}
			
			for(RelationUserAndCompanyBean vo:relationUserAndCompanyList){
				if(vo.getUserUniqueId().equals(hidUserUniqueId)){
					throw new EnjoyException("บริษัท " + companyName + "มี รหัสผู้ใช้งาน " + vo.getUserEmail() + " แล้ว");
				}
			}
			
			userDetailsBean = userdetailsBusiness.getUserdetail(EnjoyUtils.parseInt(hidUserUniqueId));
			
			relationUserAndCompanyBean.setUserUniqueId(EnjoyUtils.nullToStr(userDetailsBean.getUserUniqueId()));
			
			relationUserAndCompanyBean.setUserEmail		(userDetailsBean.getUserEmail());
			relationUserAndCompanyBean.setUserFullName	(userDetailsBean.getUserName().concat(" ").concat(userDetailsBean.getUserSurname()));
			relationUserAndCompanyBean.setUserStatusName(userDetailsBean.getUserStatusName());
			
			relationUserAndCompanyBean.setRowStatus(Constants.NEW);
			
			relationUserAndCompanyList.add(relationUserAndCompanyBean);
			
			logger.info("[newRecord] size :: " + relationUserAndCompanyList.size());
			
			
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[newRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String deleteRecord() throws Exception{
		logger.info("[deleteRecord][Begin]");
		
		try{
			logger.info("[deleteRecord] indexRow :: " + indexRow);
			
			for(int i=0;i<relationUserAndCompanyList.size();i++){
				RelationUserAndCompanyBean vo = relationUserAndCompanyList.get(i);
				
				if(vo.getSeq().equals(indexRow)){
					if(!Constants.NEW.equals(vo.getRowStatus())){
						if(!EnjoyUtils.chkNull(userUniqueIdForDelete)){
							userUniqueIdForDelete = vo.getUserUniqueId();
						}else{
							userUniqueIdForDelete += "," + vo.getUserUniqueId();
						}
					}
					
					relationUserAndCompanyList.remove(i);
					break;
				}
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[deleteRecord][End]");
		}
		
		return SEARCH;
	}
    
    public String save()throws Exception {
    	logger.info("[save][Begin]");
		
		try{
			
			relationUserAndCompanyBusiness.save(relationUserAndCompanyList, tin, userUniqueIdForDelete);
			
			userUniqueIdForDelete		= "";
			onSearch();
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[save][End]");
		}
		
		return SEARCH;
    }
    
    public String lookUp()throws Exception {
    	
    	return "lookUp";
    }
    
    @Override
    public String autoComplete()throws Exception {
    	logger.info("[autoComplete][Begin]");
    	
    	ArrayList<ComboBean> 	list 		= null;
    	JSONArray 				jSONArray 	= new JSONArray();
        JSONObject 				objDetail 	= new JSONObject();
    	
    	try{
    		logger.info("[autoComplete] autoCompleteName :: " + getAutoCompleteName());
    		logger.info("[autoComplete] autoCompParamter :: " + getAutoCompParamter());
    		
    		if("companyName".equals(getAutoCompleteName())){
    			list = companyDetailsBusiness.companyNameListForAutoComplete(getAutoCompParamter());
    		}
    		
    		if(list!=null && !list.isEmpty()){
	    		for(ComboBean bean:list){
	 			   objDetail 		= new JSONObject();
	 			   
	 			   objDetail.put("id"			,bean.getCode());
	 			   objDetail.put("value"		,bean.getDesc());
	 			   
	 			   jSONArray.add(objDetail);
	 		   	}
    		}
    		
    		writeMSG(jSONArray.toString());
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[autoComplete][End]");
    	}
    	return null;
    }
    
    
}
