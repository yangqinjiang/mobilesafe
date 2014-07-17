package com.itheima.mobilesafe;

import com.itheima.mobilesafe.utils.ActivityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private static final String TAG = "LostFindActivity";
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 判断用户是否进行过设置向导,如果没有进行过设置向导,定向页面到设置向导界面
		if (isSetup()) {
			setContentView(R.layout.activity_lostfind);
			TextView tv_number = (TextView) findViewById(R.id.tv_lostfind_number);
			// 设置保存的safenumber
			tv_number.setText(sp.getString("safenumber", ""));
			ImageView iv_lostfind_status = (ImageView) findViewById(R.id.iv_lostfind_status);
			//根据是否保护的状态来设置相应的图片
			boolean protecting = sp.getBoolean("protecting", false);
			if (protecting) {
				iv_lostfind_status.setImageResource(R.drawable.lock);
			} else {
				iv_lostfind_status.setImageResource(R.drawable.unlock);
			}
		} else {//未进入向导,则设置
			Log.i(TAG, "定向界面到设置向导界面");
			ActivityUtils.startActivityAndFinish(this, Setup1Activity.class);
		}
	}

	/**
	 * 决断用户是否完成过设置向导
	 * 
	 * @return
	 */
	private boolean isSetup() {
		return sp.getBoolean("setup", false);
	}

	/**
	 * 重新进入设置向导
	 * 
	 * @param view
	 */
	public void reEntrySetup(View view) {
		ActivityUtils.startActivityAndFinish(this, Setup1Activity.class);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.lost_find_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.item_change_name){//更改手机防盗的名称
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("更改手机防盗标题");
			final EditText et = new EditText(this);
			et.setHint("请输入新的标题");
			et.setText(sp.getString("newTitle", ""));//回显
			builder.setView(et);
			builder.setPositiveButton("确定",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String newTitle  = et.getText().toString().trim();
					Editor editor = sp.edit();
					editor.putString("newTitle",newTitle);
					editor.commit();
				}
			});
			builder.setNegativeButton("取消", null);
			builder.show();
		}
		return super.onOptionsItemSelected(item);
	}
	
}
