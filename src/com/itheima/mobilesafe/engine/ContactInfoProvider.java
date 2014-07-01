package com.itheima.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.itheima.mobilesafe.domain.ContactInfo;

public class ContactInfoProvider {
	private static final String TAG = "ContactInfoProvider";

	public static List<ContactInfo> getContactInfo(Context context) {
		// 联系人的数据库 是私有的数据库 只能通过内容提供者来获取数据
		ContentResolver resolver = context.getContentResolver();
		// 查询raw_contact表 获取联系人的id
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(
				Uri.parse("content://com.android.contacts/raw_contacts"),
				new String[] { "contact_id" }, null, null, null);
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			Log.i(TAG,"联系人的id:" + id);
			if (id != null) {//防止在某个联系人被删除后,出现空的id null
				ContactInfo info = new ContactInfo();
				Cursor dataCursor = resolver.query(dataUri, new String[] {
						"mimetype", "data1" }, "raw_contact_id=?",
						new String[] { id }, null);
				while (dataCursor.moveToNext()) {
					String mimetype = dataCursor.getString(0);
					String data1 = dataCursor.getString(1);
					if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
						// 电话
						info.setPhone(data1);
					} else if ("vnd.android.cursor.item/name".equals(mimetype)) {
						// 姓名
						info.setName(data1);
					}
				}
				Log.i(TAG, info.toString());
				dataCursor.close();
				contactInfos.add(info);
			}
		}
		cursor.close();
		return contactInfos;
	}
}
