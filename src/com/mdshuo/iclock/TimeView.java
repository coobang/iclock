package com.mdshuo.iclock;

import java.util.Calendar;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * ʱ������
 * @author TONNY
 */
public class TimeView extends LinearLayout {
	
	// ��ʼ��
	public TimeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimeView(Context context) {
		super(context);
	}

	// ��ʼ�����֮��ִ�в���
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		tvTime = (TextView) findViewById(R.id.tvTime);
		timeHandler.sendEmptyMessage(0);
	}
	
	
	// �ɼ����Ա仯
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);

		if (visibility == View.VISIBLE) {
			timeHandler.sendEmptyMessage(0);
		} else {
			timeHandler.removeMessages(0);
		}
	}
	
	// ˢ��
	private void reflishTime() {
		// System.out.println("<<<<<<<<<");
		// ����ʱ��
		Calendar c = Calendar.getInstance();
		tvTime.setText(String.format("%d:%d:%d", c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE), c.get(Calendar.SECOND)));
	}

	// ����Handler
	private Handler timeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			reflishTime();
			// �ж�tab�Ƿ�ɼ����ɼ���ʾ
			if (getVisibility() == View.VISIBLE) {
				timeHandler.sendEmptyMessageDelayed(0, 1000);
			}
		};
	};
	
	private TextView tvTime;

}
