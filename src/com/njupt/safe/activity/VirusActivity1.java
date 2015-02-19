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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class VirusActivity1 extends Activity {

	
	protected static final String tag = "AntiVirusActivity";
	protected static final int SCAN_LODING = 1;
	protected static final int FINSH_SCAN = 2;
	private ImageView im_scan;
	private ImageView im_dian;
	private TextView tv_lodingApk;
	private TextView tv_count;
	private LinearLayout ll_scanText;
	private ProgressBar pb_loding;
	
	private ScrollView scrollview;
	private List<String> allvirus;
	private List<String> existvirus;
	private File file;
	private PackageManager pm;
	
	private int count;
	
	
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SCAN_LODING:
				tv_lodingApk.setText("正在扫描:第"+count+"个应用");
				
				View view = (View) msg.obj;
				System.out.println("---------->view: "+ view);
				ll_scanText.addView(view);
				scrollview.scrollBy(0, 60);
				
				tv_count.setText("已扫描:"+count+"个应用");
				break;
			case FINSH_SCAN:
				
				int size = (Integer) msg.obj;
				if (existvirus.size() > 0) {
					tv_lodingApk.setText("扫描了" + size + "个应用程序,发现了" + existvirus.size()
							+ "个病毒");
				} else {
					tv_lodingApk.setText("扫描了" + size + "个应用程序,发现了" + existvirus.size()
							+ "个病毒");
				}
				
				ll_scanText.removeAllViews();
//				ll_scanText.addView();
				pb_loding.setProgress(0);
				
				im_scan.clearAnimation();//清除此ImageView身上的动画
				im_dian.clearAnimation();//清除此ImageView身上的动画
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.antivirus1);
		
		pm = getPackageManager();
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		
		im_scan = (ImageView) findViewById(R.id.im_scan);
		im_dian = (ImageView) findViewById(R.id.im_dian);
		tv_lodingApk = (TextView) findViewById(R.id.tv_lodingApk);
		ll_scanText = (LinearLayout) findViewById(R.id.ll_scanText);
		pb_loding = (ProgressBar) findViewById(R.id.pb_loding);
		tv_count = (TextView) findViewById(R.id.tv_count);
		
		RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(2000);
		animation.setRepeatCount(Animation.INFINITE);
		im_scan.startAnimation(animation);
		
		AlphaAnimation animation2 = new AlphaAnimation(0.0f, 1.0f);
		animation2.setDuration(3000);
		animation2.setRepeatCount(Animation.INFINITE);
		im_dian.startAnimation(animation2);
		
		count = 0;
		
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
				
		fillData();
	}

	private void fillData() {

		tv_lodingApk.setText("开始准备释放空闲CPU线程");
		new Thread(){
			public void run() {
				Message msg = new Message();
    			msg.what = SCAN_LODING;
    			TextView tv = new TextView(getApplicationContext());
    			tv.setText("正在扫描...");
    			msg.obj = tv;
    			handler.sendMessage(msg);
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
    			pb_loding.setMax(packageInfos.size());
    			for(PackageInfo packageInfo:packageInfos){
    				count++;
    				
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
    				msg2.what = SCAN_LODING;
    				msg2.obj = ll;
    				handler.sendMessage(msg2);

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
    				pb_loding.incrementProgressBy(1);

    				//睡眠一下,忽悠用户...给他感觉好真实
    				SystemClock.sleep(100);
    			}

                //告诉handler扫描完了
    			Message msg3 = new Message();
    			msg3.what = FINSH_SCAN;
    			msg3.obj = packageInfos.size();//扫描的应用程序的个数
    			handler.sendMessage(msg3);
			};
		}.start();
		
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
