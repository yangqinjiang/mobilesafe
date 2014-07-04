package com.itheima.mobilesafe.service;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.TextView;
import android.widget.Toast;

public class GPSService extends Service {
	private LocationManager lm;
	private MyListener listener;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		List<String> names = lm.getAllProviders();
		for (String name : names) {
			System.out.println(name);
		}
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 获取精确位置
		criteria.setAltitudeRequired(true);// 设置海拔敏感
		criteria.setCostAllowed(true);// gps是否允许产生费用
		criteria.setPowerRequirement(Criteria.POWER_HIGH);

		String provider = lm.getBestProvider(criteria, true);
		System.out.println("最好的位置提供者:" + provider);
		if (null != provider) {
			listener = new MyListener();
			lm.requestLocationUpdates(provider, 60000, 0, listener);
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(null!=listener){
			lm.removeUpdates(listener);
			listener = null;
		}
	}

	private class MyListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {// 位置变化时,参数是新的location对象
			String longitude = "jingdu:" + location.getLongitude();
			String latitude = "weidu:" + location.getLatitude();
			String accuracy = "jingquedu:" + location.getAccuracy();
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			Editor editor = sp.edit();
			String text = longitude + "\n" + latitude + "\n"+accuracy;
			
			editor.putString("lastlocation", text);
			editor.commit();
//			//发短信给安全号码
//			SmsManager smsManager = SmsManager.getDefault();
//
//			smsManager.sendTextMessage(sp.getString("safenumber", ""), null, text,null,null);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

	}
}
