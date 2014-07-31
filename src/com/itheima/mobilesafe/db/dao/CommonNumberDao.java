package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumberDao {

	private static final String path="/data/data/com.itheima.mobilesafe/files/commonnum.db";
	/**
	 * 得到有多少个分组
	 */
	public static int getGroupCount(){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select count(*) from classlist",null);
		cursor.moveToNext();
		int groupCount = cursor.getInt(0);
		cursor.close();
		db.close();
		return groupCount;
	}
	/**
	 * 根据分组的id得到分组下的孩子
	 */
	public static int getChildCountByPostion(int groupPosition){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		//第0个分组对应table1
		groupPosition++;
		Cursor cursor = db.rawQuery("select count(*) from table"+groupPosition,null);
		cursor.moveToNext();
		int groupCount = cursor.getInt(0);
		cursor.close();
		db.close();
		return groupCount;
	}
	/**
	 * 获取某个分组的名称
	 * @param groupPosition
	 * @return
	 */
	public static String getGroupNameByPosition(int groupPosition){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		groupPosition++;
		String sql = "select name from classlist where idx=?";
		Cursor cursor = db.rawQuery(sql,new String[]{groupPosition+""});
		String name="";
		if(cursor.moveToNext()){
			name = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return name;
	}
	/**
	 * select name,number from table1 where _id='1'
	 * 获取某个分组的某个孩子的名称
	 * @param groupPosition
	 * @param childPosition
	 * @return
	 */
	public static String getChildNameByPosition(int groupPosition,int childPosition){
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		groupPosition++;
		childPosition++;
		String sql = "select name,number from table"+groupPosition +" where _id=?";
		Cursor cursor = db.rawQuery(sql,new String[]{childPosition+""});
		String name="";
		if(cursor.moveToNext()){
			name = cursor.getString(0) + "\n"+cursor.getString(1);
		}
		cursor.close();
		db.close();
		return name;
	}
}
