package si.todo;

import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddTodoActivity extends Activity implements OnClickListener {

	private TodoDbAdapter dbHelper;
	private String id=null;
	Button saveButton = null;
	Button cancelButton = null;
	EditText esummary = null;
	EditText edescription = null;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_edit);

        saveButton = (Button)findViewById(R.id.todo_edit_button);
        saveButton.setOnClickListener(this);
        
        cancelButton = (Button)findViewById(R.id.todo_edit_buttonCancel);
        cancelButton.setOnClickListener(this);
        
        esummary = (EditText)findViewById(R.id.todo_edit_summary);
		edescription = (EditText)findViewById(R.id.todo_edit_description);
		

        dbHelper = new TodoDbAdapter(this);
		dbHelper.open();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	dbHelper.close();
    }

	@Override
	public void onClick(View v) {
		
		if (v.equals(saveButton)) {
			String title = esummary.getText().toString();
			
			String description = edescription.getText().toString();
			
			try {
				dbHelper.execSQL("INSERT INTO "+GlobalVariables.DB_TABLE+
						" (title, description,priority,categoryname) VALUES ('"+title+"', '"+description+
	            		"',0,'')");
			} catch (SQLException e) {
				Log.e("EditTodoActivity", "SQLException "+e.getMessage());
			}
			finish();
		} else if (v.equals(cancelButton)) {
			finish();
		}
		
	}
	
}
