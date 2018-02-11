package com.enjoy.core.actions;

import org.apache.log4j.Logger;

import com.enjoy.core.main.Constants;


public class LogoutAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LogoutAction.class);
	
    public String success() throws Exception {
    	if (request.getSession(false) != null) {
            request.getSession(false).removeAttribute(Constants.Session.LOGIN_KEY);
            request.getSession(false).invalidate();
            logger.info("Logout sucess");           
        }
		return SUCCESS;
	}
    
    public String clearInterval() throws Exception {
    	if (request.getSession(false) != null) {
            request.getSession(false).removeAttribute(Constants.Session.LOGIN_KEY);
            request.getSession(false).invalidate();
            logger.info("clearInterval sucess");           
        }
		return "clearInterval";
	}

}
