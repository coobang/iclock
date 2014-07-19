package com.mdshuo.iclock;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;

public class MainActivity extends Activity {
	private TabHost tabhost;
	private WatchView watchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tabhost=(TabHost) findViewById(android.R.id.tabhost);
		tabhost.setup();
		//addTab 添加选项卡  setIndicator() 设置标题；setContent(int Viewid) 设置内容；
		tabhost.addTab(tabhost.newTabSpec("tabTime").setIndicator("时钟").setContent(R.id.tabTime));
		tabhost.addTab(tabhost.newTabSpec("tabTAlarm").setIndicator("闹钟").setContent(R.id.tabAlarm));
		tabhost.addTab(tabhost.newTabSpec("tabTimer").setIndicator("计时").setContent(R.id.tabTimer));
		tabhost.addTab(tabhost.newTabSpec("tabStopWatch").setIndicator("秒表").setContent(R.id.tabStopWatch));
		
		watchView=(WatchView) findViewById(R.id.tabStopWatch);
	}
	
	
	@Override
	protected void onDestroy() {
		watchView.onDestroy();
		super.onDestroy();
	}
	
	
 
}
