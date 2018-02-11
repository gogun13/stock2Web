package com.enjoy.core.interceptors;

//import com.kcs.core.servlet.filter.XSSFilter;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XSSInterceptor extends AbstractInterceptor implements Serializable {

    private static final long serialVersionUID = -2773375159350215037L;
    private static final String POSSIBLE_XSS_ATTACK = "error";

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        final ActionContext context = invocation.getInvocationContext();
        Map<String, Object> params = context.getParameters();
        boolean checkXSS = false;
        if (params.get("command") != null) {
            if (params.get("command") instanceof String) {
                if ("search".equals(params.get("command"))) {
                    checkXSS = true;
                }
            } else if (params.get("command") instanceof String[]) {
                String[] val = (String[]) params.get("command");
                for (int i = 0; i < val.length; i++) {
                    if ("search".equals(val[i])) {
                        checkXSS = true;
                        break;
                    }
                }
            }
        }

        if (checkXSS) {
            for (String key : params.keySet()) {
                Object value = params.get(key);

                if (value != null) {

                    if (value instanceof String) {

                        boolean isMatch = false;
//                        if (XSSFilter.xssExpression != null) {
//                            StringBuilder xssExp = new StringBuilder();
//                            xssExp.append("(.*)(");
//                            xssExp.append(XSSFilter.xssExpression);
//                            xssExp.append(")(.*)");
//
//                            Pattern regex = Pattern.compile(xssExp.toString(), Pattern.CASE_INSENSITIVE);
//                            Matcher regexMatcher = regex.matcher((String) value);
//                            isMatch = regexMatcher.matches();
//                        }

                        if (isMatch) {
                            return POSSIBLE_XSS_ATTACK;
                        }
                    } else if (value instanceof String[]) {
                        String[] val = (String[]) value;
                        for (int i = 0; i < val.length; i++) {

                            boolean isMatch = false;
//                            if (XSSFilter.xssExpression != null) {
//                                StringBuilder xssExp = new StringBuilder();
//                                xssExp.append("(.*)(");
//                                xssExp.append(XSSFilter.xssExpression);
//                                xssExp.append(")(.*)");
//
//                                Pattern regex = Pattern.compile(xssExp.toString(), Pattern.CASE_INSENSITIVE);
//                                Matcher regexMatcher = regex.matcher((String) val[i]);
//                                isMatch = regexMatcher.matches();
//                            }

                            if (isMatch) {
                                return POSSIBLE_XSS_ATTACK;
                            }
                        }
                    }
                }
            }
        }

        return invocation.invoke();
    }

    private String cleanXSS(String s) {
        if (s == null) {
            return "";
        } else {
            String s1 = s;
            s1 = s1.replaceAll("\0", "");
            Pattern pattern = Pattern.compile("<script>(.*?)</script>", 2);
            s1 = pattern.matcher(s1).replaceAll("");
            pattern = Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", 42);
            s1 = pattern.matcher(s1).replaceAll("");
            pattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 42);
            s1 = pattern.matcher(s1).replaceAll("");
            pattern = Pattern.compile("</script>", 2);
            s1 = pattern.matcher(s1).replaceAll("");
            pattern = Pattern.compile("<script(.*?)>", 42);
            s1 = pattern.matcher(s1).replaceAll("");
            pattern = Pattern.compile("eval\\((.*?)\\)", 42);
            s1 = pattern.matcher(s1).replaceAll("");
            pattern = Pattern.compile("expression\\((.*?)\\)", 42);
            s1 = pattern.matcher(s1).replaceAll("");
            pattern = Pattern.compile("javascript:", 2);
            s1 = pattern.matcher(s1).replaceAll("");
            pattern = Pattern.compile("vbscript:", 2);
            s1 = pattern.matcher(s1).replaceAll("");
            pattern = Pattern.compile("onload(.*?)=", 42);
            s1 = pattern.matcher(s1).replaceAll("");
            s1 = s1.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
            s1 = s1.replaceAll("'", "&#39;");
            s1 = s1.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            s1 = s1.replaceAll("'", "&apos;");
            s1 = s1.replaceAll("%27", "");

            return s1;
        }
    }
}
