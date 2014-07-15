package com.example.AccountActivities;

import com.example.entity.PersonModel;
import com.example.me.profile4thTabActivity;
import com.example.myfirstandroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

public class MyAccountActivity extends Activity {
	private TextView weiBaoIDTextView;
	private TextView weiBaoPhoneTextView;
	private TextView weiBaoEmailIsBoundHint;
	
	private TableRow weiBaoEmailManageButton;
	private TableRow weiBaoPhoneMangeButton;
	
	private Intent boundEmailIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit_account);
		findViews();
		setIntents();
		setListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();
		setInfo();
	}

	private void findViews() {
		weiBaoEmailIsBoundHint = (TextView) findViewById(R.id.weiBaoAccountIsEmailBoundedHint);
		weiBaoIDTextView = (TextView) findViewById(R.id.weiBaoAccountIDTextView);
		weiBaoPhoneTextView = (TextView) findViewById(R.id.weiBaoAccountPhoneTextView);
		weiBaoEmailManageButton = (TableRow)findViewById(R.id.tableRow3);
		weiBaoPhoneMangeButton = (TableRow)findViewById(R.id.tableRow2);
	}

	private void setListeners() {
		//����������֤����
		weiBaoEmailManageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(boundEmailIntent);
			}
			
		});
		//�����ֻ���֤����
		weiBaoPhoneMangeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	private void setIntents() {
		boundEmailIntent = new Intent(MyAccountActivity.this,boundEmailActivity.class);
	}

	private void setInfo() {
		weiBaoIDTextView.setText(PersonModel.weibaoID);
		if (PersonModel.phoneNumber != null)
			weiBaoPhoneTextView.setText(PersonModel.phoneNumber);
		if (PersonModel.isEmailChecked == true) {
			weiBaoEmailIsBoundHint.setText("已验证");
		} else {
			weiBaoEmailIsBoundHint.setText("未验证");
		}
	};
	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent(this, profile4thTabActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}

		return super.dispatchKeyEvent(event);
	}
}
