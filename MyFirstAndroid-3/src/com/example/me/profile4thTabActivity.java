package com.example.me;

import java.util.List;

import com.example.AccountActivities.EditMyImageActivity;
import com.example.AccountActivities.MyAccountActivity;
import com.example.AccountActivities.ShowBitmapCardActivity;
import com.example.AccountActivities.changePasswordActivity;
import com.example.EditProfileActivities.EditDistrictActivity;
import com.example.connectwebservice.DBOperation;
import com.example.connectwebservice.ImageHandeller;
import com.example.entity.McItem;
import com.example.entity.PersonModel;
import com.example.myfirstandroid.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class profile4thTabActivity extends Activity implements OnClickListener{
	// 悵湔蚚誧陓洘
	// 腎暮垀眕腔intent
	private Intent imageIntent;
	private Intent cardIntent;
	private Intent districtIntent;
	private Intent accountIntent;
	private Intent changePasswodIntent;
	private ViewGroup expandView = null;
	private ImageView avatarImageView;
	private ProgressBar myProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_4th_tab);
		// 淕跺茼蚚最唗眳潔僕砅蜆preferences
		setAllIntents();
		InitListeners();
	}
	 
	public void InitListeners(){
		findViewById(R.id.row_account).setOnClickListener(this);
		findViewById(R.id.row_card).setOnClickListener(this);
		findViewById(R.id.row_district).setOnClickListener(this);
		findViewById(R.id.row_gender).setOnClickListener(this);
		findViewById(R.id.row_image).setOnClickListener(this);
		findViewById(R.id.row_name).setOnClickListener(this);
		findViewById(R.id.row_signature).setOnClickListener(this);
		findViewById(R.id.row_password).setOnClickListener(this);
	};
	@Override
	protected void onResume() {
		super.onResume();
		// 蔚党蜊腔陓洘笭陔迡善珜醱奻
		setInfo();
		myProgressBar = (ProgressBar)findViewById(R.id.wait_info);
	}

	

	
	private void setInfo() {
		TextView tv;
		tv = (TextView) findViewById(R.id.row_name).findViewById(R.id.view_show);
		tv.setText(PersonModel.name);
		tv = (TextView) findViewById(R.id.row_gender).findViewById(R.id.view_show);
		tv.setText(PersonModel.gender);
		tv = (TextView) findViewById(R.id.textDistrict4thTab);
		tv.setText(PersonModel.district);
		tv = (TextView) findViewById(R.id.row_signature).findViewById(R.id.view_show);
		tv.setText(PersonModel.signature);
		tv = (TextView) findViewById(R.id.textAccount4thTab);
		tv.setText("U0000000"+PersonModel.weibaoID);
		
		avatarImageView = (ImageView)findViewById(R.id.imageSelfImage4thTab);
		if(PersonModel.avatarLocalPathString != null){
			avatarImageView.setImageBitmap(ImageHandeller.getBitmap(PersonModel.avatarLocalPathString));
		}
	}
	private void setAllIntents() {
		imageIntent = new Intent(profile4thTabActivity.this,
				EditMyImageActivity.class);
		cardIntent = new Intent(profile4thTabActivity.this,
				ShowBitmapCardActivity.class);
		districtIntent = new Intent(profile4thTabActivity.this,
				EditDistrictActivity.class);
		accountIntent = new Intent(profile4thTabActivity.this,
				MyAccountActivity.class);
		changePasswodIntent = new Intent(profile4thTabActivity.this,changePasswordActivity.class);
		
	}
	public String closeTextView(){
		ViewGroup vg = (ViewGroup)expandView.findViewById(R.id.view_hide);
		vg.getChildAt(0).setEnabled(false);
		vg.setVisibility(View.GONE);
		return((EditText)vg.getChildAt(0)).getText().toString();
	}
	public String closeRadioView(){
		ViewGroup vg = (ViewGroup)expandView.findViewById(R.id.view_hide);
		int id = ((RadioGroup)vg.getChildAt(0)).getCheckedRadioButtonId();
		String result;
		if(id == R.id.radioFemale){
			result = "女";
		}else{
			result = "男";
		}
		vg.setVisibility(View.GONE);
		return result;
	}
	
	public String closeExpandView(){
		TextView tv;
		tv= (TextView) expandView.getChildAt(0);
		tv.setBackgroundResource(R.drawable.me_info_item_bg1);
		tv.setTextColor(Color.rgb(63, 168, 222));
		String s;
		if(expandView.getId() != R.id.row_gender){
			s = closeTextView();
		}else{
			s = closeRadioView();
		}
		
		tv = (TextView)expandView.findViewById(R.id.view_show);
		if(s.length()!=0){	
			tv.setText(s);
		}
		tv.setVisibility(View.VISIBLE);
		expandView = null;
		return s;
	}
	public void openExpandView(View view, int type){
		TextView tv;
		tv = (TextView)((ViewGroup)view).getChildAt(0);
		tv.setBackgroundResource(R.drawable.me_info_item_bg);
		tv.setTextColor(Color.rgb(255, 255, 255));
		
		String s;
		expandView = (ViewGroup)view;
		tv=(TextView)expandView.findViewById(R.id.view_show);
		s=tv.getText().toString();
		tv.setVisibility(View.GONE);
		ViewGroup vg = (ViewGroup)expandView.findViewById(R.id.view_hide);
		vg.getChildAt(0).setEnabled(true);
		if(type == 1){
			((EditText)vg.getChildAt(0)).setText(s);
		}else{
			if(s.equals("男")){
				((RadioGroup)vg.getChildAt(0)).check(R.id.radioMale);
			}else{
				((RadioGroup)vg.getChildAt(0)).check(R.id.radioFemale);
			}
			
		}
		vg.setVisibility(View.VISIBLE);
		expandView.findViewById(R.id.view_confirm).setOnClickListener(this);
	}
	@Override
	public void onClick(View view){
		int id = view.getId();
		if(id == R.id.view_confirm){
			int containerID =expandView.getId();
			String s = closeExpandView();
			
			myProgressBar.setVisibility(View.VISIBLE);
			new ChangePersonInfo(containerID , s).execute();
			return;
		} 
		if(expandView != null){
			if(id==expandView.getId())
				return;
			closeExpandView();
		}
		switch(view.getId()){
		case R.id.row_name:
			openExpandView(view,1);
			break;
		case R.id.row_signature:
			openExpandView(view,1);
			break;
		case R.id.row_image:
			startActivity(imageIntent);
			break;
		case R.id.row_gender:
			openExpandView(view,2);
			break;
		case R.id.row_district:
			startActivity(districtIntent);
			break;
		case R.id.row_account:
			startActivity(accountIntent);
			break;
		case R.id.row_card:
			startActivity(cardIntent);
			break;
		case R.id.row_password:
			startActivity(changePasswodIntent);
			break;
		}
	}
	
	
	class ChangePersonInfo extends AsyncTask
	{
		private int Result;
		String s ;
		private int id;
		public ChangePersonInfo (int id, String s){
			this.id= id;
			this.s= s;
		}
		@Override
	    protected Object doInBackground(Object... params)
	    { 
			try{
				switch(id){
					case R.id.row_name:
						Result = DBOperation.updatePersonName(s);
						break;
					case R.id.row_gender:
						Result = DBOperation.updatePersonGender(s);
						break;
					case R.id.row_signature:
						Result = DBOperation.updatePersonSig(s);
						break;
				}
				
	        }catch (Exception e)  
	        {  
	        	e.printStackTrace();
	        }
			return null;
	    }
			@Override
	        protected void onPostExecute(Object result){
				myProgressBar.setVisibility(ProgressBar.GONE);
				if(Result==1){
					Toast.makeText(getApplication(), "保存成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplication(), "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
				}
		    }
	    }

}
