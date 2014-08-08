package com.itheima.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobilesafe.db.AppLockDBOpenHelper;

/**
 * 程序锁的数据库dao 提供增删查的方法
 * 
 */
public class AppLockDao {

	private AppLockDBOpenHelper helper;

	public AppLockDao(Context context) {
		helper = new AppLockDBOpenHelper(context);
	}

	/**
	 * 添加一条应用程序包名
	 * 
	 * @param packname
	 *            应用程序包名
	 */
	public boolean add(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		long rowid = db.insert("applock", null, values);
		db.close();
		return rowid != -1;
	}

	/**
	 * 删除一条应用程序包名
	 * 
	 * @param packname
	 *            应用程序包名
	 */
	public boolean delete(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rawCount = db.delete("applock", "packname=?",
				new String[] { packname });
		db.close();
		return rawCount > 0;
	}


	/**
	 * 查询一条应用程序包名
	 * 
	 * @param packname
	 *           应用程序包名
	 */
	public boolean find(String packname) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("applock", null, "packname=?",
				new String[] { packname }, null, null, null);
		boolean result = cursor.moveToNext();
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * 查询所有应用程序包名
	 * 
	 * @param packname
	 *           应用程序包名
	 */
	public List<String> findAll() {
		List<String> results =new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("applock", new String[]{"packname"},null,
				null, null, null, null);
		while(cursor.moveToNext()){
			results.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return results;
	}
}
