package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.ActivityUtils;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Setup2Activity extends SetupBaseActivity {

	private ImageView iv_setup2_status;
	private TelephonyManager tm;

	// 读取SIM卡权限
	// android.permission.READ_PHONE_STATE
	@Override
	public void initView() {
		setContentView(R.layout.activity_setup2);
		iv_setup2_status = (ImageView) findViewById(R.id.iv_setup2_status);
		String savedSim = sp.getString("sim", null);
		if (TextUtils.isEmpty(savedSim)) {
			iv_setup2_status.setImageResource(R.drawable.unlock);
		} else {
			iv_setup2_status.setImageResource(R.drawable.lock);
		}
	}

	public void bindSim(View view) {
		String savedSim = sp.getString("sim", null);
		if (TextUtils.isEmpty(savedSim)) {

			tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);//
			// tm.getLine1Number();//在联通,移动手机上不能使用
			String sim = tm.getSimSerialNumber();// 获取sim卡的序列号
			Toast.makeText(getApplicationContext(), sim, 0).show();
			Editor editor = sp.edit();
			editor.putString("sim", sim);
			editor.commit();
			iv_setup2_status.setImageResource(R.drawable.lock);
		} else {
			Editor editor = sp.edit();
			editor.putString("sim", null);
			editor.commit();
			iv_setup2_status.setImageResource(R.drawable.unlock);
		}
	}

	@Override
	public void next(View view) {
		String sim = sp.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			Toast.makeText(getApplicationContext(), "必须绑定sim卡", 0).show();
			return;
		}
		ActivityUtils.startActivityAndFinish(this, Setup3Activity.class);
	}

	@Override
	public void pre(View view) {
		ActivityUtils.startActivityAndFinish(this, Setup1Activity.class);
		
	}

}
