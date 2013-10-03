package dacer.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.dacer.testapp.MainActivity;

import java.util.Random;

import dacer.utils.AlarmHelper;
import dacer.utils.LogRecorder;
import dacer.utils.MyNotification;
import dacer.utils.MyStrings;

/**
 * Created by Dacer on 10/3/13.
 */
public class AutoAlarmService extends Service {

    public static String TEST_LOCK = "------WAKEUP TEST LOCK END-----";
    public static String TEST2_UNLOCK = "------WAKEUP TEST UNLOCK END-----";


    public static String START = "------START-----";
    public static String ABANDON = "------ABANDON------";

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        LogRecorder.record("AutoAlarmService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String endtime = intent.getStringExtra(MyStrings.FLAG_ENDTIME);
        String type = intent.getStringExtra(MyStrings.FLAG_ALARM_TYPE);
        LogRecorder.record("End"+" screen: "
            +(isScreenLocked()? "locked":"unlocked"));

        if(isScreenLocked()){
            if(isLockTaskFinished()){
                LogRecorder.record(ABANDON);
            }else{
                LogRecorder.record(TEST_LOCK);
            }

        }else if((!isScreenLocked())){
            if(isUnlockTaskFinished()){
                LogRecorder.record(ABANDON);
            }else{
                LogRecorder.record(TEST2_UNLOCK);
            }

        }else{
            LogRecorder.record(START);
        }
        Random random = new Random();
        int max = 40;
        int min = 20;
//        int max = 3;
//        int min = 1;
        int randomNum = random.nextInt(max - min) + min;
        if(isLockTaskFinished()&&isUnlockTaskFinished()){
            MyNotification.show(this, "测试已全部完毕", "感谢您的帮助，请返回程序后发送反馈", MainActivity.class, false);
        }else{
            AlarmHelper.setAutoNormalAlarm(this, randomNum);
        }

        stopSelf();
        return START_STICKY;
    }


    private boolean isScreenLocked(){
        SharedPreferences sp = getSharedPreferences(WakeLockService.SCREENLOCKED,MODE_PRIVATE);
        boolean result = sp.getBoolean(WakeLockService.SCREENLOCKED,false);
        return result;
    }

    public static boolean isLockTaskFinished(){
        return LogRecorder.numberStringInLog(TEST_LOCK) >= 5;
    }

    public static boolean isUnlockTaskFinished(){
        return LogRecorder.numberStringInLog(TEST2_UNLOCK) >= 5;
    }

    public static int LockTaskFinishedNUM(){
        return LogRecorder.numberStringInLog(TEST_LOCK);
    }

    public static int UnlockTaskFinishedNUM(){
        return LogRecorder.numberStringInLog(TEST2_UNLOCK);
    }

}
