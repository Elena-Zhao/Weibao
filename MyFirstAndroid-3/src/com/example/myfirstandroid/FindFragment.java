package com.example.myfirstandroid;

import com.example.find.McActivity;
import com.example.find.McNearbyActivity;
import com.example.find.SubscribedMcActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public final class FindFragment extends Fragment implements OnClickListener{
	public View onCreateView(LayoutInflater inflater,  
			ViewGroup container, Bundle savedInstanceState) {  
			//---Inflate the layout for this fragment---  
			View view = inflater.inflate(  
	    	R.layout.find, container, false);
			//添加到监听列表
			TextView btn = (TextView) view.findViewById(R.id.all_btn);  
		    btn.setOnClickListener(this);  
		    btn = (TextView) view.findViewById(R.id.subscription_btn);  
		    btn.setOnClickListener(this);
		    btn = (TextView) view.findViewById(R.id.nearby_btn);  
		    btn.setOnClickListener(this);
		    return view;        
	}  
	@Override  
    public void onClick(View v) {
        int id = v.getId();
		switch(id){
		case R.id.all_btn:
			startActivity(new Intent(getActivity(),McActivity.class));
			break;
		case R.id.subscription_btn:
			startActivity(new Intent(getActivity(),SubscribedMcActivity.class));
			break;
		case R.id.nearby_btn:
			startActivity(new Intent(getActivity(),McNearbyActivity.class));
			break;
        }  
    }   
}
