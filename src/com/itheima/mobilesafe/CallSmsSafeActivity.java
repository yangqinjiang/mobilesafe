package com.itheima.mobilesafe;

import java.util.List;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.db.domain.BlackNumberInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CallSmsSafeActivity extends Activity {
	private int maxNumber = 20;
	private ListView lv_callsms_safe;
	private BlackNumberDao dao;
	private List<BlackNumberInfo> blacknumberinfos;
	private LinearLayout ll_loading;
	private CallSmsSafeAdapter adapter;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);

			if (adapter == null) {
				adapter = new CallSmsSafeAdapter();
				lv_callsms_safe.setAdapter(adapter);
			} else {// 通知界面更新
				adapter.notifyDataSetChanged();
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_callsms_safe);
		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		et_page_number = (EditText) findViewById(R.id.et_page_number);
		dao = new BlackNumberDao(this);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		fillData();

	}

	private int pageNumber = 1;

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {

			public void run() {
				blacknumberinfos = dao.findPartByPage(pageNumber, maxNumber);
				handler.sendEmptyMessage(0);// 发空消息,通知setAdapter
			};
		}.start();
	}

	private EditText et_page_number;

	public void jump(View view) {
		String strPageNumber = et_page_number.getText().toString().trim();
		if (TextUtils.isEmpty(strPageNumber)) {
			Toast.makeText(getApplicationContext(), "请输入页码", 0).show();
			return;
		} else {
			pageNumber = Integer.parseInt(strPageNumber);
			fillData();
		}
	}

	class CallSmsSafeAdapter extends BaseAdapter {

		protected String TAG ="CallSmsSafeAdapter";

		@Override
		public int getCount() {
			return blacknumberinfos.size();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder;
			// 1,复用历史缓存的view对象,减少view对象创建的次数
			if (null != convertView) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {

				view = View.inflate(getApplicationContext(),
						R.layout.list_callsms_item, null);
				// 2,存储孩子view对象的引用,减少孩子查询的次数
				holder = new ViewHolder();
				holder.tv_mode = (TextView) view
						.findViewById(R.id.tv_callsms_mode);
				holder.tv_phone = (TextView) view
						.findViewById(R.id.tv_callsms_phone);
				holder.iv_callsms_delete = (ImageView) view
						.findViewById(R.id.iv_callsms_delete);
				// 放进父view的包里
				view.setTag(holder);
			}

			BlackNumberInfo blackNumberInfo = blacknumberinfos.get(position);
			holder.tv_phone.setText(blackNumberInfo.getPhone());
			String mode = blackNumberInfo.getMode();

			if ("1".equals(mode)) {
				mode = "电话拦截";
			} else if ("2".equals(mode)) {
				mode = "短信拦截";
			} else {
				mode = "全部拦截";
			}
			holder.tv_mode.setText(mode);
			//删除按钮事件
			holder.iv_callsms_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.i(TAG , position+"被点击了"+blacknumberinfos.get(position).getPhone());
					//1.删除数据库里的黑名单号码
					dao.delete(blacknumberinfos.get(position).getPhone());
					//2.删除ui里的条目
					blacknumberinfos.remove(position);
					//3.通知ui更新内容
					adapter.notifyDataSetChanged();
					
				}
			});
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	/**
	 * 定义一个记事本,存放孩子的引用
	 * 
	 */
	static class ViewHolder {// 静态类,
		TextView tv_mode;
		TextView tv_phone;
		ImageView iv_callsms_delete;
	}
	/**
	 * 添加黑名单号码事件
	 * @param view
	 */
	public void addBlackNumber(View view){
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog dialog = builder.create();
		View dialogView = View.inflate(this, R.layout.dialog_add_blacknumber, null);
		dialog.setView(dialogView);
		dialog.show();
		et_phone=(EditText)dialogView.findViewById(R.id.et_phone);
		rg_mode=(RadioGroup)dialogView.findViewById(R.id.rg_mode);
		dialogView.findViewById(R.id.bt_cancle).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialogView.findViewById(R.id.bt_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone = et_phone.getText().toString().trim();
				if(TextUtils.isEmpty(phone)){
					Toast.makeText(getApplicationContext(), "电话号码不能为空.",0).show();
					return;
				}
				int id = rg_mode.getCheckedRadioButtonId();
				String mode = "3";//默认拦截模式
				if(id==R.id.rb_phone){
					mode="1";
				}else if(id==R.id.rb_sms){
					mode="2";
				}
				dao.add(phone, mode);//1.增加到数据库
				//2.把数据更新到ui,在集合的第一位加入新数据
				blacknumberinfos.add(0,new BlackNumberInfo(phone, mode));
				//3.通知适配器更新
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
	}
	private EditText et_phone;
	private RadioGroup rg_mode;
}
