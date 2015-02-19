package com.njupt.safe.view;

import com.njupt.safe.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private SharedPreferences sp;
	
	public MainAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	    
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		
	}
	
	private String[] textArray = new String[]{
		"手机防盗",
		"通讯卫士",
		"软件管理",
		"任务管理",
		"流量管理",
		"手机杀毒",
		
		"高级工具",
		"设置中心",
		"位置服务",
		 "云语音助手",
		 "手电筒"
	};
	private int[] iconArray = new int[]{
			R.drawable.widget1_2,
			R.drawable.widget2,
			R.drawable.widget3_3,
			R.drawable.widget4,
			R.drawable.widget5,
			R.drawable.widget6_2,
			
			R.drawable.widget8_1,
			R.drawable.widget9,
			R.drawable.widget10_2,
			R.drawable.widget11_2,
			R.drawable.widget12_1
	};
	
	@Override
	public int getCount() {
		return textArray.length;
	}

	@Override
	public Object getItem(int position) {
		return textArray[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = inflater.inflate(R.layout.main_item, null);
		ImageView iv_main = (ImageView) view.findViewById(R.id.iv_main);
		TextView tv_main = (TextView) view.findViewById(R.id.tv_main);
		iv_main.setImageResource(iconArray[position]);
		

		if(position == 0){
			String name = sp.getString("name", textArray[position]);
			tv_main.setText(name);
		}else{
			tv_main.setText(textArray[position]);
		}
		
		return view;
	}

}
