package com.itheima.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 程序锁的看门狗
 * 
 * @author lenovo
 * 
 */
public class WatchDogService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private ActivityManager am;
	boolean flag;
	@Override
	public void onCreate() {
		super.onCreate();
		flag=true;
		// 请看门狗,监视当前手机运行的程序
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		new Thread() {
			public void run() {
				while (flag) {
					// 最近打开的任务栈,会在集合的最前面,按照时间依次排序
					List<RunningTaskInfo> infos = am.getRunningTasks(100);// 任务栈
					// 得到最近的任务栈
					RunningTaskInfo info = infos.get(0);
					// 得到正在打开的程序
					System.out.println(info.topActivity.getPackageName());
					try {
						Thread.sleep(500);//休息一会
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
		flag=false;
	}
}
