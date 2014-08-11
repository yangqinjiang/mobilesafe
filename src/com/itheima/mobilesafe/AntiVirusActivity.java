package com.itheima.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class AntiVirusActivity extends Activity {

	private ImageView iv_scan;
	private ProgressBar pb_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_anti_virus);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		pb_status = (ProgressBar) findViewById(R.id.pb_status);
		// 动画,相对自身的中心点
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(Animation.INFINITE);//不停止
		iv_scan.setAnimation(ra);
		//进度条动画
		new Thread(){
			public void run() {
				pb_status.setMax(100);	
				for(int i=0;i<=100;i++){
					if(i==99){
						i=0;
					}
					pb_status.setProgress(i);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
		
	}
}
