package com.enjoy.core.actions;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.bean.RefconstantcodeBean;
import com.enjoy.core.business.RefconstantcodeBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class RefconstantcodeAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(RefconstantcodeAction.class);
	
	private ArrayList<RefconstantcodeBean> 			section1List;
	private ArrayList<RefconstantcodeBean> 			section2List;
	private boolean sendMailFlg;
	
	@Autowired
	RefconstantcodeBusiness refconstantcodeBusiness;
	
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	RefconstantcodeBean	bean	= new RefconstantcodeBean();
    	
    	try{
    		bean.setTin(getCurrentUser().getTin());
    		bean.setTypeTB("1");
    		section1List		= refconstantcodeBusiness.searchByCriteria(bean);
    		
    		bean	= new RefconstantcodeBean();
    		bean.setTin(getCurrentUser().getTin());
    		bean.setTypeTB("2");
    		section2List		= refconstantcodeBusiness.searchByCriteria(bean);
    		if(section2List!=null && !section2List.isEmpty()){
    			if("Y".equals(section2List.get(0).getCodeDisplay())){
    				sendMailFlg = true;
    			}else{
    				sendMailFlg = false;
    			}
    			
    			logger.info("[success] sendMailFlg :: " + sendMailFlg);
    		}
    		
    	}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[success][End]");
    	}
    	
		return SUCCESS;
	}
    
    public String onSave() throws Exception{
		logger.info("[onSave][Begin]");
		
		try{
			refconstantcodeBusiness.updateCodeDisplay(section1List);
			
			refconstantcodeBusiness.updateRefconstantcode(section2List);
			
			success();
			
		}catch(Exception e){
    		throw e;
    	}finally{
    		logger.info("[onSave][End]");
    	}
		return SUCCESS;
	}
    
    @Override
    public String autoComplete()throws Exception {
    	return null;
    }
    
    
}
