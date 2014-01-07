package si.todo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;
import android.app.Activity;

public class HelloMessageReceiver extends BroadcastReceiver {

	// 4
    private static final int HELLO_ID = 1;
    //
    /** 
     * Listens for a notification message from C2DM.
     * Logs the received message payload.
     * You can view log messages using 'adb logcat'.
     */
    public void onReceive(Context context, Intent intent) {
        String m = "HelloMessageReceiver/onReceive";
        Log.i(m,"Entry.");


        String action = intent.getAction();
        Log.i(m,"action=" + action);


        if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
            String payload = intent.getStringExtra("payload");
            Log.i(m,"payload=" + payload);
          

            String error = intent.getStringExtra("error");
            Log.i(m,"error=" + error);
            
            // do notification
            Intent serviceIntent = new Intent(context, NotificationService.class);
            serviceIntent.putExtra("message", payload);

            
            context.startService(serviceIntent);
        }


        Log.i(m,"Exit.");
    }
}
