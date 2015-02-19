package com.njupt.safe.engine;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.njupt.safe.R;
import com.njupt.safe.activity.BlackNumberListActivity;
import com.njupt.safe.db.BlackNumberDao;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog.Calls;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class BlackNumberService extends Service {

	private TelephonyManager tm;
	private MyPhoneStateListener listener;
	private BlackNumberDao blackNumberDao;
	private SharedPreferences sp;
	private NotificationManager nm;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		listener = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		blackNumberDao = new BlackNumberDao(this);
		
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
	}
	
	private final class MyPhoneStateListener extends PhoneStateListener{
		private long startTime = 0;
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				boolean isblackstart = sp.getBoolean("isblacknumber", false);
				if(isblackstart){
					boolean isBlackNumber = blackNumberDao.isBlackNumber(incomingNumber);
					
					if(isBlackNumber){
						endCall(incomingNumber);
						
						return ;
					}
				}
				
				startTime = System.currentTimeMillis();
				
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				break;
				
			case TelephonyManager.CALL_STATE_IDLE:
				long endTime = System.currentTimeMillis();
				
				if(endTime - startTime < 3000){
					
					Notification notification = new Notification(R.drawable.black_phone,"拦截到来电一声响",System.currentTimeMillis());
					Intent intent = new Intent(getApplicationContext(),BlackNumberListActivity.class);
					intent.putExtra("number", incomingNumber);
					PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
				
					notification.setLatestEventInfo(getApplicationContext(), "来电一声响", "拦截到来电一声响", contentIntent);
					notification.flags = notification.flags = Notification.FLAG_AUTO_CANCEL;
					nm.notify(100, notification);
					
				}
				
				break;
			default:
				break;
			}
		}
	}
	
	private void endCall(String incomingNumber){
		try {
			Class clazz = Class.forName("android.os.ServiceManager");
			Method method = clazz.getMethod("getService", String.class);
			IBinder ibinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
			ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);
			iTelephony.endCall();
			
			//删除通话记录 通话记录的保存是一个异步的操作，需要使用ContentObserver技术来实现
			Uri uri = Calls.CONTENT_URI;
			getContentResolver().registerContentObserver(uri, true, new MyContentObserver(new Handler(),incomingNumber));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private final class MyContentObserver extends ContentObserver{

		private String incomingNumber;
		public MyContentObserver(Handler handler, String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}
		
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Uri uri = Calls.CONTENT_URI;
			String where = Calls.NUMBER + " = ?";
			String[] selectionArgs = new String[]{incomingNumber};
			getContentResolver().delete(uri, where, selectionArgs);
			
			//解除监听
			getContentResolver().unregisterContentObserver(this);
		}
	}

	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	    
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
	}
}
