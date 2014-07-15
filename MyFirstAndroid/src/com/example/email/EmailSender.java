package com.example.email;


import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;
import javax.mail.util.ByteArrayDataSource;




public class EmailSender extends Authenticator {
	//微宝邮件账号固有信息
	private final String emailSender;
	private final String senderPassword;
	
	//发送邮件配置
	private final String mailServerHost="smtp.gmail.com";
	private Session session;
	
	//邮件本身相关信息
	private String toAddress;
	private String subject;
	private String content;
	
	public EmailSender(String sender,String password) {
		emailSender = sender;
		senderPassword = password;
		//初始化发送配置
		initialize();
	}
	
	private void initialize(){
		Properties props = new Properties();
		
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", mailServerHost);
		props.put("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
        props.setProperty("mail.smtp.socketFactory.fallback", "false"); 
        props.setProperty("mail.smtp.port", "465"); 
        props.setProperty("mail.smtp.socketFactory.port", "465"); 
		
		session = Session.getDefaultInstance(props, this);
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		// TODO Auto-generated method stub
		return new PasswordAuthentication(emailSender, senderPassword);
	}
	
	public synchronized void sendEmail() {
		MimeMessage message = new MimeMessage(session);
		DataHandler handler = new DataHandler(new ByteArrayDataSource(content.getBytes(), "text/plain") );
		
		//设置该邮件详细信息
		try {
			message.setSender(new InternetAddress(emailSender));
			message.setSubject(subject);
			message.setDataHandler(handler);
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
			Transport.send(message);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
