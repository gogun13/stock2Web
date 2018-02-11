package com.enjoy.core.actions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.business.UserdetailsBusiness;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChangePassAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ChangePassAction.class);
	
	private String oldPassword;
	private String newPassword;
	private String confirmNewPassword;
	
	@Autowired
	UserdetailsBusiness userdetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	oldPassword 		= "";
    	newPassword 		= "";
    	confirmNewPassword 	= "";
    	
		return SUCCESS;
	}
    
    public String saveUpdData() throws Exception{
		logger.info("[saveUpdData][Begin]");
		
		try{
			logger.info("[saveUpdData] oldPassword 			:: " + oldPassword);
			logger.info("[saveUpdData] newPassword 			:: " + newPassword);
			
			userdetailsBusiness.changePassword(oldPassword, newPassword, getCurrentUser());
			
		}catch(Exception e){
			logger.error("[saveUpdRecord] :: ",e);
			throw e;
		}finally{
			logger.info("[saveUpdRecord][End]");
		}
		
		return SUCCESS;
	}

}
