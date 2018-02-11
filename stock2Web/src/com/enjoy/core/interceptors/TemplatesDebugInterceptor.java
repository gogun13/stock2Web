/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.enjoy.core.interceptors;

import java.util.Map;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import org.apache.log4j.Logger;

public class TemplatesDebugInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 4030044344066761593L;
    private static final Logger logger = Logger.getLogger(TemplatesDebugInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        try {
            if (ServletActionContext.getActionMapping() != null) {
                String className = invocation.getAction().getClass().getCanonicalName();
                String methodName = ServletActionContext.getActionMapping().getMethod();
                logger.info(className+"."+methodName);
            }
            invocation.addPreResultListener(new PreResultListener() {
                public void beforeResult(ActionInvocation invocation,String resultCode) {
                    Map<String, ResultConfig> resultsMap = invocation.getProxy().getConfig().getResults();
                    ResultConfig finalResultConfig = resultsMap.get(resultCode);
                    logger.info(finalResultConfig.getParams());
                }
            });
        } catch (Exception e) {
            logger.error("[ERROR] Could not list templates: ", e);
        }
        return invocation.invoke();
    }
}