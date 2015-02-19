package com.njupt.safe.activity;

import com.njupt.safe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1ConfigActivity extends Activity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.set1config);
	}
	
	public void next(View v){
		Intent intent = new Intent(this,Setup2ConfigActivity.class);
	    startActivity(intent);
	    finish();
	    
	    overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
	    
	}
}
