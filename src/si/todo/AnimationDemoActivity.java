package si.todo;


	 
	import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
	import android.os.Bundle;
	import android.view.View;
	import android.view.animation.Animation;
	import android.view.animation.AnimationUtils;
	import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;
	 
	public class AnimationDemoActivity extends Activity implements AnimationListener {
	 
	    View v;
	    Boolean STOP = false;
	    Intent serviceIntent;
	    
	    
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.anim);
	        startAnimation(null);
	    }
	    public void startAnimation(View view) {
	        Animation animation = AnimationUtils.loadAnimation(this,R.anim.animation);
	        animation.setAnimationListener(this);
	        View animatedView = findViewById(R.id.textview);
	        animatedView.startAnimation(animation);
	    }
	 
	    @Override
	    public void onAnimationStart(Animation animation) {
//	    	serviceIntent = new Intent(this, NotificationService.class);
//	    	startService(serviceIntent);

	    	//	    	Intent serviceIntent = new Intent(context, NotificationService.class);
//	    	context.startService(serviceIntent);
	    	
	    	
	    	
//	    	Toast.makeText(this, "Authors: Alojzij Blatnik", Toast.LENGTH_SHORT).show();
//	    	Toast.makeText(this, "Authors: Tadeja Saje", Toast.LENGTH_SHORT).show();
//	    	Toast.makeText(this, "Authors: Tadej Žarn", Toast.LENGTH_SHORT).show();
//	    	Toast.makeText(this, "Authors: Alojzij Blatnik, Tadeja Saje, Tadej Žarn", Toast.LENGTH_LONG).show();
	    }
	 

	    public void onAnimationEnd(Animation animation) {
	    	Toast.makeText(this, "Animacija končana", Toast.LENGTH_SHORT).show();
	    	//stopService(serviceIntent);
	    	
	        finish();
	    }

	    @Override
		public void onAnimationRepeat(Animation animation) {

		} 

	}