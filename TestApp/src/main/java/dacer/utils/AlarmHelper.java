package dacer.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;

import dacer.service.AutoAlarmService;

/**
 * Created by Dacer on 10/3/13.
 */
public class AlarmHelper {

//    public static void setWakeUpAlarm(Context mContext, int min,Class<?> cls){
//        long nowMills = System.currentTimeMillis();
//        long endMills = nowMills + 60000 * min;
//        LogRecorder.record("setWakeUpAlarm: "+getEndDate(endMills));
//
//        Intent intent = new Intent(mContext, cls);
//        intent.putExtra(MyStrings.FLAG_ENDTIME,getEndDate(endMills));
//        intent.putExtra(MyStrings.FLAG_ALARM_TYPE,"WakeUp");
//
//        PendingIntent pIntent = PendingIntent.getService(mContext, 0, intent, 0);
//        AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);;
//        am.set(AlarmManager.RTC_WAKEUP, endMills, pIntent);
//    }

    public static void setAutoNormalAlarm(Context mContext, int min){
        long nowMills = System.currentTimeMillis();
        long endMills = nowMills + 60000 * min;
        LogRecorder.record("setAlarm: "+getEndDate(endMills));

        Intent intent = new Intent(mContext, AutoAlarmService.class);
        intent.putExtra(MyStrings.FLAG_ENDTIME,getEndDate(endMills));
        intent.putExtra(MyStrings.FLAG_ALARM_TYPE,"Normal");

        PendingIntent pIntent = PendingIntent.getService(mContext, 0, intent, 0);
        AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);;
        am.set(AlarmManager.RTC, endMills, pIntent);
    }


    public static void cancelAlarm(Context mContext, Class<?> cls){
        Intent intent = new Intent(mContext, cls);
        PendingIntent pIntent = PendingIntent.getService(mContext, 0, intent, 0);
        AlarmManager am = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);;
        am.cancel(pIntent);
    }


    private static String getEndDate(long endMills){
        Date date=new Date();
        SimpleDateFormat SDF=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date.setTime(endMills);
        String dString = SDF.format(date);
        return dString;
    }
}
