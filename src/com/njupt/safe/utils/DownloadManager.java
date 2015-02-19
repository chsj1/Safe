package com.njupt.safe.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.Environment;

public class DownloadManager {

	
	public static boolean download(String path,ProgressDialog mPd) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			mPd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();	
            File file = new File(Environment.getExternalStorageDirectory(), getFileName(path));
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            int count = 0;
            while((len = is.read(buffer)) != -1){
            	fos.write(buffer, 0, len);
            	count = count + len;
            	mPd.setProgress(count);
            }
            is.close();
            fos.close();
            return true;
		}
		return false;
	}
	
	public static String getFileName(String path){//进行云存储版本
		return path.substring(path.lastIndexOf("/") + 1);
	}
	
	public static String getFileName2(String path){//未进行云存储版本
		return path.substring(path.lastIndexOf("/") + 1);
	}
}
