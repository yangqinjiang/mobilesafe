package com.itheima.mobilesafe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AppManagerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_app_manager);
		tv_avail_rom=(TextView)findViewById(R.id.tv_avail_rom);
		tv_avail_sd=(TextView)findViewById(R.id.tv_avail_sd);
		lv_app_manager=(ListView)findViewById(R.id.lv_app_manager);
		//
		tv_avail_rom.setText("内存可用:"+getAvailRom());//可用内存大小
		tv_avail_sd.setText("SD卡可用:"+getAvailSD());
		
		ll_loading =(LinearLayout)findViewById(R.id.ll_loading);
		allAppInfos = new ArrayList<AppInfo>();
		fillData();
	}
	private List<AppInfo> allAppInfos;
	//用户程序集合
	private List<AppInfo> userAppInfos;
	//系统程序集合
	private List<AppInfo> systemAppInfos;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);//隐藏
			lv_app_manager.setAdapter(new AppManagerAdapter());
		};
	};
	
	
	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);//显示
		new Thread(){
			public void run() {
				allAppInfos = AppInfoProvider.getAppInfos(getApplicationContext());
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				for(AppInfo appinfo:allAppInfos){
					if(appinfo.isUserApp()){
						userAppInfos.add(appinfo);
					}else{
						systemAppInfos.add(appinfo);
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	/**
	 * 获取手机可用内存空间的大小
	 * @return
	 */
	private String getAvailRom(){
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long size = blockSize*availableBlocks;
		return Formatter.formatFileSize(this, size);
	}
	
	/**
	 * 获取手机可用q外部空间的大小
	 * @return
	 */
	private String getAvailSD(){
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long size = blockSize*availableBlocks;
		return Formatter.formatFileSize(this, size);
	}
	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	private ListView lv_app_manager;
	private LinearLayout ll_loading;
	private class AppManagerAdapter extends BaseAdapter{

		@Override
		public int getCount() {
//			return allAppInfos.size();
			return userAppInfos.size()+systemAppInfos.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			AppInfo appInfo ;//
			
			if(null!=convertView){
				view = convertView;
				holder = (ViewHolder)view.getTag();
			}else{
				view = View.inflate(getApplicationContext(), R.layout.list_appinfo_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView)view.findViewById(R.id.iv_app_icon);
				holder.tv_name=(TextView)view.findViewById(R.id.tv_app_name);
				holder.tv_location=(TextView)view.findViewById(R.id.tv_app_location);
				view.setTag(holder);
			}
			//判断位置,如果位置小于用户集合的大小
			if(position<userAppInfos.size()){
				//用户程序
				appInfo = userAppInfos.get(position);
			}else{
				//有偏移位置
				appInfo = systemAppInfos.get(position-userAppInfos.size());
			}
			holder.iv_icon.setImageDrawable(appInfo.getAppIcon());
			holder.tv_name.setText(appInfo.getAppName());
			if(appInfo.isInRom()){
				holder.tv_location.setText("手机内存");
			}else{
				holder.tv_location.setText("外部内存");
			}
			return view;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		
	}
	static class ViewHolder{
		TextView tv_name;
		TextView tv_location;
		ImageView iv_icon;
	}
}
