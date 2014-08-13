package com.itheima.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * 获取应用程序的缓存大小 109_利用反射调用隐藏api获取应用程序的缓存大小
 * 
 * @author lenovo
 * 
 */
public class CleanSDActivity extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_clean_sd);
}

}
