package com.njupt.safe.activity;

import com.njupt.safe.R;
import com.njupt.safe.engine.Const;
import com.njupt.safe.engine.SavePreference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class CopyOfHighToolsActivity extends Activity implements OnClickListener{
	private CopyOfHighToolsActivity TAG = CopyOfHighToolsActivity.this;
	private CheckBox cb_setting_ceneter_auto_ipcall;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.hightool);
	    
	    sp = getSharedPreferences("config", Context.MODE_PRIVATE);
	    
	    final CheckBox cb_is_updata = (CheckBox) findViewById(R.id.cb_is_updata);
	    cb_is_updata.setChecked(SavePreference.getBoolean(TAG, Const.ISUPDATA));
	    cb_is_updata.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(cb_is_updata.isChecked()){
					SavePreference.save(TAG, Const.ISUPDATA, true);
				}else {
					SavePreference.save(TAG, Const.ISUPDATA, false);
				}
			}
		});
	    
	    cb_setting_ceneter_auto_ipcall = (CheckBox) findViewById(R.id.cb_setting_ceneter_auto_ipcall);
	    cb_setting_ceneter_auto_ipcall.setOnClickListener(this);
	    boolean isautoipcall = sp.getBoolean("isautoipcall", false);
	    if(isautoipcall){
	    	cb_setting_ceneter_auto_ipcall.setChecked(true);
	    	cb_setting_ceneter_auto_ipcall.setText("ip拨号已开启");
	    }else{
	    	cb_setting_ceneter_auto_ipcall.setChecked(false);
	    	cb_setting_ceneter_auto_ipcall.setText("ip拨号已关闭");
	    }
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.cb_setting_ceneter_auto_ipcall:
			boolean isautoipcall = sp.getBoolean("isautoipcall", false);
			if(isautoipcall){
				cb_setting_ceneter_auto_ipcall.setChecked(false);
				cb_setting_ceneter_auto_ipcall.setText("ip拨号已关闭");
				
				Editor editor = sp.edit();
				editor.putBoolean("isautoipcall", false);
				editor.commit();
			}else{
				cb_setting_ceneter_auto_ipcall.setChecked(true);
				cb_setting_ceneter_auto_ipcall.setText("ip拨号已开启");
				
				Editor editor = sp.edit();
				editor.putBoolean("isautoipcall", true);
				editor.commit();
			}
			break;

		default:
			break;
		}
	}
}
