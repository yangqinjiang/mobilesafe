package com.itheima.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.EnterPwdActivity;
import com.itheima.mobilesafe.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * 程序锁的看门狗
 */
public class WatchDogService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}
	/**
	 * 中间人类型
	 *
	 */
	public class MyBinder extends Binder{
		public void callTempStopProtect(String packname){
			tempStopProtect(packname);
		}
	}

	private ActivityManager am;
	private AppLockDao dao;
	private Intent intent;
	boolean flag;
	private InnerReceiver receiver;
	private ScreenOffReceiver screenOffReceiver;
	private ScreenOnReceiver screenOnReceiver;
	private static final String TAG = "WatchDogService";
	private List<String> tempStopProtectPacknames;

	// 内置广播接收者
	private class InnerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String packname = intent.getStringExtra(EnterPwdActivity.PACK_NAME);
			if (null == packname)
				return;
			Log.i(TAG, "接收到自定义的广播事件" + packname);
			tempStopProtectPacknames.add(packname);
		}

	}

	/**
	 * 锁屏广播接收者
	 */
	private class ScreenOffReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			tempStopProtectPacknames.clear();// 在屏幕锁屏里,清空
			flag = false;

		}

	}

	/**
	 * 解锁广播接收者
	 */
	private class ScreenOnReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (!flag) {// 解决多次启动问题
				flag = true;
				startWatchDog();
			}
		}

	}

	private List<String> protectePackNames;

	@Override
	public void onCreate() {
		super.onCreate();
		tempStopProtectPacknames = new ArrayList<String>();
		receiver = new InnerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(EnterPwdActivity.WATCH_DOG_ACTION);
		registerReceiver(receiver, filter);
		// screenOffReceiver
		screenOffReceiver = new ScreenOffReceiver();
		IntentFilter offFilter = new IntentFilter();
		offFilter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenOffReceiver, offFilter);

		// ScreenOnReceiver
		screenOnReceiver = new ScreenOnReceiver();
		IntentFilter onFilter = new IntentFilter();
		onFilter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(screenOnReceiver, onFilter);

		dao = new AppLockDao(this);
		protectePackNames = dao.findAll();// 将数据库里的数据保存到内存,但有bug,不能立即更新内存里的数据
		intent = new Intent(this, EnterPwdActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 在服务里启动activity
		flag = true;
		// 请看门狗,监视当前手机运行的程序
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		startWatchDog();
		Uri uri = Uri.parse(AppLockDao.APP_LOCK_URI);
		observer = new AppLockObserver(new Handler());
		getContentResolver().registerContentObserver(uri, true,observer );
	}
	/**
	 * 临时停止对某个应用程序的保护
	 */
	public void tempStopProtect(String packname){
		tempStopProtectPacknames.add(packname);
	}
	private AppLockObserver observer;
	//内容观察者
	private class AppLockObserver extends ContentObserver {

		public AppLockObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Log.i(TAG, "数据库的内容变化了,但是被我给观察到了");
			protectePackNames = dao.findAll();
		}

	}

	private void startWatchDog() {
		new Thread() {
			public void run() {
				while (flag) {
					Log.i(TAG, "wowo....");
					// 最近打开的任务栈,会在集合的最前面,按照时间依次排序
					List<RunningTaskInfo> infos = am.getRunningTasks(100);// 任务栈
					// 得到最近的任务栈
					RunningTaskInfo info = infos.get(0);
					// 得到正在打开的程序
					// 清空tempStopProtectPacknames
					String packname = info.topActivity.getPackageName();
					if (protectePackNames.contains(packname)) {// 从内存里查找数据
						// 弹出输入密码的界面
						// 已经包含,需要临时停止保护
						if (!tempStopProtectPacknames.contains(packname)) {
							intent.putExtra(EnterPwdActivity.PACK_NAME,
									packname);
							startActivity(intent);
						}
					}
					try {
						Thread.sleep(500);// 休息一会
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;

		unregisterReceiver(receiver);
		receiver = null;
		unregisterReceiver(screenOffReceiver);
		screenOffReceiver = null;
		unregisterReceiver(screenOnReceiver);
		screenOnReceiver = null;
		
		getContentResolver().unregisterContentObserver(observer);
		observer=null;
	}
}
