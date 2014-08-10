package com.itheima.mobilesafe.receiver;

import com.itheima.mobilesafe.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyWidget extends AppWidgetProvider {

	private static final String TAG = "MyWidget";
	/**
	 * 当widget更新的时候,调用这方法
	 */
	@Override
	public void onUpdate(Context context, 
			AppWidgetManager appWidgetManager,//更新widget的状态
			int[] appWidgetIds) {//ppWidgetIds 在桌面上的个数,顺序
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.i(TAG,"onUpdate");
	}
	private Intent intent;
	/**
	 * 当第一个widget被创建的时候调用方法
	 */
	@Override
	public void onEnabled(Context context) {//程序初始化的操作
		super.onEnabled(context);
		Log.i(TAG,"onEnabled");
		Intent intent = new Intent(context,UpdateWidgetService.class);
		context.startService(intent);
	}
	/**
	 * 当界面最后一个widget被移除的时候,调用的方法
	 */
	@Override
	public void onDisabled(Context context) {//程序的清理操作
		super.onDisabled(context);
		Log.i(TAG,"onDisabled");
		Intent intent = new Intent(context,UpdateWidgetService.class);
		context.stopService(intent);
	}
}
