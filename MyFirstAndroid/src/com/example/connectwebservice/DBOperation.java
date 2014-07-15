package com.example.connectwebservice;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.entity.McItem;
import com.example.entity.McPreferentialItem;
import com.example.entity.PersonModel;
import com.example.entity.WeiJinEntity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.KeepAliveHttpsTransportSE;

public class DBOperation {

	private static String URL ="http://hyacinth02070.oicp.net/WebService1.asmx"; 
    private static String NAMESPACE ="http://tempuri.org/";
    private static String METHOD_NAME; 
    private static String SOAP_ACTION;
    
    private static int insert(String tableName, String columnNames, String data){
    	METHOD_NAME = "insert";
    	SOAP_ACTION = "http://tempuri.org/insert";
    	try{
    		Log.i("aaa", "INSERT INTO " + tableName + "(" + columnNames + ") VALUES (" + data + " )");
    		SoapObject rpc =new SoapObject(NAMESPACE,METHOD_NAME);
        	rpc.addProperty("tableName", tableName);
        	rpc.addProperty("columnNames", columnNames);
        	rpc.addProperty("data", data);
        	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        	envelope.bodyOut = rpc;
        	envelope.dotNet = true;
        	HttpTransportSE ht=new HttpTransportSE(URL);
        	ht.debug = true;
        	ht.call(SOAP_ACTION,envelope);
        	SoapObject soapObject = (SoapObject) envelope.bodyIn;
        	if(Boolean.valueOf(soapObject.getProperty(0).toString()))
        		return 1;
        	else
        		return 0;
    	}
    	catch (Exception e) { 
    		Log.i("aaa",e.toString());  
    		return -1;
        }  
    	
    }
    
    private static int delete(String tableName, String condition){
    	METHOD_NAME ="delete"; 
        SOAP_ACTION = "http://tempuri.org/delete";
    	try{
    		Log.i("aaa","DELETE FROM " + tableName + " WHERE " + condition);
    		SoapObject rpc =new SoapObject(NAMESPACE,METHOD_NAME);
        	rpc.addProperty("tableName", tableName);
        	rpc.addProperty("condition", condition);
        	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        	envelope.bodyOut = rpc;
        	envelope.dotNet = true;
        	HttpTransportSE ht=new HttpTransportSE(URL);
        	ht.debug = true;
        	ht.call(SOAP_ACTION,envelope);
        	SoapObject soapObject = (SoapObject) envelope.bodyIn;
        	if(Boolean.valueOf(soapObject.getProperty(0).toString()))
        		return 1;
        	else
        		return 0;
    	}
    	catch (Exception e) { 
    		Log.i("aaa",e.toString());  
    		return -1;
        }  
    	
    }
    
    private static int update(String tableName, String content, String condition){
    	METHOD_NAME ="update"; 
        SOAP_ACTION = "http://tempuri.org/update";
    	try{
    		SoapObject rpc =new SoapObject(NAMESPACE,METHOD_NAME);
        	rpc.addProperty("tableName", tableName);
        	rpc.addProperty("content", content);
        	rpc.addProperty("condition", condition);
        	Log.i("aaa", "UPDATE " + tableName + " set " + content + " WHERE " + condition);
        	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        	envelope.bodyOut = rpc;
        	envelope.dotNet = true;
        	HttpTransportSE ht=new HttpTransportSE(URL);
        	ht.debug = true;
        	ht.call(SOAP_ACTION,envelope);
        	SoapObject soapObject = (SoapObject) envelope.bodyIn;
        	if(Boolean.valueOf(soapObject.getProperty(0).toString()))
        		return 1;
        	else
        		return 0;
    	}
    	catch (Exception e) { 
    		Log.i("aaa",e.toString());  
    		return -1;
        }  
    }
    
