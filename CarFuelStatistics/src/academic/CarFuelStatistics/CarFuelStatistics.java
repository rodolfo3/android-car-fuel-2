package academic.CarFuelStatistics;

import android.app.Activity;
import android.os.Bundle;

// widgets
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.DatePicker;
import java.util.GregorianCalendar; // used with datepicker
import java.text.SimpleDateFormat; // used with datepicker


// URI
import android.database.Cursor;
import android.net.Uri;

public class CarFuelStatistics extends Activity
{
    private Uri fuelURI = Uri.parse("content://CarFuel");

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        installEvents();
    }

    public String widgetToDate(DatePicker dp)
    {
        GregorianCalendar date = new GregorianCalendar(dp.getYear(),
            dp.getMonth(), dp.getDayOfMonth());
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date.getTime());
    }

    private Cursor queryStatistics(Uri uri) {
        DatePicker dpi = (DatePicker) findViewById(R.id.date_initial);
        DatePicker dpf = (DatePicker) findViewById(R.id.date_final);

        Cursor cursor = getContentResolver().query(uri,
            // columns
            null,
            // selection
            "date >= ? and date <= ?",
            // selection args
            new String[] { widgetToDate(dpi), widgetToDate(dpf) },
            null);

        if (cursor.moveToFirst()) return cursor;
        return null;
    }

    private void showStatistics()
    {
        String statistics = "";
        Cursor cursor;

        cursor = queryStatistics(Uri.withAppendedPath(fuelURI, "total"));
        if (cursor == null) {
            Toast.makeText(getApplicationContext(),
                "IS NULL (total)", 10).show();
        } else {
            Long total = cursor.getLong(0);
            statistics += "Total: " + total + " times into this days\n";
        }
        cursor.close();

        cursor = queryStatistics(Uri.withAppendedPath(fuelURI, "sum_by_type"));
        if (cursor == null) {
            Toast.makeText(getApplicationContext(),
                "IS NULL (sum_by_type)", 10).show();
        } else {
            do
            {
                Long odometer = cursor.getLong(0);
                Long liters = cursor.getLong(1);
                String fuel = cursor.getString(2);

                statistics += "# By fuel type #"
                    + "\n> " + fuel + ": "
                    + (odometer.floatValue()/liters.floatValue()) + "km/L ("
                    + odometer + "km/" + liters + "L)\n";
            } while (cursor.moveToNext());
        }
        cursor.close();

        cursor = queryStatistics(Uri.withAppendedPath(fuelURI, "sum"));
        if (cursor == null) {
            Toast.makeText(getApplicationContext(),
                "IS NULL", 10).show();
        } else {
            Long odometer = cursor.getLong(0);
            Long liters = cursor.getLong(1);

            statistics += "# TOTAL #" + "\n"
                + (odometer.floatValue()/liters.floatValue()) + "km/L ("
                + odometer + "km/" + liters + "L)\n";
        }
        cursor.close();

        TextView text = (TextView) findViewById(R.id.text_statistics);
        text.setText(statistics);
    }

    private void installEvents()
    {
        final Button btn = (Button) findViewById(R.id.go_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showStatistics();
            }
        });

    }
}
