package com.itheima.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.itheima.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
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
		filter.setPriority(Integer.MAX_VALUE);//最高优先级
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
		public void onCallStateChanged(int state, final String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				//检查号码是否有黑名单号码
				String mode = dao.findMode(incomingNumber);//1:电话拦截,2,短信拦截,3:全部拦截
				if("1".equals(mode)|| "3".equals(mode)){
					Log.i(TAG, "发现黑名单电话,拦截");
					//在这里挂断电话
					endCall();
					//清除通话记录,通话记录不是立即生成的,需要一定的时间,
					//
					//deleteCallLog(incomingNumber);
					//需要注册内容观察者,观察呼叫记录的变化
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true,new ContentObserver(new Handler()){
						@Override
						public void onChange(boolean selfChange) {
							getContentResolver().unregisterContentObserver(this);//注册自身的观察
							deleteCallLog(incomingNumber);
						}
					});
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				
				break;
			default:
				break;
			}
		}


	}
	/**
	 * 清除黑名单的中呼叫记录
	 * @param incomingNumber
	 */
	private void deleteCallLog(String incomingNumber){
		ContentResolver resolver = getContentResolver();
		Uri uri = Uri.parse("content://call_log/calls");
		
		resolver.delete(uri, "number=?",new String[]{incomingNumber});
	}
	/**
	 * 挂断电话
	 */
	private void endCall() {
		//反射serviceManager挂断电话,使用系统隐藏的服务
		
		String className="android.os.ServiceManager";
		try {
			Class clazz = CallSmsSafeService.class.getClassLoader().loadClass(className);
			Method method =clazz.getMethod("getService",String.class);
			IBinder iBinder =(IBinder)method.invoke(null, TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(iBinder).endCall();//手动挂断电话
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
