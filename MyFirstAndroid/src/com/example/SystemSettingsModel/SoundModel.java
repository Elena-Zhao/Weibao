package com.example.SystemSettingsModel;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

public class SoundModel {
	public static void playBeepAndVibrate(){
	//发出铃声和震动
	}
	public static void playVibrate(Activity activity,long milliseconds){
	//发出震动
		Vibrator vibrator = (Vibrator)activity.getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(milliseconds);
	}
}
