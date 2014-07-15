package com.example.entity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

@SuppressLint("NewApi")
public class PersonModel {
	// 个人基本信息
	public static String name;
	public static String gender;
	public static String district;
	public static String signature;
	public static Bitmap avatar;
	public static String avatarLocalPathString;
	public static String weijin;
	public static String pension;

	// 关于账号相关信息
	public static String phoneNumber;
	public static String email;
	public static String weibaoID;
	public static String password;
	public static boolean isPhoneNumberChecked;
	public static boolean isEmailChecked;
	
	//记录了保存用户信息的preference的名字
	public static final String USER_PREF = "RegisteredUser";
	
	//PersonModel修改2.0 2013.12.1
	public static final int SIGNATURE_MAX_LENGTH = 140;
	
}