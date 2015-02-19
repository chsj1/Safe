package com.njupt.safe.receiver;

import com.njupt.safe.R;
import com.njupt.safe.activity.LostProtectActivity;
import com.njupt.safe.db.BlackNumberDao;
import com.njupt.safe.engine.GPSInfoService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CopyOfSmsReciever_GOOGLE2Baidu extends BroadcastReceiver {
	private SharedPreferences sp;
	private DevicePolicyManager devicePolicyManager;
	private static MediaPlayer mediaPlayer;
	private BlackNumberDao blackNumberDao;
	
	
	public static MediaPlayer getMediaPlayerInstance(Context context){
		if(mediaPlayer == null){
			mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
		}
		mediaPlayer.setVolume(1.0f, 1.0f);
		mediaPlayer.setLooping(true);
		
		return mediaPlayer;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("拦截到短信..");
		
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		blackNumberDao = new BlackNumberDao(context);
		
		boolean isprotected = sp.getBoolean("isprotected", false);
		if(isprotected){
			devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for(Object pdu : pdus){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
				String body = smsMessage.getDisplayMessageBody();
				
				String address = smsMessage.getDisplayOriginatingAddress();
				
				String safe_number = sp.getString("safe_number", "");
				
				if("#*location*#".equals(body)){
					GPSInfoService service = GPSInfoService.getInstance(context);
					service.registenerLocationChangeListener();
					String last_location = service.getLastLocation();
					
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safe_number, null, "shou jide weizhi shi :" + last_location, null, null);
					
					abortBroadcast();
					
					
				}else if("#*lockscreen*#".equals(body)){
					devicePolicyManager.lockNow();
					devicePolicyManager.resetPassword("123321", 0);
					
					abortBroadcast();
				}else if("#*delete*#".equals(body)){
					devicePolicyManager.wipeData(0);
					
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					mediaPlayer = getMediaPlayerInstance(context);
					
					
					mediaPlayer.start();
					
					abortBroadcast();
				}else if(body.contains("6+1") || body.contains("cctv")){
					abortBroadcast();
				}else if("#*stopalarm*#".equals(body)){
					mediaPlayer = getMediaPlayerInstance(context);
					
					if(mediaPlayer.isLooping() || mediaPlayer.isPlaying()){
						mediaPlayer.pause();
					}
					
					abortBroadcast();
				}
				
				System.out.println("收到的短信的内容是: " + body );
				
				boolean isBlackNumber = blackNumberDao.isBlackNumber(address);
			    if(isBlackNumber){
			    	abortBroadcast();
			    }
			}
		}
	}
}
