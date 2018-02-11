/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enjoy.core.interceptors;

import java.util.Map;

import com.enjoy.core.exception.SessionException;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SessionInterceptor extends AbstractInterceptor {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public String intercept(ActionInvocation ai) throws Exception {

        Map<String, Object> session = ai.getInvocationContext().getSession();
        
        if (session.isEmpty()) {
            System.out.println("No Session");
            session.put("NOSESSION", "Y");
            throw new SessionException("No session");
//            return ""; // session is empty/expired
        }
        session.remove("NOSESSION");
        return ai.invoke();
    }
}
