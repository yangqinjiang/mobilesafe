package com.itheima.mobilesafe.service;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
/**
 * 短信的黑名单拦截
 *
 */
public class CallSmsSafeService extends Service {
	private TelephonyManager tm;
private class InnerSmsReceiver extends BroadcastReceiver{

	private static final String TAG = "InnerSmsReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		Object[] objs =(Object[]) intent.getExtras().get("pdus");
		for(Object obj:objs){
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])obj);
			//得到短信的发件人
			String sender = smsMessage.getDisplayOriginatingAddress();
			//检查sender是否在黑名单数据库里面,还要检查他的拦截模式,2sms拦截,3 全部拦截
			String mode = dao.findMode(sender);
			if("2".equals(mode) || "3".equals(mode)){
				Log.i(TAG, "发现黑名单短信,拦截");
				abortBroadcast();//确保权限值最高,第一个接收到短信
			}
			String body = smsMessage.getMessageBody();
			if(body.contains("fapiao")){
				Log.i(TAG, "发现垃圾短信,拦截");
				abortBroadcast();//确保权限值最高,第一个接收到短信
			}
		}
	}
	
}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	private InnerSmsReceiver receiver;
	private BlackNumberDao dao;
	private MyPhoneListener listener;
	@Override
	public void onCreate() {
		tm =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneListener();
		//监听电话呼叫的状态
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		dao = new BlackNumberDao(this);
		receiver = new InnerSmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);//最高优先级
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		
		registerReceiver(receiver, filter);
		
	}
	@Override
		public void onDestroy() {
		unregisterReceiver(receiver);
		receiver=null;
		//
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener=null;
	}
	class MyPhoneListener extends PhoneStateListener{
		private static final String TAG = "MyPhoneListener";

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//检查号码是否有黑名单号码
				String mode = dao.findMode(incomingNumber);//1:电话拦截,2,短信拦截,3:全部拦截
				if("1".equals(mode)|| "3".equals(mode)){
					Log.i(TAG, "发现黑名单电话,拦截");
					//在这里挂断电话
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				
				break;
			default:
				break;
			}
		}
	}

}
