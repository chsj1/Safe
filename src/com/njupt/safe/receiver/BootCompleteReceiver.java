package com.njupt.safe.receiver;

import com.njupt.safe.engine.AutoClearService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("已经拦截到开机广播");

		// 一开机就启动锁屏杀死进程的服务
		Intent i = new Intent(context, AutoClearService.class);
		context.startService(i);

		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

		boolean isprotected = sp.getBoolean("isprotected", false);
		if (isprotected) {
			System.out.println("isprotected: " + isprotected);
			String sim_serial = tm.getSimSerialNumber();
			String old_sim_serial = sp.getString("sim_serial", "");
			System.out.println("old_sim_serial: " + old_sim_serial);
			System.out.println("sim_serial: " + sim_serial);
			
			if(!("".equals(old_sim_serial))){
				System.out.println("sim卡报警已开启");
				
				if (!sim_serial.equals(old_sim_serial)) {
					System.out.println("你的手机已经被盗");

					String safe_number = sp.getString("safe_number", "");
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safe_number, null,
							"sim card yijing bei genghuan ..rufei benren caozuo nide shouji yijing beidaole ..sorry.....", null, null);
				}
			}
			
		}
	}

}
