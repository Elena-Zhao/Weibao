package com.example.find;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.SystemSettingsModel.SoundModel;
import com.example.connectwebservice.DBOperation;
import com.example.entity.McItem;
import com.example.me.settings4thTabActivity;
import com.example.myfirstandroid.R;

public class McActivity extends Activity {
	protected List<McItem> mcInfoList;
	protected MyListAdapter adapter;
	protected ListView listview;
	protected String query;
	protected ProgressBar myProgressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_mc);

		listview = (ListView) findViewById(android.R.id.list);
		myProgressBar = (ProgressBar) findViewById(R.id.wait_allmc);
		mcInfoList = new ArrayList<McItem>();
		setAdapter();

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				openItem(position);
			}
		});

		query = getIntent().getStringExtra(SearchManager.QUERY);
		if (query == null) {
			getActionBar().setTitle("所有商户");
		} else {
			getActionBar().setTitle("关于 \"" + query + "\" 的搜索结果");
		}
		// mcInfoList = MerchantList.excuteQuery(query);
		startFind();
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startFind();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find, menu);
		MenuItem search = menu.findItem(R.id.action_search);
		search.collapseActionView();
		SearchView searchview = (SearchView) search.getActionView();
		// searchview.setIconifiedByDefault(false);
		SearchManager mSearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo info = mSearchManager
				.getSearchableInfo(getComponentName());
		searchview.setSearchableInfo(info);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			onSearchRequested();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void openItem(int position) {
		// TODO Auto-generated method stub
		if (mcInfoList != null) {
			McItem mcItem = new McItem(mcInfoList.get(position));
			Intent intent = new Intent();
			intent.putExtra("mcItem", mcItem);

			intent.setClass(this, ShowMcInfoActivity.class);
			startActivity(intent);
		}
	}

	public void setAdapter() {
		adapter = new MyListAdapter(mcInfoList, this, false);
		listview.setAdapter(adapter);
	}

	public void startFind() {
		new FindMc(1).execute();
	}

	public void changeSubscribe(Button btn, int pos) {
		myProgressBar.setVisibility(ProgressBar.VISIBLE);
		new ChangeSubs(btn, pos).execute();
	}

	class FindMc extends AsyncTask {
		private List<McItem> result;
		private int type;

		public FindMc(int type) {
			this.type = type;
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				if (type == 1) {
					if (query == null) {
						result = DBOperation.getMcAll();
					} else {
						result = DBOperation.getMcByName(query);
					}
				} else {
					if (query == null) {
						result = DBOperation.getMcOfMe();
					} else {
						result = DBOperation.getMcOfMeByName(query);
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			listview.post(new Runnable() {
				@Override
				public void run() {
					
					if (result == null) {
						Toast.makeText(getApplicationContext(), "网络连接失败",
								Toast.LENGTH_SHORT).show();
					} else if (result.size() == 0) {
						Toast.makeText(getApplicationContext(), "没有找到符合条件的商户",
								Toast.LENGTH_SHORT).show();
					} else {
						SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(McActivity.this); Boolean isBeepOn = sharedPref.getBoolean(settings4thTabActivity.IS_BEEP_ON, true); if(isBeepOn)
							SoundModel.playVibrate(McActivity.this, 300);

						Toast.makeText(getApplicationContext(),
								"找到" + result.size() + "家商户", Toast.LENGTH_SHORT)
								.show();
						//SharedPreferences preferences = 
						mcInfoList.clear();
						for (McItem it : result) {
							mcInfoList.add(it);
						}
						adapter.notifyDataSetChanged();
					}
					myProgressBar.setVisibility(ProgressBar.GONE);
				}
			});
			return null;

		}
	}

	class ChangeSubs extends AsyncTask {
		private McItem item;
		private int Result;
		private Button btn;

		public ChangeSubs(Button btn, int position) {
			super();
			this.item = mcInfoList.get(position);
			this.btn = btn;
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				if (btn.isSelected()) {
					Result = DBOperation.cancelSubs(item.getMcId());
				} else {
					Result = DBOperation.subscribe(item.getMcId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(Object result) {
			myProgressBar.setVisibility(ProgressBar.GONE);
			if (Result == 1) {
				btn.setSelected(!btn.isSelected());
			} else {
				Toast.makeText(getApplication(), "请求失败，请检查网络连接",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
