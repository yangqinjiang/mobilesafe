package com.itheima.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
/**
 * 启动时接收广播
 * @author lenovo
 *
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	private static final String TAG = "BootCompleteReceiver";
	private TelephonyManager tm;
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG,"手机启动了");
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		//检查手机防盗保护是否开启了
		boolean protecting = sp.getBoolean("protecting", false);
		if(protecting){
			//检查当前的sim号和我原来绑定的sim号是否一致
			tm =(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			//得到当前手机里面安装的sim号
			String realSim = tm.getSimSerialNumber() +"1";
			//得到原来绑定的sim 卡号
			String savedSim = sp.getString("sim","");
			if(!savedSim.equals(realSim)){
				//sim卡被更换了,手机可能换了,发送报警短信
				SmsManager smsManager = SmsManager.getDefault();
				String safenumber = sp.getString("safenumber", "");
				smsManager.sendTextMessage(safenumber, null, "sim changed!!!", null, null);
				
			}
		}
		
	}

}
