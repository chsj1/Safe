package com.njupt.safe.view;

import java.util.List;

import com.njupt.safe.R;
import com.njupt.safe.bean.ContactInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactInfoAdapter extends BaseAdapter {

	private Context context;
	private List<ContactInfo> contacts;
	private LayoutInflater mInflater;
	
	public ContactInfoAdapter(Context context,List<ContactInfo> contacts) {
		this.context = context;
		this.contacts = contacts;
		mInflater = LayoutInflater.from(context);
	}
	
	
	@Override
	public int getCount() {
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = mInflater.inflate(R.layout.contact_item, null);
		
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
		
		ContactInfo info = contacts.get(position);
		
		tv_name.setText(info.getName());
		tv_number.setText(info.getNumber());
		
		
		return view;
	}

}
