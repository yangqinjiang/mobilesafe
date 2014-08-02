package com.itheima.mobilesafe;

import java.util.List;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.db.domain.BlackNumberInfo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CallSmsSafeActivity extends Activity {
	private int startIndex = 0;
	private int maxNumber = 20;
	private ListView lv_callsms_safe;
	private BlackNumberDao dao;
	private List<BlackNumberInfo> blacknumberinfos;
	private LinearLayout ll_loading;
	private CallSmsSafeAdapter adapter;
	private int totalCount;// 数据库里一共有多少条记录
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
		lv_callsms_safe.setOnScrollListener(new OnScrollListener() {

			/**
			 * 当滚动状态发生变化的时候,调用的方法
			 * 
			 * @param view
			 * @param scrollState
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_FLING:// 手指已经离开屏幕,习惯性滚动

					break;
				case OnScrollListener.SCROLL_STATE_IDLE:// 当listView停止的时候,没有滚动
					int position = lv_callsms_safe.getLastVisiblePosition();// 返回最后可见条目在屏幕上的位置//19
					int total = blacknumberinfos.size();// 20
					if (position == (total - 1)) {
						// 界面拖到最后一个元素,加载数据
						startIndex += maxNumber;
						if (startIndex >= totalCount) {
							Toast.makeText(getApplicationContext(), "没有更多数据了",
									0).show();
							return;
						}
						fillData();
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 触摸滚动的状态

					break;
				default:
					break;
				}
			}

			// 当滚动的时候调用的方法
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		dao = new BlackNumberDao(this);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		totalCount = dao.getTotalCount();
		fillData();

	}

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (blacknumberinfos == null) {
					blacknumberinfos = dao.findPart(startIndex, maxNumber);
				} else {// 集合已经存在了,新获取的数据应该放到旧的集合后面
					blacknumberinfos
							.addAll(dao.findPart(startIndex, maxNumber));
				}
				handler.sendEmptyMessage(0);// 发空消息,通知setAdapter
			};
		}.start();
	}

	class CallSmsSafeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return blacknumberinfos.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
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
	}
}
