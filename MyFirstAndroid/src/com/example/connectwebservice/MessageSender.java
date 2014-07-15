package com.example.connectwebservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.net.HttpURLConnection;
import java.net.URL;
import android.util.Log;

public class MessageSender {
	private static String URL ="http://106.ihuyi.com/webservice/sms.php"; 
    private static String NAMESPACE ="http://106.ihuyi.com/";
    private static String METHOD_NAME = "Submit"; 
    private static String SOAP_ACTION = "http://106.ihuyi.com/Submit";
    
    public static int sendMessage(String phoneNumber)
    {
    	int code = (int)((Math.random()*9+1)*100000);
 
    	try{
    		Log.i("aaa",code+"");
    		SoapObject rpc =new SoapObject(NAMESPACE,METHOD_NAME);
        	rpc.addProperty("account","cf_hester");
        	rpc.addProperty("password","hester067357");
        	rpc.addProperty("mobile",phoneNumber);
        	rpc.addProperty("content","您的手机验证码是："+code+"。请不要将验证码透露给他人【微宝】");
        	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        	envelope.bodyOut = rpc;
        	envelope.dotNet = true;
        	HttpTransportSE ht=new HttpTransportSE(URL);
        	ht.debug = true;
        	ht.call(SOAP_ACTION,envelope);
        	SoapObject soapObject = (SoapObject) envelope.bodyIn;
        	if(soapObject.getProperty(0).toString().equals("2"))
        		return code;
        	else
        		return 0;
    	}
    	catch (Exception e) { 
    		Log.i("aaa",e.toString());  
    		return -1;
        } 
    }
}
