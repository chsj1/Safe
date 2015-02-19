package com.njupt.safe.engine;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.njupt.safe.R;
import com.njupt.safe.activity.MainActivity;
import com.njupt.safe.bean.UpdataBean;
import com.njupt.safe.utils.XmlParseUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginHelper {

	private static LoginHelper login;
	private Activity context;
	private UpdataBean bean;
	
	private final int UPDATA = 11;
	private final int CONNECTERROR = 12;
	private final int SERVICEERROR = 13;
	private final int DOWNLOADERROR = 14;
	
	private ProgressDialog pd;
	
	private LoginHelper(Activity context){
		this.context = context;
	}
	
	public static LoginHelper getInstance(Activity context){
		if(login == null){
			login = new LoginHelper(context);
		}
		
		return login;
	}
	
	public void loginConnect(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				connect();
			}
		}).start();
	}

	protected void connect() {
		String apkurl = context.getResources().getString(R.string.apkurl);
		Message msg = new Message();
		
		try {
			URL url = new URL(apkurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200){
				bean = XmlParseUtil.getUpdataInfo(conn.getInputStream());
				if(bean != null){
					if(bean.getVersion().equals(ViewHelper.getVersion(context))){
						enterMain();
					}else{
						msg.what = UPDATA;
						handler.sendMessage(msg);
					}
				}
			}else{
				msg.what = SERVICEERROR;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.what = CONNECTERROR;
			handler.sendMessage(msg);
		}
	}

	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATA:
				updateTipDialog();
				break;
			case CONNECTERROR:
				Toast.makeText(context, "连接服务器失败", 1).show();
				enterMain();
				break;
			case SERVICEERROR:
				Toast.makeText(context, "服务器出错", 1).show();
				enterMain();
				break;
			case DOWNLOADERROR:
				Toast.makeText(context, "下载失败", 1).show();
				break;
			}
			super.handleMessage(msg);
		};
	};
	
	private void enterMain() {
		Intent intent = new Intent(context,MainActivity.class);
		context.startActivity(intent);
		context.finish();
	}

	protected void updateTipDialog() {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("升级提示");
		builder.setMessage(bean.getDes());
		builder.setPositiveButton("升级",new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				updateApk();
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterMain();
			}
		});
		
		builder.create().show();
	}

	protected void updateApk() {
		pd = new ProgressDialog(context);
		pd.setTitle("正在下载....");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.show();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				File file = DownloadHelper.getApkFile(bean.getApkurl(), pd);
			    pd.dismiss();
			    if(file == null){
			    	Message msg = new Message();
			    	msg.what = DOWNLOADERROR;
			    	handler.sendMessage(msg);
			    }else{
			    	Intent intent = new Intent();
			    	intent.setAction("android.intent.action.VIEW");
			    	intent.addCategory("android.intent.category.DEFAULT");
			    	intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
			    	context.startActivity(intent);
			    	context.finish();
			    }
			}
		}).start();
	}
	
	public void destroy(){
		login = null;
	}
}
