package com.mdshuo.iclock;

import java.util.Calendar;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 时钟设置
 * @author TONNY
 */
public class TimeView extends LinearLayout {
	
	// 初始化
	public TimeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TimeView(Context context) {
		super(context);
	}

	// 初始化完成之后执行操作
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		tvTime = (TextView) findViewById(R.id.tvTime);
		timeHandler.sendEmptyMessage(0);
	}
	
	
	// 可见属性变化
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);

		if (visibility == View.VISIBLE) {
			timeHandler.sendEmptyMessage(0);
		} else {
			timeHandler.removeMessages(0);
		}
	}
	
	// 刷新
	private void reflishTime() {
		// System.out.println("<<<<<<<<<");
		// 设置时间
		Calendar c = Calendar.getInstance();
		tvTime.setText(String.format("%d:%d:%d", c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE), c.get(Calendar.SECOND)));
	}

	// 创建Handler
	private Handler timeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			reflishTime();
			// 判断tab是否可见，可见显示
			if (getVisibility() == View.VISIBLE) {
				timeHandler.sendEmptyMessageDelayed(0, 1000);
			}
		};
	};
	
	private TextView tvTime;

}
