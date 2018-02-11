package com.enjoy.core.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PagesDetailBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String 	  pageCodes	  = null;
	private String 	  pageNames	  = null;
	private String	  pathPages   = null;
	
	public PagesDetailBean(){
		this.pageCodes			  = "";
		this.pageNames			  = "";
		this.pathPages			  = "";
	}
}
