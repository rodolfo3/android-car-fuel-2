package academic.CarFuel;

// Log and tools
import android.util.Log;
// context
import android.content.Context;

// Database
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;

// data wrapper
import android.content.ContentValues;

public class DBAdapter {

    // Database conf
    private static String TABLE_NAME = "gas";
    private static String DB_NAME = "CarFuel";
    private static int DB_VERSION = 1;

    // class to create database table and encapsulate SQL
    private static class DatabaseHelper extends SQLiteOpenHelper {

        // initial conf
        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        // create table
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(String.format("create table %s (%s, %s, %s, %s, %s);",
                    TABLE_NAME,
                    "id integer primary key autoincrement",
                    "date text",
                    "odometer real",
                    "liters real",
                    "fuel text"
                )
            );
        }

        // update table (is not needed now)
        public void onUpgrade(SQLiteDatabase db,
             int OldVersion, int NewVersion) {
            // do nothing (by now)
        }

    }

    // adapter
    private final Context context;
    private DatabaseHelper dbhelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        context = ctx;
        dbhelper = new DatabaseHelper(context);
    }

    public DBAdapter open() throws SQLException
    {
        // open database to read and write
        db = dbhelper.getWritableDatabase();
        return this;
    }

    public void close() throws SQLException
    {
        dbhelper.close();
    }

    public int save(ContentValues content)
    {
        Integer id = content.getAsInteger("id");
        Log.d("DBAdapter", ">> SAVE: ");
        if (id == null) {
            db.insert(TABLE_NAME, null, content);
            id = 1; // FIXME get the last id
        } else {
            db.update(TABLE_NAME, content, String.format("id = %i", id), null);
        }
        Log.d("DBAdapter", Integer.toString(id));
        Log.d("DBAdapter", "SAVE <<");
        return id;
    }

    public int delete(int id)
    {
        return db.delete(TABLE_NAME, String.format("id = %i", id), null);
    }

}

