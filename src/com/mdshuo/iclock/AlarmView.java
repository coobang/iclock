package com.mdshuo.iclock;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

/**
 * 闹钟设置
 * 
 * @author TONNY
 * 
 */
public class AlarmView extends LinearLayout {

	private Button btnAlarm;
	private ListView lvAlarm;
	private ArrayAdapter<AlarmData> adapter;
	private static final String KEY_ALARM_LIST = "alarmList";
	private AlarmManager alarmManager;

	public AlarmView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public AlarmView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AlarmView(Context context) {
		super(context);
		init();
	}

	// 每个构造方法初始化,获取系统的闹钟服务
	private void init() {
		alarmManager = (AlarmManager) getContext().getSystemService(
				Context.ALARM_SERVICE);
		
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		//
		
		
		btnAlarm = (Button) findViewById(R.id.btnAlarm);
		lvAlarm = (ListView) findViewById(R.id.lvAlarm);

		adapter = new ArrayAdapter<AlarmView.AlarmData>(getContext(),
				android.R.layout.simple_list_item_1);
		lvAlarm.setAdapter(adapter);
		// 读取数据
		readSavedAlarmList();
		// 添加闹钟时间
		btnAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addAlarm();
			}
		});
		// 删除闹钟
		lvAlarm.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				new AlertDialog.Builder(getContext())
						.setTitle("操作选项")
						.setItems(new CharSequence[] { "删除", "修改" },
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										switch (which) {
										case 0:
											deleteAlarm(position);
											break;
										case 1:
											modifyAlarm(position);
											break;
										default:
											break;
										}
									}

								}).setNegativeButton("取消", null).show();
				return true;
			}
		});

	}

	// 删除闹钟
	private void deleteAlarm(int position) {
		AlarmData ad = adapter.getItem(position);
		adapter.remove(ad);
		// 删除之后保存未被删除的数据
		saveAlarmList();
		// 取消
		alarmManager.cancel(PendingIntent.getBroadcast(getContext(),
				ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));
	}

	// 修改闹钟
	private void modifyAlarm(int position) {
		AlarmData ad = adapter.getItem(position);
		adapter.remove(ad);
		addAlarm();
		saveAlarmList();
	}

	// 添加时钟
	private void addAlarm() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		//
		final Calendar calendar = Calendar.getInstance();
		TimePickerDialog timeDialog = new TimePickerDialog(getContext(),
				new TimePickerDialog.OnTimeSetListener() {
					// android.app 类 TimePickerDialog 时间选择对话框

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						// 设置时间
						calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
						// 秒和毫秒值清零，闹钟才会到分钟的时候响铃
						calendar.set(Calendar.SECOND, 0);
						// calendar.set(Calendar.MILLISECOND, 0);

						Calendar currentTime = Calendar.getInstance();
						if (calendar.getTimeInMillis() <= currentTime
								.getTimeInMillis()) {
							calendar.setTimeInMillis(calendar.getTimeInMillis()
									+ 24 * 60 * 60 * 1000);
						}

					}
				}, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true);
		// android 4.1 和 android 4.2 存在的bug
		timeDialog.setCanceledOnTouchOutside(false);

		timeDialog.show();
		timeDialog.setOnDismissListener(new OnDismissListener() {
			// android.app.Dialog setOnDismissListener 当dialog消失时被调用
			// android.content 接口 DialogInterface.OnDismissListener

			@Override
			public void onDismiss(DialogInterface dialog) {
				//
				AlarmData ad = new AlarmData(calendar.getTimeInMillis());
				adapter.add(ad);
				//
				alarmManager.setRepeating(alarmManager.RTC_WAKEUP, calendar
						.getTimeInMillis(), 5 * 60 * 1000, PendingIntent
						.getBroadcast(getContext(), ad.getId(), new Intent(
								getContext(), AlarmReceiver.class), 0));
				// 保存设置的时钟数据
				saveAlarmList();
			}
		});

	}

	// 保存时钟数据
	protected void saveAlarmList() {
		// TODO Auto-generated method stub
		// 偏好设置
		Editor editor = getContext().getSharedPreferences(
				AlarmView.class.getName(), Context.MODE_PRIVATE).edit();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < adapter.getCount(); i++) {
			sb.append(adapter.getItem(i).getTime()).append(",");
		}

		if (sb.length() > 1) {// 判断sb是否大于1 ，如果小于1，sb.length会出现异常
			String content = sb.toString().substring(0, sb.length() - 1);

			System.out.println(content);
			editor.putString(KEY_ALARM_LIST, content);
		} else {
			editor.putString(KEY_ALARM_LIST, null);
		}
		editor.commit();
	}

	// 读取保存的时钟数据
	private void readSavedAlarmList() {
		SharedPreferences sp = getContext().getSharedPreferences(
				AlarmView.class.getName(), Context.MODE_PRIVATE);
		String content = sp.getString(KEY_ALARM_LIST, null);

		if (content != null) {
			String[] timeStrings = content.split(",");
			for (String string : timeStrings) {//
				adapter.add(new AlarmData(Long.parseLong(string)));
			}
		}

	}

	// 静态内部类 AlarmData 设置时钟数据
	private static class AlarmData {

		public AlarmData(long time) {
			this.time = time;
			// 创建时间
			date = Calendar.getInstance();
			date.setTimeInMillis(time);
			timelLoble = String.format("%d月%d日 %d:%d",
					date.get(Calendar.MONTH) + 1,
					date.get(Calendar.DAY_OF_MONTH),
					date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE));
		}

		public long getTime() {
			return time;
		}

		public String getTimelLoble() {// 获取时间标签
			return timelLoble;
		}

		@Override
		public String toString() {// 返回时间标签
			// TODO Auto-generated method stub
			return getTimelLoble();
		}

		// long 类型转为int 类型 会出现越界，可以尽量优化下降低风险
		public int getId() {
			return (int) (getTime() / 1000 / 60);
		}

		private long time = 0;
		private String timelLoble = "";
		private Calendar date;
	}
	
	
}
