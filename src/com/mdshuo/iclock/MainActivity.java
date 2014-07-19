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
		//addTab ���ѡ�  setIndicator() ���ñ��⣻setContent(int Viewid) �������ݣ�
		tabhost.addTab(tabhost.newTabSpec("tabTime").setIndicator("ʱ��").setContent(R.id.tabTime));
		tabhost.addTab(tabhost.newTabSpec("tabTAlarm").setIndicator("����").setContent(R.id.tabAlarm));
		tabhost.addTab(tabhost.newTabSpec("tabTimer").setIndicator("��ʱ").setContent(R.id.tabTimer));
		tabhost.addTab(tabhost.newTabSpec("tabStopWatch").setIndicator("���").setContent(R.id.tabStopWatch));
		
		watchView=(WatchView) findViewById(R.id.tabStopWatch);
	}
	
	
	@Override
	protected void onDestroy() {
		watchView.onDestroy();
		super.onDestroy();
	}
	
	
 
}
