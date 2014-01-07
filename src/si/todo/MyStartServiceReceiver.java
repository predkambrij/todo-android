package si.todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyStartServiceReceiver extends BroadcastReceiver {
	
	private static long REPEATS = 5;

	private void cancelRepeating(Context context) {
		// get intent
		PendingIntent pending = GlobalVariables.intentsInRepeating[0];
		
		// get alarm manager
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		// cancel annoying repeating
		am.cancel(pending);
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		REPEATS--;
		if (REPEATS <= 0) {
			cancelRepeating(context);
			return;
		}
		
		
		
		Intent service = new Intent(context, NotificationService.class);
		service.putExtra("message", "from  MyStartServiceReceiver "+ "burek");
		context.startService(service);
	}
}
