package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
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
		//�ж��û��Ƿ���й�������,���û�н��й������� ,����ҳ�浽�����򵼽���
		if(isSetup()){
			//setContentView(R.layout.activity_lostfind);
//			TextView tv_number = (TextView) findViewById(R.id.tv_lostfind_number);
//			tv_number.setText(sp.getString("safenumber", ""));
//			ImageView iv_lostfind_status = (ImageView) findViewById(R.id.iv_lostfind_status);
//			boolean protecting = sp.getBoolean("protecting", false);
//			if(protecting){
//				iv_lostfind_status.setImageResource(R.drawable.lock);
//			}else{
//				iv_lostfind_status.setImageResource(R.drawable.unlock);
//			}
		}else{
			Log.i(TAG,"定向界面到设置向导界面");
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			finish();
		}
	}
	
	/**
	 * 决断用户是否完成过设置向导
	 * @return
	 */
	private boolean isSetup(){
		return sp.getBoolean("setup", false);
	}
	/**
	 * ���½���������
	 * @param view
	 */
	public void reEntrySetup(View view){
		Intent intent = new Intent(this,Setup1Activity.class);
		startActivity(intent);
		finish();
	}
}
