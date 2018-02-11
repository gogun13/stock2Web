package com.enjoy.core.actions;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONObject;

import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.Constants;
import com.enjoy.core.pdf.ViewPdfMainForm;
import com.enjoy.core.utils.EnjoyUtils;
import com.opensymphony.xwork2.ActionSupport;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseAction extends ActionSupport implements ServletRequestAware,ServletResponseAware, SessionAware {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(BaseAction.class);
	
	private static final String ERROR 			= "error";
	private static final String ERROR_BUSINESS 	= "errorBusiness";
	protected static final String SEARCH 		= "search";
	
	private String 					command;
	private Method 					method;
	public HttpServletRequest 		request;
    public HttpServletResponse 		response;
    public Map<String, Object> 		session;
    private UserDetailsBean 		currentUser;
    private ArrayList<String> 		errorMsgList;
    private String 					autoCompleteName;
    private String 					autoCompParamter;
    private String 					pageIndex;
    private String					pageSize;
    
    @Override
    public String execute() throws Exception {
    	String result = null;
        
        try {
        	errorMsgList = new ArrayList<String>();
        	
        	if (!EnjoyUtils.chkNull(command)) {
            	log.info("Call Default success method");
                command = SUCCESS;
            }
        	
        	log.info("[execute] command :: " + command);
        	
        	method = this.getClass().getMethod(command, new Class[0]);
            result = (String) method.invoke(this, new Object[0]);
            
            log.info("[execute] result :: " + result);
            
        }catch(EnjoyException e){
        	if(errorMsgList.isEmpty()){
        		errorMsgList.add(e.getMessage());
        	}
        	
        	log.error("[execute] :: ", e);
        	result = ERROR_BUSINESS;
        } catch (Exception ne) {
        	log.error("[execute] :: ", ne);
        	
        	Exception ex = null;
            if (ne.getCause() != null) {
                try {
                    ex = (Exception) ne.getCause();
                } catch (Exception eee) {
                    ex = ne;
                }
            } else {
                ex = ne;
            }
            
            if (ex instanceof EnjoyException) {
            	if(errorMsgList.isEmpty()){
            		errorMsgList.add(ex.getMessage());
            	}
            	result = ERROR_BUSINESS;
            }else{
            	errorMsgList.add("เกิดข้อผิดพลาดในการใช้ระบบ กรุณาติดต่อผู้ดูแลระบบ");
            	result = ERROR;
            }
        }
        log.info("---------------> Return result is:" + result);

        return result;
    }
    
    public UserDetailsBean getCurrentUser() {
        if (currentUser == null) {
            currentUser = (UserDetailsBean) session.get(Constants.Session.LOGIN_KEY);
        }
        return currentUser;
    }
    
    public void writeMSG(String msg) {
		PrintWriter print = null;
		try {
			log.info("msg :: " + msg);
			response.setContentType("text/html; charset=UTF-8");
			print = response.getWriter();
			print.write(msg);
		} catch (Exception e) {
			log.error("writeMSG :: ", e);
		}
	}
    
    public void writeCorePDF(String pdfName,JSONObject obj, String title) throws Exception{
    	ViewPdfMainForm			viewPdfMainForm				= new ViewPdfMainForm();
		DataOutput 				output 						= null;
		ByteArrayOutputStream	buffer						= null;
		byte[] 					bytes						= null;
		
    	try {
    		buffer = viewPdfMainForm.writeCorePDF(pdfName, obj, title);
    		
    		response.setContentType( "application/pdf" );
			response.setHeader("Content-Disposition", "filename=".concat(String.valueOf(pdfName+".pdf")));
			output 	= new DataOutputStream( this.response.getOutputStream() );
			bytes 	= buffer.toByteArray();
	
			this.response.setContentLength(bytes.length);
			for( int i = 0; i < bytes.length; i++ ) { 
				output.writeByte( bytes[i] ); 
			}
		} catch (Exception e) {
			throw e;
		}
    }
    
    public void writePDF(String pdfName,JSONObject obj, String title) throws Exception{
    	ViewPdfMainForm			viewPdfMainForm				= new ViewPdfMainForm();
		DataOutput 				output 						= null;
		ByteArrayOutputStream	buffer						= null;
		byte[] 					bytes						= null;
		
    	try {
    		buffer = viewPdfMainForm.writePDF(pdfName, obj, title);
    		
    		response.setContentType( "application/pdf" );
			response.setHeader("Content-Disposition", "filename=".concat(String.valueOf(pdfName+".pdf")));
			output 	= new DataOutputStream( this.response.getOutputStream() );
			bytes 	= buffer.toByteArray();
	
			this.response.setContentLength(bytes.length);
			for( int i = 0; i < bytes.length; i++ ) { 
				output.writeByte( bytes[i] ); 
			}
		} catch (Exception e) {
			throw e;
		}
    }
    
    public void writePDFA5(String pdfName,JSONObject obj, String title) throws Exception{
    	ViewPdfMainForm			viewPdfMainForm				= new ViewPdfMainForm();
		DataOutput 				output 						= null;
		ByteArrayOutputStream	buffer						= null;
		byte[] 					bytes						= null;
		
    	try {
    		buffer = viewPdfMainForm.writePDFA5(pdfName, obj, title);
    		
    		response.setContentType( "application/pdf" );
			response.setHeader("Content-Disposition", "filename=".concat(String.valueOf(pdfName+".pdf")));
			output 	= new DataOutputStream( this.response.getOutputStream() );
			bytes 	= buffer.toByteArray();
	
			this.response.setContentLength(bytes.length);
			for( int i = 0; i < bytes.length; i++ ) { 
				output.writeByte( bytes[i] ); 
			}
		} catch (Exception e) {
			throw e;
		}
    }
    
    public void writePDFWithPadding(String pdfName,JSONObject obj, String title,float h,float r,float b,float l) throws Exception{
    	ViewPdfMainForm			viewPdfMainForm				= new ViewPdfMainForm();
		DataOutput 				output 						= null;
		ByteArrayOutputStream	buffer						= null;
		byte[] 					bytes						= null;
		
    	try {
    		buffer = viewPdfMainForm.writePDFWithPadding(pdfName, obj, title, h, r, b, l);
    		
    		response.setContentType( "application/pdf" );
			response.setHeader("Content-Disposition", "filename=".concat(String.valueOf(pdfName+".pdf")));
			output 	= new DataOutputStream( this.response.getOutputStream() );
			bytes 	= buffer.toByteArray();
	
			this.response.setContentLength(bytes.length);
			for( int i = 0; i < bytes.length; i++ ) { 
				output.writeByte( bytes[i] ); 
			}
		} catch (Exception e) {
			throw e;
		}
    }
    
    protected PaginatedListBean createPaginate(int pageSize) throws Exception {
    	PaginatedListBean paginate = new PaginatedListBean(request.getParameterMap());
        paginate.setIndex(!EnjoyUtils.chkNull(pageIndex) ? 1 : Integer.parseInt(pageIndex));
        paginate.setPageSize(pageSize);
        return paginate;
    }

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}
