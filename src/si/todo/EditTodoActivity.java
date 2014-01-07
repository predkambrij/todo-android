package si.todo;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditTodoActivity extends Activity implements OnClickListener {

	private TodoDbAdapter dbHelper;
	private String id=null;
	Button confirmButton = null;
	Button cancelButton = null;
	EditText esummary = null;
	EditText edescription = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_edit);
        
        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
        id = extras.getString("_id");
        }


        
        dbHelper = new TodoDbAdapter(this);
		dbHelper.open();
		
		confirmButton = (Button)findViewById(R.id.todo_edit_button);
        confirmButton.setOnClickListener(this);
		
        cancelButton = (Button)findViewById(R.id.todo_edit_buttonCancel);
        cancelButton.setOnClickListener(this);
        
		try {
			Cursor cursor = dbHelper.doQuery(true, new String[]{
					"title","description","priority","categoryname"},
					"_id="+id, null, null, null, null);
			
			
			String title = cursor.getString(cursor.getColumnIndex("title"));
			
			String description = cursor.getString(cursor.getColumnIndex("description"));
			
			esummary = (EditText)findViewById(R.id.todo_edit_summary);
			esummary.setText(title);
			edescription = (EditText)findViewById(R.id.todo_edit_description);
			edescription.setText(description);
			
		} catch (SQLException e) {
			
	
			
			Log.e("EditTodoActivity", "SQLException "+e.getMessage());
		}
		
		
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	dbHelper.close();
    }

	@Override
	public void onClick(View v) {
		if (v.equals(confirmButton)) {

			String title = esummary.getText().toString();
			String description = edescription.getText().toString();
			
			try {
				dbHelper.execSQL("UPDATE "+GlobalVariables.DB_TABLE+
						" SET title='"+title+"' ,description='"+description+
	            		"' WHERE _id="+id);
			} catch (SQLException e) {
				Log.e("EditTodoActivity", "SQLException "+e.getMessage());
			}
			
			finish();
		} else if (v.equals(cancelButton)) {
			finish();
		}
		
	}
}
