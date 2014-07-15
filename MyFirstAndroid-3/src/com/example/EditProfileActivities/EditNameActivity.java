package com.example.EditProfileActivities;


import com.example.myfirstandroid.R;

import com.example.entity.PersonModel;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EditNameActivity extends Activity {
	
	private EditText editNameText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit_name);
		findAllViews();
		setAllIntents();
		setAllListeners();
	}
	@Override
	protected void onResume() {
		super.onResume();
		setInfo();
	}
	private void findAllViews(){
		editNameText = (EditText)findViewById(R.id.editNameText);
	};
	private void setInfo(){
		editNameText.setText(PersonModel.name);
	}
	private void setAllListeners() {
		editNameText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				PersonModel.name=s.toString();
			}
		});
	}
	private void setAllIntents(){
		
	}
}
