package com.example.myfirstandroid;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.example.myfirstandroid.R;

public final class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
	 {
	
	//private 
	//private boolean isSearchVisable = true;
	public ScanFragment sf;
	private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
    private PagerAdapter mPagerAdapter;
	View.OnTouchListener gestureListener;
	
	private int[] mIcon = new int[] {R.drawable.tab_weibao_normal,
			R.drawable.tab_find_normal,
			R.drawable.tab_scan_normal,
			R.drawable.tab_me_normal};
	private String[] mTitle = new String[] {"微宝" ,"发现","扫","我"};
	private String[] mActionTitle = new String[] {"微宝" ,"发现","二维码兑换","个人信息"};
	
	private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }
	}

	
	/**
     * A simple factory that returns dummy views to the Tabhost
     */
    class TabFactory implements TabContentFactory {
 
        private final Context mContext;
 
        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }
 
        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
 
    }
    @Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() != 1) {
			exit();
			return true;
		}

		return super.dispatchKeyEvent(event);
	}
    private void exit() {
		new AlertDialog.Builder(MainActivity.this)
				.setMessage(R.string.exit_confirm)
				.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int whichButton) {
								finish();
								android.os.Process
										.killProcess(android.os.Process.myPid());
							}
						}).setNegativeButton("取消", null)
				.show();
	}
    
    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // Initialise the TabHost
        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Intialise ViewPager
        this.intialiseViewPager();
        
    }
 
    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
 
    /**
     * Initialise ViewPager
     */
    private void intialiseViewPager() {
 
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, WeibaoFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, FindFragment.class.getName()));
        sf=(ScanFragment)Fragment.instantiate(this, ScanFragment.class.getName());
        fragments.add(sf);
        fragments.add(Fragment.instantiate(this, MeFragment.class.getName()));
        this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }
 
    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        MainActivity.AddTab(0,this, this.mTabHost, this.mTabHost.newTabSpec("tab1").setIndicator(getTabView(0)), ( tabInfo = new TabInfo("Tab1", WeibaoFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(1,this, this.mTabHost, this.mTabHost.newTabSpec("tab2").setIndicator(getTabView(1)), ( tabInfo = new TabInfo("Tab2", FindFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(2,this, this.mTabHost, this.mTabHost.newTabSpec("tab3").setIndicator(getTabView(2)), ( tabInfo = new TabInfo("Tab3", ScanFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(3,this, this.mTabHost, this.mTabHost.newTabSpec("tab4").setIndicator(getTabView(3)), ( tabInfo = new TabInfo("Tab4", MeFragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }
 
    /**
     * Add Tab content to the Tabhost
     * @param activity
     * @param tabHost
     * @param tabSpec
     * @param clss
     * @param args
     */
    private static void AddTab(int index, MainActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
        tabHost.getTabWidget().getChildAt(index).setBackgroundResource(R.color.tab_bg);
    }
    
    private View getTabView(int index){
    	View view = getLayoutInflater().inflate(R.layout.tab_item_view, null);
    	ImageView image = (ImageView) view.findViewById(R.id.tab_image);
    	image.setImageResource(mIcon[index]);
    	TextView text = (TextView) view.findViewById(R.id.tab_text);
    	text.setText(mTitle[index]);
    	return view;
    }
 
    /** (non-Javadoc)
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
        getActionBar().setTitle(mActionTitle[pos]);
        getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
       
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
    	
        // TODO Auto-generated method stub
 
    }
    
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        this.mTabHost.setCurrentTab(position);
        getActionBar().setTitle(mActionTitle[position]);
        getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
        //if(position ==0||position == 3){
    	//	ImageView image = (ImageView)findViewById(R.id.user_image);
    	//	BitmapDrawable bd=new BitmapDrawable(ImageHandeller.getBitmap(PersonModel.avatarLocalPathString)); 
    	//	image.invalidateDrawable(bd);
    	//}
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub
 
    }
    
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
/*
    @Override  
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(isSearchVisable);
        return super.onPrepareOptionsMenu(menu);  
    }  
  */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_logout:
			Intent intent = new Intent(this, LogInActivity.class);
	    	startActivity(intent);
			default:return super.onOptionsItemSelected(item);
		}
	}
	
	

/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);  
        mTabHost.setup();  
          
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("weibao").setContent(R.id.tab_weibao));  
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("find").setContent(R.id.tab_find));  
		// Show the Up button in the action bar.
		//setupActionBar();
        // Gesture detection
        
	}

	
*/
}