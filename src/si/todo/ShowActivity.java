package si.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ShowActivity extends Activity {

	Button loginButton, localLoginButton, aboutButton;
	TextView result;
	boolean networkFinish=false;
	boolean loginButtonClicked = false;
	String authResult=null;
	String authError = null;
	Thread checkLogin = null;
	Thread checkNetwork = null;
	int networkReqCode = 0;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	
		setContentView(R.layout.show_tb);
	    
		String message = null;
		Intent intent = getIntent();
		
		Bundle extras = intent.getExtras();
		  
	    if (extras != null) {
			message = extras.getString("message");
		} else {
			message = "extras is null ";
		}
		
		if (message == null) {
			message = "Message is null";
		}
	    result = (TextView)findViewById(R.id.lbl_result);
	    result.setText(message);
	}
}
