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
 * ��ʱ��
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

	//android.view.View  onFinishInflate ��View��������view��xml�ļ�������֮�����
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		// �õ������Ͱ�ť������
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
		// ����ı��ı����
		etHour = (EditText) findViewById(R.id.etHour);
		etHour.addTextChangedListener(new TextWatcher() {
//android.text 
//			android.text �ӿ�  TextWatcher
		
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

		// ��ʼ�����ã�ʱ��Ĭ��Ϊ00��
		etHour.setText("00");
		etHour.setTextSize((float) 20);

		etMin.setText("00");
		etMin.setTextSize((float) 20);

		etSec.setText("00");
		etSec.setTextSize((float) 20);

		// ��ť��������
		btnStart.setVisibility(View.VISIBLE);
		btnStart.setEnabled(false);

		btnPause.setVisibility(View.GONE);
		btnReset.setVisibility(View.GONE);
		btnResume.setVisibility(View.GONE);

	}

	// ��ʼ��ʱ
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
					// ��ȡ��ǰʱ��
					handler.sendEmptyMessage(MSG_WHAT_TIME_Tick);

					if (sumTimerCount <= 0) {
						// �Ի�����ʾ
						handler.sendEmptyMessage(MSG_WHAT_TIME_IS_UP);
						stopTimer();
					}
				}

			};
			// ÿ��һ��ִ��һ��
			timer.schedule(timerTask, 1000, 1000);
			btnStart.setVisibility(View.GONE);
			btnPause.setVisibility(View.VISIBLE);
			btnReset.setVisibility(View.VISIBLE);
		}

	}

	// ֹͣ��ʱ
	protected void stopTimer() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}

	// ���������߳��е�����
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_TIME_Tick:// ��ȡ��ǰʱ��
				int hour = sumTimerCount / 60 / 60;
				int min = (sumTimerCount / 60) % 60;
				int sec = sumTimerCount % 60;
				// ÿ��һ��ʱ���������õ�ǰʱ��
				etHour.setText(hour + "");
				etMin.setText(min + "");
				etSec.setText(sec + "");

				break;
			case MSG_WHAT_TIME_IS_UP:
				// �Ի�����ʾ
				new AlertDialog.Builder(getContext()).setTitle("��ʱ")
						.setMessage("ʱ���ѵ�").setNegativeButton("ȷ��", null).show();
				break;

			default:
				break;
			}
		};
	};

	// ��鿪ʼ��ť�Ƿ�����
	private void checkToEnableBntStart() {
		btnStart.setEnabled(!TextUtils.isEmpty(etHour.getText())
				&& Integer.parseInt(etHour.getText().toString()) > 0
				|| !TextUtils.isEmpty(etMin.getText())
				&& Integer.parseInt(etMin.getText().toString()) > 0
				|| !TextUtils.isEmpty(etSec.getText())
				&& Integer.parseInt(etSec.getText().toString()) > 0);
	}

	// ����; Timer ,TimerTask java.util �Ĺ�����
	private static final int MSG_WHAT_TIME_IS_UP = 1;
	private static final int MSG_WHAT_TIME_Tick = 2;

	private int sumTimerCount = 0;// ʱ���ܺ�
//	�õ� java.util.Timer  ��timer��TimerTask

	private Timer timer = new Timer();
	private TimerTask timerTask = null;
	private Button btnStart, btnPause, btnResume, btnReset;
	private EditText etHour, etMin, etSec;
	//�洢��ʱ��ĳһ��ʱ�䣬����ListView ����ʾ���Զ���һ��Adapter��ʾ�洢����
//	private ListView listView;

}
