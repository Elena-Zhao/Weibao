package com.example.myfirstandroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public final class FindPasswordActivity extends Activity implements TabHost.OnTabChangeListener{

	private TabHost mTabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		initialiseTabHost();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_password, menu);
		return true;
	}

	private void initialiseTabHost() {
        mTabHost = (TabHost)findViewById(R.id.find_tabhost);
        mTabHost.setup();
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("手机找回密码").setContent(R.id.find_phone_num));
        mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.color.button_tab_bg);
        TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv.setTextSize(17);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("邮箱找回密码").setContent(R.id.find_email));
        mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.color.button_tab_bg);
        tv = (TextView) mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv.setTextSize(17);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
    }
 
    
    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        
    }
    
    public void Configure(View view){
    	Intent intent = new Intent(this, LogInActivity.class);
    	startActivity(intent);
    }
    public void Send(View view){
    	
    }
}
