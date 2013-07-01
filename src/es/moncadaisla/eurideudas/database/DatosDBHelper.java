package es.moncadaisla.eurideudas.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatosDBHelper extends SQLiteOpenHelper {

	  public static final String TABLE_DATOS = "datos";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "name";
	  public static final String COLUMN_SURNAME = "surname";
	  public static final String COLUMN_DEUDA = "deuda";
	  public static final String COLUMN_FOTO = "foto";

	  private static final String DATABASE_NAME = "datos.db";
	  private static final int DATABASE_VERSION = 2;

	  
	  private static final String DATABASE_CREATE = "CREATE TABLE "+TABLE_DATOS+"  (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
	  		"name VARCHAR not null," +
	  		"surname VARCHAR not null," +
	  		"deuda VARCHAR not null)";
	  public DatosDBHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(DatosDBHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATOS);
	    onCreate(db);
	  }
}
