package com.itheima.mobilesafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import com.itheima.mobilesafe.domain.UpdateInfo;
import com.itheima.mobilesafe.engine.UpdateInfoParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {
	// 在检查更新时,等待的时间
	private static final int UPDATE_WAIT_TIME = 2000;
	protected static final int SERVER_ERROR = 0;
	protected static final int URL_ERROR = 1;
	protected static final int NETWORK_ERROR = 2;
	protected static final int PARSE_XML_ERROR = 3;
	protected static final int PARSE_SUCCESS = 4;
	protected static final int LOAD_MAIN_UI = 5;
	protected static final String TAG = "SplashActivity";
	private TextView tv_splash_version;
	private SharedPreferences sp;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String tip = "";
			switch (msg.what) {
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
				tip = "解析xml失败...";
				loadMainUI();
				break;
			case PARSE_SUCCESS:
				checkVersion();
				break;
			case LOAD_MAIN_UI:
				loadMainUI();
				break;
			default:
				tip = "未知错误!";
				loadMainUI();
				break;
			}
			if (tip != "") {
				Toast.makeText(getApplicationContext(), tip, Toast.LENGTH_SHORT)
						.show();
			}

		}

	};

	private void checkVersion() {

		if (getVersion().equals(updateInfo.getVersion())) {// 版本相同,无需升级,进入主界面
			loadMainUI();
		} else {
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("更新提醒:");
			builder.setMessage(updateInfo.getDescription());
			builder.setPositiveButton("立即升级", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					Log.i(TAG, "版本号不同,下载新的apk,替换安装");
					// TODO多线程下载,使用afinal框架
					tv_splash_progress.setVisibility(View.VISIBLE);
					FinalHttp finalHttp = new FinalHttp();
					File file = new File(Environment
							.getExternalStorageDirectory(), "temp.apk");
					finalHttp.download(updateInfo.getApkurl(),
							file.getAbsolutePath(), new AjaxCallBack<File>() {
								/**
								 * count文件总长度 current当前进度
								 */
								@Override
								public void onLoading(long count, long current) {
									super.onLoading(count, current);
									int progress = (int) (current * 100 / count);
									tv_splash_progress.setText("下载进度:"
											+ progress + "%");
								}

								/**
								 * 下载成功的调用方法
								 */
								@Override
								public void onSuccess(File t) {
									super.onSuccess(t);
									Toast.makeText(getApplicationContext(),
											"下载更新文件成功,准备安装...",
											Toast.LENGTH_LONG).show();
									intallApk(t);
								}

								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									super.onFailure(t, errorNo, strMsg);
									t.printStackTrace();
									Toast.makeText(getApplicationContext(),
											"下载更新文件失败", Toast.LENGTH_LONG)
											.show();
								}
							});

				}
			});
			builder.setNegativeButton("下次再说", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// 进入主界面
					Log.i(TAG, "不立即更新,下次再说");
					loadMainUI();
				}
			});
			builder.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface arg0) {
					loadMainUI();
				}
			});
			builder.show();
		}
	};

	private TextView tv_splash_progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_splash);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		System.out.println("getVersion():" + getVersion());
		tv_splash_version.setText(getString(R.string.version_tip)
				+ getVersion());
		boolean autoupdate = sp.getBoolean("autoupdate", true);
		if (autoupdate) {
			checkVersionTask();
		} else {
			Message msg = Message.obtain();
			msg.what = LOAD_MAIN_UI;
			handler.sendMessageDelayed(msg, UPDATE_WAIT_TIME);
		}

		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(UPDATE_WAIT_TIME);
		findViewById(R.id.rl_splash_root).startAnimation(aa);
		tv_splash_progress = (TextView) findViewById(R.id.tv_splash_progress);

	}

	private UpdateInfo updateInfo;
	private final int CONNECT_TIME_OUT = 5000;

	/**
	 * 连接服务器,检查版本号
	 */
	private void checkVersionTask() {
		new Thread() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {

					URL url = new URL(getString(R.string.serverurl));
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(CONNECT_TIME_OUT);
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream is = conn.getInputStream();
						updateInfo = UpdateInfoParser.getUpdateInfo(is);
						if (updateInfo != null) {
							// 解析成功,对话框
							msg.what = PARSE_SUCCESS;
						} else {
							msg.what = PARSE_XML_ERROR;
						}
					} else {
						// 请求失败
						msg.what = SERVER_ERROR;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (IOException e) {// 网络错误
					e.printStackTrace();
					msg.what = NETWORK_ERROR;
				} finally {
					long endTime = System.currentTimeMillis();
					long dTime = endTime - startTime;
					if (dTime < UPDATE_WAIT_TIME) {
						SystemClock.sleep(UPDATE_WAIT_TIME - dTime);
					}
					handler.sendMessage(msg);
				}

			}
		}.start();
	}

	/**
	 * 获取版本号
	 * 
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
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();// 关闭当前activity
	}

	/**
	 * 安装apk
	 * 
	 * @param t
	 */
	private void intallApk(File t) {

		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		// 不能同时调用
		// intent.setType("application/vnd.android.package-archive");
		// intent.setData(Uri.fromFile(t));
		intent.setDataAndType(Uri.fromFile(t),
				"application/vnd.android.package-archive");
		// startActivity(intent);
		startActivityForResult(intent, REQUEST_CODE);

	}

	private final int REQUEST_CODE = 100;// 安装apk时的请求码

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (REQUEST_CODE == requestCode) {
			loadMainUI();
		}
	}

}
