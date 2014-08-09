package com.itheima.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class TaskInfo {

	private String appName;
	private long memSize;
	private Drawable appIcon;
	private String packName;
	/**
	 * 用户进程
	 */
	private boolean userTask;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public long getMemSize() {
		return memSize;
	}
	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public boolean isUserTask() {
		return userTask;
	}
	public void setUserTask(boolean userTask) {
		this.userTask = userTask;
	}
	public TaskInfo(String appName, long memSize, Drawable appIcon,
			String packName, boolean userTask) {
		super();
		this.appName = appName;
		this.memSize = memSize;
		this.appIcon = appIcon;
		this.packName = packName;
		this.userTask = userTask;
	}
	public TaskInfo() {
	}
	@Override
	public String toString() {
		return "TaskInfo [appName=" + appName + ", memSize=" + memSize
				+ ", packName=" + packName + "]";
	}
	
	
}
