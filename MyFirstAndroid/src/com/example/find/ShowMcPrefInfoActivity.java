package com.example.find;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connectwebservice.DBOperation;
import com.example.connectwebservice.ImageHandeller;
import com.example.entity.McItem;
import com.example.entity.McPreferentialItem;
import com.example.find.ShowMcInfoActivity;
import com.example.myfirstandroid.R;

public class ShowMcPrefInfoActivity extends Activity {
	McPreferentialItem mcPreferentialItem = null;
	TextView mcNameTextView;

 
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pref_info);
		
		//点击商户名跳转至商户信息页面仍有问题，回去咨询数据库
		mcNameTextView = (TextView)findViewById(R.id.pref_mc_name);
		
		setContent();
	}

	public void setContent(){
		Intent intent = getIntent();
		mcPreferentialItem = (McPreferentialItem)intent.getSerializableExtra("mcPreferentialItem");
		
		ImageView pref_mc_logo = (ImageView)findViewById(R.id.pref_image);
		pref_mc_logo.setImageBitmap(ImageHandeller.getBitmap(mcPreferentialItem.getPrefImageUrl()));;
		
		TextView pref_mc_name = (TextView)findViewById(R.id.pref_mc_name);
		pref_mc_name.setText(mcPreferentialItem .getMcName());
		
		TextView pref_title = (TextView)findViewById(R.id.pref_title);
		pref_title.setText(mcPreferentialItem.getPrefTitle());
		
		TextView pref_info = (TextView)findViewById(R.id.pref_info);
		pref_info.setText(mcPreferentialItem.getPrefDescription());
		
	}

		public void openMc(View view) {
			
			//根据商户名查找对应商户
			new StartMc().execute();
		}
		
		class StartMc extends AsyncTask {
			McItem item;
			@Override
			protected Object doInBackground(Object... params) {
				try {
					item = DBOperation.getMcById(mcPreferentialItem .getMcId());
					
				} catch (Exception e) {
					e.printStackTrace();
				}

				return null;

			}
			@Override
			protected void onPostExecute(Object result) {
				Intent mcIntent = new Intent();
				mcIntent.putExtra("mcItem", item);
				mcIntent.setClass(ShowMcPrefInfoActivity.this, ShowMcInfoActivity.class);
				startActivity(mcIntent);
			}
		}
}
