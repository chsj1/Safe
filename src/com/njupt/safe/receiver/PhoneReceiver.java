package com.njupt.safe.receiver;

import com.njupt.safe.activity.LostProtectActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class PhoneReceiver extends BroadcastReceiver {
	
	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("拦截到外拨电话");
		
		String number = getResultData();
		if("20122012".equals(number)){
			setResultData(null);
			
			Intent i = new Intent(context,LostProtectActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}else{
			sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
			boolean isautoipcall = sp.getBoolean("isautoipcall", false);
			if(isautoipcall){
				String ip_number = sp.getString("ip_number", "");
				setResultData(ip_number + number); 
			}
		}
	}

}
