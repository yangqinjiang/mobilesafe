package com.itheima.mobilesafe;

import com.itheima.mobilesafe.db.dao.CommonNumberDao;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
	private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_number);
		elv = (ExpandableListView) findViewById(R.id.elv);
		db = SQLiteDatabase.openDatabase(CommonNumberDao.DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
		// adapter
		elv.setAdapter(new CommonNumberAdapter());
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(null!=db){
			db.close();
			db=null;
		}
	}

	class CommonNumberAdapter extends BaseExpandableListAdapter {

		private static final String TAG = "CommonNumberAdapter";

		// 得到分组的数量
		@Override
		public int getGroupCount() {
			return CommonNumberDao.getGroupCount(db);
		}

		// 得到里面孩子的数量
		@Override
		public int getChildrenCount(int groupPosition) {
			return CommonNumberDao.getChildCountByPostion(groupPosition, db);
		}
		/**
		 * 获取某个分组的view对象,获取某个分组条目显示的内容
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView tv = null;
			if(null!=convertView && convertView instanceof TextView){
				tv = (TextView)convertView;
				Log.i(TAG, "non create textview");
			}else{
				tv = new TextView(getApplicationContext());
				Log.i(TAG, "create textview");
			}
			String name = CommonNumberDao.getGroupNameByPosition(groupPosition, db);
			tv.setText("        "+name);
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
			TextView tv = null;
			if(null!=convertView && convertView instanceof TextView){
				tv = (TextView)convertView;
				Log.i(TAG, "non create textview");
			}else{
				Log.i(TAG, "create textview");
				tv = new TextView(getApplicationContext());
			}
			String name = CommonNumberDao.getChildNameByPosition(groupPosition, childPosition, db);
			tv.setText("               "+name);
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
