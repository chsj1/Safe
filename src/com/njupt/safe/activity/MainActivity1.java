package com.njupt.safe.activity;

import java.util.ArrayList;

import com.njupt.safe.R;
import com.njupt.safe.view.MyViewPagerAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.Menu;

/**
 * chw
 */

public class MainActivity1 extends Activity {

	ArrayList<ImageInfo> data;

	PageListener pageListener;

	private int currentPage;
	private MyViewPagerAdapter pagerAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);

		initData();

		ViewPager vpager = (ViewPager) findViewById(R.id.vPager);
		pagerAdapter = new MyViewPagerAdapter(MainActivity1.this, data);
		vpager.setAdapter(pagerAdapter);
		vpager.setPageMargin(50);

		pageListener = new PageListener();
		vpager.setOnPageChangeListener(pageListener);
	}

	private class PageListener extends SimpleOnPageChangeListener {
		public void onPageSelected(int position) {
			currentPage = position;
			
			System.out.println("--------->onPageSelected: " + currentPage);
			
			pagerAdapter.setCurrentPage(currentPage);
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			super.onPageScrolled(position, positionOffset, positionOffsetPixels);
			
			System.out.println("-------->onPageScrolled: " + currentPage);
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
			super.onPageScrollStateChanged(state);
			
			System.out.println("-------->onPageScrollStateChanged: " + currentPage);
		}
	}

	private void initData() {
		data = new ArrayList<ImageInfo>();
		data.add(new ImageInfo("手机防盗", R.drawable.widget1_2));
		data.add(new ImageInfo("通讯卫士", R.drawable.widget2));
		data.add(new ImageInfo("软件管理", R.drawable.widget3_3));
		data.add(new ImageInfo("任务管理", R.drawable.widget4));
		data.add(new ImageInfo("流量管理", R.drawable.widget5));
		data.add(new ImageInfo("手机杀毒", R.drawable.widget6_2));
		data.add(new ImageInfo("高级工具", R.drawable.widget8_1));
		data.add(new ImageInfo("设置中心",R.drawable.widget9));
		data.add(new ImageInfo("位置服务", R.drawable.widget10_2));
		data.add(new ImageInfo("云语音助手", R.drawable.widget11_2));
		data.add(new ImageInfo("手电筒", R.drawable.widget12_1));
		data.add(new ImageInfo("", R.drawable.traffic_list_unchecked, 2));
		data.add(new ImageInfo("", R.drawable.traffic_main_unchecked, 2));
		data.add(new ImageInfo("", R.drawable.traffic_main_unchecked, 2));
		data.add(new ImageInfo("", R.drawable.traffic_list_unchecked, 2));
		data.add(new ImageInfo("", R.drawable.traffic_main_unchecked, 2));
		data.add(new ImageInfo("", R.drawable.traffic_main_unchecked, 2));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public class ImageInfo {
		public String imageMsg;
		public int imageId;
		public int pageNum;

		public ImageInfo(String msg, int id) {
			imageId = id;
			imageMsg = msg;
		}

		public ImageInfo(String imageMsg, int imageId, int pageNum) {
			super();
			this.imageMsg = imageMsg;
			this.imageId = imageId;
			this.pageNum = pageNum;
		}

	}
}
