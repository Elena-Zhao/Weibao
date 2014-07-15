package com.example.AccountActivities;


import java.io.UnsupportedEncodingException;

import com.example.entity.CreateBarcodeCardModel;
import com.example.entity.PersonModel;
import com.example.myfirstandroid.R;
import com.google.zxing.WriterException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowBitmapCardActivity extends Activity{
	private ImageView showCardImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_show_bitmap);
		showCardImageView = (ImageView) findViewById(R.id.barcodeCardImageView);
		String personInfoString = setPersonInfo();
		
		try {
			personInfoString = translateString(personInfoString);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Bitmap selfInfoBitmap = CreateBarcodeCardModel.Create2DCode(personInfoString);
			showCardImageView.setImageBitmap(selfInfoBitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private String setPersonInfo(){
		return new String("微宝ID："+PersonModel.weibaoID+"；手机号："+PersonModel.email+"；邮箱："+PersonModel.email);
	}
	private String translateString(String toTranlateString) throws UnsupportedEncodingException{
		String resultString = new String(toTranlateString.getBytes("UTF-8"),"ISO-8859-1");
		//�޸�ʹ������ʾ����
		return resultString;
	}
}
