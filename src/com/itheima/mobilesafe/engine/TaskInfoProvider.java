package com.itheima.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.domain.TaskInfo;

public class TaskInfoProvider {

	/**
	 * 获取所有正在运行的进程
	 * @param context
	 * @return
	 */
	public static List<TaskInfo> getTaskInfos(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<RunningAppProcessInfo> infos=am.getRunningAppProcesses();
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		for(RunningAppProcessInfo info :infos){
			String processName = info.processName;//进程的包名
			TaskInfo ti = new TaskInfo();
			ti.setPackName(processName);
			//获取程序占用的内存
			long memsize = am.getProcessMemoryInfo(new int[]{info.pid})[0].getTotalPrivateDirty()*1024;
			ti.setMemSize(memsize);
			ApplicationInfo ai;
			try {
				 ai = pm.getApplicationInfo(processName, 0);
				ti.setAppIcon(ai.loadIcon(pm));
				ti.setAppName(ai.loadLabel(pm).toString());
				
				ti.setUserTask((ai.flags & ApplicationInfo.FLAG_SYSTEM)==0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				//设置默认图标
				ti.setAppIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
				//设置默认名称
				ti.setAppName(processName);
			}
			taskInfos.add(ti);
		}
		return taskInfos;
	}
}
