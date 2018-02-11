/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enjoy.core.interceptors;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author aon
 */
public class UserSessionControlListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session timeout ---->");
        try {
            HttpSession session = se.getSession();
//            ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());

//            TaskControlBusiness taskControlBusiness = (TaskControlBusiness) ctx.getBean("taskControlBusiness");
//            taskControlBusiness.deleteTransLock(session.getId());

            System.out.println("Success Removed.");
        } catch (Exception ex) {
            System.out.println("Error in sessionDestroyed:" + ex);
        }
    }

}
