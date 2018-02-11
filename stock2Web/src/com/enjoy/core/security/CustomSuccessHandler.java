package com.enjoy.core.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{
	
	private static final Logger logger = Logger.getLogger(CustomSuccessHandler.class);
	
	public CustomSuccessHandler() {
        super();
    }
	
	 @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {


		 logger.info("onAuthenticationSuccess start");
    }

}
