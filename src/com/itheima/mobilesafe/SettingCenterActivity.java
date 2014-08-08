package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.itheima.mobilesafe.service.AddressService;
import com.itheima.mobilesafe.service.CallSmsSafeService;
import com.itheima.mobilesafe.service.WatchDogService;
import com.itheima.mobilesafe.ui.SettingClickView;
import com.itheima.mobilesafe.ui.SettingView;
import com.itheima.mobilesafe.utils.ServiceUtils;

public class SettingCenterActivity extends Activity {

	private SharedPreferences sp;
	private SettingView sv_autoupdate;
	private SettingView sv_show_address;

	//黑名单拦截设置
	private SettingView sv_callsms_safe;
	private Intent callSmsSafeIntent;
	//程序锁
	private SettingView sv_watch_dog;
	private Intent watchDogIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_setting_center);
		sv_autoupdate = (SettingView) this.findViewById(R.id.sv_autoupdate);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		sv_autoupdate.setChecked(sp.getBoolean("autoupdate", true));
		sv_autoupdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Editor editor = sp.edit();
				if (sv_autoupdate.isChecked()) {
					sv_autoupdate.setChecked(false);
					editor.putBoolean("autoupdate", false);
				} else {
					sv_autoupdate.setChecked(true);
					editor.putBoolean("autoupdate", true);
				}
				editor.commit();
			}
		});
		// 归属地显示设置
		sv_show_address = (SettingView) findViewById(R.id.sv_show_address);
		addressServiceIntent = new Intent(this, AddressService.class);
		//checkService();

		//sv_show_address.setChecked(sp.getBoolean("show_address", true));
		sv_show_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "ccc", 0).show();
				//Editor editor = sp.edit();
				if (sv_show_address.isChecked()) {
					sv_show_address.setChecked(false);
					//editor.putBoolean("show_address", false);
					stopService(addressServiceIntent);
					Toast.makeText(getApplicationContext(), "ddd", 0).show();
				} else {
					sv_show_address.setChecked(true);
					//editor.putBoolean("show_address", true);
					startService(addressServiceIntent);
					Toast.makeText(getApplicationContext(), "eee", 0).show();
				}
				//editor.commit();
			}
		});
		
		//更改归属地显示位置
		scv_change_location = (SettingClickView)findViewById(R.id.scv_change_location);
		scv_change_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingCenterActivity.this, DragViewActivity.class));
			}
		});
		// 黑名单服务设置
		sv_callsms_safe = (SettingView) findViewById(R.id.sv_callsms_safe);
		callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
		//checkService();
		sv_callsms_safe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sv_callsms_safe.isChecked()) {
					sv_callsms_safe.setChecked(false);
					stopService(callSmsSafeIntent);
				} else {
					sv_callsms_safe.setChecked(true);
					startService(callSmsSafeIntent);
				}
			}
		});
		//sv_watch_dog
		sv_watch_dog = (SettingView) findViewById(R.id.sv_watch_dog);
		watchDogIntent = new Intent(this, WatchDogService.class);
		//checkService();
		sv_watch_dog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sv_watch_dog.isChecked()) {
					sv_watch_dog.setChecked(false);
					stopService(watchDogIntent);
				} else {
					sv_watch_dog.setChecked(true);
					startService(watchDogIntent);
				}
			}
		});
		checkService();
	}

	private void checkService() {
		boolean serviceRunning = ServiceUtils.isServiceRunning(this,
				"com.itheima.mobilesafe.service.AddressService");

		sv_show_address.setChecked(serviceRunning);
		
		boolean callSmsServiceRunning = ServiceUtils.isServiceRunning(this,
				"com.itheima.mobilesafe.service.CallSmsSafeService");

		sv_callsms_safe.setChecked(callSmsServiceRunning);
		//
		boolean WatchDogRunning = ServiceUtils.isServiceRunning(this,
				"com.itheima.mobilesafe.service.WatchDogService");

		sv_watch_dog.setChecked(WatchDogRunning);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		checkService();//解决service被kill掉后,activity未被更新ui
	}

	private Intent addressServiceIntent;
	
	private SettingClickView scv_change_location;
}
