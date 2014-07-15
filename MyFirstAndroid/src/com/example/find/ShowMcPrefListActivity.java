package com.example.find;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.example.SystemSettingsModel.SoundModel;
import com.example.connectwebservice.DBOperation;
import com.example.entity.McItem;
import com.example.entity.McPreferentialItem;
import com.example.me.settings4thTabActivity;
import com.example.myfirstandroid.R;

public class ShowMcPrefListActivity extends Activity{
	protected List<McPreferentialItem> allPrefInfo;
	protected ListView listview = null;
	protected MyPostInfoListAdapter adapter;
	protected String query;
	private String mcId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mc_pref_list);
		
		listview = (ListView)findViewById(android.R.id.list);
		allPrefInfo = new ArrayList<McPreferentialItem>();

		adapter = new MyPostInfoListAdapter(allPrefInfo, this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				openItem(position);
			}
		});
		
        Intent intent = getIntent();
		mcId = (String)intent.getStringExtra("mcId");

		startFind();

	}

	public void openItem(int position) {
		// TODO Auto-generated method stub
		if (allPrefInfo != null) {
			McPreferentialItem mcPreferentialItem = allPrefInfo.get(position);
			Intent intent = new Intent();
			intent.putExtra("mcPreferentialItem", mcPreferentialItem);

			intent.setClass(this, ShowMcPrefInfoActivity.class);
			startActivity(intent);
		}
	}

	public void startFind() {
		new FindMcPref().execute();
	}

	class FindMcPref extends AsyncTask {
		private List<McPreferentialItem> result;

		@Override
		protected Object doInBackground(Object... params) {
			try {
				result = DBOperation.getMyMcPreListByMcId(mcId);

			} catch (Exception e) {
				e.printStackTrace();
			}
			listview.post(new Runnable() {
				@Override
				public void run() {
					if (result == null) {
						Toast.makeText(getApplicationContext(),
								"网络连接失败", Toast.LENGTH_LONG).show();
					} else if (result.size() == 0) {
						Toast.makeText(getApplicationContext(),
								"该商户暂无优惠信息", Toast.LENGTH_LONG).show();
					} else {
						SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ShowMcPrefListActivity.this); Boolean isBeepOn = sharedPref.getBoolean(settings4thTabActivity.IS_BEEP_ON, true); if(isBeepOn)
							SoundModel.playVibrate(ShowMcPrefListActivity.this, 300);
						Toast.makeText(getApplicationContext(),
								"找到该商户的" + result.size() + "条优惠信息", Toast.LENGTH_LONG)
								.show();
						allPrefInfo.clear();
						for (McPreferentialItem it : result) {
							allPrefInfo.add(it);
						}
						adapter.notifyDataSetChanged();
					}

				}
			});
			return null;

		}
	}


}
