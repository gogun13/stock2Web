package com.enjoy.core.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.enjoy.core.utils.EnjoyUtils;

public class ConfigFile {
	private static final String PATH_LOG   			= "path.log";
	private static final String OPEN_LOG   			= "open.log";
	private static final String SYSADMIN_USER   	= "sysAdmin.user";
	private static final String SYSADMIN_PASSWORD   = "sysAdmin.password";
	private static final String MAIL_USER   		= "mail.user";
	private static final String MAIL_PWD   			= "mail.password";
	private static final String PDF_FONT   			= "pdf.font";
	private static final String PDF_PACKAGE   		= "pdf.package";
	private static final String PADING_CUS_CODE   	= "pading.cusCode";
	private static final String VAT   				= "system.vat";
	private static final String PADING_RECIVE_NO   	= "pading.reciveNo";
	private static final String PADING_INVOICE_CODE = "pading.invoiceCode";
	
	private static ConfigFile configFile;
	private static Properties properties ;
	

	public ConfigFile(String fileName) throws Exception {
		try {
			properties = new Properties(); 
			properties.load(new FileInputStream(fileName)); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("FileNotFoundException");
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("IOException");
		}
	} 	
	public static void init(String fileName) throws Exception{
		if (configFile == null) {
			configFile = new ConfigFile(fileName);
		}
	}
	public static Properties getProperties(){
		return properties;
	}
	
	public static String getText( String arg ){
		String result = ConfigFile.getProperties().getProperty( arg );		
	    result = EnjoyUtils.convertDataThai(result);
System.out.println("result  :: " + result);	
		return result;
	}
	
	public static String getPathLog() {
		return getText(PATH_LOG);
	}
	
	public static String getOpenLog() {
		return getText(OPEN_LOG);
	}
	
	public static String getSysAdminUser() {
		return getText(SYSADMIN_USER);
	}
	
	public static String getSysAdminPassword() {
		return getText(SYSADMIN_PASSWORD);
	}
	
	public static String getMAIL_USER() {
		return getText(MAIL_USER);
	}
	
	public static String getMAIL_PWD() {
		return getText(MAIL_PWD);
	}
	
	public static String getPDF_FONT() {
		return getText(PDF_FONT);
	}
	
	public static String getPDF_PACKAGE() {
		return getText(PDF_PACKAGE);
	}
	
	public static String getPadingCusCode() {
		return getText(PADING_CUS_CODE);
	}
	
	public static String getVat() {
		return getText(VAT);
	}
	
	public static String getPadingReciveNo() {
		return getText(PADING_RECIVE_NO);
	}
	
	public static String getPadingInvoiceCode() {
		return getText(PADING_INVOICE_CODE);
	}
}
