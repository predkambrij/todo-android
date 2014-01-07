package si.todo;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Menu1Activity extends ListActivity {
	
	private TodoDbAdapter dbHelper;
	

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		dbHelper = new TodoDbAdapter(this);
		dbHelper.open();
		
		String[] values = new String[] {
				"Update from server",
				"Push to server",
				"Continue", 
//				"Exit"
				};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
		
		registerForContextMenu(getListView());
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	dbHelper.close();
    }
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//String item = (String) getListAdapter().getItem(position);
		//Toast.makeText(this, item + " selected "+position, Toast.LENGTH_LONG).show();
		Intent i;
		switch (position) {
		
		// Update from server
		case 0:
			i = new Intent(this, TodoUpdateFromServerActivity.class);
			startActivityForResult(i, 1);
			break;
//			// Push to server
		case 1:
			i = new Intent(this, PushToServerActivity.class);
			startActivityForResult(i, 1);
			break;
			
			// Continue
		case 2:
			startActivityForResult(new Intent(this, ShowTodosActivity.class), 1);
			//startActivityForResult(new Intent(this, Menu2Activity.class), 1);
			break;

//		case 1:
//			break;
		
		
		
		default:
			break;
		}
	}
	

	
	/// za nafilat z baze
	
	/*
		cursor = null;//dbHelper.fetchAllTodos();
		startManagingCursor(cursor);

		String[] from = new String[] { "title" };
		int[] to = new int[] { R.id.label };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
										R.layout.todo_row, cursor, from, to);
		setListAdapter(notes);
	 */
	
	
	
}
