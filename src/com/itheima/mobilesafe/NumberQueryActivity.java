package com.itheima.mobilesafe;

import com.itheima.mobilesafe.db.dao.NumberAddressDao;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberQueryActivity extends Activity {

	private EditText et_number;
	private TextView tv_address;

	private Vibrator vibrator;//震动服务
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_number_query);
		et_number = (EditText) findViewById(R.id.et_number);
		tv_address = (TextView) findViewById(R.id.tv_address);
		//获取震动服务
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		//文件监听器
		et_number.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String number = s.toString();
				String address = NumberAddressDao.getAddress(number);
				tv_address.setText("归属地: " + address);
			}
		});
	}

	public void query(View view) {
		String number = et_number.getText().toString().trim();
		if (TextUtils.isEmpty(number)) {
			// 抖动的动画效果
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			// xml 里面有个 android:interpolator="@anim/cycle_7",插入器,
			// shake.setInterpolator(new Interpolator() {
			//
			// @Override
			// public float getInterpolation(float x) {
			// //x,y公式
			// float y = (float)0.5*x;
			// return y;
			// }
			// });
			// 使用系统提供的
			// shake.setInterpolator(new BounceInterpolator());
			et_number.startAnimation(shake);
			vibrator.vibrate(100);//震动100ms
			//等100,震动200,
			//等100,震动300
			//等100,震动500
			//3循环3次
			//记住加入权限 android.permission.VIBRATE
			vibrator.vibrate(new long[]{100, 200,100,300,100,500},3);
			Toast.makeText(this, "号码不能为空", 0).show();
			return;
		}
		String address = NumberAddressDao.getAddress(number);
		tv_address.setText("归属地: " + address);
	}
}
