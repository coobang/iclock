package com.mdshuo.iclock;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


/**
 * 秒表
 * 
 * @author TONNY
 * 
 */
public class WatchView extends LinearLayout {
	private TextView tvHour, tvMin, tvSec, tvMill;
	private Button btnStart, btnPause, btnResume, btnReset, btnLap;
	private ListView lvWatch;
	private ArrayAdapter<String> adapter;

	public WatchView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	/**
	 * 
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		tvHour = (TextView) findViewById(R.id.tvHour);
		tvHour.setText("0");
		tvHour.setTextSize(20);
		tvMin = (TextView) findViewById(R.id.tvMin);
		tvMin.setText("0");
		tvMin.setTextSize(20);
		tvSec = (TextView) findViewById(R.id.tvSec);
		tvSec.setText("0");
		tvSec.setTextSize(20);
		tvMill = (TextView) findViewById(R.id.tvMill);
		tvMill.setText("0");
		tvMill.setTextSize(20);

		btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startTimer();
				btnStart.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnLap.setVisibility(View.VISIBLE);
			}
		});
		btnPause = (Button) findViewById(R.id.btnPause);
		btnPause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTimer();
				btnPause.setVisibility(View.GONE);
				btnResume.setVisibility(View.VISIBLE);
				btnLap.setVisibility(View.GONE);
				btnReset.setVisibility(View.VISIBLE);

			}
		});
		btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTimer();
				tenMill = 0;
				adapter.clear();

				btnLap.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnReset.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnStart.setVisibility(View.VISIBLE);

			}
		});
		btnResume = (Button) findViewById(R.id.btnResume);
		btnResume.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				startTimer();
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.VISIBLE);
				btnReset.setVisibility(View.GONE);
				btnLap.setVisibility(View.VISIBLE);
			}
		});

		// 记录时间
		btnLap = (Button) findViewById(R.id.btnLap);
		btnLap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 添加记录的时间,每次都添加在第一位，也就是最前面
				adapter.insert(String.format("%d:%d:%d.%d",
						tenMill / 100 / 60 / 60, tenMill / 100 / 60 % 60,
						tenMill / 100 % 60, tenMill / 100), 0);
				System.out.println("btnlap");
			}
		});

		lvWatch = (ListView) findViewById(R.id.lvWatch);
		adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
		lvWatch.setAdapter(adapter);

		btnLap.setVisibility(View.GONE);
		btnPause.setVisibility(View.GONE);
		btnReset.setVisibility(View.GONE);
		btnResume.setVisibility(View.GONE);

		/**
		 * 显示时间计时
		 */
		showTimeTask = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(MSG_WHAT_SHOW_TIME);
			}
		};
		timer.schedule(showTimeTask, 200, 200);

	}

	private int tenMill=0;
	private Timer timer = new Timer();
	private TimerTask timerTask = null;
	
	private TimerTask showTimeTask = null;
	private static final int MSG_WHAT_SHOW_TIME = 1;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_SHOW_TIME:
				// 呈现时间的handler
				tvHour.setText(tenMill / 100 / 60 / 60 + "");
				tvMin.setText(tenMill / 100 / 60 % 60 + "");
				tvSec.setText(tenMill / 100 % 60 + "");
				tvMill.setText(tenMill % 100 + "");
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 开始计时
	 */
	private void startTimer() {
		if (timerTask == null) {
			timerTask = new TimerTask() {
				@Override
				public void run() {
					tenMill++;
				}
			};
			timer.schedule(timerTask, 10, 10);
		}
	}

	/* 取消计时 */
	public void onDestroy() {
		timer.cancel();
	}

	/* 停止TimerTask */
	private void stopTimer() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}

	}
}
