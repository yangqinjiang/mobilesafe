package com.itheima.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.mobilesafe.utils.ActivityUtils;

public class Setup3Activity extends SetupBaseActivity {
	private EditText et_phone;

	// 读取SIM卡权限
	// android.permission.READ_PHONE_STATE
	@Override
	public void initView() {
		setContentView(R.layout.activity_setup3);
		et_phone = (EditText)this.findViewById(R.id.et_phone);
		et_phone.setText(sp.getString("safenumber",""));
	}

	/**
	 * 选择联系人
	 * @param view
	 */
	public void selectContact(View view) {
		Intent intent = new Intent(this, SelectContactActivity.class);
		startActivityForResult(intent, 100);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100 && resultCode == 0){
			if(data != null){
				String phone = data.getStringExtra("phone");
				et_phone.setText(phone);
			}
		}
	}
	@Override
	public void next(View view) {
		String phone = et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "请先填写安全号码", 0).show();
			return;
		}else{
			Editor editor = sp.edit();
			editor.putString("safenumber", phone);
			editor.commit();
		}
		ActivityUtils.startActivityAndFinish(this, Setup4Activity.class);
	}

	@Override
	public void pre(View view) {
		ActivityUtils.startActivityAndFinish(this, Setup2Activity.class);
		
	}

}
