package com.njupt.safe.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.njupt.safe.R;
import com.njupt.safe.db.BlackNumberDao;
import com.njupt.safe.view.BlackNumberAdapter;
import com.njupt.safe.view.SlideCutListView;
import com.njupt.safe.view.SlideCutListView.RemoveDirection;
import com.njupt.safe.view.SlideCutListView.RemoveListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BlackNumberListActivity extends Activity implements OnClickListener,RemoveListener{
	private static final int MENU_UPDATE_ID = 0;
	private static final int MENU_DELETE_ID = 1;
	
	private TextView tv_add_blacknumber;
	private TextView empty;
	private View view;
	private LayoutInflater mInflater;
	private EditText et_number_blacknumber_dialog;
	private Button bt_ok_blacknumber_dialog;
	private Button bt_cancel_blacknumber_dialog;
	private BlackNumberDao blackNumberDao;
	private AlertDialog dialog;
	private BlackNumberAdapter mAdapter;
	
	private ArrayAdapter<String> adapter;
	List<String> blacknumbers;
	
	private int flag = 0;
	private final static int ADD = 1;
	private final static int UPDATE = 2;
	private String blacknumber;
	
	private SlideCutListView slideCutListView ;//用来实现滑动删除功能
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.blacknumber_list);
		
		blacknumber = getIntent().getStringExtra("number");
		
		tv_add_blacknumber = (TextView) findViewById(R.id.tv_add_blacknumber);
		empty = (TextView) findViewById(R.id.empty);
		
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.add_blacknumber_dialog, null);
		et_number_blacknumber_dialog = (EditText) view.findViewById(R.id.et_number_blacknumber_dialog);
		bt_ok_blacknumber_dialog = (Button) view.findViewById(R.id.bt_ok_blacknumber_dialog);
		bt_cancel_blacknumber_dialog = (Button) view.findViewById(R.id.bt_cancel_blacknumber_dialog);
		
		
		bt_ok_blacknumber_dialog.setOnClickListener(this);
		bt_cancel_blacknumber_dialog.setOnClickListener(this);
		
		
		
		blackNumberDao = new BlackNumberDao(this);
		blacknumbers = blackNumberDao.findAll();
		mAdapter = new BlackNumberAdapter(this, blacknumbers);
		
		tv_add_blacknumber.setOnClickListener(this);
		
		
		
		if(blacknumber != null){
			boolean isBlackNumber = blackNumberDao.isBlackNumber(blacknumber);
			if(!isBlackNumber){
				ViewGroup parent = (ViewGroup) view.getParent();
				if(parent != null){
					parent.removeAllViews();
				}
				
				et_number_blacknumber_dialog.setText(blacknumber);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("添加黑名单");
				builder.setView(view);
				dialog = builder.create();
				dialog.show();
				
				flag = ADD;
			}
		}
		
		init();
	}
	
	private void init() {
		slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);
		slideCutListView.setRemoveListener(this);
		
		
		slideCutListView.setAdapter(mAdapter);
		
//		registerForContextMenu(slideCutListView);
		slideCutListView.setEmptyView(empty);//当listview没有数据的显示内容
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	    
		blacknumber = intent.getStringExtra("number");
		
		if(blacknumber != null){
			boolean isBlackNumber = blackNumberDao.isBlackNumber(blacknumber);
			if(!isBlackNumber){
				ViewGroup parent = (ViewGroup) view.getParent();
				if(parent != null){
					parent.removeAllViews();
				}
				
				et_number_blacknumber_dialog.setText(blacknumber);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("添加黑名单");
				builder.setView(view);
				dialog = builder.create();
				dialog.show();
				
				flag = ADD;
			}
		}
		
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add(0,MENU_UPDATE_ID,0,"更新黑名单");
		menu.add(0, MENU_DELETE_ID, 0, "删除黑名单");
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = acmi.position;
		blacknumber = (String) mAdapter.getItem(position);
		int id = item.getItemId();
		
		switch (id) {
		case MENU_UPDATE_ID:
			ViewGroup parent = (ViewGroup) view.getParent();
			if(parent != null){
				parent.removeAllViews();
			}
			
			et_number_blacknumber_dialog.setText(blacknumber);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("更新黑名单");
			builder.setView(view);
			dialog = builder.create();
			dialog.show();
			
			flag = UPDATE;
			break;

		case MENU_DELETE_ID:
			blackNumberDao.delete(blacknumber);
			mAdapter.setBlackNumbers(blackNumberDao.findAll());
			mAdapter.notifyDataSetChanged();
			break;
			
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_add_blacknumber:
			ViewGroup parent = (ViewGroup) view.getParent();
			
			if(parent != null){
				parent.removeAllViews();
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("添加黑名单");
			builder.setView(view);
			dialog = builder.create();
			dialog.show();
			dialog.getWindow().setLayout(400, 500); //这一句一定要有且一定要放在dialog.show()的后面,否则对话框只有那么一小点...
			
			flag = ADD;
			
			break;

		case R.id.bt_ok_blacknumber_dialog:
			String number = et_number_blacknumber_dialog.getText().toString();
			
			/**
			 * 用来判断输入的号码中是否包含汉字
			 */
			Pattern pattern = Pattern.compile("[\u4E00-\u9FA5]");
			 Matcher matc = pattern.matcher(number);
			
			if("".equals(number)){
				Toast.makeText(this, "黑名单号码不能为空", 1).show();
			}else if(matc.find()){
				Toast.makeText(this, "输入的号码中不能包含汉字", 1).show();
			}else{
				boolean isBlackNumber = blackNumberDao.isBlackNumber(number);
				System.out.println("isBlackNumber: " + isBlackNumber);
				
				if(isBlackNumber){
					Toast.makeText(this, "号码已经存在于黑名单中", 1).show();
				}else{
					if(flag == ADD){
						blackNumberDao.add(number);
						Toast.makeText(this, "黑名单号码添加成功", 1).show();
					}else{
						int _id = blackNumberDao.queryId(blacknumber);
						blackNumberDao.update(_id, number);
						
						Toast.makeText(this, "黑名单号码修改成功", 1).show();
					}
					dialog.dismiss();
					
					blacknumbers = blackNumberDao.findAll();
					mAdapter.setBlackNumbers(blacknumbers);
					mAdapter.notifyDataSetChanged();
				}
			}
			
			break;
		case R.id.bt_cancel_blacknumber_dialog:
			dialog.dismiss();
			break;
			
		default:
			break;
		}
		
	}
	
	@Override
	public void removeItem(RemoveDirection direction, int position) {
		blackNumberDao.delete(mAdapter.getItem(position).toString());
		mAdapter.setBlackNumbers(blackNumberDao.findAll());
		mAdapter.notifyDataSetChanged();
		
		switch (direction) {
		case RIGHT:
			break;
		case LEFT:
			break;

		default:
			break;
		}
		
	}	
}
