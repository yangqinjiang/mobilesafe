package com.itheima.mobilesafe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.itheima.mobilesafe.domain.UpdateInfo;
import com.itheima.mobilesafe.engine.UpdateInfoParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	protected static final int SERVER_ERROR = 0;
	protected static final int URL_ERROR = 1;
	protected static final int NETWORK_ERROR = 2;
	protected static final int PARSE_XML_ERROR = 3;
	protected static final int PARSE_SUCCESS = 4;
	protected static final String TAG = "SplashActivity";
	private TextView tv_splash_version;
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			String tip = "";
			switch(msg.what){
			case SERVER_ERROR:
				tip = "服务器错误...";
				loadMainUI();
				break;
			case URL_ERROR:
				tip = "更新的地址错误...";
				loadMainUI();
				break;
			case NETWORK_ERROR:
				tip = "网络错误,请检查网络连接...";
				loadMainUI();
				break;
			case PARSE_XML_ERROR:
				tip="解析xml失败...";
				loadMainUI();
				break;
			case PARSE_SUCCESS:
				checkVersion();
				break;
			default:
				tip = "未知错误!";
				loadMainUI();
				break;
			}
			Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_SHORT).show();
			
			
		}


	};
	private void checkVersion() {

		if(getVersion().equals(updateInfo.getVersion())){//版本相同,无需升级,进入主界面
			loadMainUI();
		}else{
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("更新提醒:");
			builder.setMessage(updateInfo.getDescription());
			builder.setPositiveButton("立即升级", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					Log.i(TAG,"版本号不同,下载新的apk,替换安装");
				}
			});
			builder.setNegativeButton("下次再说", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					//进入主界面
					Log.i(TAG,"不立即更新,下次再说");
					loadMainUI();
				}
			});
			builder.show();
		}
};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView)findViewById(R.id.tv_splash_version);
		tv_splash_version.setText(getString(R.string.version_tip)+getVersion());
		
		checkVersionTask();
	}
	private UpdateInfo updateInfo ;
	/**
	 * 连接服务器,检查版本号
	 */
	private void checkVersionTask() {
		new Thread(){
			@Override
			public void run() {
				Message msg = Message.obtain();
				try {
					SystemClock.sleep(2000);
					URL url = new URL(getString(R.string.serverurl));
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					if(code == 200){
						InputStream is = conn.getInputStream();
						updateInfo = UpdateInfoParser.getUpdateInfo(is);
						if(updateInfo != null){
							//解析成功,对话框
							msg.what = PARSE_SUCCESS;
						}else{
							msg.what = PARSE_XML_ERROR;
						}
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
	private void loadMainUI() {
		Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
		startActivity(intent);
		finish();//关闭当前activity
	}

}
