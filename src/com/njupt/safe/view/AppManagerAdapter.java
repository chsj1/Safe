package com.njupt.safe.view;

import java.util.List;

import com.njupt.safe.R;
import com.njupt.safe.bean.AppInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppManagerAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<AppInfo> appInfos;
	
	
	public void setAppInfos(List<AppInfo> appInfos) {
		this.appInfos = appInfos;
	}

	public AppManagerAdapter(Context context,List<AppInfo> appInfos) {
		this.context = context;
		this.appInfos = appInfos;
		mInflater = LayoutInflater.from(context);
	}
	
	public int getCount() {
		return appInfos.size();
	}

	public Object getItem(int position) {
		return appInfos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		//1 得到控件
		//2 得到数据
		//3 绑定数据
		View view = null;
		if(convertView != null){
			view = convertView;
		}else{
			view = mInflater.inflate(R.layout.applationinstall_item, null);
		}
		
		ImageView iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
		TextView tv_appname = (TextView) view.findViewById(R.id.tv_appname);
		TextView tv_appversion = (TextView) view.findViewById(R.id.tv_appversion);
		
		AppInfo appInfo = appInfos.get(position);
		
		iv_appicon.setImageDrawable(appInfo.getApp_icon());
		String name = appInfo.getApp_name();
		if(name.length() >18){
			name = name.substring(0,15) + "...";
		}
		tv_appname.setText(name);
		tv_appversion.setText(appInfo.getApp_version());
		return view;
	}
}
