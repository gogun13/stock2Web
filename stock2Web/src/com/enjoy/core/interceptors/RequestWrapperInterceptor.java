package com.enjoy.core.interceptors;

import java.io.Serializable;
import java.util.Map;

import org.apache.struts2.interceptor.ActionMappingParametersInteceptor;
import org.springframework.web.util.HtmlUtils;

import com.opensymphony.xwork2.ActionInvocation;

public class RequestWrapperInterceptor extends ActionMappingParametersInteceptor implements Serializable {

    private String logAction ="logMonitoring";
    private String webSqlAction="webSql";
    private String specialAdminAction="specialAdmin";
    @Override
    public String intercept(ActionInvocation actionInvocation) throws Exception {        
        try {
            Map<String, Object> params = actionInvocation.getInvocationContext().getParameters();
            String action = actionInvocation.getInvocationContext().getName();
            if(!action.equals(logAction) && !action.equals(webSqlAction) && !action.equals(specialAdminAction)){
                if (params != null) {
                    for (String key : params.keySet()) {
                        Object value = params.get(key);
                        if (value != null) {
                            if (value instanceof String) {
                                value = HtmlUtils.htmlEscape((String) (value));
                            } else if (value instanceof String[]) {
                                String[] val = (String[]) value;
                                for (int i = 0; i < val.length; i++) {
                                    val[i] = HtmlUtils.htmlEscape((String) (val[i]));
                                }                            
                            }
                            params.put(key, value);
                        }
                    }
                }
            }
            actionInvocation.getInvocationContext().setParameters(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actionInvocation.invoke();
    }
}
