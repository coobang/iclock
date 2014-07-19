package com.mdshuo.iclock;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 计时器
 * 
 * @author TONNY
 * 
 */
public class TimerView extends LinearLayout {

	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimerView(Context context) {
		super(context);
	}

	//android.view.View  onFinishInflate 当View和他的子view从xml文件加载完之后调用
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		// 拿到输入框和按钮的引用
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startTimer();
			}
		});
		btnPause = (Button) findViewById(R.id.btnPause);
		btnPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTimer();
				btnStart.setVisibility(View.GONE);
				btnResume.setVisibility(View.VISIBLE);
				// btnReset.setVisibility(View.VISIBLE);
			}
		});
		btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopTimer();
				etHour.setText("00");
				etMin.setText("00");
				etSec.setText("00");
				btnStart.setVisibility(View.VISIBLE);
				btnResume.setVisibility(View.GONE);
				btnPause.setVisibility(View.GONE);
				btnReset.setVisibility(View.GONE);
			}
		});
		btnResume = (Button) findViewById(R.id.btnResume);
		btnResume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startTimer();
			/*	btnStart.setVisibility(View.GONE);
				btnResume.setVisibility(View.GONE);
				btnReset.setVisibility(View.VISIBLE);*/
			}
		});
		// 添加文本改变监听
		etHour = (EditText) findViewById(R.id.etHour);
		etHour.addTextChangedListener(new TextWatcher() {
//android.text 
//			android.text 接口  TextWatcher
		
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					int value = Integer.parseInt(s.toString());
					if (value > 59) {
						etHour.setText("59");
					} else if (value < 0) {
						etHour.setText("0");
					}
					checkToEnableBntStart();
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		etMin = (EditText) findViewById(R.id.etMin);
		etMin.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					int value = Integer.parseInt(s.toString());
					if (value > 59) {
						etMin.setText("59");
					} else if (value < 0) {
						etMin.setText("0");
					}
					checkToEnableBntStart();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		etSec = (EditText) findViewById(R.id.etSec);
		etSec.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					int value = Integer.parseInt(s.toString());
					if (value > 59) {
						etSec.setText("59");
					} else if (value < 0) {
						etSec.setText("59");
					}
					checkToEnableBntStart();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		// 初始化设置；时间默认为00；
		etHour.setText("00");
		etHour.setTextSize((float) 20);

		etMin.setText("00");
		etMin.setTextSize((float) 20);

		etSec.setText("00");
		etSec.setTextSize((float) 20);

		// 按钮设置隐藏
		btnStart.setVisibility(View.VISIBLE);
		btnStart.setEnabled(false);

		btnPause.setVisibility(View.GONE);
		btnReset.setVisibility(View.GONE);
		btnResume.setVisibility(View.GONE);

	}

	// 开始计时
	protected void startTimer() {
//		
		if (timerTask == null) {
			sumTimerCount = Integer.parseInt(etHour.getText().toString()) * 60
					* 60 + Integer.parseInt(etMin.getText().toString()) * 60
					+ Integer.parseInt(etSec.getText().toString());
			timerTask = new TimerTask() {

				@Override
				public void run() {
					sumTimerCount--;
					// 获取当前时间
					handler.sendEmptyMessage(MSG_WHAT_TIME_Tick);

					if (sumTimerCount <= 0) {
						// 对话框提示
						handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
						stopTimer();
					}
				}

			};
			// 每隔一秒执行一次
			timer.schedule(timerTask, 1000, 1000);
			btnStart.setVisibility(View.GONE);
			btnPause.setVisibility(View.VISIBLE);
			btnReset.setVisibility(View.VISIBLE);
		}

	}

	// 停止计时
	protected void stopTimer() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}

	// 处理其他线程中的事务
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_TIME_Tick:// 获取当前时间
				int hour = sumTimerCount / 60 / 60;
				int min = (sumTimerCount / 60) % 60;
				int sec = sumTimerCount % 60;
				// 每隔一定时间重新设置当前时间
				etHour.setText(hour + "");
				etMin.setText(min + "");
				etSec.setText(sec + "");

				break;
			case MSG_WHAT_TIME_IS_UP:
				// 对话框提示
				new AlertDialog.Builder(getContext()).setTitle("计时")
						.setMessage("时间已到").setNegativeButton("确定", null).show();
				break;

			default:
				break;
			}
		};
	};

	// 检查开始按钮是否启动
	private void checkToEnableBntStart() {
		btnStart.setEnabled(!TextUtils.isEmpty(etHour.getText())
				&& Integer.parseInt(etHour.getText().toString()) > 0
				|| !TextUtils.isEmpty(etMin.getText())
				&& Integer.parseInt(etMin.getText().toString()) > 0
				|| !TextUtils.isEmpty(etSec.getText())
				&& Integer.parseInt(etSec.getText().toString()) > 0);
	}

	// 变量; Timer ,TimerTask java.util 的工具类
	private static final int MSG_WHAT_TIME_IS_UP = 1;
	private static final int MSG_WHAT_TIME_Tick = 2;

	private int sumTimerCount = 0;// 时间总和
//	用到 java.util.Timer  的timer和TimerTask

	private Timer timer = new Timer();
	private TimerTask timerTask = null;
	private Button btnStart, btnPause, btnResume, btnReset;
	private EditText etHour, etMin, etSec;
	//存储计时的某一段时间，并在ListView 中显示，自定义一个Adapter显示存储数据
//	private ListView listView;

}
