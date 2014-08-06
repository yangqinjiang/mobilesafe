package com.itheima.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.itheima.mobilesafe.domain.AppInfo;

/**
 * 获取手机里面安装的应用程序的业务类
 * @author lenovo
 *
 */
public class AppInfoProvider {

	/**
	 * 获取程序信息
	 * @param context
	 * @return
	 */
	public static  List<AppInfo> getAppInfos(Context context){
		PackageManager pm = context.getPackageManager();//获取包管理器
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);//获取已经安装的包信息
		List<AppInfo> appInfos = new ArrayList<AppInfo>(); 
		for(PackageInfo packInfo:packInfos){
			String packageName = packInfo.packageName;//程序的包名
			Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);//图标
			String appName = packInfo.applicationInfo.loadLabel(pm).toString();//程序名称
			int flags = packInfo.applicationInfo.flags;
			boolean isUserApp = true;
			if((flags & ApplicationInfo.FLAG_SYSTEM)!=0){
				//系统应用
				isUserApp=false;
			}else{
				//用户应用			
				isUserApp=true;
			}
			boolean inRom = true;
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)!=0){
				//装在sd卡
				inRom = false;
			}else{
				//装在系统内存
				inRom = true;
			}
			appInfos.add(new AppInfo(appIcon,appName,packageName,inRom,isUserApp));
		}
		return appInfos;
	}
}
