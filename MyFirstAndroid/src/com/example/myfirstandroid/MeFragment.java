package com.example.myfirstandroid;

import com.example.connectwebservice.ImageHandeller;
import com.example.entity.PersonModel;
import com.example.me.bill4thTabActivity;
import com.example.me.profile4thTabActivity;
import com.example.me.settings4thTabActivity;
import com.example.myfirstandroid.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public final class MeFragment extends Fragment  implements OnClickListener{
	
	public View onCreateView(LayoutInflater inflater,  
			ViewGroup container, Bundle savedInstanceState) {  
			//---Inflate the layout for this fragment---  
			View view = inflater.inflate(  
    		R.layout.me, container, false);
			ImageButton edit_person = (ImageButton) view.findViewById(R.id.edit_person_btn);  
	        edit_person.setOnClickListener(this);  
	        TextView textView = (TextView) view.findViewById(R.id.edit_bills_btn);
	        textView.setOnClickListener(this); 
	        textView = (TextView) view.findViewById(R.id.edit_settings_btn);
	        textView.setOnClickListener(this); 
	        return view;
	}  
	
	@Override 
	public void onResume(){
		super.onResume();
		TextView name = (TextView)getActivity().findViewById(R.id.me_user_name);
		name.setText(PersonModel.name);
		
		ImageView image = (ImageView)getActivity().findViewById(R.id.user_image);
		image.setImageBitmap(ImageHandeller.getBitmap(PersonModel.avatarLocalPathString));
		
		TextView gender = (TextView)getActivity().findViewById(R.id.user_gender);
		gender.setText(PersonModel.gender);
		
		TextView location = (TextView)getActivity().findViewById(R.id.user_location);
		location.setText(PersonModel.district);
		
		TextView signature = (TextView)getActivity().findViewById(R.id.user_signature);
		signature.setText(PersonModel.signature);
	}
	@Override  
    public void onClick(View v) {
        int id = v.getId();
		switch(id){
		case R.id.edit_person_btn:
			//for example:
			//testCreatePerson();
			Log.i("aaa",profile4thTabActivity.class.toString()+LogInActivity.class.toString());
			startActivity(new Intent(getActivity(),profile4thTabActivity.class));
			//个人设置页面
			break;
		case R.id.edit_bills_btn:
			startActivity(new Intent(getActivity(),bill4thTabActivity.class));
			//我的账单页面
			break;
		case R.id.edit_settings_btn:
			startActivity(new Intent(getActivity(),settings4thTabActivity.class));
			//设置页面
			break;
        }  
    }
	private void testCreatePerson() {
		PersonModel.name = "呏鰫";
		PersonModel.district = "奻漆 樁隅Е 羚假鼠繚4800瘍";
		PersonModel.email = "shengwen@live.cn";
		PersonModel.gender = "躓";
		PersonModel.phoneNumber = "13816777671";
		PersonModel.signature = "Try Hardest to make things perfect";
		PersonModel.weibaoID = "Perfection-Isa";
		PersonModel.isEmailChecked = false;
	}
}
