package si.todo;

import android.app.PendingIntent;

public class GlobalVariables {
	public static String username = "";
	public static String password = "";
//	public static String domain = "192.168.2.120";
	public static String domain = "todo.blatnik.org";
//	public static String domain = "86.61.74.171";
//	public static String domain = "89.89.89.89";
//	public static String port = "9080";
	public static String port = "80";
	
	//public static String pathPrefix = "/tpo/web";
	public static String pathPrefix = "";
	
	public static final String DB_TABLE = "task";
	public static final String DB_MTABLE = "taskman";
	
	public static PendingIntent[] intentsInRepeating = new PendingIntent[5];

}
