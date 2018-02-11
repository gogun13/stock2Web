package com.enjoy.core.actions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;

@Data
@EqualsAndHashCode(callSuper = true)
public class HomeAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HomeAction.class);
	
	private String pageAction;
	
    public String success() throws Exception {
    	logger.info("[success]");
		return SUCCESS;
	}
    
    public String gotoChangePass(){
    	return "gotoChangePass";
    }
	
	public String gotoCompanyDetails(){
    	return "gotoCompanyDetails";
    }
	
	public String welcome(){
    	return "welcome";
    }

}
