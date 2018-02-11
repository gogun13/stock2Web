package com.enjoy.core.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.enjoy.core.exception.EnjoyException;
import com.enjoy.core.main.ConfigFile;

public class SendMail {
	
	private static final Logger logger = Logger.getLogger(SendMail.class);
	
	public void sendMail(String fullName, String userEmail, String userPwd, String email) throws EnjoyException{
		final String username = ConfigFile.getMAIL_USER();
		final String password = ConfigFile.getMAIL_PWD();
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username, "Enjoy System"));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
			message.setSubject("แจ้ง password ของคุณ " + fullName);
			message.setText("เรียนคุณ " + fullName
				+ "\n\n User : " + userEmail
				+ "\n\n Password : " + userPwd
				+ "\n\n ***กรุณาเก็บ user และ password เอาไว้สำหรับใช้ในการเข้าระบบในครั้งต่อไป ");
 
			Transport.send(message);
 
			logger.info("Send mail done !!");
 
		} catch (MessagingException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new EnjoyException("เกิดข้อผิดพลาดในการส่ง E-mai (E-mail ปลายทางไม่ถูกต้อง)");
		}catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new EnjoyException("เกิดข้อผิดพลาดในการส่ง E-mai");
		}
	}
	
}