    private static ArrayList<ArrayList<String>> select(String tableName, String columnName, String condition){
    	METHOD_NAME ="select"; 
        SOAP_ACTION = "http://tempuri.org/select";
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        try { 
        	Log.i("aaa",condition);
        	SoapObject rpc =new SoapObject(NAMESPACE,METHOD_NAME);
        	rpc.addProperty("tableName", tableName);
        	rpc.addProperty("columnName", columnName);
        	rpc.addProperty("condition", condition);
        	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        	envelope.bodyOut = rpc;
        	envelope.dotNet = true;
        	HttpTransportSE ht=new HttpTransportSE(URL);
        	ht.debug = true;
        	ht.call(SOAP_ACTION,envelope);
        	SoapObject soapObject = (SoapObject) envelope.bodyIn;
            SoapObject soap1 = (SoapObject) soapObject.getProperty(0);
            SoapObject soapChild = (SoapObject)soap1.getProperty(1);
            if (soapChild.getPropertyCount() != 0) {
           		SoapObject soap2=(SoapObject)soapChild.getProperty(0);
                
                for(int i=0;i<soap2.getPropertyCount();i++){
                	ArrayList<String> temp = new ArrayList<String>();
                	SoapObject soap3=(SoapObject)soap2.getProperty(i);
                	for(int j=0;j<soap3.getPropertyCount();j++)
                		temp.add(soap3.getProperty(j).toString());
                	temp.trimToSize();
                	result.add(temp);
                }
        	}
        	result.trimToSize();
        	return result;
        }
        catch (Exception e) { 
    		Log.i("aaa",e.toString());  
    		return null;
        }
    }
    
	private static List<McItem> getMc(String condition) {
        METHOD_NAME ="select"; 
        SOAP_ACTION = "http://tempuri.org/select";
		List<McItem> mclist = new ArrayList<McItem>();
		ArrayList<ArrayList<String>> mcListTmp = new ArrayList<ArrayList<String>>();
		mcListTmp = select("Store","*",condition);
        for(int i=0;i<mcListTmp.size();i++)
        {
        	McItem mcItem=new McItem();
            mcItem.setMcId(mcListTmp.get(i).get(0));
            mcItem.setMcAddress(mcListTmp.get(i).get(2));
            mcItem.setMcIntro(mcListTmp.get(i).get(5));
            mcItem.setMcName(mcListTmp.get(i).get(1));
            mcItem.setMcSlogen(mcListTmp.get(i).get(4));
            mcItem.setMcTelephone(mcListTmp.get(i).get(3));
            Log.i("aaa",mcListTmp.get(i).get(6));
            ImageHandeller.download(mcListTmp.get(i).get(6));
            mcItem.setMcLogoUrl(mcListTmp.get(i).get(6));
                   
            ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
            temp = select("UserSubscribeStore","*","Where UserId = "+PersonModel.weibaoID+" AND StoreId = "+mcItem.getMcId());
            if(temp.size()!=0)
               	mcItem.setIsSubscribed(true);
            else
               	mcItem.setIsSubscribed(false);
                    
                mclist.add(mcItem);//封装后存入集合中
        }
        return mclist;
    }
    
	private static int getPersonalInfo(String condition) {
		METHOD_NAME ="select"; 
        SOAP_ACTION = "http://tempuri.org/select";
        try { 
        	Log.i("aaa",condition);
         	SoapObject rpc =new SoapObject(NAMESPACE,METHOD_NAME);
           	rpc.addProperty("tableName", "WUser");
           	rpc.addProperty("columnName", "*");
           	rpc.addProperty("condition", condition);
           	SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
           	envelope.bodyOut = rpc;
        	envelope.dotNet = true;
           	HttpTransportSE ht=new HttpTransportSE(URL);
           	ht.debug = true;
            ht.call(SOAP_ACTION,envelope);        
            SoapObject soapObject = (SoapObject) envelope.bodyIn;
            SoapObject soap1 = (SoapObject) soapObject.getProperty(0);
            SoapObject soapChild = (SoapObject)soap1.getProperty(1);
            if (soapChild.getPropertyCount() != 0) {
           		SoapObject soap2=(SoapObject)soapChild.getProperty(0);
                SoapObject soap3=(SoapObject)soap2.getProperty(0);
                if(soap3.hasProperty("Name"))
                	PersonModel.name=soap3.getProperty("Name").toString();
                else
                	PersonModel.name="";
                if(soap3.hasProperty("District"))
                	PersonModel.district=soap3.getProperty("District").toString();
                else
                	PersonModel.district="";
                PersonModel.weibaoID=soap3.getProperty("UserId").toString();
                if(soap3.hasProperty("Signature"))
                    PersonModel.signature=soap3.getProperty("Signature").toString();
                else
                	PersonModel.signature="";
                if(soap3.hasProperty("Telephone"))
                    PersonModel.phoneNumber=soap3.getProperty("Telephone").toString();
                else 
					PersonModel.phoneNumber = "";
                if(soap3.hasProperty("Email"))
                    PersonModel.email= soap3.getProperty("Email").toString();
                else 
					PersonModel.email = "";
                PersonModel.weijin = ""+subFloat(Float.parseFloat(soap3.getProperty("WeiJinAmount").toString()),1);
                PersonModel.pension = ""+subFloat(Float.parseFloat(soap3.getProperty("PensionAmount").toString()),1);
                PersonModel.password = soap3.getProperty("Password").toString();
                PersonModel.avatarLocalPathString = soap3.getProperty("Avatar").toString();
                ImageHandeller.download(PersonModel.avatarLocalPathString);
                
                if(soap3.getProperty("TelephoneChecked").toString().equals("true"))
                   	PersonModel.isPhoneNumberChecked = true;
                else 
                   	PersonModel.isPhoneNumberChecked = false;
                if(soap3.getProperty("EmailChecked").toString().equals("true"))
                   	PersonModel.isEmailChecked = true;
                else 
                  	PersonModel.isEmailChecked = false;
                if(soap3.getProperty("Gender").toString().equals("0"))
                {
                  	PersonModel.gender = "男";
                }
                else if(soap3.getProperty("Gender").toString().equals("1"))
                  	PersonModel.gender = "女";
                else PersonModel.gender = "";
                return 1;
           	}
           	else 
           		return 0;
        }
        catch (Exception e) { 
        	Log.i("aaa",e.toString());
            return -1;
        }  
	}
	
