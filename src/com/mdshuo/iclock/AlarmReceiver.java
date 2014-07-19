package com.mdshuo.iclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//广播接收器；记得在清单文件中注册
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	System.out.println("闹钟执行了");
	//闹钟管理器
		AlarmManager am = (AlarmManager) context
				.getSystemService(context.ALARM_SERVICE);
		// 取消掉当前执行的闹钟
		am.cancel(PendingIntent.getBroadcast(context, getResultCode(),
				new Intent(context, AlarmReceiver.class), 0));
		//创建意图，启动activity_alarm 时间提示界面
		Intent i=new Intent(context, AlarmActvity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);

	}

}
