package academic.CarFuel;

// Log and tools
import android.util.Log;
// context
import android.content.Context;

// Database
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.database.Cursor;

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
                    "_id integer primary key autoincrement",
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

    public void save(ContentValues content)
    {
        Long id = content.getAsLong("_id");
        Log.d("DBAdapter", ">> SAVE: ");
        if (id == null) {
            db.insert(TABLE_NAME, null, content);
            id = new Long(1); // FIXME get the last id
        } else {
        Log.d("DBAdapter", ">> UPDATE >> " + id);
            db.update(TABLE_NAME, content, "_id = " + id, null);
        }
        Log.d("DBAdapter", ">>" + id);
        Log.d("DBAdapter", "SAVE <<");
        // return id; // FIXME return ID
    }

    public int delete(Long id)
    {
        return db.delete(TABLE_NAME, String.format("_id = " + id), null);
    }

    public Cursor all()
    {
        Cursor cursor = db.query(TABLE_NAME,
            new String[] {"_id", "date", "odometer", "liters", "fuel"},
            null,
            null,
            null,
            null,
            "date ASC"
        );
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }

    public ContentValues get(Long id)
    {
        Cursor cursor = db.query(TABLE_NAME,
            new String[] {"_id", "date", "odometer", "liters", "fuel"},
            "_id = " + id,
            null,
            null,
            null,
            null
            );
        if (cursor.moveToFirst()) {
            ContentValues content = new ContentValues();
            content.put("_id", cursor.getString(0));
            content.put("date", cursor.getString(1));
            content.put("odometer", cursor.getString(2));
            content.put("liters", cursor.getString(3));
            content.put("fuel", cursor.getString(4));
            return content;
        }
        return null;
    }

}

