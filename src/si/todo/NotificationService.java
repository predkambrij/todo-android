package si.todo;
import java.util.Random;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.widget.Toast;

public class NotificationService extends IntentService {

//	private static final int HELLO_ID = 1;
//	private static final int HELLO2_ID = 2;
	private static int HELLO_IDs = 1;
	
	private String message = null;
	
  /** 
   * A constructor is required, and must call the super IntentService(String)
   * constructor with a name for the worker thread.
   */
  public NotificationService() {
      super("NotificationService");
  }
  
  private void notif1() {
	  Toast.makeText(this, "service onHandleIntent", Toast.LENGTH_SHORT).show();
      // 1 Get a reference to the NotificationManager
      String ns = Context.NOTIFICATION_SERVICE;
      NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
      
      // 2 Instantiate the Notification
      int icon = R.drawable.icon;
      CharSequence tickerText = "TickerText";
      long when = System.currentTimeMillis();
      Notification notification = new Notification(icon, tickerText, when);
      
      // 3 Define the notification's message and PendingIntent
      Context context = getApplicationContext();
      CharSequence contentTitle = "contentTitle";
      CharSequence contentText = this.message;
      Intent notificationIntent = new Intent(this, ShowActivity.class);
      notificationIntent.putExtra("message", "normaln "+ this.message); 
      PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
      
//      notification.defaults = Notification.DEFAULT_ALL;
      notification.defaults = 0;
      notification.defaults |= Notification.DEFAULT_VIBRATE;
      
      // when you click on notification it should hide itself
      notification.flags |= Notification.FLAG_AUTO_CANCEL;
//      notification.defaults |= Notification.;
      
      
      notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
      
      // 4 Pass the Notification to the NotificationManager
      mNotificationManager.notify(this.HELLO_IDs, notification);
      NotificationService.HELLO_IDs++;
      //mNotificationManager.cancel(i);
      
  }
  

  /**
   * The IntentService calls this method from the default worker thread with
   * the intent that started the service. When this method returns, IntentService
   * stops the service, as appropriate.
   */
  @Override
  protected void onHandleIntent(Intent intent) {
	  
  }
 
  @Override
  public void onCreate() {
	super.onCreate();
	
	
	Toast.makeText(this, "service onCreate", Toast.LENGTH_SHORT).show();
  }
  
  @Override
  public void onDestroy() {
      super.onDestroy();
      Toast.makeText(this, "service onDestroy", Toast.LENGTH_SHORT).show();
      
      
  }
  
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle extras = intent.getExtras();
	  
	    if (extras != null) {
			this.message = extras.getString("message");
		} else {
			this.message = "extras is null ";
		}
		
		if (this.message == null) {
			this.message = "Message is null";
		}

	  Toast.makeText(this, "service onStartCommand", Toast.LENGTH_SHORT).show();
      for (int i=0; i<3; i++) {
		  // send notify
		  notif1();
		  
		  // sleep 1 min
		  try {
			Thread.sleep(1*1000);
		} catch (InterruptedException e) {
		}
		  
		stopSelf();
	  }
      return super.onStartCommand(intent,flags,startId);
  }






}
