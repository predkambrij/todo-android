package si.todo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

public class NetworkLoginActivity extends Activity implements OnClickListener {

	private TodoDbAdapter dbHelper;
	private Thread downloadJson = null;
	private Thread putIntoDb = null;
	private String jsonString = "";
	
	// downloaded
	boolean finish = false;
	
	// in db
	boolean indb = false;
	
	private int mProgressStatus = 0;
	ProgressBar mProgress = null;
    private Handler mHandler = new Handler();
    String username = null;
    String password = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_network);
        
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
        	username = extras.getString("username");
        	password = extras.getString("password");
        }
        
		String url = "http://"+GlobalVariables.domain+":"+GlobalVariables.port+
				"/tpo/web/ajax/AndroidCheckAuthentication.php?username=" +
         			username + "&password="+password;
 
		
        // prepare database
		dbHelper = new TodoDbAdapter(this);
		dbHelper.open();
        
		// buttons
        View okButton = findViewById(R.id.updateOK);
        okButton.setOnClickListener(this);
        View cancelButton = findViewById(R.id.updateCancel);
        cancelButton.setOnClickListener(this);
        
        // fetch json from internet and overwrite it to local database
        checkStatusAndUpdateProgress();
       
	}
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	dbHelper.close();		
	}
    

	private void checkStatusAndUpdateProgress() {
 	
		String url = "http://"+GlobalVariables.domain+":"+GlobalVariables.port+
							"/tpo/web/ajax/AndroidGetAllTasks.php?username="+
			GlobalVariables.username+"&password="+GlobalVariables.password+"";
    	
		downloadJson = new DownloadJson(url);
		downloadJson.start();
		
		putIntoDb = new PutIntoDb();
        putIntoDb.start();
        
		mProgress = (ProgressBar) findViewById(R.id.updateProgress);
		
		// progress bar
		new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (finish == true) {
                    	mProgressStatus = 40;
                    	if (indb == true) {
                    		mProgressStatus = 99;
                    	}
                    		
                    } else {
                    	mProgressStatus = 10;
                    }
                    
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                    
                    try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                } // while true
            }
        }).start();

		
		//Toast.makeText(this, "x="+x, Toast.LENGTH_LONG).show();
		

	
	}
	
	
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.updateOK:
			finish();
			break;
		case R.id.updateCancel:
			finish();//Toast.makeText(this, "x="+jsonString + " finish:"+finish, Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	// download json and put it to string
	class DownloadJson extends Thread {
		String url = null;
		DownloadJson(String url) {
			this.url = url;
		}
		@Override
		public void run() {
			
			
			try { 
				URL url;
				url = new URL(this.url);
				 BufferedReader in = new BufferedReader(
					        new InputStreamReader(url.openStream()));

					  String inputLine;

					  while ((inputLine = in.readLine()) != null)
					      jsonString+=inputLine;

					  in.close();
			} catch (Exception e) {
				e.printStackTrace();
				jsonString += e.getMessage();
			}
			 
			
			finish = true;
		}
	}
	
	// put string to database
	class PutIntoDb extends Thread {
		String json = null;
		PutIntoDb() {

		}
		@Override
		public void run() {
			// wait previous thread for download json
			Log.d("MY APP","JSON STARTING WAITING THE NETWORK");
			while (true) {
                if (finish == false) {
                	try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                } else {
        			this.json = jsonString;
                	break;
                }
			}
			Log.d("MY APP","STARTING PARSING JSON");
			// so we are sure that we have json in jsonString
			
			try {
				dbHelper.dropDB();
				dbHelper.createDB();
				
				// array todos
				JSONArray todos = new JSONArray(this.json);
				
				String atodo[] = new String[4];
	
				// go over todos
				for (int i = 0; i < todos.length(); i++) {
					JSONArray todo = todos.getJSONArray(i);
		            for (int j=0; j<todo.length(); j++) {
		            	atodo[j] = todo.getString(j);
		            }
		            dbHelper.execSQL("insert into task (_id, title,description,"+
		            		"priority,categoryname) values("+i+", "+
		            		"'"+atodo[0]+"', '"+atodo[1]+"', "+
		            		Integer.parseInt(atodo[2])+", '"+atodo[3]+"'"+
		            		")");
			    }

				indb = true;
				
			} catch (JSONException e) {
				Log.d("MY APP","json failed"+e.getMessage());
			}
		}
	}
}
