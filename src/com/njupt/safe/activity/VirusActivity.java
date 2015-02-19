package com.njupt.safe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.njupt.safe.R;
import com.njupt.safe.utils.DensityUtil;
import com.njupt.safe.utils.MD5;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class VirusActivity extends Activity {
	protected static final int SCANING = 0;
	protected static final int FINISH_SCAN = 1;
	private ImageView iv_scan;
	private ScrollView scrollview;
	private ProgressBar pb;
	private Button bt_kill;
	private Button bt_startscan;
	private LinearLayout ll_info;
	private AnimationDrawable ad;
	private List<String> allvirus;
	private List<String> existvirus;
	private File file;
	private PackageManager pm;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SCANING:
				
				View view = (View) msg.obj;
				ll_info.addView(view);
				// 控制图标往下移
				scrollview.scrollBy(0, 60);
				break;

			// 处理扫描完的逻辑
			case FINISH_SCAN:
				int size = (Integer) msg.obj;
				TextView tv = new TextView(getApplicationContext());
				if (existvirus.size() > 0) {
					tv.setText("扫描了" + size + "个应用程序,发现了" + existvirus.size()
							+ "个病毒");
					bt_kill.setEnabled(true);// 当发现病毒时,杀毒按钮有效
				} else {
					tv.setText("扫描了" + size + "个应用程序,发现了" + existvirus.size()
							+ "个病毒");
				}
				
				ll_info.removeAllViews();
				tv.setTextSize(DensityUtil.px2dip(getApplicationContext(), 15));
				tv.setBackgroundResource(R.drawable.call_locate_orange);//给TextView设置背景
				ll_info.addView(tv);
				// 扫描完以后将动画停下来
				ad.stop();
				pb.setProgress(0);//将进度条的刻度归0
				
				bt_startscan.setEnabled(true);//杀完毒后重新启用扫描键...
				bt_startscan.setText("开始扫描");
				
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.antivirus);

		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		pb = (ProgressBar) findViewById(R.id.pb);
		ll_info = (LinearLayout) findViewById(R.id.ll_info);
		bt_kill = (Button) findViewById(R.id.bt_kill);
		bt_startscan = (Button) findViewById(R.id.bt_startscan);
		pm = getPackageManager();

		// 完成病毒库的拷贝
		try {
			file = new File(getFilesDir(), "antivirus.db");
			InputStream is = getAssets().open("antivirus.db");
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			is.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		allvirus = new ArrayList<String>();
		existvirus = new ArrayList<String>();
	}

	public void kill_virus(View v){
    	ad = (AnimationDrawable) iv_scan.getBackground();
    	ad.start();
    	
    	bt_startscan.setEnabled(false);//杀毒后禁用杀毒按钮
    	bt_startscan.setText("正在查毒请稍等....");
    	//杀毒  是一个耗时的操作
    	new Thread(){
    		public void run() {

    			Message msg = new Message();
    			msg.what = SCANING;
    			TextView tv = new TextView(getApplicationContext());
    			tv.setText("正在扫描...");
    			msg.obj = tv;
    			mHandler.sendMessage(msg);
    			//得到病毒库信息
    			SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
    			if(db.isOpen()){
    				Cursor c = db.query("datable", new String[]{"md5"}, null, null, null, null, null);
    				while(c.moveToNext()){
    					String md5 = c.getString(0);
    					allvirus.add(md5);
    				}
    				c.close();
    				db.close();
    			}

    			//得到所有的安装程序
    			List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES|PackageManager.GET_SIGNATURES);
    			//设置进度条的最大值
    			pb.setMax(packageInfos.size());
    			for(PackageInfo packageInfo:packageInfos){
    				String packageName = packageInfo.packageName;

                    //得到应用的图片和名称
    				ApplicationInfo appInfo = packageInfo.applicationInfo;
    				Drawable icon = appInfo.loadIcon(pm);
    				String name = appInfo.loadLabel(pm).toString();

    				//通过代码生成布局
    				LinearLayout ll = new LinearLayout(getApplicationContext());
    				ll.setOrientation(LinearLayout.HORIZONTAL);
    				ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    				ll.setLayoutParams(params);
    				ImageView iv = new ImageView(getApplicationContext());
    				params = new ViewGroup.LayoutParams(18, 18);
    				iv.setLayoutParams(params);
    				iv.setImageDrawable(icon);
    				ll.addView(iv);
    				TextView tv1 = new TextView(getApplicationContext());
    				params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    				tv1.setLayoutParams(params);
    				tv1.setText(name);
    				ll.addView(tv1);

    				Message msg2 = new Message();
    				msg2.what = SCANING;
    				msg2.obj = ll;
    				mHandler.sendMessage(msg2);

    				Signature[] signatures = packageInfo.signatures;
    				StringBuilder sb = new StringBuilder();
    				for(Signature signature:signatures){
    					sb.append(signature.toCharsString());
    				}
    				String signature = MD5.getData(sb.toString());
    				if(allvirus.contains(signature)){
    					existvirus.add(packageName);
    				}

    				//进度条每次增加1个
    				pb.incrementProgressBy(1);

    				//睡眠一下,忽悠用户...给他感觉好真实
    				SystemClock.sleep(100);
    			}

                //告诉handler扫描完了
    			Message msg3 = new Message();
    			msg3.what = FINISH_SCAN;
    			msg3.obj = packageInfos.size();//扫描的应用程序的个数
    			mHandler.sendMessage(msg3);

    		};
    	}.start();
    }

	public void kill(View v) {
		for (String packageName : existvirus) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:" + packageName));
			startActivity(intent);
		}
	}
}
