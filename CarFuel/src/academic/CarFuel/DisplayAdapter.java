package academic.CarFuel;

// http://developer.android.com/reference/android/widget/CursorAdapter.html
// https://thinkandroid.wordpress.com/2010/01/09/simplecursoradapters-and-listviews/
import android.widget.SimpleCursorAdapter;
import android.content.Context;
import android.database.Cursor;

class DisplayAdapter extends SimpleCursorAdapter
{
    public DisplayAdapter(Context context, Cursor cursor)
    {
        super(context, 
              R.layout.main_item,
              cursor,
              new String[] { "date", "odometer", "liters", "fuel" },
              new int[] { R.id.date, R.id.odometer, R.id.liters, R.id.fuel }
        );
    }
}
