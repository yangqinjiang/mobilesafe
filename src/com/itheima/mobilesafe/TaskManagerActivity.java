package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.SystemInfoUtils;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Window;
import android.widget.TextView;

public class TaskManagerActivity extends Activity {

	private TextView tv_process_count,tv_mem_info;
	//正在运行的进程数量
	private int runningProcessCount;
	//手机剩余可用内存空间
	private long availRam;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_task_manager);
		tv_process_count =(TextView)findViewById(R.id.tv_process_count);
		tv_mem_info =(TextView)findViewById(R.id.tv_mem_info);
		//获取手机进程数,可用内存空间
		runningProcessCount = SystemInfoUtils.getRunningProcessCount(this);
		availRam = SystemInfoUtils.getAvailRam(this);
		tv_process_count.setText("正在运行的"+runningProcessCount+"个");
		tv_mem_info.setText("剩余内存:"+Formatter.formatFileSize(this, availRam));
		
	}
}
