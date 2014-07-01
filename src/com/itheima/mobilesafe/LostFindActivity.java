package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.ActivityUtils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private static final String TAG = "LostFindActivity";
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 判断用户是否进行过设置向导,如果没有进行过设置向导,定向页面到设置向导界面
		if (isSetup()) {
			setContentView(R.layout.activity_lostfind);
			TextView tv_number = (TextView) findViewById(R.id.tv_lostfind_number);
			// 设置保存的safenumber
			tv_number.setText(sp.getString("safenumber", ""));
			ImageView iv_lostfind_status = (ImageView) findViewById(R.id.iv_lostfind_status);
			//根据是否保护的状态来设置相应的图片
			boolean protecting = sp.getBoolean("protecting", false);
			if (protecting) {
				iv_lostfind_status.setImageResource(R.drawable.lock);
			} else {
				iv_lostfind_status.setImageResource(R.drawable.unlock);
			}
		} else {//未进入向导,则设置
			Log.i(TAG, "定向界面到设置向导界面");
			ActivityUtils.startActivityAndFinish(this, Setup1Activity.class);
		}
	}

	/**
	 * 决断用户是否完成过设置向导
	 * 
	 * @return
	 */
	private boolean isSetup() {
		return sp.getBoolean("setup", false);
	}

	/**
	 * 重新进入设置向导
	 * 
	 * @param view
	 */
	public void reEntrySetup(View view) {
		ActivityUtils.startActivityAndFinish(this, Setup1Activity.class);
	}
}
