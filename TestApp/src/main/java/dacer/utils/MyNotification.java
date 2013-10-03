package dacer.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.dacer.testapp.MainActivity;
import com.dacer.testapp.R;

/**
 * Created by Dacer on 10/3/13.
 */
public class MyNotification {

    public static void show(Context mContext,String title,String text,Class<?> cls,boolean ongoing){
        Notification notification = new Notification(R.drawable.ic_launcher, title,
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(mContext, cls);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);
        notification.setLatestEventInfo(mContext, title,
                text, pendingIntent);
        NotificationManager nm =  (NotificationManager)mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        nm.notify(22,notification);
    }

    public void cancel(Context mContext){
        NotificationManager manager=(NotificationManager)mContext.
                getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            manager.cancelAll();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
