package com.example.EditProfileActivities;

import com.example.myfirstandroid.R;

import com.example.entity.PersonModel;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressLint("NewApi")
public class EditGenderActivity extends Activity {
	private RadioGroup genderRadioGroup;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit_gender);
//		myPreferences = getSharedPreferences(personModel.USER_PREF, Activity.MODE_PRIVATE);
		findViews();
		setRadioGroup();
		setAllListeners();
		
	}
	private void findViews(){
		genderRadioGroup = (RadioGroup)findViewById(R.id.radioGroupGender);
	}
	private void setRadioGroup(){
		String userGender = PersonModel.gender;
		if(userGender.equals("ÄÐ")){
			genderRadioGroup.check(R.id.radioMale);
		}else if(userGender.equals("Å®")){
			genderRadioGroup.check(R.id.radioFemale);
		}else{
			genderRadioGroup.check(R.id.radioGenderUnknown);
		}
			
	}
	private void setAllListeners(){
		genderRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				//SharedPreferences.Editor editor = myPreferences.edit();
				if(checkedId == R.id.radioFemale){
					//SharedPreferences.Editor editor = myPreferences.edit();
					PersonModel.gender="Å®";
				}else if(checkedId == R.id.radioMale){
					PersonModel.gender="ÄÐ";
				}else{
					PersonModel.gender="Î´ÌîÐ´";
				}
//				editor.commit();
			}
		});
	}
}
