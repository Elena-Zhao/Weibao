package com.example.myfirstandroid;

import com.example.connectwebservice.DBOperation;
import com.example.email.EmailSender;
import com.example.email.UrlInEmail;
import com.example.entity.PersonModel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public final class FindPasswordActivity extends Activity{

	private TabHost mTabHost;
	private Button submit;
	private TextView email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_password);
		submit = (Button)findViewById(R.id.submit);
		email = (TextView)findViewById(R.id.find_email);
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
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tab) {
        		if(tab.equals("tab2")){
        			submit.setText("发送验证邮件");
        		}else{
        			submit.setText("提交");
        		}
            }
        });
        submit.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(submit.getText().equals("发送验证邮件"))
				{
					if(email.getText().length()==0)
					{
						Toast.makeText(getApplicationContext(), "邮箱不能为空",
								Toast.LENGTH_SHORT).show();
					}
					else 
					{
						String inputEmail = email.getText().toString();
						new SendGetPasswordEmail().execute(inputEmail);
					}
				}
			}
		});
    }
 
    
    
    public void Configure(View view){
    	Intent intent = new Intent(this, LogInActivity.class);
    	startActivity(intent);
    }
    
    public void Send(View view){
    }
    
	class SendGetPasswordEmail extends AsyncTask<String, Void, Boolean> {
		String id;
		String email;
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			boolean isEmailExist = false;
			email = params[0];
			id = DBOperation.getUserIdByEmail(email);
			if(id!=null)
				isEmailExist = true;
			return isEmailExist;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Context context = getApplicationContext();
			if(result)
				new SendEmail().execute(email,id);
			else {
				CharSequence text = "请填写正确的email账号，或者检查您是否联网";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
	}
	class SendEmail extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean isSendSuccess = false;
			EmailSender emailSender = new EmailSender("weibao2013@gmail.com",
					"weibaoSSE");
			emailSender.setToAddress(params[0]);
			emailSender.setSubject("微宝找回密码");
			if(DBOperation.updatePersonEmail(params[0])!=1){
				return false;
			}
			emailSender.setContent(UrlInEmail.getPasswordUrl(params[1]));// 填入URL
			emailSender.sendEmail();
			isSendSuccess = true;
			return isSendSuccess;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Context context = getApplicationContext();
			CharSequence text = "邮件发送中";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Context context = getApplicationContext();
			CharSequence text;
			if (result) {
				text = "邮件已发送到你的邮箱,请确认您的邮箱";
			} else {
				text = "发送失败，请检查你的网络状况";
			}
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
}
