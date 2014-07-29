package com.itheima.mobilesafe.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe.R;

/**
 * 自定义一个设置的组合控件
 *
 */
public class SettingClickView extends RelativeLayout {
	private TextView tv_title;
	private TextView tv_desc;
	private String desc_on;

	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.mobilesafe", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.mobilesafe", "desc_on");
		setTitle(title);
		setDesc(desc_on);
	}

	public SettingClickView(Context context) {
		super(context);
		initView(context);
	}
	/**
	 * 初始化 自定义的组合控件
	 * @param context 上下文
	 */
	public void initView(Context context){
		//把xml文件转化成view对象,挂载在自己身上 当前view对象上  this
		View.inflate(context, R.layout.ui_setting_click_view, this);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_desc = (TextView) this.findViewById(R.id.tv_desc);
	}
	/**
	 * 设置标题
	 * @param text
	 */
	public void setTitle(String text){
		tv_title.setText(text);
	}
	/**
	 * 设置描述
	 * @param text
	 */
	public void setDesc(String text){
		tv_desc.setText(text);
	}

	
}
