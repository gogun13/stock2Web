package com.enjoy.core.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RelationUserAndCompanyBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userUniqueId;
	private String tin;
	private String userEmail;
	private String userFullName;
	private String userStatus;
	private String userStatusName;
	private String rowStatus;
	private String seq;
	
	public RelationUserAndCompanyBean(){
		this.userUniqueId 		= "";
		this.tin 				= "";
		this.userEmail 			= "";
		this.userFullName 		= "";
		this.userStatus 		= "";
		this.userStatusName 	= "";
		this.rowStatus 			= "";
		this.seq 				= "";
	}
}
