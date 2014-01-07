package si.todo;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyScheduleReceiver extends BroadcastReceiver {

	private static final long REPEAT_TIME = 1000 * 30;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// on boot up this will be executed
		Log.d("ON RECEIVEEE", "NOTRIII");
		Intent serviceIntent = new Intent(context, NotificationService.class);
		context.startService(serviceIntent);
		
		// get alarm manager
		AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		// make intent which will be executed every n seconds
		Intent i = new Intent(context, MyStartServiceReceiver.class);
		i.putExtra("message", "imba "+ "burek");
		
		// intent for repeating 
		//PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
		PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, 0);
		GlobalVariables.intentsInRepeating[0] = pending;
				
		// Start 30 seconds after boot completed
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 30);
		
		// Fetch every 30 seconds
		service.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), REPEAT_TIME, pending);

		// service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		// REPEAT_TIME, pending);

	}
}