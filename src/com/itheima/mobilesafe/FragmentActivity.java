package com.itheima.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FragmentActivity extends android.support.v4.app.FragmentActivity implements OnClickListener {

	private TextView tv1, tv2, tv3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		//
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();//
		ft.replace(R.id.container, new Fragmemt1());
		ft.commit();
	}

	@Override
	public void onClick(View v) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();//
		switch (v.getId()) {
		case R.id.tv1:
			ft.replace(R.id.container, new Fragmemt1());
			break;
		case R.id.tv2:
			ft.replace(R.id.container, new Fragmemt2());
			break;
		case R.id.tv3:
			ft.replace(R.id.container, new Fragmemt3());
			break;
		default:
			break;
		}
		ft.commit();
	}
}
