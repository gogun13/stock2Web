package com.enjoy.core.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class BaseDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String 	createdBy;
	private Date 	createdDate;
	private String 	updatedBy;
	private Date 	updateDate;
	
}
