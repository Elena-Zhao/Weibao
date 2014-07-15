package com.example.find;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.find.McActivity.ChangeSubs;
import com.example.find.MyListAdapter.onSubscriptBtnClick;
import com.example.myfirstandroid.R;

public class ShowMcInfoActivity extends Activity {
	protected TextView mcPref = null;
	protected McItem mcItem = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_mc_info);
		
		mcPref = (TextView)findViewById(R.id.go_mc_pref);
		mcPref.setOnClickListener(new CheckMcPrefListener());
		
		setContent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_password, menu);
		menu.add(0, 1, 1, "Subscribed");
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == 1){
			Intent intent = new Intent();
			intent.putExtra("mcItem", mcItem);
			intent.setClass(this, SubscribedMcActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}


	class SubscribeButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			mcItem.setIsSubscribed(true);
			
		}
		
	}
	class CheckMcPrefListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			String mcId = mcItem.getMcId();
			Intent intent = new Intent(ShowMcInfoActivity.this,ShowMcPrefListActivity.class);
			intent.putExtra("mcId", mcId);

			startActivity(intent);
		}
		
	}
	
	public void setContent(){
		Intent intent = getIntent();
		mcItem = (McItem)intent.getSerializableExtra("mcItem");
		
		ImageView mc_image = (ImageView)findViewById(R.id.mc_logo);
		mc_image.setImageBitmap(ImageHandeller.getBitmap(mcItem.getMcLogoUrl()));
		
		TextView mc_name = (TextView)findViewById(R.id.mc_name);
		mc_name.setText(mcItem.getMcName());
		
		TextView mc_slogen = (TextView)findViewById(R.id.mc_slogen);
		mc_slogen.setText(mcItem.getMcSlogen());
		
		TextView mc_address = (TextView)findViewById(R.id.mc_address);
		mc_address.setText(mcItem.getMcAddress());
		
		TextView mc_telephone = (TextView)findViewById(R.id.mc_telephone);
		mc_telephone.setText(mcItem.getMcTelephone());
		
		TextView mc_intro = (TextView)findViewById(R.id.mc_intro);
		mc_intro.setText(mcItem.getMcIntro());
		
		Button subscribe = (Button)findViewById(R.id.subscribe);
		if(mcItem.getIsSubscribed()){
        	subscribe.setSelected(true);;
		}
		
        else{
        	subscribe.setSelected(false);
        }
		subscribe.setOnClickListener(new onSubscriptBtnClick());
		
	}
	class onSubscriptBtnClick implements OnClickListener{
		
		@Override
		public void onClick(View view){
			Button btn = (Button) view;
			new ChangeSubs(btn).execute();
		}
	}
	class ChangeSubs extends AsyncTask {
		private int Result;
		private Button btn;

		public ChangeSubs(Button btn) {
			super();
			this.btn = btn;
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				if (btn.isSelected()) {
					Result = DBOperation.cancelSubs(mcItem.getMcId());
				} else {
					Result = DBOperation.subscribe(mcItem.getMcId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Object result) {
			if (Result == 1) {
				btn.setSelected(!btn.isSelected());
				if (btn.isSelected()) {
					Toast.makeText(getApplication(), "已订阅，您将接收到该商户的优惠信息哦！",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplication(), "已取消订阅，您将无法接收该商户的优惠信息。",
							Toast.LENGTH_LONG).show();
				}
				
			} else {
				Toast.makeText(getApplication(), "请求失败，请检查网络连接",
						Toast.LENGTH_LONG).show();
			}
		}
	}

}
