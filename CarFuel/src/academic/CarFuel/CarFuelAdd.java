package academic.CarFuel;

import android.app.Activity;
import android.os.Bundle;

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
    }

    public void onDestroy()
    {
        db.close();
        super.onDestroy();
    }

    private void initDatabase()
    {
        db = new DBAdapter(this);
    }

    public ContentValues getContent()
    {
        ContentValues content = new ContentValues();
        EditText aux;

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

    public void done(View view)
    {
        ContentValues content = getContent();
        db.save(content);

        setResult(Activity.RESULT_OK);
        finish();
    }

    public void cancel(View view)
    {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void installEvents() {
        final Button btn = (Button) findViewById(R.id.done_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                done(v);
            }
        });
    }

}