    public static McItem getMcById(String McId)
    {
    	List<McItem> mclist = new ArrayList<McItem>();
		mclist = getMc("WHERE StoreId = "+ McId);
		return mclist.get(0);
    }
    
    private static List<McPreferentialItem> getMcPreListBy(String condition,String mcId)
    {
    	List<McPreferentialItem> mcPreList = new ArrayList<McPreferentialItem>();
    	ArrayList<ArrayList<String>> preList = new ArrayList<ArrayList<String>>();
    	preList = select ( "Coupon", "*", condition);
    	McItem tmp = getMcById(mcId);
    	for(int i=0; i < preList.size() ; i++ )
		{
    		McPreferentialItem temp = new McPreferentialItem();
    		temp.setMcId(mcId);
    		temp.setMcName(tmp.getMcName());
    		temp.setPrefId(preList.get(i).get(0));
    		temp.setPrefTime(preList.get(i).get(2));
    		temp.setPrefDescription(preList.get(i).get(4));
    		temp.setPrefTitle(preList.get(i).get(3));
    		ImageHandeller.download(preList.get(i).get(5));
            temp.setPrefImageUrl(preList.get(i).get(5));
			mcPreList.add(temp);
		}
    	return mcPreList;  
    }
    
    //返回某商户的优惠信息。传入商户ID
    public static List<McPreferentialItem> getMyMcPreListByMcId(String mcId)
    {
    	return getMcPreListBy("WHERE StoreId = "+mcId,mcId);
    }
    
    //返回所有我订阅的商户的优惠信息。
    public static List<McPreferentialItem> getPreOfMe()
    {
    	String McId;
    	List<McPreferentialItem> mcPreList = new ArrayList<McPreferentialItem>();
    	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		list = select ( "UserSubscribeStore", "StoreId", "Where UserId = "+PersonModel.weibaoID);
		for(int i=0; i < list.size() ; i++ )
		{
			McId = list.get(i).get(0);
			List<McPreferentialItem> temp = getMyMcPreListByMcId(McId);
			mcPreList.addAll(temp);			
		}
		return mcPreList;
    }
    
    //在我订阅的商户中查找时使用。传入搜索关键字。
    public static List<McPreferentialItem> getPreOfMeByName(String name)
    {
    	String mcId;
    	List<McPreferentialItem> mcPreList = new ArrayList<McPreferentialItem>();
    	ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		list = select ( "UserSubscribeStore", "StoreId", "Where UserId = "+PersonModel.weibaoID);
		for(int i=0; i < list.size() ; i++ )
		{
			mcId = list.get(i).get(0);
			List<McPreferentialItem> temp = getMcPreListBy("WHERE StoreId = "+mcId+" AND Discount LIKE '%"+name+"%'",mcId);
			mcPreList.addAll(temp);			
		}
		return mcPreList;
    }
   
