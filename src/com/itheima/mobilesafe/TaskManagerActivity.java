package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.domain.TaskInfo;
import com.itheima.mobilesafe.engine.TaskInfoProvider;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TaskManagerActivity extends Activity {

	private TextView tv_process_count, tv_mem_info;
	// 正在运行的进程数量
	private int runningProcessCount;
	// 手机剩余可用内存空间
	private long availRam;
	private ListView lv_task_manager;
	private List<TaskInfo> taskInfos;
	// 用户进程
	private List<TaskInfo> userTaskInfos;
	// 系统进程
	private List<TaskInfo> systemTaskInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//
		userTaskInfos= new ArrayList<TaskInfo>();
		systemTaskInfos= new ArrayList<TaskInfo>();
		//
		setContentView(R.layout.activity_task_manager);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
		// 获取手机进程数,可用内存空间
		runningProcessCount = SystemInfoUtils.getRunningProcessCount(this);
		availRam = SystemInfoUtils.getAvailRam(this);
		tv_process_count.setText("正在运行的" + runningProcessCount + "个");
		tv_mem_info.setText("剩余内存:" + Formatter.formatFileSize(this, availRam));

		lv_task_manager = (ListView) findViewById(R.id.lv_task_manager);
		//checkbox点击事件
		lv_task_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = lv_task_manager.getItemAtPosition(position);//调用adapter.getItem
				if(obj!=null){
					TaskInfo ti =(TaskInfo)obj;
					ti.setChecked(!ti.isChecked());//相反勾选
					//通知更新
					adapter.notifyDataSetChanged();
				}
			}
		});
		// 开启子线程获取数量
		fillData();

	}
private TaskManagerAdapter adapter;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
			adapter= new TaskManagerAdapter();
			lv_task_manager.setAdapter(adapter);
		};
	};

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				taskInfos = TaskInfoProvider
						.getTaskInfos(TaskManagerActivity.this);
				//分类
				for(TaskInfo ti:taskInfos){
					if(ti.isUserTask()){
						userTaskInfos.add(ti);
					}else{
						systemTaskInfos.add(ti);
					}
				}
				handler.sendEmptyMessage(0);// 发空消息
			};
		}.start();
	}

	private LinearLayout ll_loading;

	class TaskManagerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userTaskInfos.size()+systemTaskInfos.size()+2;//额外两个标签
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TaskInfo taskInfo ;
			if(position==0){
				TextView tv=new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("用户进程:"+userTaskInfos.size()+"个");
				return tv;
			}else if(position==(userTaskInfos.size()+1)){
				TextView tv=new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("系统进程:"+systemTaskInfos.size()+"个");
				return tv;
			}else if(position<=userTaskInfos.size()){//用户进程
				taskInfo = userTaskInfos.get(position-1);
			}else{//系统进程
				taskInfo = systemTaskInfos.get(position-1-userTaskInfos.size()-1);
			}
			
			View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.list_task_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_task_icon);
				holder.tv_name = (TextView) view
						.findViewById(R.id.tv_task_name);
				holder.tv_mem = (TextView) view.findViewById(R.id.tv_task_mem);
				holder.cb_status =(CheckBox)view.findViewById(R.id.cb_status);
				view.setTag(holder);
			}
			
			holder.iv_icon.setImageDrawable(taskInfo.getAppIcon());
			holder.tv_name.setText(taskInfo.getAppName());
			holder.tv_mem.setText("内存占用:"
					+ Formatter.formatFileSize(getApplicationContext(),
							taskInfo.getMemSize()));
			//记录勾选状态
			holder.cb_status.setChecked(taskInfo.isChecked());
			return view;
		}

		@Override
		public Object getItem(int position) {
			TaskInfo taskInfo = null;
			
			if(position==0){
				return null;
			}else if(position==(userTaskInfos.size()+1)){
				return null;
			}else if(position<=userTaskInfos.size()){//用户进程
				taskInfo = userTaskInfos.get(position-1);
			}else{//系统进程
				taskInfo = systemTaskInfos.get(position-1-userTaskInfos.size()-1);
			}
			return taskInfo;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_mem;
		CheckBox cb_status;

	}
	
	/**
	 * 选择全部
	 * @param view
	 */
	public void selectAll(View view){
		for(TaskInfo info:userTaskInfos){
			info.setChecked(true);
		}
		for(TaskInfo info:systemTaskInfos){
			info.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 反选
	 * @param view
	 */
	public void selectOpposite(View view){
		for(TaskInfo info:userTaskInfos){
			info.setChecked(!info.isChecked());
		}
		for(TaskInfo info:systemTaskInfos){
			info.setChecked(!info.isChecked());
		}
		adapter.notifyDataSetChanged();
	}
}
