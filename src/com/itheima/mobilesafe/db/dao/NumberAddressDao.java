package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressDao {
	//File Dir :/data/data/com.itheima.mobilesafe/files

	private static final String dbpath = "/data/data/com.itheima.mobilesafe/files/address.db";
	/**
	 * 获取电话号码的归属地
	 * 
	 * @param number 电话号码
	 * @return 归属地
	 */
	public static String getAddress(String number) {
		
		String location = number;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.OPEN_READONLY);
		String sql = "select location from data2 where id = (select outkey from data1 where id =?)";
		Cursor cursor = db.rawQuery(sql, new String[]{number.substring(0,7)});
		if(cursor.moveToNext()){
			location = cursor.getString(0);
		}
		cursor.close();db.close();
		return location;
	}
}
/**
 * android.database.sqlite.SQLiteException: unable to open database file
at android.database.sqlite.SQLiteDatabase.dbopen(Native Method)
at android.database.sqlite.SQLiteDatabase.<init>(SQLiteDatabase.java:1849)
at android.database.sqlite.SQLiteDatabase.openDatabase(SQLiteDatabase.java:820)
at com.itheima.mobilesafe.db.dao.NumberAddressDao.getAddress(NumberAddressDao.java:16)
at com.itheima.mobilesafe.test.NumberAddressDaoTest.testGetAddress(NumberAddressDaoTest.java:22)
at java.lang.reflect.Method.invokeNative(Native Method)
at android.test.InstrumentationTestCase.runMethod(InstrumentationTestCase.java:204)
at android.test.InstrumentationTestCase.runTest(InstrumentationTestCase.java:194)
at android.test.AndroidTestRunner.runTest(AndroidTestRunner.java:169)
at android.test.AndroidTestRunner.runTest(AndroidTestRunner.java:154)
at android.test.InstrumentationTestRunner.onStart(InstrumentationTestRunner.java:529)
at android.app.Instrumentation$InstrumentationThread.run(Instrumentation.java:1448)


 * 
 */
