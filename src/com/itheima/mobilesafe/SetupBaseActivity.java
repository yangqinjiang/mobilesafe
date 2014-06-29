package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Window;
/**
 * 设置向导的父类Activity
 *
 */
public abstract class SetupBaseActivity extends Activity {
	protected SharedPreferences sp;
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		sp = getSharedPreferences("config", MODE_PRIVATE);
		initView();
		
	}
	/**
	 * 初始化当前activity显示的内容
	 */
	public abstract void initView();
	
}
