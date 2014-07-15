package com.example.email;

import java.io.UnsupportedEncodingException;

import org.kobjects.base64.Base64;

import com.example.entity.PersonModel;

public class UrlInEmail {
	public static String getPasswordUrl(String id)
	{
		String url = "http://hyacinth02070.oicp.net/YourPassword.aspx?id=";
		byte[] encode;
		try {
			encode = id.getBytes("UTF-8");
			// base64 加密  
			String idEncode = new String(Base64.encode(encode));  
			return url+idEncode;
		} 
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  
	}
	public static String confrimUrl()
	{
		String url = "http://hyacinth02070.oicp.net/Confirm.aspx?id=";
		byte[] encodeId,encodeEmail;
		try {
			encodeId = PersonModel.weibaoID.getBytes("UTF-8");
			encodeEmail = PersonModel.email.getBytes("UTF-8");
			// base64 加密  
			String idEncode = new String(Base64.encode(encodeId));   
			String emailEncode = new String(Base64.encode(encodeEmail));
			url = url+idEncode+"&email="+emailEncode;
			return url;
		} 
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}  
	}
}
