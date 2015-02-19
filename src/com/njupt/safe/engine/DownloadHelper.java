package com.njupt.safe.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.Environment;

public class DownloadHelper {
	public static File getApkFile(String url,ProgressDialog pd){
		int last = url.lastIndexOf("/");
		File file = new File(Environment.getExternalStorageDirectory(),url.substring(last +  1));
		
		try {
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200){
				pd.setMax(conn.getContentLength());
				int num = 0;
				InputStream is = conn.getInputStream();
				FileOutputStream os = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len;
				while( (len = is.read(buffer)) != -1 ){
					os.write(buffer,0,len);
					num += len;
					Thread.sleep(30);
					pd.setProgress(num);
				}
				
				os.flush();
				os.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return file;
	}
}
