package com.njupt.safe.engine;

import java.util.List;

import com.njupt.safe.R;
import com.njupt.safe.activity.MainActivity;
import com.njupt.safe.bean.SmsInfo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class BackupSmsService extends Service {

	private SmsInfoService smsInfoService;
	private NotificationManager nm;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		smsInfoService = new SmsInfoService(this);
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		new Thread(){
			public void run() {
				//1 得到所有的短信
				//2 生成一个xml文件
				List<SmsInfo> smsInfos = smsInfoService.getSmsInfos();
				
				try {
					smsInfoService.createXml(smsInfos);
					//发送一个通知告诉用户备份完成
					Notification notification = new Notification(R.drawable.sms, "短信备份完毕", System.currentTimeMillis());
					Intent intent = new Intent(getApplicationContext(),MainActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, 0);
					notification.setLatestEventInfo(getApplicationContext(), "提示信息", "短信备份完毕", contentIntent);
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					nm.notify(100, notification);
				} catch (Exception e) {
					e.printStackTrace();
					//looper是一个消息泵,从消息队列（MessageQueue）里面抽取消息,把消息交给Handler处理
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "短信备份失败", 0).show();
					Looper.loop();
				}
				stopSelf();//停止服务
			};
		}.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


}
