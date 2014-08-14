package com.itheima.mobilesafe.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 读取用户日志的服务
 * android.permission.READ_LOGS 权限
 *
 */
public class ReadLogService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		//在子线程里运行
		new Thread(){
			public void run() {
				try {
					Process process = Runtime.getRuntime().exec("logcat");
					InputStream is = process.getInputStream();//拿到输入的信息
					BufferedReader br = new BufferedReader(new InputStreamReader(is));
					String line;
					File file = new File(getCacheDir(),"log.txt");
					FileOutputStream fos = new FileOutputStream(file);
					while((line=br.readLine())!=null){//不断地读取数据
						//不能直接输出到控制台
						//只关心Info
						if(line.contains("I/ActivityManager")){
							fos.write(line.getBytes());
							fos.write("\n".getBytes());
						}
					}
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

}
