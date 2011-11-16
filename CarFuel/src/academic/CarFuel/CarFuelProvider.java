package academic.CarFuel;

// content provider
import android.content.ContentProvider;

// URI
import android.net.Uri;
import android.content.UriMatcher;

// database
import android.database.Cursor;

// data wrapper
import android.content.ContentValues;

public class CarFuelProvider extends ContentProvider
{
    private static final int ALL = 1;
    private static final int TOTAL = 2;
    private static final int SUM_BY_TYPE = 3;
    private static final int SUM = 4;

    private DBAdapter db; // database wrapper
    public static final String AUTHORITY = "CarFuel";
    private static final UriMatcher matcher =
        new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(AUTHORITY, null, ALL);
        matcher.addURI(AUTHORITY, "total", TOTAL);
        matcher.addURI(AUTHORITY, "sum_by_type", SUM_BY_TYPE);
        matcher.addURI(AUTHORITY, "sum", SUM);
    }

    public boolean onCreate()
    {
        db = new DBAdapter(getContext());
        db.open(); // FIXME put into a singleton
        return true;
    }

    public String getType(Uri uri)
    {
        switch (matcher.match(uri))
        {
            case ALL:
                return "academic.CarFuel.Fuel";
            case TOTAL:
                return "java.lang.Long";
            case SUM_BY_TYPE:
                return "academic.CarFuel.Fuel";
            case SUM:
                return "academic.CarFuel.Fuel";
        }
        return null;
    }

    public Cursor query(Uri uri, String[] projection, String selection,
        String[] selectionArgs, String sortOrder)
    {

        switch (matcher.match(uri))
        {
            case ALL:
                return db.filter(selection, selectionArgs, null);
            case TOTAL:
                return db.filter(new String[] { "count(1)" },
                    selection, selectionArgs, null);
            case SUM_BY_TYPE:
                return db.filter(
                    new String[] { "sum(odometer)", "sum(liters)", "fuel" },
                    selection, selectionArgs, null, "fuel");
            case SUM:
                return db.filter(
                    new String[] { "sum(odometer)", "sum(liters)" },
                    selection, selectionArgs, null);
        }

        return null;
    }

    public Uri insert(Uri uri, ContentValues values)
    {
        return null;
    }

    public int update(Uri uri, ContentValues values,
        String selection, String[] selectionArgs)
    {
        return 0;
    }

    public int delete(Uri uri,
        String selection, String[] selectionArgs)
    {
        return 0;
    }
}
