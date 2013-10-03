package com.dacer.testapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

import dacer.service.AutoAlarmService;
import dacer.service.WakeLockService;
import dacer.utils.LogRecorder;
import dacer.utils.MyNotification;

public class MainActivity extends Activity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(!isWakeLockServiceRunning()){
                    Intent startServiceIntent = new Intent(MainActivity.this, WakeLockService.class);
                    MainActivity.this.startService(startServiceIntent);
                    btn.setText("Stop");
                }else{
                    stopService(new Intent(MainActivity.this,WakeLockService.class));
                    LogRecorder.record("Stop service by user.");
                    btn.setText("Start");
                }
            }
        });

        Button btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                sendLog();
            }
        });

    }

    @Override
    protected  void onResume(){
        super.onResume();
        if(isWakeLockServiceRunning()){
            btn.setText("Stop");
        }
        int finishNUM = AutoAlarmService.LockTaskFinishedNUM()+AutoAlarmService.UnlockTaskFinishedNUM();
        ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar);
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText("完成度："+finishNUM+"/10");
        bar.setProgress(100*finishNUM/10);
    }

    private boolean isWakeLockServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (WakeLockService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    void sendLog(){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{"dacerfeedback@gmail.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "测试程序的反馈");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Dacer.log");
        Uri emailUri = Uri.fromFile(file);
        emailIntent.putExtra(Intent.EXTRA_STREAM, emailUri);
        emailIntent.setType("application/pdf");
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}

