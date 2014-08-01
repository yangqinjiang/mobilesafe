package com.itheima.mobilesafe;

import java.io.IOException;

import com.itheima.mobilesafe.engine.SmsTools;
import com.itheima.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AtoolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_atools);
	}

	// 号码归属地查询
	public void numberAddressQuery(View view) {
		Intent intent = new Intent(this, NumberQueryActivity.class);
		startActivity(intent);
	}

	public void commonNumberAddressQuery(View view) {
		Intent intent = new Intent(this, CommonNumberQueryActivity.class);
		startActivity(intent);
	}

	private ProgressDialog pd ;
	/**
	 * 
	 * @param view
	 */
	public void smsBackup(View view) {
		pd = new ProgressDialog(this);
		pd.setTitle("提醒");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//进度条 
		pd.setMessage("正在备份短信...");
		pd.show();
		// 换成子线程里运行
		new Thread() {
			public void run() {
				try {
					SmsTools.backUpSms(AtoolsActivity.this,pd);
					ToastUtils.showToastInThread(AtoolsActivity.this, "备份成功");
				} catch (Exception e) {
					ToastUtils.showToastInThread(AtoolsActivity.this, "备份失败");
					e.printStackTrace();
				}finally{
					pd.dismiss();
				}
			};
		}.start();

	}

	public void smsRestore(View view) {

	}
}
