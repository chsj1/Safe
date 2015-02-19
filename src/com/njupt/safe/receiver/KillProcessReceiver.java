package com.njupt.safe.receiver;

import com.njupt.safe.utils.TaskUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println(" 得到广播  ，清理进程");
		TaskUtil.killAllProcess(context);

	}

}
