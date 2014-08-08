package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe.db.dao.AppLockDao;
import com.itheima.mobilesafe.domain.AppInfo;
import com.itheima.mobilesafe.engine.AppInfoProvider;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AppLockActivity extends Activity implements OnClickListener {
	private TextView tv_unlock;
	private TextView tv_locked;

	private LinearLayout ll_unlock;
	private LinearLayout ll_locked;
	//
	private ListView lv_unlock;
	private ListView lv_locked;
	//
	private TextView tv_unlock_count;
	private TextView tv_locked_count;
	private AppLockDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_app_lock);
		//
		tv_unlock = (TextView) findViewById(R.id.tv_unlock);
		tv_locked = (TextView) findViewById(R.id.tv_locked);
		ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
		ll_locked = (LinearLayout) findViewById(R.id.ll_locked);
		lv_unlock = (ListView) findViewById(R.id.lv_unlock);
		lv_locked = (ListView) findViewById(R.id.lv_locked);
		tv_unlock_count = (TextView) findViewById(R.id.tv_unlock_count);
		tv_locked_count = (TextView) findViewById(R.id.tv_locked_count);
		// event
		tv_unlock.setOnClickListener(this);
		tv_locked.setOnClickListener(this);

		List<AppInfo> allAppInfos = AppInfoProvider.getAppInfos(this);// 第一次打开,所有应用程序都是未加锁
		unlockAppInfos = new ArrayList<AppInfo>();
		lockedAppInfos = new ArrayList<AppInfo>();
		dao = new AppLockDao(this);
		for (AppInfo appInfo : allAppInfos) {
			if (dao.find(appInfo.getPackName())) {
				lockedAppInfos.add(appInfo);
			} else {
				unlockAppInfos.add(appInfo);
			}
		}
		//
		unlockAdapter = new AppItemAdapter(true);
		lockedAdapter = new AppItemAdapter(false);

		lv_unlock.setAdapter(unlockAdapter);
		lv_locked.setAdapter(lockedAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_unlock:// 显示未加锁的
			tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
			tv_locked.setBackgroundResource(R.drawable.tab_left_default);
			ll_unlock.setVisibility(View.VISIBLE);
			ll_locked.setVisibility(View.GONE);
			break;
		case R.id.tv_locked:// 显示已经加锁的
			tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
			tv_locked.setBackgroundResource(R.drawable.tab_left_pressed);
			ll_unlock.setVisibility(View.GONE);
			ll_locked.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	private List<AppInfo> unlockAppInfos;// 未加锁列表
	private List<AppInfo> lockedAppInfos;// 已经加锁列表
	private AppItemAdapter unlockAdapter;
	private AppItemAdapter lockedAdapter;

	// adapter
	private class AppItemAdapter extends BaseAdapter {

		private boolean unlockflag;

		/**
		 * 构造方法
		 * 
		 * @param unlockflag
		 *            true 已经加锁列表 false 未加锁列表
		 */
		public AppItemAdapter(boolean unlockflag) {
			this.unlockflag = unlockflag;
		}

		@Override
		public int getCount() {
			if (unlockflag) {
				tv_unlock_count.setText("未加锁软件:" + unlockAppInfos.size() + "个");
				return unlockAppInfos.size();
			}
			tv_locked_count.setText("已加锁软件:" + lockedAppInfos.size() + "个");
			return lockedAppInfos.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			final View view;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.list_applock_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.tv_status = (TextView) view.findViewById(R.id.tv_status);
				holder.iv_status = (ImageView) view
						.findViewById(R.id.iv_status);

				view.setTag(holder);
			}
			final AppInfo appInfo;

			if (unlockflag) {
				appInfo = unlockAppInfos.get(position);
				holder.tv_status.setText("加锁");
				holder.iv_status.setImageResource(R.drawable.lock);
			} else {
				appInfo = lockedAppInfos.get(position);
				holder.tv_status.setText("解锁");
				holder.iv_status.setImageResource(R.drawable.unlock);
			}
			// 锁图标的点击事件
			// 将unlockAppinfo集合中去掉
			// 加入到lockedAppinfo集合中
			holder.iv_status.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (unlockflag) {
						unlockAppInfos.remove(appInfo);
						lockedAppInfos.add(appInfo);
						dao.add(appInfo.getPackName());
						// 播放一个向右移动的动画
						TranslateAnimation ta = new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 1.0f,//水平方向移动
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0);
						ta.setDuration(500);
						view.startAnimation(ta);
					} else {
						unlockAppInfos.add(appInfo);
						lockedAppInfos.remove(appInfo);
						dao.delete(appInfo.getPackName());
						//播放一个向左移动的动画
						TranslateAnimation ta = new TranslateAnimation(
								Animation.RELATIVE_TO_SELF, 0f,
								Animation.RELATIVE_TO_SELF, -1.0f,//水平方向移动
								Animation.RELATIVE_TO_SELF, 0,
								Animation.RELATIVE_TO_SELF, 0);
						ta.setDuration(500);
						view.startAnimation(ta);
					}
					//使用Handler来处理delay后的ui动画
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							unlockAdapter.notifyDataSetChanged();
							lockedAdapter.notifyDataSetChanged();
						}
					}, 500);
					
				}
			});
			holder.iv_icon.setImageDrawable(appInfo.getAppIcon());
			holder.tv_name.setText(appInfo.getAppName());

			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	static class ViewHolder {
		ImageView iv_icon;// 程序图标
		TextView tv_name;// 程序名
		TextView tv_status;// 锁图标
		ImageView iv_status;// 锁的文字
	}
}
