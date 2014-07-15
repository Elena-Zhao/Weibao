package com.example.AccountActivities;



import com.example.connectwebservice.DBOperation;
import com.example.entity.PersonModel;
import com.example.me.profile4thTabActivity;
import com.example.myfirstandroid.R;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class changePasswordActivity extends Activity {
	Button saveNewPasswordButton;
	EditText oldPasswordEditText;
	EditText newPasswordEditText;
	EditText newPasswordAgainEditText;
	Intent backToMenuIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		findAllViews();
		backToMenuIntent = new Intent(this,profile4thTabActivity.class);
		setAllListeners();
	}
	private void findAllViews(){
		oldPasswordEditText = (EditText)findViewById(R.id.editText_OldPassword);
		newPasswordEditText = (EditText)findViewById(R.id.editText_NewPassword);
		newPasswordAgainEditText = (EditText)findViewById(R.id.editText_NewPasswordAgain);
		saveNewPasswordButton = (Button)findViewById(R.id.button_SaveNewPassword);
	}
	private void setAllListeners(){
		saveNewPasswordButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String inputNewPasswordString = newPasswordEditText.getText().toString().trim();
				String inputNewPasswordAgainString = newPasswordAgainEditText.getText().toString().trim();
				String inputOldPasswordString = oldPasswordEditText.getText().toString().trim();
				
				Context context = getApplicationContext();
				CharSequence textCharSequence;
				int duration = Toast.LENGTH_SHORT;
				
				//1检查是否有空输入
				if(inputOldPasswordString.equals("")){
					textCharSequence = "请输入旧密码";
					Toast.makeText(context, textCharSequence,duration).show();
					return;
				}else if(inputNewPasswordString.equals("")){
					textCharSequence = "请输入新密码";
					Toast.makeText(context, textCharSequence,duration).show();
					return;
					
				}else if(inputNewPasswordAgainString.equals("")){
					textCharSequence = "请再次输入新密码";
					Toast.makeText(context, textCharSequence,duration).show();
					return;
				}
				//2检查新密码和再次输入新密码是否匹配
				if(!inputNewPasswordAgainString.equals(inputNewPasswordString)){
					textCharSequence = "两次输入的新密码不匹配";
					Toast.makeText(context, textCharSequence, duration).show();
					return;
				}
				//3检查旧密码是否输入正确
				if(!inputOldPasswordString.equals(PersonModel.password)){
					textCharSequence = "输入的旧密码错误";
					Toast.makeText(context, textCharSequence, duration).show();
					return;
				}
				//连接数据库修改密码
				new ChangePassword().execute(inputNewPasswordAgainString);
				startActivity(backToMenuIntent);
				//如果成功则返回成功并修改PersonModel里面的密码
				//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				
				
			}
		});
	}
	
	class ChangePassword extends AsyncTask<String, Void, Integer>{
		String newPassword;
		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			newPassword = params[0];
			return new Integer(DBOperation.updatePassword(params[0]));
		}
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Context context = getApplicationContext();
			CharSequence text;
			if(result.equals(1)){
				text = "修改密码成功";
				PersonModel.password = new String(newPassword);
			}else{
				text = "修改密码失败，请检查您的网络";
			}
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			
		}
	}
	
	
}
