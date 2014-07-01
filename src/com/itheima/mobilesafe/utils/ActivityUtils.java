package com.itheima.mobilesafe.utils;

import android.app.Activity;
import android.content.Intent;

public class ActivityUtils {

	/**
	 * 开启新的activity,并且关闭当前界面
	 * 
	 * @param context Activity上下文
	 * @param cls 启动的Activity class
	 */
	public static void startActivityAndFinish(Activity context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
		context.finish();
	}
}
