package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.ActivityUtils;

import android.content.Intent;
import android.view.View;

public class Setup1Activity extends SetupBaseActivity {

	@Override
	public void initView() {
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void next(View view) {
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
		ActivityUtils.startActivityAndFinish(this, Setup2Activity.class);
	}

	@Override
	public void pre(View view) {
		// TODO Auto-generated method stub
		
	}

}
