package com.example.myfirstandroid;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SystemSettingsModel.SoundModel;
import com.example.connectwebservice.DBOperation;
import com.example.connectwebservice.ImageHandeller;
import com.example.entity.McPreferentialItem;
import com.example.entity.PersonModel;
import com.example.find.ShowMcPrefInfoActivity;
import com.example.me.settings4thTabActivity;

public final class WeibaoFragment extends Fragment implements OnClickListener {
	protected List<McPreferentialItem> allPrefInfo;
	protected ListView listview = null;
	protected com.example.find.MyPostInfoListAdapter adapter;
	protected String query;
	//添加设置信息
	SharedPreferences sharedPref;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ---Inflate the layout for this fragment---
		//添加设置信息
		sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		
		View view = inflater.inflate(R.layout.weibao, container, false);
		listview = (ListView) view.findViewById(android.R.id.list);
		allPrefInfo = new ArrayList<McPreferentialItem>();

		adapter = new com.example.find.MyPostInfoListAdapter(allPrefInfo, this.getActivity());
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				openItem(position);
			}
		});
		
		query = getActivity().getIntent().getStringExtra(SearchManager.QUERY);

		startFind();

		return view;
	}
	
	@Override 
	public void onResume(){
		super.onResume();
		TextView name = (TextView)getActivity().findViewById(R.id.user_name);
		name.setText(PersonModel.name);
		
		ImageView image = (ImageView)getActivity().findViewById(R.id.user_image1);
		image.setImageBitmap(ImageHandeller.getBitmap(PersonModel.avatarLocalPathString));
		Log.i("aaa", "image");
		
		TextView yanglao = (TextView)getActivity().findViewById(R.id.num_yanglao);
		yanglao.setText(PersonModel.pension);
		
		TextView weijin = (TextView)getActivity().findViewById(R.id.num_weijin);
		weijin.setText(PersonModel.weijin);
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem search = menu.findItem(R.id.action_search);
		search.collapseActionView();
		SearchView searchview = (SearchView) search.getActionView();
		// searchview.setIconifiedByDefault(false);
		SearchManager mSearchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo info = mSearchManager
				.getSearchableInfo(getActivity().getComponentName());
		searchview.setSearchableInfo(info);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_search:
			getActivity().onSearchRequested();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}


	public void openItem(int position) {
		// TODO Auto-generated method stub
		if (allPrefInfo != null) {
			McPreferentialItem mcPreferentialItem = allPrefInfo.get(position);
			Intent intent = new Intent();
			intent.putExtra("mcPreferentialItem", mcPreferentialItem);

			intent.setClass(getActivity(), ShowMcPrefInfoActivity.class);
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
				if(query == null){
					result = DBOperation.getPreOfMe();
				}
				else {
					result = DBOperation.getPreOfMeByName(query);
				}				

			} catch (Exception e) {
				e.printStackTrace();
			}
			listview.post(new Runnable() {
				@Override
				public void run() {
					Boolean isBeenOn = sharedPref.getBoolean(settings4thTabActivity.IS_BEEP_ON, true);
					if (result == null) {
						Toast.makeText(getActivity().getApplicationContext(),
								"网络连接失败", Toast.LENGTH_SHORT).show();
						if(isBeenOn)
							SoundModel.playVibrate(getActivity(), 300);
					} else if (result.size() == 0) {
						Toast.makeText(getActivity().getApplicationContext(),
								"您目前没有任何优惠信息哦，快去订阅吧！", Toast.LENGTH_SHORT).show();
						if(isBeenOn)
							SoundModel.playVibrate(getActivity(), 300);
					} else {
						Toast.makeText(getActivity().getApplicationContext(),
								"您现在收到" + result.size() + "条优惠哦！", Toast.LENGTH_SHORT)
								.show();
						//添加震动
						if(isBeenOn)
						SoundModel.playVibrate(getActivity(), 300);
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