package si.todo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class HelloRegistrationReceiver extends BroadcastReceiver {


    /** 
     * Listens for a registration response message from C2DM.
     * Logs the received registration_id string.
     * You can view log messages using 'adb logcat'.
     */
    public void onReceive(Context context, Intent intent) {
        String m = "HelloRegistrationReceiver/onReceive";
        Log.i(m,"Entry.");
  
        String action = intent.getAction();
        Log.i(m,"action=" + action);
  
        if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
            String registrationId = intent.getStringExtra("registration_id");
            Log.i(m,"registrationId=" + registrationId);
            String error = intent.getStringExtra("error");
            Log.i(m,"error=" + error);
        }


        Log.i(m,"Exit.");
    }
}
