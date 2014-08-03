package com.itheima.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima.mobilesafe.db.BlackNumberDBOpenHelper;
import com.itheima.mobilesafe.db.domain.BlackNumberInfo;

/**
 * 黑名单的数据库dao 提供增删改查的方法
 * 
 */
public class BlackNumberDao {

	private BlackNumberDBOpenHelper helper;

	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}

	/**
	 * 添加一条黑名单号码
	 * 
	 * @param phone
	 *            黑名单号码
	 * @param mode
	 *            拦截方式 1 电话拦截 2 短信拦截 3 全部拦截
	 */
	public boolean add(String phone, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		long rowid = db.insert("blacknumber", null, values);
		db.close();
		return rowid != -1;
	}

	/**
	 * 删除一条黑名单号码
	 * 
	 * @param phone
	 *            黑名单号码
	 */
	public boolean delete(String phone) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rawCount = db.delete("blacknumber", "phone=?",
				new String[] { phone });
		db.close();
		return rawCount > 0;
	}

	/**
	 * 修改一条黑名单号码的拦截模式
	 * 
	 * @param phone
	 *            黑名单号码
	 */
	public boolean update(String phone, String newMode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newMode);
		int rowCount = db.update("blacknumber", values, "phone=?",
				new String[] { phone });
		db.close();
		return rowCount > 0;
	}

	/**
	 * 查询一条黑名单号码是否存在
	 * 
	 * @param phone
	 *            黑名单号码
	 */
	public boolean find(String phone) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", null, "phone=?",
				new String[] { phone }, null, null, null);
		boolean result = cursor.moveToNext();
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * 查询一条黑名单号码是否存在
	 * 
	 * @param phone
	 *            黑名单号码
	 * @return null 不是黑名单号码  1电话拦截,2:短信拦截,3,全部拦截
	 */
	public String findMode(String phone) {
		String mode =null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", new String[]{"mode"}, "phone=?",
				new String[] { phone }, null, null, null);
		if(cursor.moveToNext()){
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return mode;
	}

	/**
	 * 查询全部的号码,比较消耗时间
	 * 
	 * @return
	 */
	public List<BlackNumberInfo> findAll() {
		List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber",
				new String[] { "phone", "mode" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			infos.add(new BlackNumberInfo(cursor.getString(0), cursor
					.getString(1)));
		}
		cursor.close();
		db.close();
		return infos;
	}

	/**
	 * 查询部分的号码,使用select phone,mode from blacknumber limit maxNumber offset startIndex
	 * 
	 * @param startIndex
	 *            从哪个位置开始获取数据
	 * @param manNumber
	 *            最多获取多少条数据
	 * @return
	 */
	public List<BlackNumberInfo> findPart(int startIndex, int maxNumber) {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select phone,mode from blacknumber order by _id desc limit ? offset ?",
				new String[] { String.valueOf(maxNumber),
						String.valueOf(startIndex) });
		while (cursor.moveToNext()) {
			infos.add(new BlackNumberInfo(cursor.getString(0), cursor
					.getString(1)));
		}
		cursor.close();
		db.close();
		return infos;
	}
	/**
	 * 分布查询号码
	 * 
	 * @param pageNumber
	 *            页码
	 *            页码1 0~19
	 *            页码2 20~39
	 *            页码n (n-1)*20 ~n*20-1
	 * @param manNumber
	 *            最多获取多少条数据
	 * @return
	 */
	public List<BlackNumberInfo> findPartByPage(int pageNumber, int maxNumber) {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select phone,mode from blacknumber order by _id desc limit ? offset ? ",
				new String[] { String.valueOf(maxNumber),
						String.valueOf((pageNumber-1)*maxNumber) });
		while (cursor.moveToNext()) {
			infos.add(new BlackNumberInfo(cursor.getString(0), cursor
					.getString(1)));
		}
		cursor.close();
		db.close();
		return infos;
	}
	/**
	 * 查询部分的号码,使用select phone,mode from blacknumber limit maxNumber offset startIndex
	 * 
	 * @param startIndex
	 *            从哪个位置开始获取数据
	 * @param manNumber
	 *            最多获取多少条数据
	 * @return
	 */
	public int getTotalCount() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select count(*) from blacknumber",
				null);
		cursor.moveToNext();
		int count = cursor.getInt(0);
		
		cursor.close();
		db.close();
		return count;
	}
}
