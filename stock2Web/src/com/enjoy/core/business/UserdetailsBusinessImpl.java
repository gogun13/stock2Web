package com.enjoy.core.business;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enjoy.core.bean.ComboBean;
import com.enjoy.core.bean.PagesDetailBean;
import com.enjoy.core.bean.PaginatedListBean;
import com.enjoy.core.bean.RefconstantcodeBean;
import com.enjoy.core.bean.RelationUserAndCompanyBean;
import com.enjoy.core.bean.UserDetailsBean;
import com.enjoy.core.bean.UserPrivilegeBean;
import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.Constants;
import com.enjoy.core.services.RefconstantcodeService;
import com.enjoy.core.services.RelationUserAndCompanyService;
import com.enjoy.core.services.UserdetailsService;
import com.enjoy.core.utils.EnjoyEncryptDecrypt;
import com.enjoy.core.utils.EnjoyUtils;
import com.enjoy.core.utils.SendMail;

@Service
public class UserdetailsBusinessImpl extends AbstractBusiness implements UserdetailsBusiness{
	private static final Logger logger = Logger.getLogger(UserdetailsBusinessImpl.class);
	
	@Autowired
	UserdetailsService userdetailsService;
	
	@Autowired
	RefconstantcodeService refconstantcodeService;
	
	@Autowired
	RelationUserAndCompanyService relationUserAndCompanyService;

	@Override
	public UserDetailsBean userSelect(String userEmail, String pass) throws Exception {
		logger.info("[userSelect][Begin]");
		
		UserDetailsBean 				bean 				= null;
		String							passEncypt			= null;
		ArrayList<UserPrivilegeBean> 	userPrivilegeList 	= null;
		ArrayList<PagesDetailBean>		pagesDetailList		= null;
		try{
			passEncypt	= EnjoyEncryptDecrypt.enCryption(userEmail, pass);
			bean 		= userdetailsService.userSelect(userEmail, passEncypt);
			
			if(bean!=null){
				userPrivilegeList = userdetailsService.userPrivilegeListSelect(bean.getUserPrivilege());
				
				for(UserPrivilegeBean vo:userPrivilegeList){
					pagesDetailList = userdetailsService.pagesDetailListSelect(vo.getPagesCode());
					vo.setPagesDetail(pagesDetailList);
				}
				
				bean.setUserPrivilegeList(userPrivilegeList);
				
			}
			
		}catch(Exception e){
			logger.error("[userSelect] :: ",e);
			throw e;
		}finally{
			logger.info("[userSelect][End]");
		}
		
		return bean;
	}
	
	@Override
	public void changePassword(String oldPassword, String newPassword, UserDetailsBean userData) throws Exception{
		logger.info("[changePassword][Begin]");
		
		String				oldUserPassword	    = null;
		String				newUserPassword	    = null;
		UserDetailsBean		vo					= new UserDetailsBean();
		int					rowUpd				= 0;
		
		try{
			oldUserPassword		= EnjoyEncryptDecrypt.enCryption(userData.getUserEmail(), oldPassword);
			newUserPassword		= EnjoyEncryptDecrypt.enCryption(userData.getUserEmail(), newPassword); 
			
			if (!oldUserPassword.equals(userData.getPwd())) {
				throw new EnjoyException("รหัสผ่านเดิมไม่ถูกต้อง");
			}
			
			vo.setUserUniqueId(userData.getUserUniqueId());
			vo.setPwd(newUserPassword);
			
			rowUpd = userdetailsService.updateUserPassword(vo);
			
			if(rowUpd!=1){
				throw new EnjoyException("การเปลี่ยนรหัสผ่านเกิดข้อผิดพลาด กรุณาติดต่อผู้ดูแลระบบ");
			}
			
		}catch(Exception e){
			logger.error("[changePassword] :: ",e);
			throw e;
		}finally{
			logger.info("[changePassword][End]");
		}
	}

	@Override
	public ArrayList<ComboBean> getRefuserstatusCombo() throws Exception {
		return userdetailsService.getRefuserstatusCombo();
	}

	@Override
	public ArrayList<UserPrivilegeBean> getUserprivilege() throws Exception {
		return userdetailsService.getUserprivilege();
	}

	@Override
	public UserDetailsBean getUserdetail(int userUniqueId) throws Exception {
		return userdetailsService.getUserdetail(userUniqueId);
	}

	@Override
	public int checkDupUserEmail(String userEmail, String pageMode,int userUniqueId) throws Exception {
		return userdetailsService.checkDupUserEmail(userEmail, pageMode, userUniqueId);
	}

