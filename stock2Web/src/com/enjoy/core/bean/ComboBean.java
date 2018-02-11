package com.enjoy.core.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComboBean extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String	code;
	private String	desc;
	
	public ComboBean(){
		this.code		= "";
		this.desc		= "";
		
	}
	
	public ComboBean(String code, String desc){
		this.code		= code;
		this.desc		= desc;
		
	}
}
