package com.mdshuo.iclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//�㲥���������ǵ����嵥�ļ���ע��
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	System.out.println("����ִ����");
	//���ӹ�����
		AlarmManager am = (AlarmManager) context
				.getSystemService(context.ALARM_SERVICE);
		// ȡ������ǰִ�е�����
		am.cancel(PendingIntent.getBroadcast(context, getResultCode(),
				new Intent(context, AlarmReceiver.class), 0));
		//������ͼ������activity_alarm ʱ����ʾ����
		Intent i=new Intent(context, AlarmActvity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);

	}

}
