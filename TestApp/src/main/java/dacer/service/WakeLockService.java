package dacer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;

import com.dacer.testapp.MainActivity;
import com.dacer.testapp.R;

import dacer.utils.AlarmHelper;
import dacer.utils.LogRecorder;
import dacer.utils.MyNotification;

/**
 * Created by Dacer on 10/3/13.
 * Main Service to control whole test.
 */
public class WakeLockService extends Service {
    private PowerManager.WakeLock wl;
    public static final String SCREENLOCKED = "screen_locked";


    private BroadcastReceiver mybroadcast = new BroadcastReceiver() {

        //When Event is published, onReceive method is called
        @Override
        public void onReceive(Context context, Intent intent) {

            SharedPreferences sp = getSharedPreferences(SCREENLOCKED,MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                LogRecorder.record("--Screen on--");

                editor.putBoolean(SCREENLOCKED,false);

            }
            else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
                LogRecorder.record("--Screen off--");
                editor.putBoolean(SCREENLOCKED,true);
            }

            editor.commit();

        }
    };

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        LogRecorder.record("---WakelockService started---");
        init();

//        MyNotification.show(this,"d","3",MainActivity.class,false);
    }

    public void onDestroy (){
        LogRecorder.record("---WakelockService destroyed---");
        if(wl.isHeld()){
            wakeRelase();
        }
        unregisterReceiver(mybroadcast);
        AlarmHelper.cancelAlarm(this,AutoAlarmService.class);
    }

    private void init(){
        //Wake Lock
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Dacer's TestApp");
        wakeLock();

        //Screen Lock Recorder
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(mybroadcast, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        //Put service to foreground
        Notification notification = new Notification(R.drawable.ic_launcher, "测试进程已启动",
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, "测试进程启动中",
                "点击我可终止", pendingIntent);
        startForeground(22, notification);


        //start auto service
        AlarmHelper.setAutoNormalAlarm(this,1);
    }

    private void wakeLock(){
        wl.acquire();
        LogRecorder.record("---Wake Locked---");
    }

    private void wakeRelase(){
        wl.release();
        LogRecorder.record("---Wake Released---");
    }



}
