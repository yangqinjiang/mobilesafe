package com.itheima.mobilesafe.receiver;

import com.itheima.mobilesafe.LostFindActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OutCallReceiver extends BroadcastReceiver {

	private static final String TAG = "OutCallReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "有新的电话打出来了...");
		String number = getResultData();
		if("20182018".equals(number)){
			//使用显式intent启动
			Intent i = new Intent(context,LostFindActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在receiver中启动activity,必须加上此标志
			context.startActivity(i);
			setResultData(null);//中止事件,就不会在通话记录里显示
			//abortBroadcast();//中止事件
		}

	}

}
