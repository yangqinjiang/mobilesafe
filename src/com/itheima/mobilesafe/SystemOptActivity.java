package com.itheima.mobilesafe;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class SystemOptActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tabhost);
		//父窗体窗器
		TabHost mTabHost = getTabHost();
		//子控件,(标签,内容)
		TabSpec tab1 = mTabHost.newTabSpec("tab1");
		TabSpec tab2 = mTabHost.newTabSpec("tab2");
		TabSpec tab3 = mTabHost.newTabSpec("tab3");
		//内容
		tab1.setIndicator("功能1");
		tab2.setIndicator("功能2");
		tab3.setIndicator("功能3");
		
		tab1.setContent(new Intent(this,CleanCacheActivity.class));
		tab2.setContent(new Intent(this,CleanSDActivity.class));
		tab3.setContent(new Intent(this,Test3Activity.class));
		//把三个子控件加入到父窗体窗器
		mTabHost.addTab(tab1);
		mTabHost.addTab(tab2);
		mTabHost.addTab(tab3);
		
	}
	
}
