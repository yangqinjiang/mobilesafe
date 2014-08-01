package com.itheima.mobilesafe.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

/**
 * 短信工具
 * @author lenovo
 *
 */
public class SmsTools {

	/**
	 * 备份短信,到xml文件
	 * @param context
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 */
	public static void backUpSms(Context context,ProgressDialog pd) throws Exception{
		XmlSerializer serializer = Xml.newSerializer();
		File file = new File(Environment.getExternalStorageDirectory(),"backup.xml");
		FileOutputStream os = new FileOutputStream(file);
		serializer.setOutput(os, "utf-8");
		serializer.startDocument("utf-8",true);//utf-8编码,独立文件
		//头标签 <smss>
		serializer.startTag(null,"smss");
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms");
		String[] projection = new String[]{"address",//短信地址
										"date",//发送时间
										"type",//发送或者是接收
										"body"};//短信内容
		Cursor cursor = resolver.query(uri , projection , null,null,null);
		//
		pd.setMax(cursor.getCount());
		int progress = 0;
		while(cursor.moveToNext()){
			Thread.sleep(1000);
			serializer.startTag(null,"sms");
			
			serializer.startTag(null,"address");
			String address = cursor.getString(0);
			serializer.text(address);
			serializer.endTag(null,"address");
			
			serializer.startTag(null,"date");
			String date = cursor.getString(1);
			serializer.text(date);
			serializer.endTag(null,"date");
			
			serializer.startTag(null,"type");
			String type = cursor.getString(2);
			serializer.text(type);
			serializer.endTag(null,"type");
			
			serializer.startTag(null,"body");
			String body=cursor.getString(3);
			serializer.text(body);
			serializer.endTag(null,"body");
			
			serializer.endTag(null,"sms");
			progress++;
			pd.setProgress(progress);
		}
		cursor.close();
		serializer.endTag(null, "smss");
		serializer.endDocument();
		os.close();
	}
	
}
