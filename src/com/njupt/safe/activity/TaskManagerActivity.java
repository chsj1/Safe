package com.njupt.safe.activity;

import java.util.ArrayList;
import java.util.List;

import com.njupt.safe.R;
import com.njupt.safe.bean.TaskInfo;
import com.njupt.safe.utils.MyToast;
import com.njupt.safe.utils.TaskUtil;
import com.njupt.safe.utils.TextFormat;
import com.njupt.safe.view.LoadingDialog;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskManagerActivity extends Activity {
	protected static final int SUCCESS_GET_TASKINFO = 0;

	private static final int MENU_ALL_SELECTED_ID = 0;
	private static final int MENU_CANCEL_SELECTED_ID = 1;
	private TextView tv_task_manager_task_count;
	private TextView tv_task_manager_task_memory;
	private RelativeLayout rl_loading;
	private ListView lv_taskmanager;
	private ActivityManager am;//类似一个任务管理器    系统服务
	private List<TaskInfo> taskInfos;

	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;

	private TaskManagerAdapter mAdapter;
	private SharedPreferences sp;

	private long total;

	LoadingDialog dialog;//自定义的loadingdialog
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_GET_TASKINFO:
				total = TaskUtil.getAvailMem(getApplicationContext());
				for(TaskInfo taskInfo:taskInfos){
					total = total + taskInfo.getTask_memory()* 1024;
				}
				//可用内存
				String availMemStr = TextFormat.formatByte(TaskUtil.getAvailMem(getApplicationContext()));
				//总内存
				String totalMemStr = TextFormat.formatByte(total);
				tv_task_manager_task_memory.setText("可用/总内存:" + availMemStr+"/" + totalMemStr);
				mAdapter = new TaskManagerAdapter();
				mAdapter.setInfos(taskInfos);
//				rl_loading.setVisibility(View.GONE);
				dialog.dismiss();
				
				lv_taskmanager.setAdapter(mAdapter);

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		setContentView(R.layout.task_manager);

		tv_task_manager_task_count = (TextView) findViewById(R.id.tv_task_manager_task_count);
		tv_task_manager_task_memory = (TextView) findViewById(R.id.tv_task_manager_task_memory);
//		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
		dialog = new LoadingDialog(this);
		dialog.setCancelable(false);
		
		lv_taskmanager = (ListView) findViewById(R.id.lv_taskmanager);

		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		//进程数量
		int size = TaskUtil.getRuninngAppProcessInfoSize(this);
		tv_task_manager_task_count.setText("进程数:" + size);

		dialog.show();
       new Thread(){
    	   public void run() {
    		   taskInfos = TaskUtil.getTaskInfos(getApplicationContext());
    		   userTaskInfos = new ArrayList<TaskInfo>();
    		   systemTaskInfos = new ArrayList<TaskInfo>();

    		   //分离用户进程和系统进程
    		   for(TaskInfo taskInfo:taskInfos){
    			   if(taskInfo.isUserTask()){
    				   userTaskInfos.add(taskInfo);
    			   }else{
    				   systemTaskInfos.add(taskInfo);
    			   }
    		   }

    		   Message msg = new Message();
    		   msg.what = SUCCESS_GET_TASKINFO;
    		   mHandler.sendMessage(msg);
    	   };
       }.start();

       lv_taskmanager.setOnItemClickListener(new MyOnItemClickListener());
       lv_taskmanager.setOnItemLongClickListener(new MyOnItemLongClickListener());

	}

    //长按条目激活应用程序的详情界面
    //因为这个功能系统中已经有,所以我们只需要怎么去激活它就行了,里面的逻辑有多复杂不管我们的事
	private final class MyOnItemLongClickListener implements OnItemLongClickListener{

		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			
			//激活系统的组件
/*            <intent-filter>  2.3
            <action android:name="android.settings.APPLICATION_DETAILS_SETTINGS" />
            <category android:name="android.intent.category.DEFAULT" />
            <data android:scheme="package" />
            </intent-filter>*/
			TaskInfo taskInfo = (TaskInfo) mAdapter.getItem(position);
			String packageName = taskInfo.getPackageName();
			int version = getSdkVersion();
			if(version > 8){
				Intent intent = new Intent();
				intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
				intent.setData(Uri.parse("package:" + packageName));
				startActivity(intent);
			}else{
/*	            <intent-filter> 2.2
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.VOICE_LAUNCH" />
            </intent-filter>*/
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.addCategory("android.intent.category.VOICE_LAUNCH");
				intent.putExtra("pkg", packageName);
				startActivity(intent);
			}


			return true;
		}

	}

	private final class MyOnItemClickListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			System.out.println(" posistion " + position);
			
			CheckBox cb_task_manager_selected = ((ViewHolder)view.getTag()).cb_task_manager_selected;
			TaskInfo taskInfo = (TaskInfo) mAdapter.getItem(position);

			if(taskInfo.getPackageName().equals(getPackageName())){
				return;
			}
			if(taskInfo.isChecked()){
				taskInfo.setChecked(false);
				cb_task_manager_selected.setChecked(false);
			}else{
				taskInfo.setChecked(true);
				cb_task_manager_selected.setChecked(true);
			}
		}

	}



	static class ViewHolder{
		ImageView iv_task_manager_icon;
		TextView tv_task_manager_name;
		TextView tv_task_manager_memory;
		CheckBox cb_task_manager_selected;
	}

	private final class TaskManagerAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		private List<TaskInfo> infos;


		public void setInfos(List<TaskInfo> infos) {
			this.infos = infos;
		}

		public TaskManagerAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

        //isEnabled(int position) 决定哪个位置可用哪个位置不可用
		@Override
		public boolean isEnabled(int position) {
			if(position == 0){//如果是用户进程分隔条目,则不可用
				return false;
			}else if(position == userTaskInfos.size() + 1){//如果是系统进程分隔条目,则不可用
				return false;
			}
			return super.isEnabled(position);//否则可用,因为默认就是可用
		}

		public int getCount() {
			boolean isdisplaysystem = sp.getBoolean("isdisplaysystem", true);
			if(isdisplaysystem){//如果显示系统进程
				return userTaskInfos.size() + systemTaskInfos.size() + 2;
			}else{//如果不显示系统进程,即只显示用户进程
				return userTaskInfos.size() + 1;
			}

		}

		public Object getItem(int position) {
			//如果他是分隔条目,那么返回的数据为null,否则返回该条目的信息
			if(position == 0){
				return null;
			}else if(position <= userTaskInfos.size()){
				return userTaskInfos.get(position - 1);
			}else if(position == userTaskInfos.size() + 1){
				return null;
			}else{
				return systemTaskInfos.get(position - userTaskInfos.size() - 2);
			}
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;

			if(position == 0){//用于显示用户进程的那个分隔条目
				TextView tv = new TextView(getApplicationContext());
				tv.setText("用户进程(" + userTaskInfos.size() + ")");
				tv.setTextSize(20);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}else if(position <= userTaskInfos.size()){//显示用户进程

                // !(convertView instanceof TextView) . 不将分隔条目作为缓存
                // convertView != null 如果缓存不为空
				if(convertView != null && !(convertView instanceof TextView)){
					view = convertView;
					holder = (ViewHolder) view.getTag();
				}else{
					view = mInflater.inflate(R.layout.task_manager_item, null);
					holder = new ViewHolder();
					holder.iv_task_manager_icon = (ImageView) view.findViewById(R.id.iv_task_manager_icon);
					holder.tv_task_manager_name = (TextView) view.findViewById(R.id.tv_task_manager_name);
					holder.tv_task_manager_memory = (TextView) view.findViewById(R.id.tv_task_manager_memory);
					holder.cb_task_manager_selected = (CheckBox) view.findViewById(R.id.cb_task_manager_selected);
					view.setTag(holder);
				}

				TaskInfo taskInfo = userTaskInfos.get(position -1);

				holder.iv_task_manager_icon.setImageDrawable(taskInfo.getTask_icon());
				holder.tv_task_manager_name.setText(taskInfo.getTask_name());
				holder.tv_task_manager_memory.setText("占用的内存:" + TextFormat.formatByte(taskInfo.getTask_memory()*1024));

				String packageName = taskInfo.getPackageName();
				if(packageName.equals(getPackageName())){
					holder.cb_task_manager_selected.setVisibility(View.GONE);
				}else{
					holder.cb_task_manager_selected.setVisibility(View.VISIBLE);
				}

				boolean isChecked = taskInfo.isChecked();
				if(isChecked){
					holder.cb_task_manager_selected.setChecked(true);
				}else{
					holder.cb_task_manager_selected.setChecked(false);
				}
				return view;
			}else if(position == userTaskInfos.size() + 1){//显示系统进程的那个分隔条目
				TextView tv = new TextView(getApplicationContext());
				tv.setText("系统进程(" + systemTaskInfos.size() + ")");
				tv.setTextSize(20);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}else{//显示系统进程
				if(convertView != null && !(convertView instanceof TextView)){
					view = convertView;
					holder = (ViewHolder) view.getTag();
				}else{
					view = mInflater.inflate(R.layout.task_manager_item, null);
					holder = new ViewHolder();
					holder.iv_task_manager_icon = (ImageView) view.findViewById(R.id.iv_task_manager_icon);
					holder.tv_task_manager_name = (TextView) view.findViewById(R.id.tv_task_manager_name);
					holder.tv_task_manager_memory = (TextView) view.findViewById(R.id.tv_task_manager_memory);
					holder.cb_task_manager_selected = (CheckBox) view.findViewById(R.id.cb_task_manager_selected);
					view.setTag(holder);
				}

                // systemTaskInfos注意这里不要写成infos.这时要从系统进程中取而不是从进程中体中取
				TaskInfo taskInfo = systemTaskInfos.get(position - userTaskInfos.size() - 2);

				holder.iv_task_manager_icon.setImageDrawable(taskInfo.getTask_icon());
				holder.tv_task_manager_name.setText(taskInfo.getTask_name());
				holder.tv_task_manager_memory.setText("占用的内存:" + TextFormat.formatByte(taskInfo.getTask_memory()*1024));

				String packageName = taskInfo.getPackageName();
				if(packageName.equals(getPackageName())){
					holder.cb_task_manager_selected.setVisibility(View.GONE);
				}else{
					holder.cb_task_manager_selected.setVisibility(View.VISIBLE);
				}

				boolean isChecked = taskInfo.isChecked();
				if(isChecked){
					holder.cb_task_manager_selected.setChecked(true);
				}else{
					holder.cb_task_manager_selected.setChecked(false);
				}
				return view;
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ALL_SELECTED_ID, 0, "全选");
		menu.add(0, MENU_CANCEL_SELECTED_ID, 0, "取消选择");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case MENU_ALL_SELECTED_ID:
			for(TaskInfo taskInfo:taskInfos){
				if(!taskInfo.getPackageName().equals(getPackageName())){
					taskInfo.setChecked(true);
				}
			}
			mAdapter.notifyDataSetChanged();
			break;
		case MENU_CANCEL_SELECTED_ID:
			for(TaskInfo taskInfo:taskInfos){
				taskInfo.setChecked(false);
			}
			mAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void kill_process(View v){
/*		//存放没有杀死的进程
		List<TaskInfo> newTaskInfos = new ArrayList<TaskInfo>();

		for(TaskInfo taskInfo:taskInfos){
			if(taskInfo.isChecked()){
				//把你杀死
				am.killBackgroundProcesses(taskInfo.getPackageName());

			}else{
				newTaskInfos.add(taskInfo);
			}
		}

		mAdapter.setInfos(newTaskInfos);
		mAdapter.notifyDataSetChanged();*/

		List<TaskInfo> killTaskInfos = new ArrayList<TaskInfo>();
		//判断用户进程中杀死那些进程
		for(TaskInfo taskInfo:userTaskInfos){
			if(taskInfo.isChecked()){
				//把你杀死
				am.killBackgroundProcesses(taskInfo.getPackageName());
				killTaskInfos.add(taskInfo);
			}
		}

        //判断系统进程中杀死那些进程
		for(TaskInfo taskInfo:systemTaskInfos){
			if(taskInfo.isChecked()){
				//把你杀死
				am.killBackgroundProcesses(taskInfo.getPackageName());
				killTaskInfos.add(taskInfo);
			}
		}

		//从显示列表中移除已经杀死的进程
		long totalMemory = 0;//杀死进程的所释放的总内存数
		for(TaskInfo taskInfo:killTaskInfos){
			totalMemory = totalMemory + taskInfo.getTask_memory();
			if(taskInfo.isUserTask()){
				userTaskInfos.remove(taskInfo);
			}else{
				systemTaskInfos.remove(taskInfo);
			}
		}

		mAdapter.notifyDataSetChanged();
		//显示杀死进程后的进程总数,用来忽悠用户
		tv_task_manager_task_count.setText("进程数:" + (userTaskInfos.size() + systemTaskInfos.size()));

        //修改杀死进城后的可用内存和总内存
		//可用内存
		String availMemStr = TextFormat.formatByte(TaskUtil.getAvailMem(getApplicationContext()));
		//总内存
		String totalMemStr = TextFormat.formatByte(total);
		tv_task_manager_task_memory.setText("可用/总内存:" + availMemStr+"/" + totalMemStr);

		//杀死进程之后的提示信息
		String msg = "杀死了" +killTaskInfos.size() + "个进程,清理了" + TextFormat.formatByte(totalMemory * 1024) + "内存";
		MyToast.show(this, msg);
	}


    //程序设置的功能
	public void displaysetup(View v){
		Intent intent = new Intent(this,TaskManagerSettingActivity.class);
		//用来处理那种一设置回来,就只显示用户进程的情况
		startActivityForResult(intent, 100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100){
			mAdapter.notifyDataSetChanged();//刷新界面
		}
	}

	//得到当前系统的sdk的版本号
	private int getSdkVersion(){
		return android.os.Build.VERSION.SDK_INT;
	}

}
