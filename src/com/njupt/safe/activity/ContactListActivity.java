package com.njupt.safe.activity;

import java.util.List;

import com.njupt.safe.R;
import com.njupt.safe.bean.ContactInfo;
import com.njupt.safe.engine.ContactInfoService;
import com.njupt.safe.view.ContactInfoAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class ContactListActivity extends Activity {
	private ListView lv_contact;
	private ContactInfoService service;
	private ContactInfoAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.contact_list);
		
		lv_contact = (ListView) findViewById(R.id.lv_contact);
		
		service = new ContactInfoService(this);
		List<ContactInfo> contacts = service.getContacts();
		
		mAdapter = new ContactInfoAdapter(this, contacts);
		lv_contact.setAdapter(mAdapter);
		lv_contact.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	private final class MyOnItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ContactInfo info = (ContactInfo) mAdapter.getItem(position);
			String number = info.getNumber();
			Intent data = new Intent();
			data.putExtra("number", number);
			setResult(200, data);
			finish();
		}
	}
}
