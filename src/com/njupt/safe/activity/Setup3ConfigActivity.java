package com.njupt.safe.activity;

import com.njupt.safe.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3ConfigActivity extends Activity {
	private EditText et_safe_number;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.set3config);
		
		et_safe_number = (EditText) findViewById(R.id.et_safe_number);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		String safe_number = sp.getString("safe_number", "");
		
		if("".equals(safe_number)){
			
		}else{
			et_safe_number.setText(safe_number);
			
		}
	}
	
	public void select_contact(View v){
		Intent intent = new Intent(this,ContactList1Activity.class);
		startActivityForResult(intent, 100);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
	    
		System.out.println("-------->返回来了...");
		if(requestCode == 100){
			if(data != null){
				String number = data.getStringExtra("number");
				System.out.println("------->获取到的number是: " + number);
				et_safe_number.setText(number);
			}
			
			String number = sp.getString("select_number", "");
			et_safe_number.setText(number);
		}
	}
	
	public void pre(View v){
		Intent intent = new Intent(this,Setup2ConfigActivity.class);
		startActivity(intent);
		finish();
		
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	}
	
	public void next(View v){
		String safe_number = et_safe_number.getText().toString();
		if("".equals(safe_number)){
			Toast.makeText(this, "安全号码不能为空", 1).show();
		}else{
			Editor editor = sp.edit();
			editor.putString("safe_number", safe_number);
			editor.commit();
			/**
			Intent intent = new Intent(this,Setup4ConfigActivity.class);
			startActivity(intent);
			finish();
			
			overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
		    */
		}
	}
}
