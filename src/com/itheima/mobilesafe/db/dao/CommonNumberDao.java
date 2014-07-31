package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CommonNumberDao {

	public static final String DB_PATH="/data/data/com.itheima.mobilesafe/files/commonnum.db";
	private static final String TAG = "CommonNumberDao";
	/**
	 * 得到有多少个分组
	 * @param db TODO
	 */
	public static int getGroupCount(SQLiteDatabase db){
		Cursor cursor = db.rawQuery("select count(*) from classlist",null);
		cursor.moveToNext();
		int groupCount = cursor.getInt(0);
		cursor.close();
		return groupCount;
	}
	/**
	 * 根据分组的id得到分组下的孩子
	 * @param db TODO
	 */
	public static int getChildCountByPostion(int groupPosition, SQLiteDatabase db){
		//第0个分组对应table1
		groupPosition++;
		Cursor cursor = db.rawQuery("select count(*) from table"+groupPosition,null);
		cursor.moveToNext();
		int groupCount = cursor.getInt(0);
		cursor.close();
		return groupCount;
	}
	/**
	 * 获取某个分组的名称
	 * @param groupPosition
	 * @param db TODO
	 * @return
	 */
	public static String getGroupNameByPosition(int groupPosition, SQLiteDatabase db){
		Log.i(TAG, "getGroupNameByPosition");
		groupPosition++;
		String sql = "select name from classlist where idx=?";
		Cursor cursor = db.rawQuery(sql,new String[]{groupPosition+""});
		String name="";
		if(cursor.moveToNext()){
			name = cursor.getString(0);
		}
		cursor.close();
		return name;
	}
	/**
	 * select name,number from table1 where _id='1'
	 * 获取某个分组的某个孩子的名称
	 * @param groupPosition
	 * @param childPosition
	 * @param db TODO
	 * @return
	 */
	public static String getChildNameByPosition(int groupPosition,int childPosition, SQLiteDatabase db){
		Log.i(TAG, "getChildNameByPosition");
		groupPosition++;
		childPosition++;
		String sql = "select name,number from table"+groupPosition +" where _id=?";
		Cursor cursor = db.rawQuery(sql,new String[]{childPosition+""});
		String name="";
		if(cursor.moveToNext()){
			name = cursor.getString(0) + "\n               "+cursor.getString(1);
		}
		cursor.close();
		return name;
	}
}
