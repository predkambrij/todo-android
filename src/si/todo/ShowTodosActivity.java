package si.todo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ShowTodosActivity extends ListActivity implements OnClickListener {
	private TodoDbAdapter dbHelper;
	private Cursor cursor;
	ListView lv = null;
	
    
	public void onCreate(Bundle savedInstanceState) { 
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.todo_list);
		
		//contactAdapter.setNotifyOnChange(true);

		// just for long press

		
		lv = getListView();
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
		    	onListItemClick(lv, v, pos, id);
		    }
		});
		
		
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
		        final long abid = id;
				AlertDialog.Builder builder = new AlertDialog.Builder(ShowTodosActivity.this);
				builder.setMessage("Are you sure you want delete task #"+id+"?")
				       .setCancelable(false)
				       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   
				        	   dbHelper.execSQL("delete from "+GlobalVariables.DB_TABLE+
				        			   " where _id="+abid ); 
				        	   
				        	   updateList();
				           }
				       })
				       .setNegativeButton("No", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				           }
				       });
				AlertDialog alert = builder.create();
				alert.show();
    	
		    	return true;
		    }
		});
		// enf of just for long press :)
		
		
		//this.getListView().setDividerHeight(2);
		//dbHelper = new TodoDbAdapter(this);
		//dbHelper.open();
		
		//updateList();
		
    }


    private void updateList() {
    	cursor = dbHelper.doQuery(true, new String[]{"_id","title"}, null, null, null, null, null);
		startManagingCursor(cursor);

		String[] from = new String[] {"_id", "title" };
		int[] to = new int[] { R.id.label, R.id.label1 };
		
		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.todo_row, cursor, from, to);
		
		setListAdapter(notes);
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	dbHelper.close();
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	//Toast.makeText(this, "ajdi je "+id, Toast.LENGTH_LONG).show();
    	Intent intent = new Intent(this, EditTodoActivity.class);
		intent.putExtra("_id", id+"");
		startActivityForResult(intent, 3);
    }

    
    
	@Override
	public void onClick(View v) {
		
		
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.menuAddTodo:
	    	startActivityForResult(new Intent(this, AddTodoActivity.class), 2);
	    	updateList();

	        return true;
	    case R.id.menuUpdateFromServer:
	    	i = new Intent(this, TodoUpdateFromServerActivity.class);
			startActivityForResult(i, 2);
	    	updateList();

	        return true;
	    case R.id.menuPushToServer:
	    	i = new Intent(this, PushToServerActivity.class);
			startActivityForResult(i, 1);
	    	updateList();
	    	return true;	
	    default:
	        return false;
	    }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	 protected void onActivityResult(int requestCode, int resultCode,
             Intent data) {
         if (requestCode == 2) {
             if (resultCode == RESULT_OK) {
     	    	updateList();
             }
         }
     }

}
