package com.njupt.safe.engine;

import com.njupt.safe.activity.LocationActivity;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;

public class ShakeShowLocationService extends Service implements SensorEventListener{

	private SensorManager mSensorManager;
	private Vibrator vibrator;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		System.out.println("----->>safe:ShakeShowLocationService服务启动...");
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		
		
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		mSensorManager.unregisterListener(this);
	}
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		
		int sensorType = event.sensor.getType();
		float[] values = event.values;
		
		if(sensorType == Sensor.TYPE_ACCELEROMETER){
			
			
			if(Math.abs(values[0] )> 14 &&Math.abs(values[1] )> 14 &&Math.abs(values[2] )> 14  ){
				
				System.out.println("safe:晃动了手机...2");
				vibrator.vibrate(500);
				
				//以下演示在service中调用activity的方法
//				Intent intent = new Intent(getApplicationContext(),LocationActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 关键之处
//				startActivity(intent);
				
				Intent intent = new Intent();
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //关键之处
				intent.setAction(Intent.ACTION_DIAL);
				startActivity(intent);
			}
			
			
		}
		
	}
	
	

}
