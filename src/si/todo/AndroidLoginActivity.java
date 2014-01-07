package si.todo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidLoginActivity extends Activity implements OnClickListener {
	
	Button loginButton, localLoginButton, aboutButton;
	TextView result;
	boolean networkFinish=false;
	boolean loginButtonClicked = false;
	String authResult=null;
	String authError = null;
	Thread checkLogin = null;
	Thread checkNetwork = null;
	private TodoDbAdapter dbHelper;
	int networkReqCode = 0;
	
	// C2DM TEST
	final static String _developerEmail = "test1server@gmail.com";
	//
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // C2DM TEST TODO
        String m = "HelloActivity/onCreate";
        Log.i(m,"Entry.");
        
//        TextView tv = new TextView(this);
//        tv.setText("Hello, Android");
//        setContentView(tv);
        
        // Start an intent to register this app instance with the C2DM service.
        Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
        intent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
        intent.putExtra("sender", _developerEmail);
        startService(intent);
        
        Log.i(m,"Exit.");
        
        // end of C2DM TEST
        
        setContentView(R.layout.login);
        
        dbHelper = new TodoDbAdapter(this);
		dbHelper.open();
        
        // Login button clicked
		loginButton = (Button)findViewById(R.id.btn_login);
        loginButton.setOnClickListener(this);
        
        localLoginButton = (Button)findViewById(R.id.btn_local_login);
        localLoginButton.setOnClickListener(this);
        
        aboutButton = (Button)findViewById(R.id.btn_about);
        aboutButton.setOnClickListener(this);
        
        result = (TextView)findViewById(R.id.lbl_result);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	dbHelper.close();
    }
  
    public void processLocalLogin(String username, String password) {
    	try {
			Cursor cursor = dbHelper.doMQuery(true, new String[]{"username","password"},
					null, null, null, null, null);
			// TODO fix out of bound exception
			String dusername = cursor.getString(cursor.getColumnIndex("username"));
			String dpassword = cursor.getString(cursor.getColumnIndex("password"));
			
			//if (username.equals(dusername) && password.equals(dpassword)) {
				GlobalVariables.username = username;
				GlobalVariables.password = password;
				result.setText("Login successful");
				
				startActivity(new Intent(this, ShowTodosActivity.class));
 				//startActivity(new Intent(this, Menu1Activity.class));
 				finish(); //FLAG_ACTIVITY_NO_HISTORY
 				
			//} else {
			//	result.setText("Login failed");
			//}
		} catch (SQLException e) {
			Log.e("AndroidLogin localLoginButton", "SQLException "+e.getMessage());
		}
    }

    public void processLogin(String username, String password) {
    	if(authResult != null) { // TODO ==
    		//Toast.makeText(this, "AuthError= "+authError, Toast.LENGTH_LONG).show();
    		Toast.makeText(this, "ConnectionError", Toast.LENGTH_LONG).show();
 		} else {
 			//if (authResult.equalsIgnoreCase("false")) {
 			//	result.setText("Login failed");
            //} else if (authResult.equalsIgnoreCase("true")) {
            	result.setText("Login successful");
 				GlobalVariables.username = username;
 				GlobalVariables.password = password;
 				
 				Intent i;
 				
 				// TODO, če je username && pass enak kakor je v bazi ne flushat
 				// flush both databases, because potentional new user
 				dbHelper.dropDB();
 				dbHelper.dropMDB();
 				
 				dbHelper.createDB();
 				dbHelper.createMDB();
 				
 				// insert user management data
 				dbHelper.execSQL("insert into "+GlobalVariables.DB_MTABLE
 						+" (_id, username, password) values "+
	            		"("+0+", "+"'"+username+"', '"+password+"'"+")");
 				
 				//TODO start unnecessary animation
 				
 				
				startActivity(new Intent(this, ShowTodosActivity.class));
 				//startActivity(new Intent(this, Menu1Activity.class));
 				finish(); //FLAG_ACTIVITY_NO_HISTORY
 				
// 				i = new Intent(this, AnimationDemoActivity.class);
// 				startActivityForResult(i, 2);
 				
 				
 				finish(); //FLAG_ACTIVITY_NO_HISTORY 
            //} else {
            	// there is a big problem
            	//Log.e("AndroidLogin.java", "AuthResult= "+authResult);
            	//Toast.makeText(this, "AuthResult= "+authResult,
            		//						Toast.LENGTH_LONG).show();
            //} // end authResult.equalsIgnoreCase("false")
 		} // end authResult == null
    }
    public static String hashFunction (String str) {
		MessageDigest m;
		String hashtext = null;
		try {
			m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(str.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while(hashtext.length() < 32 ){
			  hashtext = "0"+hashtext;
			}
		} catch (NoSuchAlgorithmException e) {
		}
		
		return hashtext;

	}
	@Override
	public void onClick(View view) {
		EditText uname = (EditText)findViewById(R.id.txt_username);
     	String username = uname.getText().toString();

     	EditText pword = (EditText)findViewById(R.id.txt_password);
     	
     	String password = pword.getText().toString();
     	password = hashFunction(password); // do hash from password
     	
     	String url = "http://"+GlobalVariables.domain+":"+GlobalVariables.port+
				GlobalVariables.pathPrefix+
				"/ajax/AndroidCheckAuthentication.php?username="+
				username+"&password="+password+"";
		
     	Log.d("login", url);
		if(view == loginButton){
         	
			
			// network login
//			Intent intent = new Intent(this, NetworkLoginActivity.class);
//			intent.putExtra("username", username);
//			intent.putExtra("password", password);
//			startActivityForResult(intent, networkReqCode);
			//TODO

			//SELECT name FROM sqlite_master WHERE type='table' AND name='table_name';
			result.setText("Login in progress...");
			
         	if (loginButtonClicked == false) {
         		loginButtonClicked = true;
         		
         		//checkLogin = new CheckAuthentication(url);
             	//checkLogin.start();
         	}
         	
         	
         	
         	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}

//         	if (networkFinish == true) {
         		processLogin(username,password);
         		loginButtonClicked = false;
//         		networkFinish = false;
//     		} else {
     			
//     		}
         	
		} else if (view == localLoginButton) {
			processLocalLogin(username,password);
			
		} else if (view == aboutButton){
			startActivity(new Intent(this, AnimationDemoActivity.class));
		} else {
			Log.e("AndroidLogin", "unknown button"+view);
		}
	}

	
	
	class CheckAuthentication extends Thread {
		String url = null;
		CheckAuthentication(String url) {
			this.url = url;
		}
		@Override
		public void run() {
			
			try {
				URL url;
				url = new URL(this.url);
				BufferedReader in = new BufferedReader(
								new InputStreamReader(url.openStream()));
				
				authResult = "";
				String inputLine;
				  
				while ((inputLine = in.readLine()) != null)
					authResult+=inputLine;
				
				in.close();
			} catch (Exception e) {
				authResult = null;
				authError = e.getMessage();
			}
						 
			networkFinish = true;
		}
	}

	

	
}


