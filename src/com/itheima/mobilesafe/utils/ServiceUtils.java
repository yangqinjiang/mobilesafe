package com.itheima.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {

	/**
	 * 判断服务是处于开启状态
	 * @param context 上下文
	 * @param serviceName 检查的服务名称
	 * @return true,则开启,false 则没有开启
	 */
	public static boolean isServiceRunning(Context context,String serviceName){
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String runningServiceName = runningServiceInfo.service.getClassName();
			if(serviceName.equals(runningServiceName)){
				return true;
			}
		}
		return false;
	}
}
