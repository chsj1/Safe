package com.njupt.safe.engine;

import java.util.Timer;
import java.util.TimerTask;

import com.njupt.safe.R;
import com.njupt.safe.receiver.KillProcessReceiver;
import com.njupt.safe.receiver.ProcessReceiver;
import com.njupt.safe.utils.TaskUtil;
import com.njupt.safe.utils.TextFormat;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class ProcessWidgetService extends Service {

	private Timer timer;
	private TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			//得到widget的管理器
			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
			ComponentName provider = new ComponentName(getApplicationContext(), ProcessReceiver.class);
			RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
			int process_count = TaskUtil.getRuninngAppProcessInfoSize(getApplicationContext());
			views.setTextViewText(R.id.process_count, "正在运行软件:" + process_count);
			String process_memory = TextFormat.formatByte(TaskUtil.getAvailMem(getApplicationContext()));
			views.setTextViewText(R.id.process_memory, "可用内存:" + process_memory);
			Intent intent = new Intent(getApplicationContext(),KillProcessReceiver.class);
			intent.setAction("cn.itcast.action.killprocess");
			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, 0);
			views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
			appWidgetManager.updateAppWidget(provider, views);
		}
	};
	@Override
	public void onCreate() {
		super.onCreate();
		//不断的对widget里面的数据进行更新   用定时器来实现
		timer = new Timer();
		timer.schedule(task, 0, 1000);
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		task = null;
	}
}
