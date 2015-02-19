package com.njupt.safe.activity;

import com.njupt.safe.R;
import com.njupt.safe.engine.Const;
import com.njupt.safe.engine.SavePreference;
import com.njupt.safe.utils.MD5;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LostProtectActivity extends Activity {
	private SharedPreferences sp;
	private LayoutInflater mInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		String pwd = sp.getString("pwd", "");
		if("".equals(pwd)){
			//第一次进入手机防盗界面
			showFirstEntryDialog();
		}else{
			//正常的进入手机防盗界面
			showNormalEntryDialog();
		}
		
	}


	//正常的进入手机防盗界面
	private void showNormalEntryDialog() {
		View view = mInflater.inflate(R.layout.normal_entry_dialog, null);
		final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd_normal_entry_dialog);
		Button bt_ok = (Button) view.findViewById(R.id.bt_ok_normal_entry_dialog);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel_normal_entry_dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请输入密码");
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		
		
       
		dialog.show();
		dialog.getWindow().setLayout(400, 500); //这一句一定要有且一定要放在dialog.show()的后面,否则对话框只有那么一小点...
		
		
		
		bt_ok.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				String pwd = et_pwd.getText().toString();
//				String md5_pwd = MD5.getData(pwd);
				String md5_pwd = pwd;
				String old_pwd = sp.getString("pwd", "");
				if("".equals(pwd)){
					Toast.makeText(getApplicationContext(), "密码不能为空", 1).show();
				}else if(old_pwd.equals(md5_pwd)){
					//进入手机防盗的向导界面
					loadGuideUI();
					dialog.dismiss();
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "您输入的密码不正确", 1).show();
				}
			}
		});
		bt_cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});
	}



	protected void loadGuideUI() {
		boolean issetup = sp.getBoolean("issetup", false);
		if(issetup){
			Intent intent = new Intent(this,LostProtectedSettingActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(this,SetupConfigActivity.class);
		    startActivity(intent);
		}
	}


	//第一次进入手机防盗界面
	private void showFirstEntryDialog() {
		View view = mInflater.inflate(R.layout.first_entry_dialog, null);
		final EditText et_pwd_first_entry_dialog = (EditText) view.findViewById(R.id.et_pwd_first_entry_dialog);
		final EditText et_repwd_first_entry_dialog = (EditText) view.findViewById(R.id.et_repwd_first_entry_dialog);
		Button bt_ok_first_entry_dialog = (Button) view.findViewById(R.id.bt_ok_first_entry_dialog);
		Button bt_cancel_first_entry_dialog = (Button) view.findViewById(R.id.bt_cancel_first_entry_dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle("输入密码");
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getWindow().setLayout(400, 500); //这一句一定要有且一定要放在dialog.show()的后面,否则对话框只有那么一小点...
		
		
		bt_ok_first_entry_dialog.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				String pwd = et_pwd_first_entry_dialog.getText().toString();
				String repwd = et_repwd_first_entry_dialog.getText().toString();
				if("".equals(pwd)||"".equals(repwd)){
					Toast.makeText(getApplicationContext(), "密码不能为空", 1).show();
				}else if(pwd.equals(repwd)){
					//对密码进行加密
//					String md5_pwd = MD5.getData(pwd);
					String md5_pwd = pwd;
					//密码相同保存密码
					Editor editor = sp.edit();
					editor.putString("pwd", md5_pwd);
					editor.commit();
					
					dialog.dismiss();
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "两次输入的密码不相同", 1).show();
				}
				
			}
		});
		bt_cancel_first_entry_dialog.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});
	}
	
	
}
