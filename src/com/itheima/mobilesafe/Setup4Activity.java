package com.itheima.mobilesafe;

import com.itheima.mobilesafe.receiver.MyAdmin;
import com.itheima.mobilesafe.utils.ActivityUtils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Toast;

public class Setup4Activity extends SetupBaseActivity {

	// 读取SIM卡权限
	// android.permission.READ_PHONE_STATE
	@Override
	public void initView() {
		setContentView(R.layout.activity_setup4);
		final CheckBox cb = (CheckBox) findViewById(R.id.cb_setup4_status);
		boolean protecting = sp.getBoolean("protecting", false);

		cb.setChecked(protecting);
		if (protecting) {
			cb.setText("防盗保护已经开启");
		} else {
			cb.setText("防盗保护已经开启");
		}
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					cb.setText("防盗保护已经开启");
				} else {
					cb.setText("防盗保护已经开启");
				}
				Editor editor = sp.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();
			}
		});
		CheckBox cb_admin = (CheckBox) findViewById(R.id.cb_setup4_admin);
		cb_admin.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					Intent intent = new Intent(
							DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
					ComponentName mDeviceAdminSample = new ComponentName(
							Setup4Activity.this, MyAdmin.class);

					intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
							mDeviceAdminSample);
					intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
							"激活吧");
					startActivity(intent);
				} else {
					DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);// 设备策略服务

					ComponentName who = new ComponentName(Setup4Activity.this, MyAdmin.class);
					dpm.removeActiveAdmin(who);
				}

			}
		});
	}

	@Override
	public void next(View view) {
		Editor editor = sp.edit();
		editor.putBoolean("setup", true);
		editor.commit();
		//先调用overridePendingTransition
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
		//后调用startActivityAndFinish
		ActivityUtils.startActivityAndFinish(this, LostFindActivity.class);
	}

	@Override
	public void pre(View view) {
		//先调用startActivityAndFinish
		ActivityUtils.startActivityAndFinish(this, Setup3Activity.class);
		//后调用overridePendingTransition
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

}
