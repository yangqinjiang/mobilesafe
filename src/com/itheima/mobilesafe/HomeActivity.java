package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.Md5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private SharedPreferences sp;
	private final String TAG = "HomeActivity";
	private static final String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "系统优化", "高级工具", "设置中心" };
	private static final int[] icons = { R.drawable.safe,
			R.drawable.callmsgsafe, R.drawable.app, R.drawable.taskmanager,
			R.drawable.netmanager, R.drawable.trojan, R.drawable.sysoptimize,
			R.drawable.atools, R.drawable.settings };
	private GridView gv_home;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		gv_home = (GridView) findViewById(R.id.gv_home);
		gv_home.setAdapter(new HomeAdapter());
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position) {
				case 0:
					// 判断用户是否设置过密码
					if (isSetPwd()) {// 进入输入密码的界面
						showEnterPWdDialog();
						Log.i(TAG,"进入输入密码界面");
					} else {// 进入设置密码的界面
						Log.i(TAG,"进入设置密码的界面");
						showSetUpPwdDialog();
					}
					Log.i(TAG, "手机防盗");
					break;
				case 8:// 设置中心Activity
					intent = new Intent(HomeActivity.this,
							SettingCenterActivity.class);
					startActivity(intent);
					break;
				}
			}

		});
	}
	private AlertDialog dialog;
	private EditText et_pwd;
	private EditText et_pwd_confirm;
	private Button bt_ok;
	private Button bt_cancle;
	/**
	 * 设置密码的对话框
	 */
	protected void showSetUpPwdDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_setup_pwd, null);
		//去掉边框的距离
		dialog = builder.create();
		et_pwd = (EditText) view.findViewById(R.id.et_pwd);
		et_pwd_confirm = (EditText) view.findViewById(R.id.et_pwd_confirm);
		bt_ok = (Button)view.findViewById(R.id.bt_ok);
		bt_cancle = (Button)view.findViewById(R.id.bt_cancle);
		bt_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO:mmmm
				String pwd = et_pwd.getText().toString().trim();
				String pwd_confirm = et_pwd_confirm.getText().toString().trim();
				if(TextUtils.isEmpty(pwd)||TextUtils.isEmpty(pwd_confirm)){
					Toast.makeText(getApplicationContext(),"密码不能为空",0).show();
					return;
				}
				if(pwd.equals(pwd_confirm)){//密码一致
					
					Editor edit = sp.edit();
					edit.putString("password",Md5Utils.encode(pwd));
					edit.commit();
					dialog.dismiss();
					showEnterPWdDialog();
					
				}else{//密码不一致
					Toast.makeText(getApplicationContext(),"两次密码不一致,请检查.",0).show();
					return;
				}
				
			}
		});
		bt_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		//builder.setView(view);// set自定义的view
		//builder.show();
	}

	/**
	 * 输入密码对话框
	 */
	protected void showEnterPWdDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.dialog_enter_pwd, null);
		//去掉边框的距离
		dialog = builder.create();
		et_pwd = (EditText) view.findViewById(R.id.et_pwd);
		bt_ok = (Button)view.findViewById(R.id.bt_ok);
		bt_cancle = (Button)view.findViewById(R.id.bt_cancle);
		bt_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO:mmmm
				String pwd = et_pwd.getText().toString().trim();
				if(TextUtils.isEmpty(pwd)){
					Toast.makeText(getApplicationContext(),"密码不能为空",0).show();
					return;
				}
				//获取用户原来存储的密码
				String saved_pwd = sp.getString("password", "");
				//比较密码是否一致
				if(saved_pwd.equals(Md5Utils.encode(pwd))){//密码一致
					Log.i(TAG,"密码输入正确,进入手机防盗主界面");
					dialog.dismiss();
					Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);
					
				}else{//密码不一致
					Toast.makeText(getApplicationContext(),"密码错误,请检查.",0).show();
					return;
				}
				
			}
		});
		bt_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		//builder.setView(view);// setView自定义的view
		//builder.show();

	}

	/**
	 * 判断是否设置过了密码
	 * 
	 * @return
	 */
	private boolean isSetPwd() {
		String password = sp.getString("password", null);
		return !TextUtils.isEmpty(password);
		// if(TextUtils.isEmpty(password)){
		// return false;
		// }
		// return true;
	}

	//
	private class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		/**
		 * Drawable img = getResources().getDrawable(mImageViewArray[index]);
		 * img
		 * .setBounds(0,0,img.getIntrinsicHeight()-5,img.getIntrinsicWidth());
		 * //img.get, img.getIntrinsicHeight());//(left, top, right, bottom);
		 * textView.setCompoundDrawables(null, img, null, null);
		 */
		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			// 填充view
			View view = View.inflate(getApplicationContext(),
					R.layout.list_home_item, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_home_name);
			tv_name.setText(names[position]);
			Drawable img = getResources().getDrawable(icons[position]);
			img.setBounds(0, 0, img.getIntrinsicHeight() - 5,
					img.getIntrinsicWidth());
			tv_name.setCompoundDrawables(null, img, null, null);
			// ImageView iv_icon =
			// (ImageView)view.findViewById(R.id.iv_home_icon);
			// iv_icon.setImageResource(icons[position]);
			return view;
		}

	}
}
