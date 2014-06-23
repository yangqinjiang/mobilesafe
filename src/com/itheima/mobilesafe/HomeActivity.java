package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {

	private static final String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "系统优化", "高级工具", "设置中心" };
	private static final int[] icons = { R.drawable.safe,
			R.drawable.callmsgsafe, R.drawable.app,R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };
	private GridView gv_home;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
		setContentView(R.layout.activity_home);
		gv_home = (GridView) findViewById(R.id.gv_home);
		gv_home.setAdapter(new HomeAdapter());
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent ; 
				 switch(position){
				 case 8://设置中心Activity
					 intent = new Intent(HomeActivity.this,SettingCenterActivity.class);
					 startActivity(intent);
					 break;
				 }
			}
		});
	}

	//
	private class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
/**
 * Drawable img = getResources().getDrawable(mImageViewArray[index]);  
        img.setBounds(0,0,img.getIntrinsicHeight()-5,img.getIntrinsicWidth());//img.get, img.getIntrinsicHeight());//(left, top, right, bottom);  
        textView.setCompoundDrawables(null, img, null, null);  
 */
		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			//填充view
			View view = View.inflate(getApplicationContext(), R.layout.list_home_item, null);
			TextView tv_name = (TextView)view.findViewById(R.id.tv_home_name);
			tv_name.setText(names[position]);
			Drawable img = getResources().getDrawable(icons[position]);  
	        img.setBounds(0,0,img.getIntrinsicHeight()-5,img.getIntrinsicWidth());
			tv_name.setCompoundDrawables(null, img, null,null);
			//ImageView iv_icon = (ImageView)view.findViewById(R.id.iv_home_icon);
			//iv_icon.setImageResource(icons[position]);
			return view;
		}

	}
}
