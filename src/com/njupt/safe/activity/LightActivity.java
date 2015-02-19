package com.njupt.safe.activity;


import com.njupt.safe.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LightActivity extends Activity {

	private ToggleButton tb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.light);
		
		tb = (ToggleButton) this.findViewById(R.id.toggleButton1);
		tb.setOnCheckedChangeListener(new ToggleListener());
	}

	private class ToggleListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				SetBright(1.0F);
				tb.setBackgroundResource(R.drawable.w_screen_on);
			} else {
				SetBright(0.1F);
				tb.setBackgroundResource(R.drawable.w_screen_off);
			}
		}
	}

	private void SetBright(float light) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = light;
		getWindow().setAttributes(lp);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
