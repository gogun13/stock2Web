package com.enjoy.core.actions;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.bean.UserPrivilegeBean;
import com.enjoy.core.business.UserdetailsBusiness;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.Constants;
import com.enjoy.core.utils.EnjoyEncryptDecrypt;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.core.utils.SendMail;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDetailsMaintanancesAction extends BaseAction implements ActionControl{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UserDetailsMaintanancesAction.class);
	
	private UserDetailsBean 				userDetailsBean;
	private ArrayList<ComboBean> 			statusCombo;
	private ArrayList<UserPrivilegeBean> 	userprivilegeList;
	private String							pageMode;
	private String							titlePage;
	private int								userUniqueId;
	private String							sendMailFlag;
	private String							userStatusDis;
	private boolean							flagChangePassword;
	private boolean							flagSalesman;
	private String							showBackFlag;
	
	@Autowired
	UserdetailsBusiness userdetailsBusiness;
	
    public String success() throws Exception {
    	logger.info("[success][Begin]");
    	
    	userDetailsBean 	= new UserDetailsBean();
    	titlePage 			= "เพิ่มผู้ใช้งานระบบ";
    	pageMode			= Constants.NEW;
    	sendMailFlag		= userdetailsBusiness.getSendMailFlag(getCurrentUser().getTin());
    	
    	userDetailsBean.setUserStatus("A");
    	userStatusDis		= userDetailsBean.getUserStatus();
    	flagChangePassword 	= false;
    	flagSalesman		= false;
    	
    	setRefference();
    	
		return SUCCESS;
	}
    
    private void setRefference() throws Exception{
		
		logger.info("[setRefference][Begin]");
		
		try{
			statusCombo 		= userdetailsBusiness.getRefuserstatusCombo();
			userprivilegeList 	= userdetailsBusiness.getUserprivilege();
			
		}catch(Exception e){
			logger.error("setRefference :: ", e);
			throw e;
		}finally{
			logger.info("[setRefference][End]");
		}
	}
    
    public String onGetUserDetail() throws Exception{
		logger.info("[onGetUserDetail][Begin]");
		
		try{
			logger.info("[onGetUserDetail] userUniqueId :: " + userUniqueId);
			
			titlePage 		= "แก้ไขผู้ใช้งานระบบ";
			pageMode		= Constants.UPDATE;
			sendMailFlag	= userdetailsBusiness.getSendMailFlag(getCurrentUser().getTin());
			
			setRefference();
			
			userDetailsBean = userdetailsBusiness.getUserdetail(userUniqueId);
			if(userDetailsBean==null){
				throw new EnjoyException("เกิดข้อผิดพลาดในการดึงข้อมูลผู้ใช้งาน");
			}
			userStatusDis	= userDetailsBean.getUserStatus();
			
			if(userDetailsBean.getFlagChangePassword().equals("Y")){
				flagChangePassword = true;
			}else{
				flagChangePassword = false;
			}
			
			if(userDetailsBean.getFlagSalesman().equals("Y")){
				flagSalesman = true;
			}else{
				flagSalesman = false;
			}
			
		}catch(Exception e){
			logger.error("onGetUserDetail :: ", e);
			throw e;
		}finally{
			logger.info("[onGetUserDetail][End]");
		}
		
		return SUCCESS;
		
	}
    
	public String checkDupUserEmail() throws Exception{
		logger.info("[checkDupUserEmail][Begin]");
		
		int 				cou				= 0;
		JSONObject 			obj 			= null;
		
		try{
			logger.info("[checkDupUserEmail] userEmail 		:: " + userDetailsBean.getUserEmail());
			logger.info("[checkDupUserEmail] pageMode 		:: " + pageMode);
			logger.info("[checkDupUserEmail] userUniqueId 	:: " + userUniqueId);
			
			
			cou	= userdetailsBusiness.checkDupUserEmail(userDetailsBean.getUserEmail(), pageMode, userUniqueId);
			
			obj = new JSONObject();
			obj.put("COU",cou);
			
			logger.info("[checkDupUserEmail] cou :: " + cou);
		}catch(Exception e){
			logger.error("checkDupUserEmail :: ", e);
			throw e;
		}finally{
			writeMSG(obj.toString());
			logger.info("[checkDupUserEmail][End]");
		}
		return null;
	}

	
	public String onSave() throws Exception{
		logger.info("[onSave][Begin]");
		
		JSONObject 	obj 		= new JSONObject();
		String 		password	= "";
		
		try{
			if(flagChangePassword){
				userDetailsBean.setFlagChangePassword("Y");
			}else{
				userDetailsBean.setFlagChangePassword("N");
			}
			
			if(flagSalesman){
				userDetailsBean.setFlagSalesman("Y");
			}else{
				userDetailsBean.setFlagSalesman("N");
			}
			
			if(pageMode.equals(Constants.NEW)){
				password = EnjoyUtils.genPassword(8);
			}
			
			userDetailsBean.setUserUniqueId(userUniqueId);
			userUniqueId = userdetailsBusiness.saveUserDetailsBean(userDetailsBean, pageMode, getCurrentUser(), sendMailFlag ,password);
			
			obj = new JSONObject();
			obj.put("pwd"			,password);
			obj.put("userUniqueId"	,userUniqueId);
			
			writeMSG(obj.toString());
			
			
		}catch(Exception e){
			logger.error("onSave :: ", e);
			throw e;
		}finally{
			logger.info("[onSave][End]");
		}
		
		return null;
	}
	
	public String resetPass() throws Exception{
		logger.info("[resetPass][Begin]");
		
		String				pwd			= null;
		String				pwdEncypt	= null;
		JSONObject 			obj 		= null;
		UserDetailsBean 	vo			= null;
		SendMail			sendMail	= null;
		String				fullName	= null;
		
		try{
			logger.info("[resetPass] userUniqueId 		:: " + userUniqueId);
			logger.info("[resetPass] sendMailFlag 		:: " + sendMailFlag);
			
			vo = userdetailsBusiness.getUserdetail(userUniqueId);
			
			pwd							= EnjoyUtils.genPassword(8);
			pwdEncypt					= EnjoyEncryptDecrypt.enCryption(vo.getUserEmail(), pwd);
			
			userdetailsBusiness.changePassword(pwdEncypt ,userUniqueId);
			
			/*Begin send new password to email*/
			fullName = vo.getUserName() + " " + vo.getUserSurname();
			
			if("Y".equals(sendMailFlag)){
				sendMail = new SendMail();
				sendMail.sendMail(fullName, vo.getUserEmail(), pwd, vo.getUserEmail());
			}
			/*End send new password to email*/
			
			obj = new JSONObject();
			obj.put("pwd"	,pwd);
			
			writeMSG(obj.toString());
			
		}catch(Exception e){
			logger.error("resetPass :: ", e);
			throw e;
		}finally{
			logger.info("[resetPass][End]");
		}
		
		return null;
	}
	
	public String genPdf() throws Exception{
		logger.info("[genPdf][Begin]");
		
		JSONObject 				obj 						= new JSONObject();
		String 					fullName 					= null;
		String 					userEmail 					= null;
		String 					pwd 						= null;
		UserDetailsBean 		vo							= null;
		int						id							= 0;
		
		try{
			id 	= EnjoyUtils.parseInt(request.getParameter("userUniqueId"));
			pwd = EnjoyUtils.nullToStr(request.getParameter("pwd"));
			
			logger.info("[genPdf] id  :: " + id);
			logger.info("[genPdf] pwd :: " + pwd);
			
			vo 	= userdetailsBusiness.getUserdetail(id);
			
			fullName 		= vo.getUserName().concat(" ").concat(vo.getUserSurname());
			userEmail 		= vo.getUserEmail();
			
			obj.put("fullName"	,fullName);
			obj.put("userEmail"	,userEmail);
			obj.put("pwd"		,pwd);
			
			writeCorePDF("UserDetailPdfForm", obj, "แจ้งรหัสผู้ใช้งานและรหัสผ่าน");
			
		}catch(Exception e){
			logger.error("genPdf :: ", e);
			throw e;
		}finally{
			logger.info("[genPdf][End]");
		}
		
		return null;
	}
	
	public String onBack()throws Exception {
		return "onBack";
	}

	@Override
	public String autoComplete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