	@Override
	public int saveUserDetailsBean(UserDetailsBean vo, String pageMode, UserDetailsBean userData, String sendMailFlag, String password)throws Exception {
		logger.info("[saveUserDetailsBean][Begin]");
		
		String 		pwd;
		String		userLevel	= null;
		int			userUniqueId = 0;
		RelationUserAndCompanyBean relationUserAndCompanyBean;
		String		fullName;
		SendMail	sendMail;
		
		try{
			logger.info("[saveUserDetailsBean] pageMode 	:: " + pageMode);
			logger.info("[saveUserDetailsBean] sendMailFlag :: " + sendMailFlag);
			
			userLevel	= vo.getUserPrivilege().indexOf("R01") > -1?"9":"1";
			vo.setUserLevel(userLevel);
			
			if(pageMode.equals(Constants.NEW)){
				pwd	= EnjoyEncryptDecrypt.enCryption(vo.getUserEmail(), password);
				vo.setPwd(pwd);
				
				userdetailsService.saveUserDetails(vo);
				userUniqueId = userdetailsService.lastId();
				
				//ไม่ใช่ user admin enjoy ให้ insert ลง table relationuserncompany ด้วย
				if(userData.getUserUniqueId()!=1){
					relationUserAndCompanyBean = new RelationUserAndCompanyBean();
					relationUserAndCompanyBean.setUserUniqueId(String.valueOf(userUniqueId));
					relationUserAndCompanyBean.setTin(userData.getTin());
					
					relationUserAndCompanyService.insertRelationUserAndCompany(relationUserAndCompanyBean);
				}
				
				/*Begin send new password to email*/
				fullName = vo.getUserName() + " " + vo.getUserSurname();
				if("Y".equals(sendMailFlag)){
					sendMail = new SendMail();
					sendMail.sendMail(fullName, vo.getUserEmail(), password, vo.getUserEmail());
				}
				/*End send new password to email*/
				
			}else{
				userdetailsService.updateUserDetail(vo);
				userUniqueId = vo.getUserUniqueId();
			}
		}catch(Exception e){
			logger.error("[saveUserDetailsBean] :: ",e);
			throw e;
		}finally{
			logger.info("[saveUserDetailsBean][End]");
		}
		return userUniqueId;
	}
	
	@Override
	public String getSendMailFlag(String tin)throws Exception {
		logger.info("[getSendMailFlag][Begin]");
		
		RefconstantcodeBean refconstantcodeBean = null;
		String				codeDisplay			= null;
		
		try{
			refconstantcodeBean = refconstantcodeService.getDetail(Constants.SEND_MAIL_ID, tin);
			codeDisplay = refconstantcodeBean.getCodeDisplay();
			
		}catch(Exception e){
			logger.error("[getSendMailFlag] :: ",e);
			throw e;
		}finally{
			logger.info("[getSendMailFlag][End]");
		}
		return codeDisplay;
	}

	@Override
	public void changePassword(String newPassword, int userUniqueId)throws Exception {
		userdetailsService.changePassword(newPassword, userUniqueId);
		
	}

	@Override
	public ArrayList<ComboBean> userFullNameList(String userFullName,String tin, int userUniqueId) throws Exception {
		return userdetailsService.userFullNameList(userFullName, tin, userUniqueId);
	}

	@Override
	public void getListUserdetail(PaginatedListBean paginatedList,UserDetailsBean userDetailsBean) throws Exception {
		logger.info("[getListUserdetail][Begin]");
		
		ArrayList<UserPrivilegeBean> 	listUserprivilege   = null;
		Hashtable<String, String> 		fUserprivilege		= new Hashtable<String, String>();
		ArrayList<UserDetailsBean>		userDetailsList 	= null;
		String[]						arrPrivilegeCode	= null;
		String							privilegeName		= "";
		
		try{
			listUserprivilege = userdetailsService.getUserprivilege();
			for(UserPrivilegeBean userprivilege :listUserprivilege){
				fUserprivilege.put(userprivilege.getPrivilegeCode() , userprivilege.getPrivilegeName());
			}
			
			userdetailsService.getListUserdetail(paginatedList,userDetailsBean);
			
			userDetailsList = (ArrayList<UserDetailsBean>) paginatedList.getList();
			
			logger.info("[getListUserdetail] userDetailsList :: " + userDetailsList.size());
			
			for(UserDetailsBean vo:userDetailsList){
				arrPrivilegeCode = vo.getUserPrivilege().split("\\,");
				privilegeName	 = "";
				
				for(int j=0;j<arrPrivilegeCode.length;j++){
					if (EnjoyUtils.chkNull(privilegeName)) privilegeName = privilegeName + "<br>";
					privilegeName   = privilegeName + "- " +fUserprivilege.get(arrPrivilegeCode[j]);
				}
				
				vo.setUserPrivilege(privilegeName);
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			logger.info("[getListUserdetail][End]");
		}
	}

	@Override
	public ArrayList<ComboBean> getCompanyList(int userUniqueId)throws Exception {
		return relationUserAndCompanyService.getCompanyList(userUniqueId);
	}

	@Override
	public ComboBean getCompanyForAdminEnjoy(int userUniqueId) throws Exception {
		return relationUserAndCompanyService.getCompanyForAdminEnjoy(userUniqueId);
	}
	
	@Override
	public void getListUserdetailForLookUp(PaginatedListBean paginatedList,UserDetailsBean userDetailsBean) throws Exception {
		userdetailsService.getListUserdetailForLookUp(paginatedList,userDetailsBean);
	}

	@Override
	public void updateUserStatusByUserEmail(String userEmail, String userStatus)throws Exception {
		userdetailsService.updateUserStatusByUserEmail(userEmail, userStatus);
		
	}

	@Override
	public UserDetailsBean getUserdetailByTin(String fullName, String tin)throws Exception {
		return userdetailsService.getUserdetailByTin(fullName, tin);
	}

}
