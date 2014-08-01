package com.itheima.mobilesafe.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * 在toast时显示子线程消息
 * 
 * @author lenovo
 * 
 */
public class ToastUtils {
	/**
	 * 在子线程里显示toast的工具方法
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToastInThread(final Activity context,
			final String msg) {
		context.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(context, msg, 0).show();
			}
		});
	}
}
