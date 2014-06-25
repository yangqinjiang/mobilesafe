package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.itheima.mobilesafe.ui.SettingView;

public class SettingCenterActivity extends Activity {

	private SharedPreferences sp;
	private SettingView sv_autoupdate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		setContentView(R.layout.activity_setting_center);
		sv_autoupdate = (SettingView) this.findViewById(R.id.sv_autoupdate);
		sp = getSharedPreferences("config",MODE_PRIVATE);
		sv_autoupdate.setChecked(sp.getBoolean("autoupdate",true));
		sv_autoupdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Editor editor = sp.edit();
				if(sv_autoupdate.isChecked()){
					sv_autoupdate.setChecked(false);
					editor.putBoolean("autoupdate",false);
				}else{
					sv_autoupdate.setChecked(true);
					editor.putBoolean("autoupdate",true);
				}
				editor.commit();
			}
		});	
		
	}
}
