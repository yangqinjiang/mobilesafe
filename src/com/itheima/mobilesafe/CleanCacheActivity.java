package com.itheima.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 获取应用程序的缓存大小 109_利用反射调用隐藏api获取应用程序的缓存大小
 * 
 * @author lenovo
 * 
 */
public class CleanCacheActivity extends Activity {

	protected static final int SCANING = 1;
	protected static final int SCAN_FINISH = 2;
	private ProgressBar pb_status;
	private TextView tv_status;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SCANING:
				PackageInfo info = (PackageInfo)msg.obj;
				String name = info.applicationInfo.loadLabel(pm).toString();
				tv_status.setText("正在扫描:"+name);
				break;
			case SCAN_FINISH:
				if(cacheInfos.size()>0){
					for(CacheInfo cacheInfo :cacheInfos){
						String r = cacheInfo.appName+"缓存大小:"+Formatter.formatFileSize(getApplicationContext(), cacheInfo.cache);
						System.out.println(r);
						TextView tv = new TextView(getApplicationContext());
						tv.setTextColor(Color.GRAY);
						tv.setBackgroundColor(Color.BLUE);
						tv.setText(r);
						final String pn = cacheInfo.packname;
						tv.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								try{
									//清理程序的缓存
									//android.permission.DELETE_CACHE_FILES 系统程序专用
									Method method =PackageManager.class.getMethod("deleteApplicationCacheFiles", String.class,IPackageDataObserver.class);
									method.invoke(pm, pn,new MyDataObserver());
								}catch(Exception e){
									e.printStackTrace();
								}
							}
						});
						ll_container.addView(tv,0);
					}
				}else{
					Toast.makeText(getApplicationContext(), "无", 0).show();
				}
				break;
			default:
				break;
			}
			
		}
	};
	/**
	 * 一键清理
	 * @param v
	 */
	public void cleanAll(View v){
		Method[] methods = PackageManager.class.getMethods();
		for(Method m:methods){
			if(m.getName().equals("freeStorageAndNotify")){
				try {
					//权限:android.permission.CLEAR_APP_CACHE"
					m.invoke(pm, Integer.MAX_VALUE,new MyDataObserver());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}
	private class MyDataObserver extends IPackageDataObserver.Stub{

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			
		}
		
	}
	private List<CacheInfo> cacheInfos;
	private LinearLayout ll_container;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_clean_cache);
		//写入测试数据
		//wirteCache();
		
		pb_status=(ProgressBar)findViewById(R.id.pb_status);
		tv_status=(TextView)findViewById(R.id.tv_status);
		ll_container=(LinearLayout)findViewById(R.id.ll_container);
		pm = getPackageManager();
		scanCache();

	}
	private void wirteCache(){
		try{
			File file = new File(getCacheDir(),"haha.txt");
			FileOutputStream fos = new FileOutputStream(file);
			fos.write("dfafa".getBytes());
			fos.close();
		}catch(Exception e){
			
		}
	}
	private PackageManager pm;
	private void scanCache() {
		cacheInfos = new ArrayList<CacheInfo>();
		new Thread(){
			public void run() {
				sleepSomeTime(1000);
				List<PackageInfo> infos = pm.getInstalledPackages(0);
				pb_status.setMax(infos.size());
				int step=0;
				for(PackageInfo info :infos){
					String packname = info.packageName;
					
					getPackSize(packname,info.applicationInfo.loadLabel(pm).toString());
					sleepSomeTime(new Random().nextInt(500));
					//
					Message msg = Message.obtain();
					msg.what = SCANING;
					msg.obj=info;
					handler.sendMessage(msg);
					pb_status.setProgress(++step);
				}
				//通知
				Message msg = Message.obtain();
				msg.what=SCAN_FINISH;
				handler.sendMessage(msg);
			}

			private void sleepSomeTime(long t) {
				try {
					Thread.sleep(t);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	private void getPackSize(String packname,String appName){
		try {
			Method method = PackageManager.class.getDeclaredMethod(
					"getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			method.invoke(pm,packname, new MyObserver(appName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class CacheInfo{
		String packname;
		long cache;
		String appName;
	}
	private class MyObserver extends IPackageStatsObserver.Stub {
		private String appName;
		public MyObserver(String appName) {
			this.appName = appName;
		}

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			// 获取大小
			long cache = pStats.cacheSize;
			System.out.println("cache..."+cache);
			if(cache>0){//只关注cache值大于0
				System.out.println("cache>0");
				CacheInfo cacheInfo = new CacheInfo();
				cacheInfo.cache = cache;
				cacheInfo.packname = pStats.packageName;
				cacheInfo.appName = this.appName;
				cacheInfos.add(cacheInfo);
			}
		}

	}

}
