package com.njupt.safe.activity;

import com.njupt.safe.R;
import com.njupt.safe.engine.FloatWindowService;
import com.njupt.safe.view.MainAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.GridView;

public class MainActivity extends Activity {

	private MainAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Intent intent = new Intent(MainActivity.this,FloatWindowService.class);
		startService(intent);
		
		GridView gv_main_gv = (GridView) findViewById(R.id.gv_main_gv);

		mAdapter = new MainAdapter(this);
		gv_main_gv.setAdapter(mAdapter);

		gv_main_gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					Intent intent = new Intent(MainActivity.this,
							LostProtectActivity.class);
					MainActivity.this.startActivity(intent);
					break;
				case 1:
					Intent i1 = new Intent(getApplicationContext(),
							BlackNumberListActivity.class);
					startActivity(i1);
					break;
				case 2:
					Intent i2 = new Intent(getApplicationContext(),
							AppManagerActivity.class);
					startActivity(i2);
					break;

				case 3:
					Intent i3 = new Intent(getApplicationContext(),
							TaskManagerActivity.class);
					startActivity(i3);
					break;

				case 4:
					Intent i4 = new Intent(getApplicationContext(),
							TrafficManagerActivity.class);
					startActivity(i4);
					break;

				case 5:
					Intent i5 = new Intent(getApplicationContext(),
							VirusActivity1.class);
					startActivity(i5);
					break;

				case 6:
					Intent i7 = new Intent(getApplicationContext(),
							AtoolsActivity.class);
					startActivity(i7);
					break;
				case 7:
					Intent i = new Intent(MainActivity.this,
							SettingCenterActivity.class);
					MainActivity.this.startActivity(i);
					break;
				case 8:
					Intent i8 = new Intent(MainActivity.this,
							LocationActivity.class);
					MainActivity.this.startActivity(i8);
					break;
				case 9:
					Intent i9 = new Intent(MainActivity.this,
							BDVoiceTipsActivity.class);
					MainActivity.this.startActivity(i9);
					break;
				case 10:
					Intent i10 = new Intent(MainActivity.this,LightActivity.class);
					startActivity(i10);
					break;
					
				default:
					break;
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		mAdapter.notifyDataSetChanged();
	}
}
