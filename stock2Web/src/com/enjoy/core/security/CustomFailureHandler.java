package com.enjoy.core.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	
	private static final Logger logger = Logger.getLogger(CustomFailureHandler.class);
	
	public CustomFailureHandler() {
        super();
    }
	
	@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
		logger.info("onAuthenticationFailure start");
    }

}
