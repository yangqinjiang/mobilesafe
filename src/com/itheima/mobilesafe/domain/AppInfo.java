package com.itheima.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {
//图标
	private Drawable appIcon;
	//应用程序名称
	private String appName;
	//包名
	private String packName;
	//是否安装在rom里
	private boolean inRom;
	//用户安装的程序
	private boolean userApp;
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public boolean isInRom() {
		return inRom;
	}
	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}
	public boolean isUserApp() {
		return userApp;
	}
	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	public AppInfo(Drawable appIcon, String appName, String packName,
			boolean inRom, boolean userApp) {
		super();
		this.appIcon = appIcon;
		this.appName = appName;
		this.packName = packName;
		this.inRom = inRom;
		this.userApp = userApp;
	}
	@Override
	public String toString() {
		return "AppInfo [" +   " appName=" + appName
				+ ", packName=" + packName + ", inRom=" + inRom + ", userApp="
				+ userApp + "]";
	}
	
	
}
