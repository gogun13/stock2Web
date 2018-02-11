package com.enjoy.core.bean;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDetailsBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -742607612367707102L;
	
	private int 							userUniqueId;
	private String							userEmail;
	private String 							userName;
	private String 							userSurname;
	private String 							userPrivilege;
	private String 							userLevel;
	private String 							userStatus;
	private String 							flagChangePassword;
	private String 							flagAlertStock;
	private ArrayList<UserPrivilegeBean> 	userPrivilegeList;
	private String 							pwd;
	private String  						currentDate;
	private String							errMsg;
	private String							userFullName;
	private String							userStatusName;
	private	String							flagChkCompany;
	private String							flagSalesman;
	private String							commission;
	private String							remark;
	private String							tin;
	private String							companyName;
	private String 							seqDis;
	
	public UserDetailsBean(){
		this.userUniqueId		= 0;
		this.userName			= "";
		this.userSurname		= "";
		this.userPrivilege		= "";
		this.userLevel			= "";
		this.userStatus			= "";
		this.flagChangePassword	= "";
		this.flagAlertStock		= "N";//ตอนนี้ไม่ใช้แล้วเลยเซตเป็น N ตลอด
		this.userPrivilegeList	= new ArrayList<UserPrivilegeBean>();
		this.pwd				= "";
		this.currentDate		= "";
		this.userEmail			= "";
		this.errMsg			    = "";
		this.userFullName		= "";
		this.userStatusName		= "";
		this.flagChkCompany		= "";
		this.flagSalesman		= "";
		this.commission			= "";
		this.remark				= "";
		this.tin				= "";
		this.companyName		= "";
		this.seqDis				= "1";
	}
}
