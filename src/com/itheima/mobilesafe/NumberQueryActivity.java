package com.itheima.mobilesafe;

import com.itheima.mobilesafe.db.dao.NumberAddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberQueryActivity extends Activity {

	private EditText et_number;
	private TextView tv_address;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_number_query);
		et_number = (EditText)findViewById(R.id.et_number);
		tv_address =(TextView)findViewById(R.id.tv_address);
	}
	public void query(View view){
		String number = et_number.getText().toString().trim();
		if(TextUtils.isEmpty(number)){
			Toast.makeText(this, "号码不能为空", 0).show();
			return;
		}
		String address = NumberAddressDao.getAddress(number);
		tv_address.setText("归属地: "+address);
	}
}
