package com.example.photo;

import java.io.File;

import android.os.Environment;

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {
	//��λ����Ϊcamera�ı�׼·��
	private  final String CAMERA_DIR ="/dcim/weiBao/";
	@Override
	public File getAlbumStorageDir(String albumName) {
		// TODO Auto-generated method stub
		return new File(Environment.getExternalStorageDirectory()+CAMERA_DIR+albumName);
	}

}
