package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
/**
 * 设置向导的父类Activity
 *
 */
public abstract class SetupBaseActivity extends Activity {
	protected static final String TAG = "SetupBaseActivity";
	protected SharedPreferences sp;
	private GestureDetector mGestureDetector;//声明手势识别器
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		sp = getSharedPreferences("config", MODE_PRIVATE);
		initView();
		//生成
		mGestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener(){
			/**
			 * 当手指在触摸滑动的时候,调用的方法
			 * @param e1 手指第一次触摸屏幕
			 * @param e2 手指离开屏幕一瞬间
			 * @param velocityX 手指水平方向移动的速度
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				if(e1.getRawX() - e2.getRawX() > 200){
					Log.i(TAG, "next");
					next(null);//显示下一个界面
					return true;//终止当前的触摸事件
				}else if(e2.getRawX() - e1.getRawX() > 200){
					pre(null);//显示上一个界面
					Log.i(TAG, " pre");
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);//让手势识别器生效
		return super.onTouchEvent(event);
	}
	/**
	 * 初始化当前activity显示的内容
	 */
	public abstract void initView();
	
	public abstract void next(View view);
	public abstract void pre(View view);
	
}
