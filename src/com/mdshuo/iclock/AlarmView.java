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
 * ��������
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

	// ÿ�����췽����ʼ��,��ȡϵͳ�����ӷ���
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
		// ��ȡ����
		readSavedAlarmList();
		// �������ʱ��
		btnAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addAlarm();
			}
		});
		// ɾ������
		lvAlarm.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				new AlertDialog.Builder(getContext())
						.setTitle("����ѡ��")
						.setItems(new CharSequence[] { "ɾ��", "�޸�" },
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

								}).setNegativeButton("ȡ��", null).show();
				return true;
			}
		});

	}

	// ɾ������
	private void deleteAlarm(int position) {
		AlarmData ad = adapter.getItem(position);
		adapter.remove(ad);
		// ɾ��֮�󱣴�δ��ɾ��������
		saveAlarmList();
		// ȡ��
		alarmManager.cancel(PendingIntent.getBroadcast(getContext(),
				ad.getId(), new Intent(getContext(), AlarmReceiver.class), 0));
	}

	// �޸�����
	private void modifyAlarm(int position) {
		AlarmData ad = adapter.getItem(position);
		adapter.remove(ad);
		addAlarm();
		saveAlarmList();
	}

	// ���ʱ��
	private void addAlarm() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		//
		final Calendar calendar = Calendar.getInstance();
		TimePickerDialog timeDialog = new TimePickerDialog(getContext(),
				new TimePickerDialog.OnTimeSetListener() {
					// android.app �� TimePickerDialog ʱ��ѡ��Ի���

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
							int minute) {
						// ����ʱ��
						calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
						calendar.set(Calendar.MINUTE, minute);
						// ��ͺ���ֵ���㣬���ӲŻᵽ���ӵ�ʱ������
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
		// android 4.1 �� android 4.2 ���ڵ�bug
		timeDialog.setCanceledOnTouchOutside(false);

		timeDialog.show();
		timeDialog.setOnDismissListener(new OnDismissListener() {
			// android.app.Dialog setOnDismissListener ��dialog��ʧʱ������
			// android.content �ӿ� DialogInterface.OnDismissListener

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
				// �������õ�ʱ������
				saveAlarmList();
			}
		});

	}

	// ����ʱ������
	protected void saveAlarmList() {
		// TODO Auto-generated method stub
		// ƫ������
		Editor editor = getContext().getSharedPreferences(
				AlarmView.class.getName(), Context.MODE_PRIVATE).edit();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < adapter.getCount(); i++) {
			sb.append(adapter.getItem(i).getTime()).append(",");
		}

		if (sb.length() > 1) {// �ж�sb�Ƿ����1 �����С��1��sb.length������쳣
			String content = sb.toString().substring(0, sb.length() - 1);

			System.out.println(content);
			editor.putString(KEY_ALARM_LIST, content);
		} else {
			editor.putString(KEY_ALARM_LIST, null);
		}
		editor.commit();
	}

	// ��ȡ�����ʱ������
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

	// ��̬�ڲ��� AlarmData ����ʱ������
	private static class AlarmData {

		public AlarmData(long time) {
			this.time = time;
			// ����ʱ��
			date = Calendar.getInstance();
			date.setTimeInMillis(time);
			timelLoble = String.format("%d��%d�� %d:%d",
					date.get(Calendar.MONTH) + 1,
					date.get(Calendar.DAY_OF_MONTH),
					date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE));
		}

		public long getTime() {
			return time;
		}

		public String getTimelLoble() {// ��ȡʱ���ǩ
			return timelLoble;
		}

		@Override
		public String toString() {// ����ʱ���ǩ
			// TODO Auto-generated method stub
			return getTimelLoble();
		}

		// long ����תΪint ���� �����Խ�磬���Ծ����Ż��½��ͷ���
		public int getId() {
			return (int) (getTime() / 1000 / 60);
		}

		private long time = 0;
		private String timelLoble = "";
		private Calendar date;
	}
	
	
}
