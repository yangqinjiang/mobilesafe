package com.itheima.mobilesafe.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

public class SystemInfoUtils {

	/**
	 * 获取正在运行的进程数量
	 * @param context
	 * @return int
	 */
	public static int getRunningProcessCount(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();//获取app运行的进程数量
	}
	/**
	 * 获取手机可用内存空间
	 * @param context
	 * @return long 
	 */
	public static long getAvailRam(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		long availMem = outInfo.availMem;//可用内存
//		long totalMem = outInfo.totalMem;
		return availMem;
	}
}
