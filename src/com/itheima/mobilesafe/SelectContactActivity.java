package com.itheima.mobilesafe;

import java.util.List;

import com.itheima.mobilesafe.domain.ContactInfo;
import com.itheima.mobilesafe.engine.ContactInfoProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SelectContactActivity extends Activity {
	private ListView lv_contact;
	private List<ContactInfo> contactInfos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.activity_select_contact);

		lv_contact = (ListView) findViewById(R.id.lv_contact);
		// 获取联系人
		contactInfos = ContactInfoProvider.getContactInfo(this);
		lv_contact.setAdapter(new ContactAdapter());
		lv_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phone = contactInfos.get(position).getPhone();
				Intent data = new Intent();
				data.putExtra("phone", phone);
				setResult(0, data);
				finish();

			}
		});
	}

	protected class ContactAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(),
					R.layout.list_contact_item, null);
			TextView tv_name = (TextView) view
					.findViewById(R.id.tv_contact_name);
			TextView tv_phone = (TextView) view
					.findViewById(R.id.tv_contact_phone);
			ContactInfo info = contactInfos.get(position);
			tv_name.setText(info.getName());
			tv_phone.setText(info.getPhone());
			return view;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contactInfos.size();
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
}
