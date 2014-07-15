package com.example.myfirstandroid;

import com.example.connectwebservice.DBOperation;
import com.example.entity.McItem;
import com.example.entity.PersonModel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public final class LogInActivity extends Activity {

	public static String EXTRA_MESSAGE = "com.example.myfirstandroid.EXTRA_MESSAGE";
	// 增加记住密码
	public static final String MY_PREFS_FOR_USER_STRING = "WeiBao_Preference_For_Password";
	//
	private TextView useraccount;
	private TextView userpassword;
	private ProgressBar myProgressBar;
	private Button login_btn;
	private RadioButton rememberMeButton;

	// 关于保存密码等
	SharedPreferences pref;
	Boolean isRememberMe;
	String weibaoPassword;
	String weibaoUsername;
	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		useraccount = (TextView) findViewById(R.id.account);
		userpassword = (TextView) findViewById(R.id.password);
		login_btn = (Button) findViewById(R.id.login_btn);
		myProgressBar = (ProgressBar) findViewById(R.id.wait_login);
		myProgressBar.setVisibility(ProgressBar.GONE);
		rememberMeButton = (RadioButton) findViewById(R.id.radio_rem_key);
		pref = getSharedPreferences(MY_PREFS_FOR_USER_STRING, MODE_PRIVATE);
		weibaoUsername = pref.getString("username", "");
		weibaoPassword = pref.getString("password", "");
		isRememberMe = pref.getBoolean("is_rem", true);
		
		//处理是否记住密码
		if(isRememberMe.equals(true)){
			useraccount.setText(weibaoUsername);
			userpassword.setText(weibaoPassword);
			rememberMeButton.setButtonDrawable(R.drawable.checkbox_true);
			rememberMeButton.setSelected(true);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}

	public void sendMessage(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.account);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	public void findPassword(View view) {
		Intent intent = new Intent(this, FindPasswordActivity.class);
		startActivity(intent);
	}

	public void logIn(View view) {

		if (useraccount.getText().length() == 0
				|| userpassword.getText().length() == 0) {
			Toast.makeText(getApplicationContext(), "邮箱名和密码不能为空",
					Toast.LENGTH_LONG).show();
		} else {
			String account = useraccount.getText().toString();
			String password = userpassword.getText().toString();
			// Intent intent = new Intent(this, MainActivity.class);
			// startActivity(intent);
			login_btn.setEnabled(false);
			Log.i("aaa", "login");
			myProgressBar.setVisibility(ProgressBar.VISIBLE);
			new Verify(account, password).execute();
		}
	}

	public void RemKey(View view) {
		RadioButton radio = (RadioButton) view;
		Log.i("aaa", "" + radio.isSelected());

		if (radio.isSelected()) {
			radio.setButtonDrawable(R.drawable.checkbox_false);
			radio.setSelected(false);
		} else {
			radio.setButtonDrawable(R.drawable.checkbox_true);
			radio.setSelected(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {

		case R.id.action_signin:
			Intent intent = new Intent(this, SignUpActivity.class);
			startActivity(intent);

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void test() {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		EditText editText = (EditText) findViewById(R.id.account);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}

	class Verify extends AsyncTask {
		private String account;
		private String password;
		private int Result;
		private boolean type;

		public Verify(String account, String password) {
			super();
			this.account = account;
			this.password = password;
			if (account.contains("@"))
				type = false;
			else
				type = true;
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				if (type) {
					Result = DBOperation.getPersonalInfoByTelephone(account);
				} else {
					Result = DBOperation.getPersonalInfoByEmail(account);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Object result) {
			myProgressBar.setVisibility(ProgressBar.GONE);
			if (Result == 1) {
				if (PersonModel.password.equals(password)) {
					Intent intent = new Intent(getApplication(),
							MainActivity.class);
					//修改记住密码的内容
					getSharedPreferences(MY_PREFS_FOR_USER_STRING,MODE_PRIVATE)
			        .edit()
			        .putString("username", useraccount.getText().toString().trim())
			        .putString("password", userpassword.getText().toString().trim())
			        .putBoolean("is_rem", rememberMeButton.isSelected())
			        .commit();
					startActivity(intent);
				} else {
					Toast.makeText(getApplication(), "密码错误！", Toast.LENGTH_LONG)
							.show();
				}
			} else if (Result == 0) {
				Toast.makeText(getApplication(), "账户名不存在!", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(getApplication(), "登陆失败,请检查网络连接",
						Toast.LENGTH_LONG).show();
			}
			login_btn.setEnabled(true);
		}
	}
}
