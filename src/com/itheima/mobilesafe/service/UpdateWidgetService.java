package com.itheima.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.receiver.MyWidget;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	private static final String TAG = "UpdateWidgetService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private Timer timer;
	private TimerTask task;

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				Log.i(TAG, "TimerTask");
				// 更新widget的ui
				AppWidgetManager awm = AppWidgetManager
						.getInstance(getApplicationContext());
				
				ComponentName provider = new ComponentName(
						getApplicationContext(), MyWidget.class);
				
				RemoteViews views = new RemoteViews(getPackageName(),
						R.layout.process_widget);
				String pc = "正在运行进程:"
						+ SystemInfoUtils
								.getRunningProcessCount(getApplicationContext())
						+ "个";
				Log.i(TAG, pc);
				views.setTextViewText(R.id.process_count, pc);
				String pm = "剩余内存:"
						+ Formatter.formatFileSize(getApplicationContext(),
								SystemInfoUtils
										.getAvailRam(getApplicationContext()))
						+ "个";
				Log.i(TAG, pm);
				views.setTextViewText(R.id.process_memory,pm);
				awm.updateAppWidget(provider, views);
			}
		};
		timer.schedule(task, 1000, 3000);
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		task.cancel();
		timer = null;
		task = null;
	}

}
