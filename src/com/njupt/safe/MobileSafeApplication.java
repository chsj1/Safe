package com.njupt.safe;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;

import android.app.Application;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

public class MobileSafeApplication extends Application {

	private String packname;
	
	
	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}



	@Override
	public void onCreate() {
		
		super.onCreate();
	}

	
	
}
