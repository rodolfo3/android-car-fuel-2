package academic.CarFuel;

import android.app.Activity;
import android.os.Bundle;

// Log
import android.util.Log;

// widgets
import android.view.View;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import android.widget.EditText;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Button;

// data wrapper
import android.content.ContentValues;

// toast
import android.widget.Toast;

// menu
import android.view.Menu;
import android.view.MenuItem;


public class CarFuelAdd extends Activity
{
    DBAdapter db; // database wrapper

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        installEvents();
        initDatabase();
        db.open();

        // http://www.balistupa.com/blog/2009/08/passing-data-or-parameter-to-another-activity-android/
        Long id = getId();
        if (id != -1) {
            load(id);
        }
    }

    private Long getId()
    {
        Bundle bundle = this.getIntent().getExtras();
        return bundle.getLong("id");
    }

    public void onDestroy()
    {
        db.close();
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem item;
        super.onCreateOptionsMenu(menu);

        // add(groupid, itemid, order e title)
        // add(title)
        item = menu.add("Done");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            public boolean onMenuItemClick(MenuItem item)
            {
                done();
                return true;
            }
        });
        // item.setIcon(R.drawable.<?>);

        if (getId() != -1) {
            // add(groupid, itemid, order e title)
            // add(title)
            item = menu.add("Delete");
            item.setOnMenuItemClickListener(
                new MenuItem.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        delete();
                        return true;
                    }
                }
            );
            // item.setIcon(R.drawable.<?>);
        }

        // add(groupid, itemid, order e title)
        // add(title)
        item = menu.add("Cancel");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            public boolean onMenuItemClick(MenuItem item)
            {
                cancel();
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

    private void load(Long id)
    {
        ContentValues content = db.get(id);
        String aux;

        // date
        aux = (String) content.get("date");
        DatePicker dp = (DatePicker) findViewById(R.id.date);

        int year = Integer.parseInt(aux.substring(0, 4));
        // datepicke month starts with 0
        int month = Integer.parseInt(aux.substring(5, 7)) - 1;
        int day = Integer.parseInt(aux.substring(8, 10));
        dp.updateDate(year, month, day);

        // km
        EditText auxText = (EditText) findViewById(R.id.km);
        aux = (String) content.get("odometer");
        auxText.setText(aux);

        // liters
        auxText = (EditText) findViewById(R.id.liters);
        aux = (String) content.get("liters");
        auxText.setText(aux);
        Log.d("TAG", "liters = " + aux);

        // type
        aux = (String) content.get("fuel");
        RadioGroup aux2 = (RadioGroup) findViewById(R.id.type);
        for(int i=0; i<aux2.getChildCount(); i++)
        {
            RadioButton aux3 = (RadioButton) aux2.getChildAt(i);
            Log.d("TAG", ">>" + aux3.getText().toString() + "==" + aux);
            if (aux3.getText().toString().equals(aux)) {
                Log.d("TAG", ">> Y << " + Integer.toString(i));
                aux2.check(aux3.getId());
                break;
            }
        }
    }

    private ContentValues getContent()
    {
        ContentValues content = new ContentValues();
        EditText aux;

        Long id = getId();
        if (id != -1) {
            content.put("_id", id);
        }

        // date
        DatePicker dp = (DatePicker) findViewById(R.id.date);
        GregorianCalendar date = new GregorianCalendar(dp.getYear(),
            dp.getMonth(), dp.getDayOfMonth());
        SimpleDateFormat sdf =
                  new SimpleDateFormat("yyyy-MM-dd");
        content.put("date", sdf.format(date.getTime()));

        // km
        aux = (EditText) findViewById(R.id.km);
        content.put("odometer", Float.parseFloat(aux.getText().toString()));

        // liters
        aux = (EditText) findViewById(R.id.liters);
        content.put("liters", Float.parseFloat(aux.getText().toString()));

        // type
        RadioGroup aux2 = (RadioGroup) findViewById(R.id.type);
        RadioButton aux3 = (RadioButton) findViewById(aux2.getCheckedRadioButtonId());
        content.put("fuel", aux3.getText().toString());

        return content;
    }

    public void done()
    {
        ContentValues content = getContent();
        Log.d("TAG", "save!!!");
        db.save(content);
        Log.d("TAG", "saved :)");

        setResult(Activity.RESULT_OK);
        finish();
    }

    public void cancel()
    {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void delete()
    {
        Long id = getId();
        db.delete(id);
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void installEvents() {
        final Button btn = (Button) findViewById(R.id.done_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                done();
            }
        });
    }

}
