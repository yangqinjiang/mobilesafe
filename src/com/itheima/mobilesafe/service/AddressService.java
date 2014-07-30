package com.itheima.mobilesafe.service;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.db.dao.NumberAddressDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;
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
			//Toast.makeText(context, address, 1).show();
			showAddressInWindow(address);
		}


		
	}
	private WindowManager wm;
	private View view;
	WindowManager.LayoutParams params;
	private void showAddressInWindow(String address) {
		params = new WindowManager.LayoutParams();
		//if(view == null){
			view = View.inflate(getApplicationContext(), R.layout.toast_address,null);
			view.setOnTouchListener(new OnTouchListener() {
				int startX;
				int startY;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						startX = (int)event.getRawX();
						startY=(int)event.getRawY();
						break;
					case MotionEvent.ACTION_MOVE:
						int newX =(int)event.getRawX();
						int newY =(int)event.getRawY();
						int dx=newX-startX;
						int dy=newY-startY;
						params.x+=dx;
						params.y+=dy;
						//判断是否移出窗体
						if(params.x<0){
							params.x=0;
						}
						if(params.y<0){
							params.y=0;
						}
						if(params.x>wm.getDefaultDisplay().getWidth()){
							params.x=wm.getDefaultDisplay().getWidth()-50;
						}
						if(params.y>wm.getDefaultDisplay().getHeight()){
							params.y=wm.getDefaultDisplay().getHeight()-80;
						}
						wm.updateViewLayout(view, params);//更新位置
						startX = (int)event.getRawX();
						startY=(int)event.getRawY();
						break;
					case MotionEvent.ACTION_UP:
						//保存位置
						Editor edit = sp.edit();
						edit.putInt("lastx",params.x);
						edit.putInt("lasty",params.y);
						edit.commit();
						break;
					}
					return true;
				}
			});
			TextView tv = (TextView)view.findViewById(R.id.tv_address);
			tv.setText(address);
			
			
			params.height = WindowManager.LayoutParams.WRAP_CONTENT;
			params.width = WindowManager.LayoutParams.WRAP_CONTENT;
			params.flags = //WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |//不可点击
					//WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |//不获取焦点
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;//点亮屏幕
			
			params.format = PixelFormat.TRANSLUCENT;//半透明
			
			params.windowAnimations =android.R.style.Animation_Toast;//动画效果
			params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;//高级别的Ui//加入权限:android.permission.SYSTEM_ALERT_WINDOW
			
			params.gravity =Gravity.LEFT + Gravity.TOP;//左上对齐
			
			//sp
			params.x =sp.getInt("lastx",0);
			params.y =sp.getInt("lasty",0);
		//}
		wm.addView(view, params);
	}
	private InnerReceiver receiver;
	SharedPreferences sp;
	@Override
	public void onCreate() {
		super.onCreate();
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
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
		sp = getSharedPreferences("config",MODE_PRIVATE);
	}

	class MyListener extends PhoneStateListener{
		//当电话状态发生变化的时候,调用的方法
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch(state){
			case TelephonyManager.CALL_STATE_IDLE://空闲状态,没有电话打进来
				if(null!=view){
					wm.removeView(view);//移除view toast
					view = null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING://响铃状态
				String address = NumberAddressDao.getAddress(incomingNumber);
				//Toast.makeText(getApplicationContext(), address, 0).show();
				showAddressInWindow(address);
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
