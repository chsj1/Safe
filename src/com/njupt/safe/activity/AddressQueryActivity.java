package com.njupt.safe.activity;

import java.io.File;

import com.njupt.safe.R;
import com.njupt.safe.engine.AddressQueryService;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddressQueryActivity extends Activity {
	
	private EditText et_number;
	private TextView tv_info;
	private AddressQueryService serivce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.address_query);
		
		et_number = (EditText) findViewById(R.id.et_number);
		tv_info = (TextView) findViewById(R.id.tv_info);
		
		serivce = new AddressQueryService();
	}
	
	public void query(View v){
		String number = et_number.getText().toString();
		if("".equals(number)){
			Toast.makeText(this, "查询的号码不能为空", 1).show();
		}else{
			//查询
			boolean isExist = serivce.isExist();
//			File file = new File(getFilesDir(), "address.db");
//			boolean isExist = file.exists();
			if(!isExist){
				Toast.makeText(this, "归属地数据库不存在", 1).show();
			}else{
				String info = serivce.query(number);
				tv_info.setText(info);
			}
		}
	}
}
