package com.example.myfirstandroid;

import android.app.Application;

public class WeiBaoApp extends Application{
	private static WeiBaoApp instance;  
    
    
    public static WeiBaoApp getContext(){  
        return instance;  
    }  
  
  
    @Override  
    public void onCreate() {  
        // TODO Auto-generated method stub  
        super.onCreate();  
        instance=this;  
    }  
}
