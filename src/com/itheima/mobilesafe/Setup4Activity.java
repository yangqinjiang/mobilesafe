package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.ActivityUtils;

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
		final CheckBox cb = (CheckBox)findViewById(R.id.cb_setup4_status);
		boolean protecting = sp.getBoolean("protecting", false);
		
		cb.setChecked(protecting);
		if(protecting){
			cb.setText("防盗保护已经开启");
		}else{
			cb.setText("防盗保护已经开启");
		}
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cb.setText("防盗保护已经开启");
				}else{
					cb.setText("防盗保护已经开启");
				}
				Editor editor = sp.edit();
				editor.putBoolean("protecting", isChecked);
				editor.commit();
			}
		});
	}


	@Override
	public void next(View view) {
		Editor editor = sp.edit();
		editor.putBoolean("setup", true);
		editor.commit();
		ActivityUtils.startActivityAndFinish(this, LostFindActivity.class);
	}

	@Override
	public void pre(View view) {
		ActivityUtils.startActivityAndFinish(this, Setup3Activity.class);
		
	}

}
