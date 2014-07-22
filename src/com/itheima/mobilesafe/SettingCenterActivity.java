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
import com.itheima.mobilesafe.ui.SettingView;
import com.itheima.mobilesafe.utils.ServiceUtils;

public class SettingCenterActivity extends Activity {

	private SharedPreferences sp;
	private SettingView sv_autoupdate;
	private SettingView sv_show_address;

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
		boolean serviceRunning = ServiceUtils.isServiceRunning(this,
				"com.itheima.mobilesafe.service.AddressService");

		sv_show_address.setChecked(serviceRunning);

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
	}

	private Intent addressServiceIntent;
}
