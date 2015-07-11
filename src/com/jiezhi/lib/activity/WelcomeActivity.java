package com.jiezhi.lib.activity;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * 欢迎界面
 * 
 * @author Jiezhi.G
 * 
 * 
 */
public class WelcomeActivity extends Activity implements Runnable {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		// FullScreen
		/*
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
		countdown();
		MobclickAgent.updateOnlineConfig(getBaseContext());
		UmengUpdateAgent.setUpdateOnlyWifi(true);
		UmengUpdateAgent.update(this);
		new Thread(this).start();
	}

	private void countdown() {
		TextView welcomeTV = (TextView) findViewById(R.id.welcome_tv);
		welcomeTV.setText("掌上南中医");
		/*
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		long time_now = calendar.getTimeInMillis();
		calendar.set(2014, 9, 15);// 注意9表示10月
		long time_xiaoqing = calendar.getTimeInMillis();
		long time_left = ((time_xiaoqing - time_now) / (1000 * 60 * 60 * 24));
		System.out.println("距离校庆还有：" + time_left + "天");

		if (time_left <= 0) {
			// 今天校庆
			welcomeTV
					.setText(getResources().getString(R.string.xiaoqing_today));
		} else {
			// 距离校庆还有。。。天
			welcomeTV.setText(getResources().getString(
					R.string.xiaoqing_countdown)
					+ String.valueOf(time_left) + "天");
		}
		*/
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(5000);
			Intent i = new Intent(getApplication(), MainActivity.class);
			startActivity(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
