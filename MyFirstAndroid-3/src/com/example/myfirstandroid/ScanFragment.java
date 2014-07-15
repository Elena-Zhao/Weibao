package com.example.myfirstandroid;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.example.connectwebservice.DBOperation;
import com.example.entity.WeiJinEntity;
import com.example.myfirstandroid.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import com.zxing.activity.CaptureActivity;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public final class ScanFragment extends Fragment implements OnClickListener{
	private MainActivity mactivity; 
	private TextView resultTextView;
	private LinearLayout resultLinearLayout;
	
	
	private LinearLayout weiJinLinearLayout;
	private TextView storeNameTextView;
	private TextView briefInfoTextView;
	private TextView amounTextView;
	private TextView createTimeTextView;
	private TextView scanTimeTextView;
	
	//处理微金结果的两个button
	private String storeIDString;
	private String storeName;
	private String briefInfoString;
	private String amountString;
	private String createTimeString;
	private String barcodeID;
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
	           mactivity =(MainActivity)activity;
	    }catch(ClassCastException e){
	           throw new ClassCastException(activity.toString());
	    }
	}
	public View onCreateView(LayoutInflater inflater,  
			ViewGroup container, Bundle savedInstanceState) {  
			//---Inflate the layout for this fragment---  
			View view = inflater.inflate(  
	    	R.layout.scan, container, false);
			

		    return view;        
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		findAllViews();
		setAllIntents();
		backToUnScanStatus();
	}
	@Override
	public void onResume(){
		super.onResume();
	}

	private void findAllViews() {
		resultTextView = (TextView) mactivity.findViewById(R.id.tv_scan_result);
		((Button) mactivity.findViewById(R.id.btn_scan_barcode)).setOnClickListener(this);
		resultLinearLayout = (LinearLayout)mactivity.findViewById(R.id.scanResultLinearLayout_2rd_tab);
		weiJinLinearLayout = (LinearLayout)mactivity.findViewById(R.id.scanWeiJinDetalLinearLayout_2rd_tab);
		storeNameTextView = (TextView)mactivity.findViewById(R.id.StoreName_2rd_tab);
		briefInfoTextView = (TextView)mactivity.findViewById(R.id.ObjectInfo_2rd_tab);
		amounTextView = (TextView)mactivity.findViewById(R.id.Amount_2rd_tab);
		createTimeTextView = (TextView)mactivity.findViewById(R.id.CreateTime_2rd_tab);
		scanTimeTextView = (TextView)mactivity.findViewById(R.id.ScanTime_2rd_tab);
		((Button)mactivity.findViewById(R.id.btnAddWeiJin_2rd_tab)).setOnClickListener(this);
		((Button)mactivity.findViewById(R.id.btnCancelOrReturn_2rd_tab)).setOnClickListener(this);
	}

	private void setAllIntents() {
		
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
			case R.id.btn_scan_barcode:
				Intent openCameraIntent = new Intent(getActivity(),
						CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
				break;
			case R.id.btnCancelOrReturn_2rd_tab:
				backToUnScanStatus();
				break;
			case R.id.btnAddWeiJin_2rd_tab:
				//addWeiJin();//预留函数来添加微金
				new AddWeijin().execute();
				break;
		}
	}
	
	
	class AddWeijin extends AsyncTask {
		private int Result;
		@Override
		protected Object doInBackground(Object... params) {
			try {
				Result = DBOperation.addWeiJin(storeIDString, 
							Float.parseFloat(amountString)
							,createTimeString,briefInfoString,barcodeID);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}
		@Override
		protected void onPostExecute(Object result) {
			if (Result == 1) {
				Toast.makeText(getActivity(), "已加入我的账单！",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "生成账单失败，该条微金已添加，或请检查您的网络",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	
	private void addWeiJin(){
		//添加微金，开启后台线程
		//会退到最开始状态，只有扫描信息
		backToUnScanStatus();
	}
	@Override
	public void onActivityResult(int x, int resultCode, Intent data) {
		if (resultCode == mactivity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			//暂时保留，确定读取微金
			//读取二维码字符串，并确定是否属于合格的微金
			
			if(getWeiJinRecord(scanResult)){
				//确定是一条微金
				resultTextView.setText("扫描微金成功！");
				storeNameTextView.setText(storeName);
				briefInfoTextView.setText(briefInfoString);
				amounTextView.setText(amountString);
				createTimeTextView.setText(createTimeString);
				DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); 
				Date scanDate = new Date(System.currentTimeMillis());
				
				scanTimeTextView.setText(sdf.format(scanDate));
				weiJinLinearLayout.setVisibility(LinearLayout.VISIBLE);
			}else{
				//不是合法的微金
				resultTextView.setText("这个二维码不是微金哟亲~");
			}
			resultLinearLayout.setVisibility(LinearLayout.VISIBLE);
		}
	}
	//处理得到的字符串,获得一条微金
	Boolean getWeiJinRecord(String unhandledString){
		Boolean isGetWeijinRecord = false;
		StringTokenizer tokens = new StringTokenizer(unhandledString.trim(),"|");
		if(tokens.countTokens()==6){
			storeIDString = tokens.nextToken();
			storeName = tokens.nextToken();
			briefInfoString = tokens.nextToken();
			amountString = tokens.nextToken();
			createTimeString = tokens.nextToken();
			barcodeID = tokens.nextToken();
			isGetWeijinRecord = true;
			//需要检查该storeID是否存在！！！！！！
			
			
		}else{
			//格式不正确
			return false;
		}
		return isGetWeijinRecord;
	}
	//将扫描的结果的View隐藏
	private void backToUnScanStatus() {
		resultLinearLayout.setVisibility(LinearLayout.GONE);
		weiJinLinearLayout.setVisibility(LinearLayout.GONE);
	}

	

}
