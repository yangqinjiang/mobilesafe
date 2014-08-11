package com.itheima.mobilesafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.itheima.mobilesafe.db.dao.AntivirusDao;
import com.itheima.mobilesafe.utils.Md5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AntiVirusActivity extends Activity {

	private ImageView iv_scan;
	private ProgressBar pb_status;
	private TextView tv_scan_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_anti_virus);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		pb_status = (ProgressBar) findViewById(R.id.pb_status);
		tv_scan_status=(TextView)findViewById(R.id.tv_scan_status);
		ll_container=(LinearLayout)findViewById(R.id.ll_container);
		// 动画,相对自身的中心点
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(Animation.INFINITE);//不停止
		iv_scan.setAnimation(ra);
		scanVirus();
		
	}

	private LinearLayout ll_container;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANNING:
				ScanInfo info =(ScanInfo)msg.obj;
				tv_scan_status.setText("正在扫描:"+info.appName);
				//更新界面
				TextView tv = new TextView(getApplicationContext());
				if(info.isVirus){
					tv.setText("发现病毒:"+info.appName+info.desc);
					tv.setTextColor(Color.RED);
				}else{
					tv.setText("扫描安全:"+info.appName);
					tv.setTextColor(Color.BLACK);
				}
				ll_container.addView(tv, 0);//加到最上方
				break;
			case SCAN_FINISH:
				//停止动画
				tv_scan_status.setText("扫描完毕.");
				iv_scan.clearAnimation();
				iv_scan.setVisibility(View.INVISIBLE);
				if(null !=virusInfos &&virusInfos.size()>0){
					 AlertDialog.Builder builder = new AlertDialog.Builder(AntiVirusActivity.this);
					 builder.setTitle("警告!");
					 builder.setMessage("在您的手机里面发现了"+virusInfos.size()+"个病毒...");
					 builder.setNegativeButton("以后再说",null);
					 builder.setPositiveButton("立刻清理",new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							for(ScanInfo info :virusInfos){
								Intent intent = new Intent();
								intent.setAction(Intent.ACTION_DELETE);
								intent.setData(Uri.parse("package:"+info.packName));
								startActivity(intent);
							}
						}
					});
					
					 
				}
				break;
			default:
				break;
			}
		};
	};
	private List<ScanInfo> virusInfos;
	/**
	 * 扫描
	 */
	private void scanVirus() {
		tv_scan_status.setText("正在初始化杀毒引擎...");
		//
		virusInfos= new ArrayList<ScanInfo>();
		new Thread(){
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//检查每一个应用程序的签名信息
				PackageManager pm = getPackageManager();
				List<PackageInfo> packInfos = pm.getInstalledPackages(PackageManager.GET_SIGNATURES | PackageManager.GET_UNINSTALLED_PACKAGES );
				pb_status.setMax(packInfos.size());
				int total =0;
				System.out.println("扫描开始...");
				for(PackageInfo packInfo:packInfos){
					try {
						Thread.sleep(new Random().nextInt(500));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(total+"/"+packInfos.size());
					//获取签名
					Signature[] ss = packInfo.signatures;
					if(ss ==null || ss.length<=0)continue;
					String signature = ss[0].toCharsString();
					System.out.println(packInfo.applicationInfo.loadLabel(pm));
					signature = Md5Utils.encode(signature);
					String result = AntivirusDao.findVirus(signature);
					ScanInfo info = new ScanInfo();
					info.appName= packInfo.applicationInfo.loadLabel(pm).toString();
					info.packName=packInfo.packageName;
					if(result!=null){
						System.out.println("发现:"+result+packInfo.applicationInfo.loadLabel(pm));
						info.isVirus=true;
						info.desc=result;
						virusInfos.add(info);//加入扫描结果集
					}else{
						System.out.println("扫描安全");
						info.isVirus=false;
					}
					
					
					total++;
					
					//send msg 
					Message msg = Message.obtain();
					msg.what = SCANNING;//数据种类
					msg.obj = info;//携带数据
					handler.sendMessage(msg);
					
					pb_status.setProgress(total);
				}
				//扫描完毕
				//send msg 
				Message msg = Message.obtain();
				msg.what = SCAN_FINISH;//数据种类
				handler.sendMessage(msg);
			};
		}.start();
		
	}
	private static final int SCANNING =1;
	private static final int SCAN_FINISH =2;
	/**
	 * 扫描信息
	 * @author lenovo
	 *
	 */
	class ScanInfo{
		String appName;
		String packName;
		boolean isVirus;
		String desc;
	}
}
