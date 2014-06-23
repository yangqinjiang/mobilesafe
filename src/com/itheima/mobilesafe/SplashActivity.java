package com.itheima.mobilesafe;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	protected static final int SERVER_ERROR = 0;
	protected static final int URL_ERROR = 1;
	protected static final int NETWORK_ERROR = 2;
	private TextView tv_splash_version;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			String tip = "";
			switch(msg.what){
			case SERVER_ERROR:
				tip = "服务器错误...";
				break;
			case URL_ERROR:
				tip = "更新的地址错误...";
				break;
			case NETWORK_ERROR:
				tip = "网络错误,请检查网络连接...";
				break;
			default:
				tip = "未知错误!";
				break;
			}
			Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_SHORT).show();
			
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView)findViewById(R.id.tv_splash_version);
		tv_splash_version.setText(getString(R.string.version_tip)+getVersion());
		checkVersionTask();
	}
	/**
	 * 连接服务器,检查版本号
	 */
	private void checkVersionTask() {
		new Thread(){
			@Override
			public void run() {
				Message msg = Message.obtain();
				try {
					URL url = new URL(getString(R.string.serverurl));
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					if(code == 200){
						
					}else{
						//请求失败
						msg.what = SERVER_ERROR;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					msg.what = URL_ERROR;
				}catch(IOException e){//网络错误
					e.printStackTrace();
					msg.what = NETWORK_ERROR;
				}finally{
					handler.sendMessage(msg);
				}
			
			}
		}.start();
	}
	/**
	 * 获取版本号
	 * @return
	 */
	private String getVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
			return packinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

}
