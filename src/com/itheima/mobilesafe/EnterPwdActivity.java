package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EnterPwdActivity extends Activity {
	private EditText et_password;
	private String packname ="";
	/**
	 * 看门狗的动作
	 */
	public static final String WATCH_DOG_ACTION ="com.itheima.mobilesafe.gaga";
	/**
	 * 传播自定义广播时,参数的key
	 */
	public static final String PACK_NAME="packname";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_pwd);
		et_password = (EditText) findViewById(R.id.et_password);
		Intent intent = getIntent();
		packname = intent.getStringExtra(PACK_NAME);
	}

	public void click(View v) {
		String pwd = et_password.getText().toString().trim();
		if("123".equals(pwd)){//默认值
			finish();
			//通知看门狗,停止对当前应用程序的保护
			//自定义广播事件
			Intent intent = new Intent();
			intent.putExtra(PACK_NAME, packname);
			intent.setAction(WATCH_DOG_ACTION);
			sendBroadcast(intent);
			
		}else{
			Toast.makeText(this, "密码错误...",Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onBackPressed() {
		//super.onBackPressed();//不退出
		//返回桌面
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		//
		startActivity(intent);
		finish();
		
	}
}
