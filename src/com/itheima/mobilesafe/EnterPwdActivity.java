package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 在清单文件里,        
 * android:launchMode="singleInstance" 开启新的任务栈,只有一个实例,这样,就能解决
 * 在本程序在运行时,出现一个bug,因为一个程序可能有多个任务栈
 *
 */
public class EnterPwdActivity extends Activity {
	private EditText et_password;
	private String packname ="";
	private TextView tv_name;
	private ImageView iv_icon;
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
		tv_name =(TextView)findViewById(R.id.tv_name);
		iv_icon =(ImageView)findViewById(R.id.iv_icon);
		
		PackageManager pm = getPackageManager();
		try {
			ApplicationInfo applicationInfo = pm.getApplicationInfo(packname, 0);
			iv_icon.setImageDrawable(applicationInfo.loadIcon(pm));
			tv_name.setText(applicationInfo.loadLabel(pm));
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
