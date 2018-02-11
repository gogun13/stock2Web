package com.enjoy.core.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.business.CompanyDetailsBusiness;
import com.enjoy.core.business.UserdetailsBusiness;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.ConfigFile;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter{
	
	private static final Logger logger = Logger.getLogger(CustomAuthenticationProcessingFilter.class);
	
	@Autowired
    private UserdetailsBusiness userdetailsBusiness;
	@Autowired
	private CompanyDetailsBusiness companyDetailsBusiness;
	
	public CustomAuthenticationProcessingFilter() {
        super();
    }
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		logger.info("[attemptAuthentication][Begin]");
        
        Authentication 						authen = null;
        String 								username;
        String 								password;
        UsernamePasswordAuthenticationToken authRequest;
        UserDetailsBean 					bean;
        ArrayList<ComboBean> 				companyList;
        JSONObject 							obj 						= new JSONObject();
        JSONObject							companyObj;
        int									countUserIncompany			= 0;
        JSONArray							companyObjList 				= new JSONArray();
        String								tin;
        HttpSession 						session 					= request.getSession(true);
        TreeMap<String, Integer> 			mapLoginWrong 				= new TreeMap<String, Integer>();
        int									loginWrong					= 0;
        
        try{
        	obj.put(Constants.STATUS, Constants.SUCCESS);
        	tin = request.getParameter("tin");
        	
        	logger.info("[attemptAuthentication] tin :: " + tin);
        	
        	username = obtainUsername(request);
            password = obtainPassword(request);
            
            bean = userdetailsBusiness.userSelect(username, password);
            
            logger.info("[attemptAuthentication] bean :: " + bean);
            
            if(bean!=null){
            	if("R".equals(bean.getUserStatus())){
            		throw new EnjoyException("รหัสผู้ใช้งานนี้ถูกยกเลิกการใช้งานแล้ว");
            	}else if("B".equals(bean.getUserStatus())){
            		throw new EnjoyException("รหัสผู้ใช้งานนี้โดนระงับการใช้งานชั่วคราว เนื่องจาก Login ผิดเกิน 3 ครั้ง กรุณาติดต่อผู้ดูแลระบบ");
            	}
            	
            	authRequest = new UsernamePasswordAuthenticationToken(ConfigFile.getSysAdminUser(), ConfigFile.getSysAdminPassword());
            	
            	request.getSession(false).removeAttribute(Constants.Session.LOGIN_WRONG);
            	
            	logger.info("[attemptAuthentication] UserUniqueId :: " + bean.getUserUniqueId());
            	
            	//เช็คว่า user มีบริษัทสังกัดหรือยัง ยกเว้น user admin
        		if(bean.getUserUniqueId()!=1){
        			if(!EnjoyUtils.chkNull(tin)){
	        			companyList 		= userdetailsBusiness.getCompanyList(bean.getUserUniqueId());
	        			countUserIncompany 	= companyList.size();
	        			
	        			logger.info("[attemptAuthentication] countUserIncompany :: " + countUserIncompany);
	        			
	        			if(countUserIncompany > 0){
	        				bean.setFlagChkCompany("N");
	            			
	            			obj.put("flagChkCompany"	, bean.getFlagChkCompany());
	            			obj.put("FlagChange"		, bean.getFlagChangePassword());
	            			obj.put("countUserIncompany", countUserIncompany);
	            			
	            			if(countUserIncompany==1){
	            				bean.setTin(companyList.get(0).getCode());
	            				bean.setCompanyName(companyList.get(0).getDesc());
	            				
	            				session.setAttribute(Constants.Session.LOGIN_KEY, bean);
	            			}else{
	            				for(ComboBean vo:companyList){
	            					companyObj = new JSONObject();
	            					
	            					companyObj.put("code"	, vo.getCode());
	            					companyObj.put("desc"	, vo.getDesc());
	            					
	            					companyObjList.add(companyObj);
	            				}
	            				
	            				obj.put("companyObjList", companyObjList);
	            				
	            				return null;
	            			}
	            			
	            		}else{
	            			throw new EnjoyException("ไม่สามารถใช้งานได้ เนื่องจากยังไม่ได้ระบุว่าเป็นพนักงานของบริษัทในระบบ กรุณาติดต่อผู้ดูแลระบบ");
	            		}
        			}else{
        				obj.put("flagChkCompany"	, bean.getFlagChkCompany());
            			obj.put("FlagChange"		, bean.getFlagChangePassword());
            			obj.put("countUserIncompany", 1);
            			
        				bean.setTin(tin);
        				bean.setCompanyName(companyDetailsBusiness.getCompanyName(tin));
        				
        				session.setAttribute(Constants.Session.LOGIN_KEY, bean);
        			}
        		}else{
        			//Admin enjoy สังกัดได้ บ เดียวเท่านั้น
        			ComboBean comboBean 		= userdetailsBusiness.getCompanyForAdminEnjoy(bean.getUserUniqueId());
        			bean.setTin(comboBean.getCode());
        			bean.setCompanyName(comboBean.getDesc());
        			
        			obj.put(Constants.STATUS	, Constants.SUCCESS);
        			obj.put("flagChkCompany"	, bean.getFlagChkCompany());
        			obj.put("FlagChange"		, bean.getFlagChangePassword());
        			obj.put("countUserIncompany", 1);
        			
        			session.setAttribute(Constants.Session.LOGIN_KEY, bean);
        		}
            	
            	
            }else{
            	if(!"admin".equals(username)){
	            	if(session.getAttribute(Constants.Session.LOGIN_WRONG)==null){
	            		mapLoginWrong.put(username, ++loginWrong);
	            		session.setAttribute(Constants.Session.LOGIN_WRONG, mapLoginWrong);
	            	}else{
	            		mapLoginWrong = (TreeMap<String, Integer>) session.getAttribute(Constants.Session.LOGIN_WRONG);
	            		if(mapLoginWrong.containsKey(username)){
	            			loginWrong = mapLoginWrong.get(username) + 1;
	            			
	            			logger.info("loginWrong :: " + loginWrong);
	            			
	            			if(loginWrong>3){
	            				userdetailsBusiness.updateUserStatusByUserEmail(username, "B");
	            				throw new EnjoyException("รหัสผู้ใช้งานนี้โดนระงับการใช้งานชั่วคราว เนื่องจาก Login ผิดเกิน 3 ครั้ง กรุณาติดต่อผู้ดูแลระบบ");
	            			}else{
	            				mapLoginWrong.put(username, loginWrong);
	                    		session.setAttribute(Constants.Session.LOGIN_WRONG, mapLoginWrong);
	            			}
	            		}else{
	            			mapLoginWrong.put(username, ++loginWrong);
	                		session.setAttribute(Constants.Session.LOGIN_WRONG, mapLoginWrong);
	            		}
	            	}
            	}
            	
    			throw new EnjoyException("ระบุรหัสผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง");
            }
            
            // Allow subclasses to set the "details" property
            setDetails(request, authRequest);
            
            authen = this.getAuthenticationManager().authenticate(authRequest);
            
        }catch(Exception e){
        	logger.error("attemptAuthentication",e);
        	
        	obj.put(Constants.STATUS	,Constants.ERROR);
        	
        	if (e instanceof EnjoyException) {
    			obj.put(Constants.MSG,e.getMessage());
        	}else{
        		obj.put(Constants.MSG,"เกิดข้อผิดพลาดระหว่างการเข้าสู่ระบบ กรุณาติดต่อผู้ดูแลระบบ");
        		throw new InsufficientAuthenticationException("", e);
        	}
        }finally{
        	writeMSG(obj.toString(), response);
        	logger.info("[attemptAuthentication][End]");
        }

        return authen;
    }
	
	public void writeMSG(String msg,HttpServletResponse response) {
		PrintWriter print = null;
		try {
			response.setContentType("text/html; charset=UTF-8");
			print = response.getWriter();
			print.write(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        if (request.getMethod().equals("POST")) {
            // If the incoming request is a POST, then we send it up
            // to the AbstractAuthenticationProcessingFilter.
            super.doFilter(request, response, chain);
        } else {
            // If it's a GET, we ignore this request and send it
            // to the next filter in the chain.  In this case, that
            // pretty much means the request will hit the /login
            // controller which will process the request to show the
            // login page.            
            chain.doFilter(request, response);
            
        }
    }
	
	/**
     * This filter by default responds to <code>/j_spring_security_check</code>.
     *
     * @return the default
     */
    public String getDefaultFilterProcessesUrl() {
        return "/j_spring_security_check";
    }

}
