package com.njupt.safe.engine;

import com.njupt.safe.utils.TaskUtil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;

public class AutoClearService extends Service {

	private SharedPreferences sp;

	@Override
	public void onCreate() {
		super.onCreate();

		sp = getSharedPreferences("config", Context.MODE_PRIVATE);

		setForeground(true);//设置为前台进程,这样锁屏杀进程时就不会杀死这个服务
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		LockScreenReceiver receiver = new LockScreenReceiver();
		registerReceiver(receiver, filter);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private final class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//清理内存的操作
			boolean isautoclear = sp.getBoolean("isautoclear", false);
			if(isautoclear){
				TaskUtil.killAllProcess(context);
			}
		}

	}

}
