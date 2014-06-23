package com.itheima.mobilesafe.engine;

import java.io.InputStream;




import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.itheima.mobilesafe.domain.UpdateInfo;

public class UpdateInfoParser {
	/**
	 * 返回解析后的更新信息
	 * 
	 * @param is
	 * @return
	 */
	public static UpdateInfo getUpdateInfo(InputStream is) {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is,"utf-8");
			int type = parser.getEventType();
			UpdateInfo updateInfo = new UpdateInfo();
			while(type != XmlPullParser.END_DOCUMENT){
				if(type==XmlPullParser.START_TAG){
					if("version".equals(parser.getName())){
						String version = parser.nextText();
						updateInfo.setVersion(version);
					}else if("description".equals(parser.getName())){
						String description = parser.nextText();
						updateInfo.setDescription(description);
					}else if ("apkurl".equals(parser.getName())){
						String apkurl = parser.nextText();
						updateInfo.setApkurl(apkurl);
					}
				}
				type = parser.next();
			}
			is.close();
			return updateInfo;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
