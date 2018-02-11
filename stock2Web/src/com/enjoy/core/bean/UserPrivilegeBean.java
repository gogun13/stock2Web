package com.enjoy.core.bean;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserPrivilegeBean extends BaseDTO{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String privilegeCode;
	private String privilegeName;
	private String pagesCode;
	private ArrayList<PagesDetailBean> 	pagesList;
	
	public UserPrivilegeBean(){
		this.privilegeCode			= "";
		this.privilegeName			= "";
		this.pagesList			  	= new ArrayList<PagesDetailBean>();
		this.pagesCode				= "";
	}

	public String getPrivilegeCode() {
		return privilegeCode;
	}

	public void setPrivilegeCode(String privilegeCode) {
		this.privilegeCode = privilegeCode;
	}

	public String getPrivilegeName() {
		return privilegeName;
	}

	public void setPrivilegeName(String privilegeName) {
		this.privilegeName = privilegeName;
	}

	public ArrayList<PagesDetailBean> getPagesDetail() {
		return pagesList;
	}

	public void setPagesDetail(ArrayList<PagesDetailBean> pagesList) {
		this.pagesList = pagesList;
	}

	public String getPagesCode() {
		return pagesCode;
	}

	public void setPagesCode(String pagesCode) {
		this.pagesCode = pagesCode;
	}

	
}
