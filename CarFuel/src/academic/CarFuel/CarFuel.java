package academic.CarFuel;

import android.app.Activity;
import android.os.Bundle;

// widgets
import android.view.View;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.content.Intent;

// data wrapper
import android.content.ContentValues;

// database
import android.database.Cursor;

public class CarFuel extends Activity
{
    DBAdapter db; // database wrapper
    private static int ADD_RESULT = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        installEvents();
        initDatabase();
        db.open(); // FIXME put into a singleton
        reloadData();
    }

    public void onDestroy()
    {
        super.onDestroy();
        db.close();
    }

    private void initDatabase()
    {
        db = new DBAdapter(this);
    }

    private void reloadData()
    {
        Cursor cursor = db.all();
        if (cursor != null) {
            // http://developer.android.com/reference/android/widget/CursorAdapter.html
            // TO SEE: https://thinkandroid.wordpress.com/2010/01/09/simplecursoradapters-and-listviews/
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.main_item,
                cursor,
                new String[] { "date", "odometer", "liters", "fuel" },
                new int[] { R.id.date, R.id.odometer, R.id.liters, R.id.fuel }
            );
            ListView gridview = (ListView) findViewById(R.id.list);
            gridview.setAdapter(adapter);
        } else {
            Toast.makeText(
                getApplicationContext(),
                ":'(",
                Toast.LENGTH_LONG).show();
        }
    }

    private void installEvents()
    {
        final Button btn = (Button) findViewById(R.id.add_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CarFuelAdd.class);
                startActivityForResult(intent, ADD_RESULT);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            Toast.makeText(getApplicationContext(), "Saved!", 10).show();
            reloadData();
        } else {
            Toast.makeText(getApplicationContext(), "Canceled!", 10).show();
        }
    }

}
