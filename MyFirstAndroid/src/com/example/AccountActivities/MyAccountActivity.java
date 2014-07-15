package com.example.AccountActivities;

import com.example.entity.PersonModel;
import com.example.myfirstandroid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyAccountActivity extends Activity {
	private TextView weiBaoIDTextView;
	private TextView weiBaoPhoneTextView;
	private TextView weiBaoEmailIsBoundHint;
	
	private Button weiBaoEmailManageButton;
	private Button weiBaoPhoneMangeButton;
	
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
		weiBaoEmailManageButton = (Button)findViewById(R.id.emailManageButton);
		weiBaoPhoneMangeButton = (Button)findViewById(R.id.phoneManageButton);
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
}
