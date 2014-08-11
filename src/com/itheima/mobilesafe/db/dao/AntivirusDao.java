package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntivirusDao {

	public static final String DB_PATH = "/data/data/com.itheima.mobilesafe/files/antivirus.db";

	// select desc from datable where md5='..........'

	public static String findVirus(String md5) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select desc from datable where md5=?",
				new String[] { md5 });
		String desc = null;
		if (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return desc;
	}
}
