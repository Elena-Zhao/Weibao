package com.example.connectwebservice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.example.email.EmailSender;
import com.example.entity.PersonModel;
import com.example.imagehandle.ImageFileCache;
import com.example.imagehandle.ImageMemoryCache;
import com.example.myfirstandroid.WeiBaoApp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ImageHandeller {
	private static String URL = "http://hyacinth02070.oicp.net/WebService1.asmx";
	private static String NAMESPACE = "http://tempuri.org/";
	private static String METHOD_NAME;
	private static String SOAP_ACTION;
	private static ImageMemoryCache memoryCache = new ImageMemoryCache(
			WeiBaoApp.getContext());
	private static ImageFileCache fileCache = new ImageFileCache();

	public static Bitmap getBitmap(String url) {
		// 从内存缓存中获取图片
		Bitmap result = memoryCache.getBitmapFromCache(url);
		if (result == null) {
			// 文件缓存中获取
			result = fileCache.getImage(url);
			memoryCache.addBitmapToCache(url, result);
		}
		return result;
	}

	public static void download(String url) {
		METHOD_NAME = "download";
		SOAP_ACTION = "http://tempuri.org/download";
		try {
			// 从内存缓存中获取图片
			Bitmap result = memoryCache.getBitmapFromCache(url);
			if (result == null) {
				// 文件缓存中获取
				result = fileCache.getImage(url);
				if (result == null) {
					SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
					rpc.addProperty("url", url);
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.bodyOut = rpc;
					envelope.dotNet = true;
					HttpTransportSE ht = new HttpTransportSE(URL);
					ht.debug = true;
					ht.call(SOAP_ACTION, envelope);
					Log.i("aaa", envelope.toString());
					if (envelope.bodyIn != null) {
						SoapObject soapObject = (SoapObject) envelope.bodyIn;
						byte[] data = Base64.decode(soapObject.getProperty(0)
								.toString());
						Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						Log.i("aaa", bitmap.toString());
						// 将bitmap缓存到内存和文件缓存中
						fileCache.saveBitmap(bitmap, url);
						memoryCache.addBitmapToCache(url, bitmap);
					}
				} else
					memoryCache.addBitmapToCache(url, result);
			}
		} catch (Exception e) {
			Log.i("aaa", e.toString());
		}
	}

	public static boolean uploadAvatar(String url) {
		METHOD_NAME = "upload";
		SOAP_ACTION = NAMESPACE + METHOD_NAME;
		try {
			/* FileInputStream fis = new FileInputStream(url); */
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 添加压缩图片内容
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(url, options);
			options.inSampleSize = calculateInSampleSize(options, 480, 800);
			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeFile(url, options);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			//
			/*
			 * byte[] buffer = new byte[1024]; int count = 0; while((count =
			 * fis.read(buffer)) >= 0){ baos.write(buffer, 0, count); }
			 * fis.close();
			 */
			String uploadBuffer = new String(Base64.encode(baos.toByteArray())); // 进行Base64编码
			SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
			String[] temp = url.split("/");
			String fileName = temp[temp.length - 1];
			rpc.addProperty("fileName", fileName);
			rpc.addProperty("image", uploadBuffer);
			rpc.addProperty("userId", PersonModel.weibaoID);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.debug = true;
			ht.call(SOAP_ACTION, envelope);
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			if (Boolean.valueOf(soapObject.getProperty(0).toString())) {
				String fileUrl = "F:/WeiBao/" + convertUrlToFileName(url);
				PersonModel.avatarLocalPathString = fileUrl;
				Bitmap avatar = BitmapFactory.decodeFile(url);
				fileCache.saveBitmap(avatar, fileUrl);
				memoryCache.addBitmapToCache(fileUrl, avatar);
				return true;
			} else
				return false;
		} catch (Exception e) {
			Log.i("aaa", e.toString());
			return false;
		}
	}

	private static String convertUrlToFileName(String url) {
		String[] strs = url.split("/");
		return strs[strs.length - 1];
	}

	// 计算图片缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		int intSampleSize = 1;
		int height = options.outHeight;
		int width = options.outWidth;

		if (height > reqHeight || width > reqWidth) {
			int heightRatio = Math.round((float) height / (float) reqHeight);
			int widthRatio = Math.round((float) width / (float) reqWidth);
			intSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return intSampleSize;
	}
}