    //搜索商户名称时调用，传入搜索关键字
	public static List<McItem> getMcByName(String name)
	{
		List<McItem> mclist = new ArrayList<McItem>();
		mclist = getMc("WHERE Name LIKE '%"+ name +"%' ");
		return mclist;
	}
	
	//在已订阅商户中搜索商户名称时调用，传入搜索关键字
	public static List<McItem> getMcOfMeByName(String name)
	{
		List<McItem> mcListOfMe = new ArrayList<McItem>();
		List<McItem> mcListAll = new ArrayList<McItem>();
		mcListAll = getMc("WHERE Name LIKE '%"+ name +"%' ");
		for(int i=0;i<mcListAll.size();i++)
		{
			McItem temp = mcListAll.get(i);
			if(temp.getIsSubscribed())
				mcListOfMe.add(temp);
		}
		return mcListOfMe;
	}
	
	//返回所有商户信息
	public static List<McItem> getMcAll()
	{
		List<McItem> mclist = new ArrayList<McItem>();
		mclist = getMc("");
		return mclist;
	}
	
	//返回我订阅的商户信息
	public static List<McItem> getMcOfMe()
	{
		List<McItem> mclist = new ArrayList<McItem>();
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		list = select ( "UserSubscribeStore", "StoreId", "Where UserId = "+PersonModel.weibaoID );
		Log.i("aaa",list.toString());
		for(int i=0; i < list.size() ; i++ )
		{
			McItem temp = new McItem();
			temp = getMcById(list.get(i).get(0));
			mclist.add(temp);
		}
		return mclist;
	}
	
	//用电话号码查询用户信息，若不存在该用户则返回false
	public static int getPersonalInfoByTelephone(String telephone)
	{
		return getPersonalInfo("WHERE Telephone = '"+ telephone + "' AND TelephoneChecked = 'True'");
	}
	
	//用email查询用户信息，若不存在该用户则返回false
	public static int getPersonalInfoByEmail(String email)
	{
		return getPersonalInfo("WHERE Email = '"+ email + "' AND EmailChecked = 'True'");
	}
	
	//订阅商户的数据库操作，传入订阅的商户id
	public static int subscribe(String McId)
	{
		return insert("UserSubscribeStore","UserId,StoreId",PersonModel.weibaoID + ","+ McId);
	}
	
	//退订商户的数据库操作，传入退订的商户id
	public static int cancelSubs(String McId)
	{
		return delete("UserSubscribeStore","UserId = "+ PersonModel.weibaoID + " AND StoreId = "+ McId);
	}
	
	//获得我所有的微金条目
	private static List<WeiJinEntity> getMyWeiJinBy(String condition)
	{
		List<WeiJinEntity> myWeiJin = new ArrayList<WeiJinEntity>();
		ArrayList<ArrayList<String>> temp = select("WeiJin","*","Where UserId = "+PersonModel.weibaoID+condition);
		for(int i=0;i<temp.size();i++)
		{
			WeiJinEntity tmp = new WeiJinEntity();
			McItem mc = getMcById(temp.get(i).get(2));
			String mcName = mc.getMcName();
			tmp.setWeiJinIDString(temp.get(i).get(0));
			tmp.setUserIDString(temp.get(i).get(1));
			tmp.setStoreIDString(temp.get(i).get(2));
			tmp.setStoreName(mcName);
			tmp.setAmount(Float.parseFloat(temp.get(i).get(3)));
			tmp.setCreateTimeString(temp.get(i).get(4));
			tmp.setBriefInfoString(temp.get(i).get(5));
			tmp.setIsExchanged(Boolean.parseBoolean(temp.get(i).get(6)));
			tmp.setExchangedTimeString(temp.get(i).get(7));
			myWeiJin.add(tmp);
		}
		return myWeiJin;
	}
	
	public static List<WeiJinEntity> getMyWeiJin()
	{
		return getMyWeiJinBy("");
	}
	
	public static List<WeiJinEntity> getExchangedWeiJin()
	{
		return getMyWeiJinBy(" AND IsExchanged = 'True'");
	}
	
	public static List<WeiJinEntity> getNotExchangedWeiJin()
	{
		return getMyWeiJinBy(" AND IsExchanged = 'False'");
	}
	
