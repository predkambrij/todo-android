package si.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "todoApplication";
	private static final String DATABASE_DROP = "DROP TABLE IF EXISTS task";
	private static final String DATABASE_MANAGEMENT_DROP = "DROP TABLE IF EXISTS taskman";
	private static final String DATABASE_CREATE =  
			"create table task"+
			"("+
			   "_id					 INTEGER PRIMARY KEY AUTOINCREMENT,"+
			   "title                varchar(300),"+
			   "description          varchar(8000),"+
			   "estimated_time       int,"+
			   "priority             int,"+
			   "categoryname         varchar(150),"+
			   "start_date           datetime,"+
			   "due_date             datetime,"+
			   "repeat_time          int,"+
			   "repeat_ends          date,"+
			   "acknowledge          int,"+
			   "reminder_email       varchar(300),"+
			   "reminder_sms         varchar(300)"+
			")"
;
	private static final String DATABASE_MANAGEMENT_CREATE =  
			"create table taskman"+
			"("+
			   "_id					 int,"+
			   "username             varchar(300),"+
			   "password             varchar(300)"+
			")"
;
	
	private static final int DATABASE_VERSION = 1;

	public TodoDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE); // TODO change it to database.insert database.update
		database.execSQL(DATABASE_MANAGEMENT_CREATE);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL(DATABASE_DROP);
		database.execSQL(DATABASE_MANAGEMENT_DROP);
		database.execSQL(DATABASE_CREATE);
		database.execSQL(DATABASE_MANAGEMENT_CREATE);
	}
	
	public void dropDB(SQLiteDatabase database) {
		database.execSQL(DATABASE_DROP);
	}
	public void createDB(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
	
	public void dropMDB(SQLiteDatabase database) {
		database.execSQL(DATABASE_MANAGEMENT_DROP);
	}
	public void createMDB(SQLiteDatabase database) {
		database.execSQL(DATABASE_MANAGEMENT_CREATE);
	}
	
}
