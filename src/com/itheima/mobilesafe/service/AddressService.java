package com.itheima.mobilesafe.service;

import com.itheima.mobilesafe.db.dao.NumberAddressDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
/**
 * 来电归属地显示
 * @author lenovo
 *
 */
public class AddressService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	private TelephonyManager tm;
	private MyListener listener;
	private class InnerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();
			//来电显示
			//使用代码来注册广播接收者
			//可以在清单文件里注册,也可以在代码里注册
			String address = NumberAddressDao.getAddress(number);
			Toast.makeText(context, address, 1).show();
		}
		
	}
	private InnerReceiver receiver;
	
	@Override
	public void onCreate() {
		super.onCreate();
		//服务创建的时候,初始化广播接受者
		receiver= new InnerReceiver();
		//代码注册广播接收者
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		
		tm=(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		//注册监听者,监听呼叫的状态
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	class MyListener extends PhoneStateListener{
		//当电话状态发生变化的时候,调用的方法
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch(state){
			case TelephonyManager.CALL_STATE_IDLE://空闲状态,没有电话打进来
				
				break;
			case TelephonyManager.CALL_STATE_RINGING://响铃状态
				String address = NumberAddressDao.getAddress(incomingNumber);
				Toast.makeText(getApplicationContext(), address, 0).show();
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK://电话已经接通了
				break;
			default:
				break;
			}
		}
	}
	@Override
	public void onDestroy() {
		//注销listener
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
		
		//注销广播接收者
		unregisterReceiver(receiver);
		receiver = null;
	}
}
