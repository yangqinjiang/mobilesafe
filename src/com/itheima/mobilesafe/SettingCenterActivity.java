package com.itheima.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

public class SettingCenterActivity extends Activity {

	//private RelativeLayout rl_update;
	private TextView tv_update_status;
	private CheckBox cb_update_status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		setContentView(R.layout.activity_setting_center);
		
		//rl_update = (RelativeLayout)findViewById(R.id.rl_setting_update);
		tv_update_status = (TextView)findViewById(R.id.tv_setting_update_status);
		cb_update_status = (CheckBox)findViewById(R.id.cb_setting_update_status);
		
		
	}
	public void enableUpdateStatus(View v){
		if(cb_update_status.isChecked()){
			cb_update_status.setChecked(false);
			tv_update_status.setText(getString(R.string.auto_update_close));
		}else{
			cb_update_status.setChecked(true);
			tv_update_status.setText(getString(R.string.enable_auto_update));
		}
	}
}
