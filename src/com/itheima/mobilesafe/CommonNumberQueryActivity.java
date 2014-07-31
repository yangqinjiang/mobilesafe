package com.itheima.mobilesafe;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * 常用号码查询
 * 
 * @author lenovo
 * 
 */
public class CommonNumberQueryActivity extends Activity {

	private ExpandableListView elv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_number);
		elv = (ExpandableListView) findViewById(R.id.elv);
		// adapter
		elv.setAdapter(new CommonNumberAdapter());
	}

	class CommonNumberAdapter extends BaseExpandableListAdapter {

		// 得到分组的数量
		@Override
		public int getGroupCount() {
			return 5;
		}

		// 得到里面孩子的数量
		@Override
		public int getChildrenCount(int groupPosition) {
			return 8;
		}
		/**
		 * 获取某个分组的view对象,获取某个分组条目显示的内容
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView tv = new TextView(getApplicationContext());
			tv.setText("      哈哈我是一个分组,我的id为:"+groupPosition);
			tv.setTextColor(Color.RED);
			tv.setTextSize(20);
			return tv;
		}
		/**
		 * 获取某个分组的某个孩子的view对象,获取某个分组的某个孩子条目显示的内容
		 */
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView tv = new TextView(getApplicationContext());
			tv.setText("我是第"+groupPosition+"分组,第"+childPosition+"个孩子");
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(14);
			return tv;
		}

		// 返回分组对应的数据
		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		// 返回某个分组里的某个孩子的数据
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		// 返回分组的id
		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		// 返回某个分组的某个孩子的id
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

	}
}
