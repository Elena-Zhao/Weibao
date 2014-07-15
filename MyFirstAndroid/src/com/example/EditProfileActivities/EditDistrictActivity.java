package com.example.EditProfileActivities;

import com.example.entity.PersonModel;
import com.example.myfirstandroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditDistrictActivity extends Activity {
	private EditText editDistrictText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit_district);
		findAllViews();
		setAllListeners();
	}
	protected void onResume() {
		super.onResume();
		setInfo();
	}
	private void findAllViews(){
		editDistrictText = (EditText)findViewById(R.id.editDistrictText);
	};
	private void setInfo(){
		editDistrictText.setText(PersonModel.district);
	}
	private void setAllListeners() {
		editDistrictText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				PersonModel.district=s.toString();
			}
		});
	}
}
