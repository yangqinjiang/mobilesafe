package com.itheima.mobilesafe.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.itheima.mobilesafe.EnterPwdActivity;
import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.receiver.MyWidget;
import com.itheima.mobilesafe.utils.SystemInfoUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	private InnerReceiver receiver;

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");

		receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(KILL_ALL);
		registerReceiver(receiver, filter);
		
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
				views.setTextViewText(R.id.process_memory, pm);
				
				Intent intent = new Intent();
				intent.setAction(KILL_ALL);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						getApplicationContext(), 0, intent, 0);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				
				awm.updateAppWidget(provider, views);
			}
		};
		timer.schedule(task, 1000, 3000);
		super.onCreate();

	}

	// 接收KILL_ALL广播
	private class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "接收到"+KILL_ALL+"广播");
			
			ActivityManager am =(ActivityManager)getSystemService(ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
			for(RunningAppProcessInfo info:infos){
				if(info.processName.equals(getPackageName())){
					continue;
				}
				//只能杀别的程序,不能自杀
				am.killBackgroundProcesses(info.processName);
			}
			//自杀
//			android.os.Process.killProcess(pid);
		}

	}

	public static final String KILL_ALL = "com.itheima.mobilesafe.killall.gaga";

	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		task.cancel();
		timer = null;
		task = null;
		unregisterReceiver(receiver);
		receiver = null;
	}

}
