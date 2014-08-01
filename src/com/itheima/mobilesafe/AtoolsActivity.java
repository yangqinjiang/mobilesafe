package com.itheima.mobilesafe;

import java.io.IOException;

import com.itheima.mobilesafe.engine.SmsTools;
import com.itheima.mobilesafe.engine.SmsTools.SmsToolsCallBack;
import com.itheima.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AtoolsActivity extends Activity implements SmsToolsCallBack {

	private ProgressBar pb_smsbackup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_atools);
		pb_smsbackup = (ProgressBar) findViewById(R.id.pb_smsbackup);
		pb_smsbackup.setVisibility(View.INVISIBLE);
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

	private ProgressDialog pd;

	/**
	 * 
	 * @param view
	 */
	public void smsBackup(View view) {
		pd = new ProgressDialog(this);
		pd.setTitle("提醒");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 进度条
		pd.setMessage("正在备份短信...");
		pd.show();
		pb_smsbackup.setVisibility(View.VISIBLE);
		// 换成子线程里运行
		new Thread() {
			public void run() {
				try {
					SmsTools.backUpSms(AtoolsActivity.this, AtoolsActivity.this);
					ToastUtils.showToastInThread(AtoolsActivity.this, "备份成功");
				} catch (Exception e) {
					ToastUtils.showToastInThread(AtoolsActivity.this, "备份失败");
					e.printStackTrace();
				} finally {
					pd.dismiss();
					// pb_smsbackup.setVisibility(View.INVISIBLE);
					pd = null;
				}
			};
		}.start();

	}

	/**
	 * 还原短信
	 * 
	 * @param view
	 */
	public void smsRestore(View view) {
		//简单插入一条短信
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://sms");
		ContentValues values = new ContentValues();
		values.put("address","95588");
//		values.put("date","1379662758577");
		values.put("date",System.currentTimeMillis());
		values.put("type", "1");
		values.put("body","不好意思,转账有误!");//已经转账100,000,000元到您账户
		resolver.insert(uri, values);
	}

	@Override
	public void beforeSmsBackup(int max) {
		pd.setMax(max);
		pb_smsbackup.setMax(max);

	}

	@Override
	public void onSmsBackup(int progress) {
		pd.setProgress(progress);
		pb_smsbackup.setProgress(progress);
	}

	@Override
	public void endSmsBackup() {

	}
}
