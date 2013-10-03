package dacer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dacer.service.WakeLockService;
import dacer.utils.LogRecorder;

/**
 * Created by Dacer on 10/3/13.
 */
public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        LogRecorder.record("System boot completed.");
        Intent startServiceIntent = new Intent(context, WakeLockService.class);
        context.startService(startServiceIntent);
    }
}
