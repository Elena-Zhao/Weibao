package com.example.me;

import java.util.ArrayList;
import java.util.List;

import com.example.connectwebservice.DBOperation;
import com.example.entity.McItem;
import com.example.entity.WeiJinEntity;
import com.example.find.MyListAdapter;
import com.example.find.ShowMcInfoActivity;
import com.example.myfirstandroid.R;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class bill4thTabActivity extends Activity {
	protected List<WeiJinEntity> billList;
	protected MyBillListAdapter adapter;
	protected ListView listview;
	protected String query;
	protected ProgressBar myProgressBar;
	private Button lastSetlected;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_bill);

		listview = (ListView) findViewById(android.R.id.list);
		myProgressBar = (ProgressBar) findViewById(R.id.wait_allbill);
		billList = new ArrayList<WeiJinEntity>();
		setAdapter();
		
		lastSetlected = (Button) findViewById(R.id.all_bill_btn);
		lastSetlected.setSelected(true);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				//openItem(position);
			}
		});

		
		getActionBar().setTitle("我的账单");
		
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
		getMenuInflater().inflate(R.menu.find_password, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
/*
	public void openItem(int position) {
		// TODO Auto-generated method stub
		if (mcInfoList != null) {
			McItem mcItem = new WeiJinEntity(mcInfoList.get(position));
			Intent intent = new Intent();
			intent.putExtra("mcItem", mcItem);

			intent.setClass(this, ShowMcInfoActivity.class);
			startActivity(intent);
		}
	}
*/
	public void setAdapter() {
		adapter = new MyBillListAdapter(billList, this, false);
		listview.setAdapter(adapter);
	}

	public void startFind() {
		new FindBill(1).execute();
	}
	
	public void onAllBillClick(View view){
		lastSetlected.setSelected(false);
		lastSetlected = (Button)view;
		lastSetlected.setSelected(true);
		new FindBill(1).execute();
	}
	
	public void onExchangedBillClick(View view){
		lastSetlected.setSelected(false);
		lastSetlected = (Button)view;
		lastSetlected.setSelected(true);
		new FindBill(2).execute();
	}
	
	public void onUnexchangedBillClick(View view){
		lastSetlected.setSelected(false);
		lastSetlected = (Button)view;
		lastSetlected.setSelected(true);
		new FindBill(3).execute();
	}

	public void changeSubscribe(Button btn, int pos) {
		myProgressBar.setVisibility(ProgressBar.VISIBLE);
		new Exchange(btn, pos).execute();
	}

	class FindBill extends AsyncTask {
		private List<WeiJinEntity> result;
		private int type;
		public FindBill(int type){
			this.type = type;
		}
		@Override
		protected Object doInBackground(Object... params) {
			try {
				if(type ==1){
					result = DBOperation.getMyWeiJin();
				}else if(type == 2){
					result = DBOperation.getExchangedWeiJin();
				}else{
					result = DBOperation.getNotExchangedWeiJin();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			listview.post(new Runnable() {
				@Override
				public void run() {
					if (result == null) {
						Toast.makeText(getApplicationContext(), "网络连接失败",
								Toast.LENGTH_LONG).show();
					} else if (result.size() == 0) {
						billList.clear();
						adapter.notifyDataSetChanged();
						Toast.makeText(getApplicationContext(), "您目前还没有账单!",
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"共有" + result.size() + "条账单信息", Toast.LENGTH_LONG)
								.show();
						billList.clear();
						for (WeiJinEntity it : result) {
							billList.add(it);
						}
						adapter.notifyDataSetChanged();
					}
					myProgressBar.setVisibility(ProgressBar.GONE);
				}
			});
			return null;

		}
	}

	class Exchange extends AsyncTask {
		private WeiJinEntity item;
		private int Result;
		private Button btn;

		public Exchange(Button btn, int position) {
			super();
			this.item = billList.get(position);
			this.btn = btn;
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				if (!btn.isSelected()) 
					Result = DBOperation.exchangeWeiJin(item);
				else 
					Result = 2;
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
			}
			else if(Result == 2) {
				Toast.makeText(getApplication(), "该条微金已兑换，无法再次兑换",
						Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(getApplication(), "兑换失败，请检查网络连接",
						Toast.LENGTH_LONG).show();
			}
		}
	}
}
