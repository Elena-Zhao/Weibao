package com.example.AccountActivities; 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.connectwebservice.DBOperation;
import com.example.connectwebservice.ImageHandeller;
import com.example.email.EmailSender;
import com.example.entity.CheckModel;
import com.example.entity.PersonModel;
import com.example.imagehandle.ImageFileCache;
import com.example.me.profile4thTabActivity;
import com.example.myfirstandroid.R;
import com.example.photo.AlbumStorageDirFactory;
import com.example.photo.BaseAlbumDirFactory;
import com.example.photo.FroyoAlbumDirFactory;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
public class EditMyImageActivity extends Activity {
	// ��������
	private  final int ACTION_TAKE_PHOTO_B = 1;
	private  final int ACTION_TAKE_PHOTO_S = 2;
	private  final int ACTION_GET_LOCAL_PHOTO = 3;
	private  final String JPEG_FILE_PREFIX = "AVATAR_IMG_";
	private  final String JPEG_FILE_SUFFIX = ".jpg";
	private Bitmap mImageBitmap;
	private ImageView mImageView;
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	// ����albumNameΪ�̶���ֵ,�ļ���·����ر���
	private  final String STANDARD_ALBUM_NAME = "WeiBaoAvatar";
	private String mCurrentPhotoPathString;

	private Button takePictureButton;
	private Button getLocalPictureButton;
	private Button cancelButton;

	private Intent backToProfileIntent;
	private Intent getLocalPicturesIntent;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_edit_image);
		findViews();
		setIntents();
		setListeners();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setInfo();
	}

	private void setInfo() {
		if (PersonModel.avatarLocalPathString != null) {
			this.mCurrentPhotoPathString = new String(
					PersonModel.avatarLocalPathString);
			//setPic();
			Bitmap resultAvatar = ImageHandeller.getBitmap(PersonModel.avatarLocalPathString);
			this.mImageView.setImageBitmap(resultAvatar);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case ACTION_TAKE_PHOTO_B: {
			if (resultCode == RESULT_OK)
				handleBigCameraPhoto();
			break;
		}

		case ACTION_TAKE_PHOTO_S: {
			handleSmallCameraPhoto(data);
			break;
		}
		case ACTION_GET_LOCAL_PHOTO:{
			if(resultCode == RESULT_OK){
				Uri uri = data.getData();  
	            Log.e("uri", uri.toString());  
	            ContentResolver cr = this.getContentResolver();  
	            try {  
	                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri)); 
	                mImageView.setImageBitmap(bitmap);  
	                
	                String[] proj = { MediaStore.Images.Media.DATA };
	                @SuppressWarnings("deprecation")
					Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);

	                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

	                actualimagecursor.moveToFirst();

	                String img_path = actualimagecursor.getString(actual_image_column_index);
	                this.mCurrentPhotoPathString = img_path;
	                new uploadLocalImage().execute(img_path);
	               
	            } catch (FileNotFoundException e) {  
	                Log.e("Exception", e.getMessage(),e);  
	            }  
	        }  
			break;
		}
		default:
			break;
		}
	}

	private void findViews() {
		takePictureButton = (Button) findViewById(R.id.btnAvatarTakePictureNow);
		getLocalPictureButton = (Button) findViewById(R.id.btnAvatarGetLocalPicture);
		cancelButton = (Button) findViewById(R.id.btnCancelChangeAvatar);
		mImageView = (ImageView) findViewById(R.id.imageAvatarView);
	}

	private void setIntents() {
		backToProfileIntent = new Intent(EditMyImageActivity.this,
				profile4thTabActivity.class);
		//takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		getLocalPicturesIntent = new Intent();
		getLocalPicturesIntent.setType("image/");
		getLocalPicturesIntent.setAction(Intent.ACTION_GET_CONTENT); 
	}

	private void setListeners() {
		// ��cancelbutton��ص����ĸ�tab��������
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(backToProfileIntent);
			}
		});
		if (CheckModel.isIntentAvailable(EditMyImageActivity.this,
				MediaStore.ACTION_IMAGE_CAPTURE)) {
			takePictureButton.setOnClickListener(new OnClickListener() {
				// �������հ�ť��������ģʽ
				@Override
				public void onClick(View v) {
					dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
				}
			});
		} else {
			// ������ܴ򿪼���������ʱ
			takePictureButton.setText("不能" + takePictureButton.getText());
			takePictureButton.setClickable(false);
		}
		getLocalPictureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(getLocalPicturesIntent, ACTION_GET_LOCAL_PHOTO);
			}
		});
	}

	// ����ͷ����ļ���
	private File getAlbumDir() {
		File storageDirFile = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			storageDirFile = mAlbumStorageDirFactory
					.getAlbumStorageDir(STANDARD_ALBUM_NAME);
			if (storageDirFile != null) {
				if (!storageDirFile.mkdirs()) {
					// failure to make directory
					if (!storageDirFile.exists()) {
						Log.d("Weibao Take Avartar Photo",
								"failed to create directory");
						return null;
					}
				}
			}
		} else {
			Log.v("Weibao Take Avatar Photo",
					"External storage is not mounted READ/WRITE.");
		}
		return storageDirFile;
	}

	// ����ͷ������ļ�
	private File createImageFile() throws IOException {
		// ͼƬ�м����������յ�ʱ���
		String timeStampString = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = JPEG_FILE_PREFIX +PersonModel.weibaoID+"_"+ timeStampString + "_";
		File albumFile = getAlbumDir();
		File imageFile = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX,
				albumFile);
		return imageFile;
	}

	private File setUpPhotoFile() throws IOException {
		File file = createImageFile();
		mCurrentPhotoPathString = file.getAbsolutePath();
		return file;
	}


	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(
				"android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File file = new File(mCurrentPhotoPathString);
		Uri contentUri = Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	private void dispatchTakePictureIntent(int requestCode) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		switch (requestCode) {
		case ACTION_TAKE_PHOTO_B:
			File file = null;
			try {
				file = setUpPhotoFile();
//				mCurrentPhotoPathString = file.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(file));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				file = null;

			}
		default:
			break;
		}

		startActivityForResult(takePictureIntent, requestCode);
	}
	//压缩大图片并上传头像
	private void handleBigCameraPhoto() {
		if (mCurrentPhotoPathString != null) {
			galleryAddPic();
			
			new uploadLocalImage().execute(mCurrentPhotoPathString);
			Context context = getApplicationContext();
			CharSequence text;
			text = "头像上传中，请耐心等待⋯⋯";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			// mCurrentPhotoPathString = null;
		}
	}

	private void handleSmallCameraPhoto(Intent intent) {
		Bundle extrasBundle = intent.getExtras();
		mImageBitmap = (Bitmap) extrasBundle.get("small_image_data");
		mImageView.setImageBitmap(mImageBitmap);
	}
	
	class uploadLocalImage extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			Boolean isSendSuccess = false;
			isSendSuccess = ImageHandeller.uploadAvatar(params[0]);
			return isSendSuccess;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Context context = getApplicationContext();
			CharSequence text;
			if (result) {
				text = "头像修改成功";
				mImageView.setImageBitmap(ImageHandeller.getBitmap(PersonModel.avatarLocalPathString));
			} else {
				text = "修改头像失败，请检查你的网络状况";
			}
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
}
