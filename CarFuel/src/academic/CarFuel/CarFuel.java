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
import android.widget.ListView;
import android.content.Intent;
import android.widget.AdapterView;

// menu
import android.view.Menu;
import android.view.MenuItem;

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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        // groupid, itemid, order e title
        MenuItem item = menu.add(0, 0, 0, "Add");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            public boolean onMenuItemClick(MenuItem item)
            {
                add();
                return true;
            }
        });
        // item.setIcon(R.drawable.<?>);
        return true;
    }

    private void initDatabase()
    {
        db = new DBAdapter(this);
    }

    public void add()
    {
        this.edit(new Long(-1));
    }

    public void edit(Long id)
    {
        Intent intent = new Intent(getApplicationContext(), CarFuelAdd.class);
        intent.putExtra("id", id);
        startActivityForResult(intent, ADD_RESULT);
    }

    private void reloadData()
    {
        Cursor cursor = db.all();
        if (cursor != null) {
            DisplayAdapter adapter = new DisplayAdapter(
                getApplicationContext(),
                cursor
            );
            ListView view = (ListView) findViewById(R.id.list);
            view.setAdapter(adapter);
            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(
                        AdapterView parent,
                        View v,
                        int position,
                        long id) {
                    edit(id);
                }
            });
        } else {
            Toast.makeText(
                getApplicationContext(),
                ":'(",
                Toast.LENGTH_LONG).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            Toast.makeText(getApplicationContext(), "Done!", 10).show();
            reloadData();
        } else {
            // Toast.makeText(getApplicationContext(), "Canceled!", 10).show();
        }
    }

    private void installEvents()
    {
        // other functions, like "sort" into the title columns
    }
}
