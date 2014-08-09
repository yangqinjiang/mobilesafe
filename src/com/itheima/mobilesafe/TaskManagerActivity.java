package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.domain.TaskInfo;
import com.itheima.mobilesafe.engine.TaskInfoProvider;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

import android.app.Activity;
import android.app.ActivityManager;
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
import android.widget.Toast;

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

		//
		setContentView(R.layout.activity_task_manager);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
		// 获取手机进程数,可用内存空间
		runningProcessCount = SystemInfoUtils.getRunningProcessCount(this);
		availRam = SystemInfoUtils.getAvailRam(this);
		updateProcessStatus();

		lv_task_manager = (ListView) findViewById(R.id.lv_task_manager);
		// checkbox点击事件
		lv_task_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = lv_task_manager.getItemAtPosition(position);// 调用adapter.getItem
				if (obj != null) {
					TaskInfo ti = (TaskInfo) obj;
					if (ti.getPackName().equals(getPackageName())) {
						return;
					}
					ti.setChecked(!ti.isChecked());// 相反勾选
					// 通知更新
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
			adapter = new TaskManagerAdapter();
			lv_task_manager.setAdapter(adapter);
		};
	};

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				userTaskInfos = new ArrayList<TaskInfo>();
				systemTaskInfos = new ArrayList<TaskInfo>();
				taskInfos = TaskInfoProvider
						.getTaskInfos(TaskManagerActivity.this);
				// 分类
				for (TaskInfo ti : taskInfos) {
					if (ti.isUserTask()) {
						userTaskInfos.add(ti);
					} else {
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
			return userTaskInfos.size() + systemTaskInfos.size() + 2;// 额外两个标签
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TaskInfo taskInfo;
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("用户进程:" + userTaskInfos.size() + "个");
				return tv;
			} else if (position == (userTaskInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("系统进程:" + systemTaskInfos.size() + "个");
				return tv;
			} else if (position <= userTaskInfos.size()) {// 用户进程
				taskInfo = userTaskInfos.get(position - 1);
			} else {// 系统进程
				taskInfo = systemTaskInfos.get(position - 1
						- userTaskInfos.size() - 1);
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
				holder.cb_status = (CheckBox) view.findViewById(R.id.cb_status);
				view.setTag(holder);
			}

			holder.iv_icon.setImageDrawable(taskInfo.getAppIcon());
			holder.tv_name.setText(taskInfo.getAppName());
			holder.tv_mem.setText("内存占用:"
					+ Formatter.formatFileSize(getApplicationContext(),
							taskInfo.getMemSize()));
			// 记录勾选状态
			holder.cb_status.setChecked(taskInfo.isChecked());
			// 隐藏本程序的checkbox
			if (taskInfo.getPackName().equals(getPackageName())) {
				holder.cb_status.setVisibility(View.INVISIBLE);
			} else {// 关键,因为有其它的
				holder.cb_status.setVisibility(View.VISIBLE);
			}
			return view;
		}

		@Override
		public Object getItem(int position) {
			TaskInfo taskInfo = null;

			if (position == 0) {
				return null;
			} else if (position == (userTaskInfos.size() + 1)) {
				return null;
			} else if (position <= userTaskInfos.size()) {// 用户进程
				taskInfo = userTaskInfos.get(position - 1);
			} else {// 系统进程
				taskInfo = systemTaskInfos.get(position - 1
						- userTaskInfos.size() - 1);
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
	 * 
	 * @param view
	 */
	public void selectAll(View view) {
		for (TaskInfo info : userTaskInfos) {
			// 排除自身
			if (info.getPackName().equals(getPackageName())) {
				continue;
			}
			info.setChecked(true);
		}
		for (TaskInfo info : systemTaskInfos) {
			info.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 反选
	 * 
	 * @param view
	 */
	public void selectOpposite(View view) {
		for (TaskInfo info : userTaskInfos) {
			// 排除自身
			if (info.getPackName().equals(getPackageName())) {
				continue;
			}
			info.setChecked(!info.isChecked());
		}
		for (TaskInfo info : systemTaskInfos) {
			info.setChecked(!info.isChecked());
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 清理全部选中的进程
	 * 
	 * @param view
	 */
	public void killAll(View view) {
		List<TaskInfo> killedTaskInfos = new ArrayList<TaskInfo>();
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (TaskInfo info : userTaskInfos) {
			if (info.isChecked()) {
				// 排除自身
				if (info.getPackName().equals(getPackageName())) {
					continue;
				}
				// userTaskInfos.remove(info);//遍历的时候,不能修改大小
				killedTaskInfos.add(info);
				// kill it
				// 加入权限:android.permission.KILL_BACKGROUND_PROCESSES
				am.killBackgroundProcesses(info.getPackName());
			}
		}
		for (TaskInfo info : systemTaskInfos) {
			if (info.isChecked()) {
				// kill it
				// systemTaskInfos.remove(info);
				killedTaskInfos.add(info);
				am.killBackgroundProcesses(info.getPackName());
			}
		}
		long savedMem = 0;
		for (TaskInfo info : killedTaskInfos) {
			if (info.isUserTask()) {
				userTaskInfos.remove(info);
			} else {
				systemTaskInfos.remove(info);
			}
			savedMem += info.getMemSize();
		}
		String free_mem = Formatter.formatFileSize(this, savedMem);
		Toast.makeText(getApplicationContext(),
				"杀死了" + killedTaskInfos.size() + "个进程,释放了" + free_mem + "M 内存",
				0).show();
		// 不要去重新刷新界面fillData()
		adapter.notifyDataSetChanged();
		//更新
		runningProcessCount-=killedTaskInfos.size();
		availRam+=savedMem;
		//
		updateProcessStatus();
	}

	/**
	 * 更新状态文字
	 * 
	 */
	private void updateProcessStatus() {
		tv_process_count.setText("正在运行的" + runningProcessCount + "个");
		tv_mem_info.setText("剩余内存:" + Formatter.formatFileSize(this, availRam));
	}
}
