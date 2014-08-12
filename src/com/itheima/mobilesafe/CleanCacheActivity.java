package com.itheima.mobilesafe;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
/**
 * 获取应用程序的缓存大小
 * 109_利用反射调用隐藏api获取应用程序的缓存大小
 * @author lenovo
 *
 */
public class CleanCacheActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PackageManager pm = getPackageManager();
		//权限:android.permission.GET_PACKAGE_SIZE
	try {
		Method method = PackageManager.class.getDeclaredMethod("getPackagerSizeInfo",String.class,IPackageStatsObserver.class);
		method.invoke(pm,"com.itheima.mobilesafe",new MyObserver());
	} catch (Exception e) {
		e.printStackTrace();
	}
		
	}
	private class MyObserver extends IPackageStatsObserver.Stub{

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			//获取大小
			long cache = pStats.cacheSize;
			long data = pStats.dataSize;
			long code = pStats.codeSize;
			System.out.println("cacheSize:"+Formatter.formatFileSize(getApplicationContext(), cache));
			System.out.println("data:"+Formatter.formatFileSize(getApplicationContext(), data));
			System.out.println("code:"+Formatter.formatFileSize(getApplicationContext(), code));
		}
		
	}
	
}
