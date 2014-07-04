package com.itheima.mobilesafe.receiver;


import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.service.GPSService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.provider.MediaStore.Audio.Media;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
/**
 * 短信接收者
 * @author lenovo
 *
 */
public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "短信到来了...");
		//判断手机防盗是否处于开启状态
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean protecting = sp.getBoolean("protecting", false);
		if(protecting){
			Object[] objs = (Object[])intent.getExtras().get("pdus");
			for (Object obj :objs) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String sender = smsMessage.getOriginatingAddress();//TODO:暂时不使用
				String body = smsMessage.getMessageBody();
				if("#*location*#".equals(body)){
					Log.i(TAG, "返回手机的位置信息");
					Intent i = new Intent(context, GPSService.class);
					context.startService(i);
					abortBroadcast();
				}else if("#*getgps*#".equals(body)){
					Log.i(TAG, "获取地理位置");//mp3文件放在res/raw/下
					//发短信给安全号码
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(sp.getString("safenumber", ""), null, sp.getString("lastlocation",""),null,null);
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					Log.i(TAG, "播放报警音乐");//mp3文件放在res/raw/下
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					player.setVolume(1.0f, 1.0f);//手机的音量不会影响这个声音
					player.setLooping(false);
					player.start();
					abortBroadcast();
				}else if("#*wipedata*#".equals(body)){
					Log.i(TAG, "清除数据");
					DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);// 设备策略服务
					//是否是设备超级管理员
					ComponentName who = new ComponentName(context, MyAdmin.class);
					if(dpm.isAdminActive(who)){
						dpm.wipeData(0);
					}
					abortBroadcast();
				}else if("#*lockscreen*#".equals(body)){
					Log.i(TAG, "锁定屏幕");
					DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);// 设备策略服务
					//是否是设备超级管理员
					ComponentName who = new ComponentName(context, MyAdmin.class);
					if(dpm.isAdminActive(who)){
						Log.i(TAG, "锁定屏幕");
						dpm.resetPassword("123", 0);
						dpm.lockNow();
					}
					abortBroadcast();
				}//else 
			}
		}
	}

}
