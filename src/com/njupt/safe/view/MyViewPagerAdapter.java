package com.njupt.safe.view;

import java.util.ArrayList;






import com.njupt.safe.R;
import com.njupt.safe.activity.AppManagerActivity;
import com.njupt.safe.activity.AtoolsActivity;
import com.njupt.safe.activity.BDVoiceTipsActivity;
import com.njupt.safe.activity.BlackNumberListActivity;
import com.njupt.safe.activity.LightActivity;
import com.njupt.safe.activity.LocationActivity;
import com.njupt.safe.activity.LostProtectActivity;
import com.njupt.safe.activity.MainActivity;
import com.njupt.safe.activity.SettingCenterActivity;
import com.njupt.safe.activity.TaskManagerActivity;
import com.njupt.safe.activity.TrafficManagerActivity;
import com.njupt.safe.activity.VirusActivity1;
import com.njupt.safe.activity.MainActivity1.ImageInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.AvoidXfermode;
import android.opengl.Visibility;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewPagerAdapter extends PagerAdapter {

	Vibrator vibrator;

	ArrayList<ImageInfo> data;
	Activity activity;
	LayoutParams params;

	private int currentPage;
	
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public MyViewPagerAdapter(Activity activity, ArrayList<ImageInfo> data) {
		this.activity = activity;
		this.data = data;
		vibrator = (Vibrator) activity
				.getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int index) {
		Log.v("test", index + "index");

		View view = activity.getLayoutInflater().inflate(R.layout.grid, null);
		GridView gridView = (GridView) view.findViewById(R.id.gridView1);
		gridView.setNumColumns(2);
		gridView.setVerticalSpacing(5);
		gridView.setHorizontalSpacing(5);
		gridView.setAdapter(new BaseAdapter() {

			@Override
			public int getCount() {
				return 8;
			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View item = LayoutInflater.from(activity).inflate(
						R.layout.grid_item, null);
				ImageView iv = (ImageView) item.findViewById(R.id.imageView1);
				iv.setImageResource((data.get(index * 8 + position)).imageId);
				TextView tv = (TextView) item.findViewById(R.id.msg);
				tv.setText((data.get(index * 8 + position)).imageMsg);
				
				if(index*8 + position > 9){
//					iv.setVisibility(View.INVISIBLE);
					item.setVisibility(View.INVISIBLE);
				}
				return item;
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (currentPage*8 + position) {
				case 0:
					Intent intent = new Intent(activity,
							LostProtectActivity.class);
					activity.startActivity(intent);
					break;
				case 1:
					Intent i1 = new Intent(activity,BlackNumberListActivity.class);
					activity.startActivity(i1);
					break;
				case 2:
					Intent i2 = new Intent(activity,
							AppManagerActivity.class);
					activity.startActivity(i2);
					break;

				case 3:
					Intent i3 = new Intent(activity,
							TaskManagerActivity.class);
					activity.startActivity(i3);
					break;

				case 4:
					Intent i4 = new Intent(activity,
							TrafficManagerActivity.class);
					activity.startActivity(i4);
					break;

				case 5:
					Intent i5 = new Intent(activity,
							VirusActivity1.class);
					activity.startActivity(i5);
					break;

				case 6:
					Intent i7 = new Intent(activity,
							AtoolsActivity.class);
					activity.startActivity(i7);
					break;
				case 7:
					Intent i = new Intent(activity,
							SettingCenterActivity.class);
					activity.startActivity(i);
					break;
				case 8:
					Intent i8 = new Intent(activity,
							LocationActivity.class);
					activity.startActivity(i8);
					break;
				case 9:
					Intent i9 = new Intent(activity,
							BDVoiceTipsActivity.class);
					activity.startActivity(i9);
					break;
				case 10:
					Intent i10 = new Intent(activity,LightActivity.class);
					activity.startActivity(i10);
					break;
					
					
				default:
					break;
				}
			}
		});
		
		// gridView.setOnTouchListener(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// return true;
		// }
		// });
		((ViewPager) container).addView(view);

		return view;
	}
}
