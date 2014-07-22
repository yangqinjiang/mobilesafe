package com.itheima.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressDao {
	// File Dir :/data/data/com.itheima.mobilesafe/files

	private static final String dbpath = "/data/data/com.itheima.mobilesafe/files/address.db";

	/**
	 * 获取电话号码的归属地
	 * 
	 * @param number
	 *            电话号码
	 * @return 归属地
	 */
	public static String getAddress(String number) {

		String location = number;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbpath, null,
				SQLiteDatabase.OPEN_READONLY);
		// 判断号码类型
		// 手机11位,都是1开头的,13x 14x 15x 18x 9位数字
		String exp = "^1[3458]\\d{9}$";
		if (number.matches(exp)) {
			String sql = "select location from data2 where id = (select outkey from data1 where id =?)";
			Cursor cursor = db.rawQuery(sql,
					new String[] { number.substring(0, 7) });
			if (cursor.moveToNext()) {
				location = cursor.getString(0);
			}
			cursor.close();
		}else{//其他号码
			switch(number.length()){
			case 3:
				location = "报警电话";
			case 4:
				location = "模拟器";
				break;
			case 5:
				location = "客服电话";
				break;
			case 7:
			case 8:
				location = "本地电话";
			break;
			default:
				if(number.length()>=10&&number.startsWith("0")){
					//试着去查询号码的前3位或前4位,
					String sql = "select location from data2 where area = ?";
					//前3位
					Cursor cursor = db.rawQuery(sql, new String[]{number.substring(1,3)});
					if(cursor.moveToNext()){
						String address = cursor.getString(0);
						location = address.substring(0,address.length()-2);
					}
					cursor.close();
					//前4位
					cursor = db.rawQuery(sql, new String[]{number.substring(1,4)});
					if(cursor.moveToNext()){
						String address = cursor.getString(0);
						location = address.substring(0,address.length()-2);
					}
					cursor.close();
				}
				break;
			}
			
		}
		db.close();
		return location;
	}
}