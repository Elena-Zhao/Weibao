package com.example.find;

import java.util.List;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.mapapi.utils.CoordinateConvert;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.connectwebservice.DBOperation;
import com.example.entity.McItem;
import com.example.find.McActivity.FindMc;
import com.example.myfirstandroid.R;

public class McNearbyActivity extends Activity implements CloudListener {
	private BMapManager mcMapMan = null;
	private MapView mcMapView = null;
	private double latitude;
	private double longitude;
	MKSearch mMKSearch = null;
	GeoPoint point = null;
	GeoPoint converted_point = null;
	private String strKey = "nxkTmMiVH1fLQvaXsYeP6rsq";

	private View viewCache = null;
	private View popupInfo = null;
	private View popupLeft = null;
	private View popupRight = null;
	private TextView popupText = null;
	private PopupOverlay pop = null;

	private CloudPoiInfo mCurItem = null;
	private McItem mcItem;

	String tag = "Elena :)";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(tag, "start onCreate~~~");

		// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		initEngineManager(this);

		setContentView(R.layout.activity_map_mc_nearby);
		mcMapView = (MapView) findViewById(R.id.bmapsView);

		// 定位
		get_user_loc();
		// 展示地图
		show_map();
		// 云搜索
		cloud_search();
		// 在地图上标明我的位置
		show_user_loc();