	//添加一条微金条目
	public static int addWeiJin(String mcId, float amount, String createTime,String brifInfo,String barCodeId)
	{
		String columnNames,content;
		columnNames = "UserId,StoreId,Amount,CreateTime,BrifInfo,IsExchanged,ExchangeTime,BarCodeId";
		content = PersonModel.weibaoID+", "+mcId+","+amount+",'"+createTime+"','"+brifInfo+"', 'False','"+createTime+"','"+barCodeId+"'";
		int i = insert("WeiJin", columnNames, content);
		if(i==1)
		{
			float newWeiJinAmount = Float.parseFloat(PersonModel.weijin)+amount;
			update("WUser","WeiJinAmount = "+newWeiJinAmount,"UserId = "+PersonModel.weibaoID);
		}
		return i;
	}
	
	//用户注册时调用。传入密码和注册的手机号码。
	public static int register(String password,String phoneNumber)
	{
		return insert("WUser","Password,Telephone,TelephoneChecked","'"+password+"','"+phoneNumber+"', 1");
	}
	
	public static int updatePersonName(String name)
	{
		int result = update("WUser","Name = '"+name+"'","UserId = "+ PersonModel.weibaoID);
		if(result == 1)
			PersonModel.name = name;
		Log.i("aaa",""+result);
		return result;
	}
	
	public static int updatePersonGender(String gender)
	{
		int result;
		if(PersonModel.gender.equals("男"))
			result = update("WUser","Gender = 0","UserId = "+ PersonModel.weibaoID);
		else
			result = update("WUser","Gender = 1","UserId = "+ PersonModel.weibaoID);
		if(result == 1)
			PersonModel.gender = gender;
		return result;
	}
	
	public static int updatePersonSig(String signature)
	{
		int result = update("WUser","Signature = '"+signature+"'","UserId = "+ PersonModel.weibaoID);
		if(result == 1)
			PersonModel.signature = signature;
		return result;
	}
	
	public static int disboundEmail()
	{
		int result = update("WUser","EmailChecked = 0","UserId = "+ PersonModel.weibaoID);
		Log.i("aaa",result+"");
		if(result == 1)
			PersonModel.isEmailChecked = false;
		return result;
	}
	
	public static int updatePersonEmail(String email)
	{
		int result = update("WUser","Email = '"+email+"'","UserId = "+ PersonModel.weibaoID);
		if(result == 1)
			PersonModel.email = email;
		return result;
	}
	
	public static int updatePersonPhone(String phoneNumber)
	{
		int result = update("WUser","Telephone = '"+phoneNumber+"'","UserId = "+ PersonModel.weibaoID);
		if(result == 1)
			PersonModel.phoneNumber = phoneNumber;
		return result;
	}

	public static int updatePassword(String password)
	{
		int result = update("WUser","Password = '"+password+"'","UserId = "+ PersonModel.weibaoID);
		if(result == 1)
			PersonModel.password = password;
		return result;
	}
	
	public static int exchangeWeiJin(WeiJinEntity weijin)
	{
		int i = update("WeiJin","IsExchanged = 1","WeiJinId = "+weijin.getWeiJinIDString());
		int j = 0,k=0;
		if(i==1)
		{
			float weijinAmount = weijin.getAmount();
			float pensionAmount = weijinAmount/10;
			float newWeiJinAmount = subFloat(Float.parseFloat(PersonModel.weijin)-weijinAmount, 1);
			float newPensionAmount = subFloat(Float.parseFloat(PersonModel.pension)+pensionAmount,1);
			j = update("WUser","WeiJinAmount ="+newWeiJinAmount,"UserId = "+PersonModel.weibaoID);
			if(j==1)
				k = update("WUser","PensionAmount ="+newPensionAmount,"UserId = "+PersonModel.weibaoID);
			if(k!=1)
			{
				update("WeiJin","IsExchanged = 0","WeiJinId = "+weijin.getWeiJinIDString());
			}
			else{
				PersonModel.weijin = newWeiJinAmount+"";
				PersonModel.pension = newPensionAmount+"";
			}
				
		}
		return k;
	}
	private static float subFloat(float f,int lenght)
	{
	String fStr=String.valueOf(f);
	int i=fStr.indexOf('.');
	String returnStr=fStr.substring(0,i+1+lenght);
	float returnf=(Float.valueOf(returnStr)).floatValue();
	return returnf;
	}

}