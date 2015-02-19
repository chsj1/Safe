package com.njupt.safe.receiver;

import com.njupt.safe.engine.ProcessWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class ProcessReceiver extends AppWidgetProvider {
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		//启动服务
		Intent service = new Intent(context,ProcessWidgetService.class);
		context.startService(service);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	//当被别人移除时停止服务
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		//停止服务
		Intent service = new Intent(context,ProcessWidgetService.class);
		context.stopService(service);
	}
}
