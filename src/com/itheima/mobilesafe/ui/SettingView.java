package com.itheima.mobilesafe.ui;

import com.itheima.mobilesafe.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 自定义一个设置控件
 *
 */
public class SettingView extends RelativeLayout {
	public static final String UI_NS = "http://schemas.android.com/apk/res/com.itheima.mobilesafe";
	private TextView tv_title;
	private TextView tv_desc;
	private CheckBox cb_status;
	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private String desc_on;
	private String desc_off;
	private String title_show;
	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
//		int count  = attrs.getAttributeCount();
//		for (int i = 0; i < count; i++) {
//			System.out.println(attrs.getAttributeValue(i));
//		}
		title_show = attrs.getAttributeValue(UI_NS, "title_show");
		desc_on = attrs.getAttributeValue(UI_NS, "desc_on");
		desc_off = attrs.getAttributeValue(UI_NS, "desc_off");
		setTitle(title_show);
		setDesc(desc_on);
	}

	public SettingView(Context context) {
		super(context);
		initView(context);
	}
	/**
	 * 初始化自定义控件
	 */
	private void initView(Context context) {
		//context 上下文
		//resId layout文件id
		//this  ViewGroup 依附在当前对象上RelativeLayout
		View.inflate(context, R.layout.ui_setting_view, this);//
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_desc = (TextView) this.findViewById(R.id.tv_desc);
		cb_status =(CheckBox)this.findViewById(R.id.cb_status);
//		cb_status.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
	}
	public boolean isChecked(){
		return cb_status.isChecked();
	}
	public void setChecked(boolean checked){
		System.out.println("setChecked:"+checked);
		cb_status.setChecked(checked);
		tv_desc.setTextColor(checked ? 0x80000000 : Color.RED);
		setDesc(checked ? desc_on : desc_off);
	}
	public void setTitle(String text){
		tv_title.setText(text);
	}
	public void setDesc(String text){
		tv_desc.setText(text);
	}
	
//	public void enableUpdateStatus(View v){
//		
//	}

}
