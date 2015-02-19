package com.njupt.safe.view;

import java.util.List;

import com.njupt.safe.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BlackNumberAdapter extends BaseAdapter {

	private Context context;
	private List<String> blacknumbers;
	private LayoutInflater mInflater;
	
	public BlackNumberAdapter(Context context ,List<String> blacknumbers) {
		this.context = context;
		this.blacknumbers = blacknumbers;
		
		mInflater = LayoutInflater.from(context);
	}
	
	
	public List<String> getBlacknumbers() {
		return blacknumbers;
	}

	public void setBlackNumbers(List<String> blacknumbers){
		this.blacknumbers = blacknumbers;
	}
	
	@Override
	public int getCount() {
		return blacknumbers.size();
	}

	@Override
	public Object getItem(int position) {
		return blacknumbers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = null;
		if(convertView != null){
			view = convertView;
		}else{
			view = mInflater.inflate(R.layout.blacknumber_item, null);
		}
		
		TextView tv_blacknumber = (TextView) view.findViewById(R.id.tv_blacknumber);
		
		String blacknumber = blacknumbers.get(position);
		tv_blacknumber.setText(blacknumber);
		
		return view;
	}

}
