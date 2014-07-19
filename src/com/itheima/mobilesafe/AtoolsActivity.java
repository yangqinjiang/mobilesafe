package com.itheima.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class AtoolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_atools);
	}
	//号码归属地查询
	public void numberAddressQuery(View view){
		Intent intent = new Intent(this,
				NumberQueryActivity.class);
		startActivity(intent);
	}
}
