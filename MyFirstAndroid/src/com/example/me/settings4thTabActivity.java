package com.example.me;

import com.example.myfirstandroid.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;

public class settings4thTabActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	// �г����е�prefererence�ļ�ֵ
	public static final String IS_RECEIVE_STORE_MSG = "isReceiveMessage";// �����̻���Ϣ����key
	public static final String IS_RECEIVE_FRIEND_MSG = "isReceiveFriendsMessage";// ��������Ȧ��Ϣkey
	public static final String IS_SOUND_ON = "isSoundOn";// ��������Key
	public static final String IS_BEEP_ON = "isBeepOn";// ������ key

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.system_settings_4th_tab);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		CheckBoxPreference preference;
		// �ж������Ƿ���ˣ������ˣ���������ʾ����Ӧ������ϢҲ����仯
		preference = (CheckBoxPreference) findPreference(key);
		if (key.equals(IS_RECEIVE_STORE_MSG)) {
			if (preference.isChecked()) {
				preference.setSummary("选择接受已订阅的商户推送的新消息");
				
			} else {
				preference.setSummary("拒绝接受已订阅的商户推送的新消息");
			}
		} else if (key.equals(IS_RECEIVE_FRIEND_MSG)) {
			if (preference.isChecked()) {
				preference.setSummary("选择接受朋友圈推送的新消息");
			} else {
				preference.setSummary("拒绝接受朋友圈推送的新消息");
			}
		} else if (key.equals(IS_BEEP_ON)) {
			if (preference.isChecked()) {
				preference.setSummary("开启震动");
			} else {
				preference.setSummary("关闭震动");
			}
		} else if (key.equals(IS_SOUND_ON)) {
			if (preference.isChecked()) {
				preference.setSummary("开启声音");
			} else {
				preference.setSummary("关闭声音");
			}
		}
	}

}
