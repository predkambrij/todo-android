package si.todo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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

import si.todo.TodoUpdateFromServerActivity.DownloadJson;


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

public class PushToServerActivity extends Activity implements OnClickListener {

	private TodoDbAdapter dbHelper;
	private Thread pushingJson = null;
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
    private Thread uploadJson = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_to_server);
        
        // prepare database
		dbHelper = new TodoDbAdapter(this);
		dbHelper.open();
        
		// buttons
        View okButton = findViewById(R.id.pushOK);
        okButton.setOnClickListener(this);
        View cancelButton = findViewById(R.id.pushCancel);
        cancelButton.setOnClickListener(this);
        
        
        String url = "http://"+GlobalVariables.domain+":"+GlobalVariables.port+
				"/tpo/web/ajax/AndroidPutAllTasks.php?username="+
		GlobalVariables.username+"&password="+GlobalVariables.password+"";
		
		uploadJson = new UploadJson(url);
		uploadJson.start();

        // fetch json from internet and overwrite it to local database
        checkStatusAndUpdateProgress();
       
	}
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	dbHelper.close();
    }

	private void checkStatusAndUpdateProgress() {
 
		mProgress = (ProgressBar) findViewById(R.id.pushProgress);
		// progress bar
		new Thread(new Runnable() {
            public void run() {
            	while (true) {
	            	mProgressStatus = 40;
	            	if (finish == true) {
	            		mProgressStatus = 99;
	            		try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
	            		finish();
	            	}
	                // Update the progress bar
	                mHandler.post(new Runnable() {
	                    public void run() {
	                        mProgress.setProgress(mProgressStatus);
	                    }
	                });
            	}
            }
        }).start();	
	}
	
	
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.pushOK:
			finish();
			break;
		case R.id.pushCancel:
			finish();//Toast.makeText(this, "x="+jsonString + " finish:"+finish, Toast.LENGTH_LONG).show();
			break;
		}
	}
	
	class UploadJson extends Thread {
		String url = null;
		UploadJson(String url) {
			this.url = url;
		}
		@Override
		public void run() {
			
			try {
/*
// example
String buf = "["+
"[\"16\",\"Klikni z mi\u0161ko\",\"Na mi\u0161ki je na vrhu spredaj levo gumb, ki ga rabi\u0161 pritisnit.\",null,\"1\",\"Slu\u017eba\",\"2012-01-09 05:15:00\",\"2012-01-17 05:20:00\",\"0\",\"2012-01-17\",\"1\",\"-1\",\"-1\"],"+
"[\"20\",\"sfe\",null,null,\"1\",\"Slu\u017eba\",\"2012-01-23 00:00:00\",\"2012-01-19 01:00:00\",\"0\",\"2012-01-17\",\"0\",\"-1\",\"-1\"]"+
"]";
*/				
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

Cursor cursor = dbHelper.doQuery(true, new String[]{//task talble
		"title","description","estimated_time","priority","categoryname","categoryname","start_date",
		"due_date","repeat_time","repeat_ends","acknowledge","reminder_email","reminder_sms"},
		null, null, null, null, null);


String title = cursor.getString(cursor.getColumnIndex("title"));

String description = cursor.getString(cursor.getColumnIndex("description"));


/* grab the posts from the db */
String query = "SELECT task.ID_task, task.title, task.description, "+
			"task.estimated_time, task.priority, category.name, "+
			"task.start_date, task.due_date, task.repeat_time, "+
			"task.repeat_ends, task.acknowledge, task.reminder_email, "+
			"task.reminder_sms "+
		"FROM task, user, category "+
		"WHERE task.ID_user=user.ID_user and user.ID_user=category.ID_user and "+
		"category.ID_category=task.ID_category and "+
		"user.username=" ;


				// build JSONArray
				JSONArray todos = new JSONArray(); // buf

					
				String data = URLEncoder.encode("json", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8"); //buf
			    
				URL url;
				url = new URL(this.url);
			    URLConnection conn = url.openConnection();
			    conn.setDoOutput(true);
			    
				BufferedWriter wr = new BufferedWriter(
					        new OutputStreamWriter(conn.getOutputStream()));
				wr.write(data);
			    wr.flush();
			    
			    // request sent
			    
			    // Get the response
			    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    String line;
			    while ((line = rd.readLine()) != null) {
			        Log.d("push:", "line:"+line);
			    }
			    wr.close();
			    rd.close();
			    
			} catch (Exception e) {
				e.printStackTrace();
				jsonString += e.getMessage();
			}
				
			finish = true;
			
		
			
		}
	}
}


//JSONArray


