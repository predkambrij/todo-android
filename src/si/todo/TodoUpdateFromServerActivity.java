package si.todo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

public class TodoUpdateFromServerActivity extends Activity implements OnClickListener {

	private TodoDbAdapter dbHelper;
	private Thread downloadJson = null;
	private Thread putIntoDb = null;
	private String jsonString = "";
	//private TodoDbAdapter mDbHelper;
	
	// downloaded
	boolean finish = false;
	
	// in db
	boolean indb = false;
	
	private int mProgressStatus = 0;
	ProgressBar mProgress = null;
    private Handler mHandler = new Handler();

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatefromserver);
        
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
							GlobalVariables.pathPrefix+
							"/ajax/AndroidGetAllTasks.php?username="+
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
                    		try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							}
                    		finish();
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
/*
			"create table task"+
			"("+
			   "_id					 INTEGER PRIMARY KEY AUTOINCREMENT,"+
			   "title                varchar(300),"+
			   "description          varchar(8000),"+
			   "estimated_time       int,"+
			   "priority             int,"+
			   "categoryname         varchar(150)"+
			   "start_date           datetime,"+
			   "due_date             datetime,"+
			   "repeat_time          int,"+
			   "repeat_ends          date,"+
			   "acknowledge          bool,"+
			   "reminder_email       bool,"+
			   "reminder_sms         bool,"+
			")"
			
 */
			try {
				dbHelper.dropDB();
				dbHelper.createDB();
				
				// array todos
				JSONArray todos = new JSONArray(this.json);
				
				String atodo[] = new String[13];
	
				// go over todos
				for (int i = 0; i < todos.length(); i++) {
					JSONArray todo = todos.getJSONArray(i);
		            for (int j=0; j<todo.length(); j++) {
		            	atodo[j] = todo.getString(j);
		            }
		            dbHelper.execSQL("insert into task (_id,title,description,"+
		            "estimated_time, priority, categoryname, start_date, "+
		            "due_date, repeat_time, repeat_ends, acknowledge, "+
            		"reminder_email, reminder_sms) values("+atodo[0]+","+ //tid
            		" '"+atodo[1]+"' "+","+" '"+atodo[2]+"' "+","+ //title, description
            		atodo[3]+","+atodo[4]+","+" '"+atodo[5]+"' "+ // estimated time, priority, categoryname
            		","+" '"+atodo[6]+"' "+","+" '"+atodo[7]+"' "+","+ //start_date, due_date
            		atodo[8]+","+" '"+atodo[9]+"' "+","+atodo[10]+","+ //repeat_time, repeat_ends, acknowledge
            		" '"+atodo[11]+"' "+","+" '"+atodo[12]+"' "+")"); // reminder_email, reminder_sms
			    }

				indb = true;
				
			} catch (JSONException e) {
				Log.d("MY APP","json failed"+e.getMessage());
			}
		}
	}
}





