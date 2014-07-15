package com.example.AccountActivities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import com.example.connectwebservice.DBOperation;
import com.example.email.EmailSender;
import com.example.entity.PersonModel;
import com.example.myfirstandroid.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class boundEmailActivity extends Activity {
	private Button saveEditButton;
	private Button sendEmailOrDisboundButton;
	private TextView emailHintTextView;
	private EditText emailEditText;

	private Intent backToAccountMenuIntent;
	
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_bound_email);
		saveEditButton = (Button) findViewById(R.id.saveOrEditEmailButton);
		sendEmailOrDisboundButton = (Button) findViewById(R.id.sendEmailOrDisboundButton);
		emailHintTextView = (TextView) findViewById(R.id.boundEmailHintTextView);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		backToAccountMenuIntent = new Intent(this, MyAccountActivity.class);
		setAllListeners();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setInfo();

	}
	
	//checkEmailFormat检查输入的邮箱string是否符合邮箱的格式
	Boolean checkEmailFormat(String emailString){
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		Matcher emailMatcher = emailPattern.matcher(emailString);
		if(emailMatcher.matches()){
			return true;
		}else{
			return false;
		}

	}

	private void setAllListeners() {
		sendEmailOrDisboundButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 判断账号的三种状况，未验证，验证，未填入
				if (PersonModel.email == null || PersonModel.email.equals("")) {
					// 未填入
				} else if (PersonModel.isEmailChecked == false) {
					// 填入，未验证
					// 此时该功能为重新发送验证邮件
					new SendBoundAccountEmail().execute(PersonModel.email);
					// 给出提示
					giveEmailBoundHintMessage(1);
				} else {
					// 已经验证，目前该button的状态是解除绑定
					// 需要操作改变数据库里的emailcheck状态！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
				
					new DisboundEmail().execute();
					
					// SaveEditButton.setVisibility(TextView.VISIBLE);
					// SaveEditButton.setText("保存");
					// emailEditText.setText("");
					// emailEditText.setFocusable(true);
					// emailHintTextView.setText("如果你更改了邮件地址，你需要对邮件地址重新进行验证。");
					// sendEmailOrDisboundButton.setVisibility(View.GONE);
					startActivity(backToAccountMenuIntent);
				}

			}
		});
		saveEditButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (PersonModel.email.equals("") || PersonModel.email == null) {
					// 无邮箱数据
					// 数据库操作！！！!!!!!!!!!!!!!!!!!!!!!!!
//					PersonModel.email = emailEditText.getText().toString()
//							.trim();
					// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					if(!checkEmailFormat(emailEditText.getText().toString().trim())){
						//输入的邮件不合格
						Context context = getApplicationContext();
						CharSequence text = "邮件格式不合格";
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
						startActivity(backToAccountMenuIntent);
						return;
						
					}
					new SendBoundAccountEmail().execute(emailEditText.getText().toString()
							.trim());
					
					startActivity(backToAccountMenuIntent);
				} else if (PersonModel.isEmailChecked == false) {

					// 有email但未进行验证
					if (saveEditButton.getText().equals("编辑")) {
						emailEditText.setFocusableInTouchMode(true);
						emailEditText.setFocusable(true);
						emailEditText.requestFocus();
						sendEmailOrDisboundButton.setVisibility(Button.GONE);
						saveEditButton.setText("保存");
					} else {
						// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						if(!checkEmailFormat(emailEditText.getText().toString().trim())){
							//输入的邮件不合格
							Context context = getApplicationContext();
							CharSequence text = "邮件格式不合格";
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
							startActivity(backToAccountMenuIntent);
							return;
							
						}
						PersonModel.email = emailEditText.getText().toString()
								.trim();
						// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
						
						new SendBoundAccountEmail().execute(PersonModel.email);
						startActivity(backToAccountMenuIntent);
					}
				} else {
					// 已经验证

				}
			}
		});

	}

	// resultCode返��������验�����件��否�����
	private void giveEmailBoundHintMessage(int resultCode) {
		// �����1��������������失败

	}

	// 如�����邮箱�����未�����证�����示�����证�����箱不可修改，���辑���存�����显示为编���，���������������新�����验�����件��钮：���辑�����箱�����辑�����可以点�����次�����邮���
	// //如�����邮箱���已���验���，�����箱不可修改，�����出��解�����定�������
	// //如�����邮箱�����saveEdit��钮��示保���，����������示，��������定����������件��那个button��部消失
	private void setInfo() {
		// 预先根据账号邮箱的三种状态进行处理
		if (PersonModel.email == null || PersonModel.email.equals("")) {
			saveEditButton.setText("保存");
			emailEditText.setText("");
			emailEditText.setFocusable(true);
			emailHintTextView.setText("如果你更改了邮件地址，你需要对邮件地址重新进行验证。");
			sendEmailOrDisboundButton.setVisibility(View.GONE);
		} else if (PersonModel.isEmailChecked == false) {
			emailEditText.setText(PersonModel.email);
			emailEditText.setFocusable(false);
			emailHintTextView.setText("该邮箱还未验证，请登陆你的邮箱查收邮件并进行验证。");
			Log.i("aaa", "aaa");
			sendEmailOrDisboundButton.setVisibility(View.VISIBLE);
			sendEmailOrDisboundButton.setText("重新发送验证邮件");
			saveEditButton.setText("编辑");
		} else {
			emailEditText.setText(PersonModel.email);
			emailEditText.setFocusable(false);
			emailHintTextView.setText("如果你更改了邮件地址，你需要对邮件地址重新进行验证。");
			sendEmailOrDisboundButton.setVisibility(View.VISIBLE);
			sendEmailOrDisboundButton.setText("解除绑定");
			saveEditButton.setVisibility(View.GONE);

		}

	}

	// �������件�����步线程类
	class DisboundEmail extends AsyncTask<Void,Void,Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
		    return DBOperation.disboundEmail() == 1?true:false;
			
		}
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(!result){
				Context context = getApplicationContext();
				CharSequence text = "解除邮箱绑定失败，请检查你的网络状况";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}else {
				PersonModel.isEmailChecked = false;
				PersonModel.email = "";
				// //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				Context context = getApplicationContext();
				CharSequence text = "成功解除邮箱绑定";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
		
	};
	class SendBoundAccountEmail extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean isSendSuccess = false;
			// �������邮件���详���过���
			EmailSender emailSender = new EmailSender("weibao2013@gmail.com",
					"weibaoSSE");
			// 必须设置�������容
			emailSender.setToAddress(params[0]);
			emailSender.setSubject("微宝验证确认");
			// 这个��方填����������信�����！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���！���
			if(DBOperation.updatePersonEmail(params[0])!=1){
				return false;
			}
			emailSender.setContent("test success!");// 填入URL
			emailSender.sendEmail();
			isSendSuccess = true;
			return isSendSuccess;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Context context = getApplicationContext();
			CharSequence text = "验证邮件发送中";
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
				text = "验证邮件已发送到你的邮箱";
				PersonModel.email = emailEditText.getText().toString().trim();
				
			} else {
				text = "发送失败，请检查你的网络状况";
			}
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
}
