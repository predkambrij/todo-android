package si.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TodoDbAdapter {
	// Database fields
	private Context context;
	private SQLiteDatabase db;
	private TodoDatabaseHelper dbHelper;

	public TodoDbAdapter(Context context) {
		this.context = context;
	}

	public TodoDbAdapter open() throws SQLException {
		dbHelper = new TodoDatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}



	
/** * Return a Cursor positioned at the defined todo */

	public Cursor doQuery(boolean distinct, String[] columns, 
			String selection, String[] selectionArgs, String groupBy, 
			String having, String orderBy) throws SQLException {

		Cursor mCursor = db.query(distinct, GlobalVariables.DB_TABLE, columns, 
				selection , selectionArgs, groupBy, having, orderBy, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor doMQuery(boolean distinct, String[] columns, 
			String selection, String[] selectionArgs, String groupBy, 
			String having, String orderBy) throws SQLException {

		Cursor mCursor = db.query(distinct, GlobalVariables.DB_MTABLE, columns, 
				selection , selectionArgs, groupBy, having, orderBy, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public void execSQL(String sql) {
		db.execSQL(sql);
	}
	
	
	
	public void dropDB() {
		dbHelper.dropDB(db);
	}
	public void createDB() {
		dbHelper.createDB(db);
	}
	public void dropMDB() {
		dbHelper.dropMDB(db);
	}
	public void createMDB() {
		dbHelper.createMDB(db);
	}

}
