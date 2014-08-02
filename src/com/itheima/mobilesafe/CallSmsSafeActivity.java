package com.itheima.mobilesafe;

import java.util.List;

import com.itheima.mobilesafe.db.dao.BlackNumberDao;
import com.itheima.mobilesafe.db.domain.BlackNumberInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CallSmsSafeActivity extends Activity {

	private ListView lv_callsms_safe;
	private BlackNumberDao dao;
	private List<BlackNumberInfo> blacknumberinfos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_callsms_safe);
		lv_callsms_safe = (ListView)findViewById(R.id.lv_callsms_safe);
		dao = new BlackNumberDao(this);
		blacknumberinfos = dao.findAll();
		lv_callsms_safe.setAdapter(new CallSmsSafeAdapter());
	}
	class CallSmsSafeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return blacknumberinfos.size();
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view =null;
			ViewHolder holder;
			//1,复用历史缓存的view对象,减少view对象创建的次数
			if(null!=convertView){
				view = convertView;
				holder = (ViewHolder)view.getTag();
			}else{
				
				view = View.inflate(getApplicationContext(), R.layout.list_callsms_item, null);
				//2,存储孩子view对象的引用,减少孩子查询的次数
				holder = new ViewHolder();
				holder.tv_mode=(TextView)view.findViewById(R.id.tv_callsms_mode);
				holder.tv_phone = (TextView)view.findViewById(R.id.tv_callsms_phone);
				//放进父view的包里
				view.setTag(holder);
			}
			
			BlackNumberInfo blackNumberInfo = blacknumberinfos.get(position);
			holder.tv_phone.setText(blackNumberInfo.getPhone());
			String mode = blackNumberInfo.getMode();
			
			if("1".equals(mode)){
				mode="电话拦截";
			}else if("2".equals(mode)){
				mode="短信拦截";
			}else{
				mode="全部拦截";
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
	static class ViewHolder{//静态类,
		TextView tv_mode;
		TextView tv_phone;
	}
}
