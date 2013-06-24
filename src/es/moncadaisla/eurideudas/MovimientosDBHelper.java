package es.moncadaisla.eurideudas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MovimientosDBHelper extends SQLiteOpenHelper {

	  public static final String TABLE_MOVIMIENTOS = "movimientos";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_PRECIO = "precio";
	  public static final String COLUMN_ITEM = "item";
	  public static final String COLUMN_TIMESTAMP = "timestamp";

	  private static final String DATABASE_NAME = "movimientos.db";
	  private static final int DATABASE_VERSION = 1;

	  
	  private static final String DATABASE_CREATE = "CREATE TABLE "+TABLE_MOVIMIENTOS+"  (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
	  		COLUMN_PRECIO+" VARCHAR not null," +
	  		COLUMN_ITEM+" VARCHAR not null," +
	  		COLUMN_TIMESTAMP+" VARCHAR not null)";
	  public MovimientosDBHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(MovimientosDBHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIMIENTOS);
	    onCreate(db);
	  }
}
