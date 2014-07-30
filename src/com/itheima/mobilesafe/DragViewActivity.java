package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class DragViewActivity extends Activity {
	private final static String  TAG = "DragViewActivity";
	private WindowManager wm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_drag_view);
		wm = getWindowManager();
		iv_drag=(ImageView)findViewById(R.id.iv_drag_view);
		tv_drag_top = (TextView)findViewById(R.id.tv_drag_view_top);
		tv_drag_bottom = (TextView)findViewById(R.id.tv_drag_view_bottom);
		sp = getSharedPreferences("config",MODE_PRIVATE);
		int lastx = sp.getInt("lastx", 0);
		int lasty = sp.getInt("lasty", 0);
		Log.i(TAG, "距离屏幕左边的距离:"+lastx);
		Log.i(TAG, "距离屏幕上边的距离:"+lasty);
		if(lasty>wm.getDefaultDisplay().getHeight()/2){
			tv_drag_top.setVisibility(View.VISIBLE);
			tv_drag_bottom.setVisibility(View.INVISIBLE);
		}else{
			tv_drag_top.setVisibility(View.INVISIBLE);
			tv_drag_bottom.setVisibility(View.VISIBLE);
		}
		
		//设置给iv_drag位置,在onCreate里失效
		//必须是在view对象的第二个阶段时生效
		//oncreate是第一个阶段,view对象还没有被显示到界面上
		//iv_drag.layout(lastx, lasty, lastx+iv_drag.getWidth(), lasty+iv_drag.getHeight());
		//解决方案:在第一阶段
		RelativeLayout.LayoutParams params = (LayoutParams) iv_drag.getLayoutParams();//获取imageView的窗体参数
		params.leftMargin = lastx;
		params.topMargin = lasty;
		iv_drag.setLayoutParams(params);
		//注册双击事件,模拟
		//难点:触摸事件和点击事件同时对应
		//触摸事件和点击事件的区别:
		//点击事件的组合:按下--停留(时间长短)--离开
		//事件传递过程:activity-->layout-->widget
		iv_drag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(firstClickedTime>0){//原来点击了
					long sencondClickedTime = System.currentTimeMillis();
					long dTime = sencondClickedTime -firstClickedTime;
					if(dTime<500){
						Log.i(TAG, "双击事件");
						firstClickedTime =0;
						//双击居中
						int screenWidth =wm.getDefaultDisplay().getWidth();
						int ivWidth = iv_drag.getWidth();
						int l= screenWidth/2-ivWidth/2;
						int t = iv_drag.getTop();
						int r = screenWidth/2+ivWidth/2;
						int b=iv_drag.getBottom();
						iv_drag.layout(l,t,r,b);
						//居中后,保存位置
						Editor edit = sp.edit();//记录位置
						edit.putInt("lastx", iv_drag.getLeft());
						edit.putInt("lasty", iv_drag.getTop());
						edit.commit();
						return ;
					}
				}
				firstClickedTime = System.currentTimeMillis();
			}
		});
		
		//注册Touch事件监听
		iv_drag.setOnTouchListener(new OnTouchListener() {
			//手指的初始位置
			int startX;
			int startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN://手指按下
					Log.i(TAG,"手指按到了屏幕");
					//记录手指在屏幕上的初始坐标
					startX = (int)event.getRawX();
					startY =(int)event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE://移动
					Log.i(TAG,"手指在屏幕上移动");
					
					int newX = (int)event.getRawX();
					//getRawX是当前点击位置到屏幕左边的值
					//getRawY是当前点击位置到屏幕上方的值
					//getX 是当前点击位置到控件左边的值
					//getY是当前点击位置到控件上边的值
					int newY =(int)event.getRawY();
					//计算偏移量
					int dx = newX - startX;
					int dy = newY - startY;
					
					//参数一,是元件左边框到屏幕左边的距离
					//参数二,是元件上边框到屏幕上方的距离
					//参数三,是元件右边框到屏幕左边的距离
					//参数四,是元件上边框到屏幕上方的距离
					int l = iv_drag.getLeft() +dx;
					int t = iv_drag.getTop() +dy;
					int r =iv_drag.getRight()+dx;
					int b =iv_drag.getBottom()+dy;
					//判断位置是否超出窗体
					//wm.getDefaultDisplay是整个显示屏的高度,应该减少一点
					if(l<0 || t<0 ||r>wm.getDefaultDisplay().getWidth() || b>wm.getDefaultDisplay().getHeight()-40){
						break;
					}
					
					//判断新顶部坐标在屏幕上的位置
					Display defaultDisplay = wm.getDefaultDisplay();//获取屏幕分辨率
					//Point outSize = new Point();//高版本的api
					//defaultDisplay.getSize(outSize);//记录
					int screenHeight = defaultDisplay.getHeight();
					if(t>screenHeight/2){//控件在窗体下方
						tv_drag_top.setVisibility(View.VISIBLE);
						tv_drag_bottom.setVisibility(View.INVISIBLE);
					}else{//控件在窗体上方
						tv_drag_top.setVisibility(View.INVISIBLE);
						tv_drag_bottom.setVisibility(View.VISIBLE);						
					}

					//立刻更新控件在窗体上的位置
					iv_drag.layout(l, t, r, b);
					//重新初始化手指的位置
					startX = (int)event.getRawX();
					startY =(int)event.getRawY();
					break;
				case MotionEvent.ACTION_UP://弹起,手指离开屏幕
					Log.i(TAG,"手指离开了屏幕");
					Editor edit = sp.edit();//记录位置
					edit.putInt("lastx", iv_drag.getLeft());
					edit.putInt("lasty", iv_drag.getTop());
					edit.commit();
					break;
				}
				//return true;//返回true,表示已经处理了事件,不会处理点击事件了
				return false;//false,继续处理点击事件
			}
		});
	}
	long[] mHits = new long[3];
	public void mulitClick(View view){//
		//src 要copy的源数组
		//srcPos 从源数组的哪个位置开始copy
		//dst 要copy的目标数组
		//dstPos 目标数组的哪个位置开始copy
		//length 要copy多少个元素
		System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
		mHits[mHits.length-1]= SystemClock.uptimeMillis();//uptimeMillis开机时间
		if(mHits[0]>=(SystemClock.uptimeMillis()-500)){//第一次点击的时间和最后一次点击的时间在500ms内
			Toast.makeText(this, "三击事件", 0).show();
		}
	}
	private long firstClickedTime;
	private SharedPreferences sp;
	private ImageView iv_drag;
	private TextView tv_drag_top;
	private TextView tv_drag_bottom;
}
