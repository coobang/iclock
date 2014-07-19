package com.mdshuo.iclock;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 *启动 此时间提示界面同时 播放闹钟铃声
 * @author TONNY
 *
 */
public class AlarmActvity extends Activity {
	private MediaPlayer mp;
	private Button btnqd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playalarm);
		
		mp=MediaPlayer.create(this, R.raw.in_call_alarm);
		mp.start();//播放
		
		btnqd=(Button) findViewById(R.id.queding);
		btnqd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//待定
			}
		});
		
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mp.stop();
		mp.release();//释放资源
	}
	
	@Override
	protected void onStop() {
		
		super.onStop();
		
	}

	
	
	
}