		initOverlay();

	}

	public void initEngineManager(Context context) {
		if (mcMapMan == null) {
			mcMapMan = new BMapManager(context);
		}

		if (!mcMapMan.init(strKey, new MyGeneralListener())) {
			Toast.makeText(context, "BMapManager  初始化错误!", Toast.LENGTH_LONG)
					.show();
		}
	}

	private void get_user_loc() {
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 100, 10,
				new TestLocationListener());
		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		} else {
			Toast.makeText(getApplicationContext(), "位置获取失败，请检查GPS或网络连接是否正常",
					Toast.LENGTH_LONG).show();
		}
	}

	private void show_map() {

		mcMapView.setBuiltInZoomControls(true);
		// 设置启用内置的缩放控件
		MapController mMapController = mcMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		point = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
		converted_point = CoordinateConvert.fromWgs84ToBaidu(point);
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(converted_point);// 设置地图中心点
		mMapController.setZoom(18);// 设置地图zoom级别
	}

	private void show_user_loc() {
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mcMapView);
		LocationData locationData = new LocationData();
		locationData.latitude = (double) (converted_point.getLatitudeE6() / 1E6);
		locationData.longitude = (double) (converted_point.getLongitudeE6() / 1E6);
		locationData.direction = 2.0f;
		myLocationOverlay.setData(locationData);
		mcMapView.getOverlays().add(myLocationOverlay);
		mcMapView.refresh();
		mcMapView.getController().animateTo(converted_point);

	}

	private void search_poi() {
		mMKSearch = new MKSearch();
		mMKSearch.init(mcMapMan, new MySearchListener());// 注意，MKSearchListener只支持一个，以最后一次设置为准

		mMKSearch.poiSearchNearBy("kfc", converted_point, 5000);
	}

	private void cloud_search() {
		CloudManager.getInstance().init(this);
		NearbySearchInfo info = new NearbySearchInfo();
		// info.ak = "D9ace96891048231e8777291cda45ca0";
		info.ak = "nxkTmMiVH1fLQvaXsYeP6rsq";
		info.geoTableId = 46122;
		info.location = (double) (converted_point.getLatitudeE6() / 1E6) + ","
				+ (double) (converted_point.getLongitudeE6() / 1E6);
		info.radius = 30000;
		CloudManager.getInstance().nearbySearch(info);
	}

	private void initOverlay() {
		viewCache = getLayoutInflater().inflate(R.layout.find_popup_view, null);
		popupInfo = (View) viewCache.findViewById(R.id.popinfo);
		popupLeft = (View) viewCache.findViewById(R.id.popleft);
		popupRight = (View) viewCache.findViewById(R.id.popright);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);

		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				if (index == 0) {
					String mc_id = mCurItem.tags;
					System.out.println("-------------" + mc_id);
					new FindMc(mc_id).execute();
					if (mcItem != null) {
						Intent intent = new Intent();
						intent.putExtra("mcItem", mcItem);
						System.out.println(mcItem.toString());

						intent.setClass(McNearbyActivity.this,
								ShowMcInfoActivity.class);
						startActivity(intent);
					}
					else {
						Toast.makeText(getApplicationContext(), "商户信息连接失败，请稍后重试。",
								Toast.LENGTH_LONG).show();
					}
				} else if (index == 2) {
					GeoPoint destinationPoint = new GeoPoint((int) (mCurItem.latitude * 1E6), (int) (mCurItem.longitude * 1E6));
					startNavi(converted_point, destinationPoint);
				}
			}
		};
		pop = new PopupOverlay(mcMapView, popListener);

	}

	@Override
	protected void onDestroy() {
		Log.e(tag, "start onDestroy~~~");
		mcMapView.destroy();
		if (mcMapMan != null) {
			mcMapMan.destroy();
			mcMapMan = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.e(tag, "start onPause~~~");
		mcMapView.onPause();
		if (mcMapMan != null) {
			mcMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.e(tag, "start onRestart~~~");
		// TODO Auto-generated method stub
		if (mcMapMan != null) {
			mcMapMan.start();
		}
		// 云搜索
		cloud_search();
		// 在地图上标明我的位置
		show_user_loc();
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.e(tag, "start onResume~~~");
		mcMapView.onResume();
		if (mcMapMan != null) {
			mcMapMan.start();
		}
		// 云搜索
		cloud_search();
		// 在地图上标明我的位置
		show_user_loc();

		super.onResume();
	}

	protected class TestLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(getApplicationContext(), "您的网络出错啦！",
						Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(getApplicationContext(), "输入正确的检索条件！",
						Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(getApplicationContext(), "地图授权失败！",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	protected class MySearchListener implements MKSearchListener {
		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			// 返回地址信息搜索结果
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
			// 返回驾乘路线搜索结果
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
			// 返回公交搜索结果
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
			// 返回步行路线搜索结果
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			// 返回公交车详情信息搜索结果
		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult result, int type,
				int error) {
			// 在此处理短串请求返回结果.
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetPoiResult(MKPoiResult res, int type, int error) {
			if (error == MKEvent.ERROR_RESULT_NOT_FOUND) {
				Toast.makeText(getApplicationContext(), "抱歉，未找到结果",
						Toast.LENGTH_LONG).show();
				return;
			} else if (error != 0 || res == null) {
				Toast.makeText(getApplicationContext(), "搜索出错啦..",
						Toast.LENGTH_LONG).show();
				return;
			}
			PoiOverlay poiOverlay = new PoiOverlay(McNearbyActivity.this,
					mcMapView);
			poiOverlay.setData(res.getAllPoi());
			// mcMapView.getOverlays().clear();
			mcMapView.getOverlays().add(poiOverlay);
			mcMapView.refresh();
			for (MKPoiInfo info : res.getAllPoi()) {
				if (info.pt != null) {
					mcMapView.getController().animateTo(info.pt);
					break;
				}
			}

		}
	}

	@Override
	public void onGetDetailSearchResult(DetailSearchResult result, int error) {
		if (result != null) {
			if (result.poiInfo != null) {
				Toast.makeText(this, result.poiInfo.title, Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(this, "status:" + result.status,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onGetSearchResult(CloudSearchResult result, int error) {
		if (result != null && result.poiList != null
				&& result.poiList.size() > 0) {
			CloudOverlay poiOverlay = new CloudOverlay(this, mcMapView);
			poiOverlay.setData(result.poiList);
			mcMapView.getOverlays().add(poiOverlay);
			mcMapView.refresh();
			mcMapView.getController().animateTo(new
			GeoPoint((int)(result.poiList.get(0).latitude * 1e6),
			(int)(result.poiList.get(0).longitude * 1e6)));
		} else {
			Toast.makeText(this, "no result!" + result.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mcMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mcMapView.onRestoreInstanceState(savedInstanceState);
	}

	class CloudOverlay extends ItemizedOverlay {

		List<CloudPoiInfo> mLbsPoints;
		Activity mContext;

		public CloudOverlay(Activity context, MapView mMapView) {
			super(null, mMapView);
			mContext = context;
		}

		public void setData(List<CloudPoiInfo> lbsPoints) {
			if (lbsPoints != null) {
				mLbsPoints = lbsPoints;
			}
			for (CloudPoiInfo rec : mLbsPoints) {
				GeoPoint pt = new GeoPoint((int) (rec.latitude * 1e6),
						(int) (rec.longitude * 1e6));
				OverlayItem item = new OverlayItem(pt, rec.title, rec.address);
				Drawable marker1 = this.mContext.getResources().getDrawable(
						R.drawable.icon_marka);
				item.setMarker(marker1);
				addItem(item);
			}
		}

		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}

		@Override
		protected boolean onTap(int arg0) {
			CloudPoiInfo item = mLbsPoints.get(arg0);
			mCurItem = item;
			Toast.makeText(mContext, item.title, Toast.LENGTH_LONG).show();
			popupText.setText(item.title);
			Bitmap[] bitMaps = { BMapUtil.getBitmapFromView(popupLeft),
					BMapUtil.getBitmapFromView(popupInfo),
					BMapUtil.getBitmapFromView(popupRight) };
			GeoPoint point = new GeoPoint((int) (item.latitude * 1E6),
					(int) (item.longitude * 1E6));

			pop.showPopup(bitMaps, point, 32);

			return super.onTap(arg0);
		}
	}

	class FindMc extends AsyncTask {
		private List<McItem> result;
		private String mcId;

		public FindMc(String mcId) {
			this.mcId = mcId;
		}

		@Override
		protected Object doInBackground(Object... params) {
			try {
				mcItem = DBOperation.getMcById(mcId);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	public void startNavi(GeoPoint pt1, GeoPoint pt2){		
	    // 构建 导航参数
        NaviPara para = new NaviPara();
        para.startPoint = pt1;
        para.startName= "从这里开始";
        para.endPoint  = pt2;
        para.endName   = "到这里结束";
        
        try {
        	
			 BaiduMapNavigation.openBaiduMapNavi(para, this);
			 
		} catch (BaiduMapAppNotSupportNaviException e) {
			e.printStackTrace();
			  AlertDialog.Builder builder = new AlertDialog.Builder(this);
			  builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
			  builder.setTitle("提示");
			  builder.setPositiveButton("确认", new OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				 dialog.dismiss();
				 BaiduMapNavigation.GetLatestBaiduMapApp(McNearbyActivity.this);
			   }
			  });

			  builder.setNegativeButton("取消", new OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			   }
			  });

			  builder.create().show();
			 }
		}

}
