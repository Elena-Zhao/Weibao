package com.example.find;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.connectwebservice.DBOperation;
import com.example.entity.McItem;
import com.example.find.McActivity.ChangeSubs;
import com.example.find.McActivity.FindMc;
import com.example.myfirstandroid.R;

public final class SubscribedMcActivity extends McActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public void changeSubscribe(Button btn, int pos){
		dialog(pos,btn);
	}
	
	@Override
	public void setAdapter(){
		adapter = new MyListAdapter(mcInfoList,this,false);
		listview.setAdapter(adapter);
	}
	@Override
	public void startFind(){
		new FindMc(2).execute();
	}
	
	class onConfirmSubscriptBtnClick implements DialogInterface.OnClickListener{
		private int position;
		private Button btn;
		public onConfirmSubscriptBtnClick(int pos, Button btn){
			position = pos;
			this.btn = btn;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			new ChangeSubs1(btn, position).execute();
		}
	}
	protected void dialog(int pos, Button btn) {
		  AlertDialog.Builder builder = new Builder(this);
		  builder.setMessage("确认要取消订阅吗？");

		  builder.setTitle("取消订阅");

		  builder.setPositiveButton("确认", new onConfirmSubscriptBtnClick(pos,btn));

		  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

		   @Override
		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });

		  builder.create().show();
		 }
	
	class ChangeSubs1 extends AsyncTask
	{
		private McItem item;
		private int Result;
		private int pos;
		public ChangeSubs1(Button btn, int position){
			super();
			this.item = mcInfoList.get(position);
			pos = position;
		}
		@Override
	    protected Object doInBackground(Object... params)
	    { 
			try{
					Result = DBOperation.cancelSubs(item.getMcId());
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
				mcInfoList.remove(pos);
				adapter.notifyDataSetChanged();
			}else{
				Toast.makeText(getApplication(), "请求失败，请检查网络连接", Toast.LENGTH_LONG).show();
			}
	    }
	}

}
