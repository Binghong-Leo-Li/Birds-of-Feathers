package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;

// The first activity started when the app is launched
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private Spinner currentQuarterDropDown;

    private AppStorage storage;
    private Button timebutton;
    private Button datebutton;
    private int year;
    // defaults in case only time or only date is mocked (shouldn't do that)
    private String dateString = "1/1/2022", timeString = "00:00";
    private DatePickerDialog datePickerDialog;

    private static final DateFormat DATE_PARSER =
            new SimpleDateFormat("M/d/yyyy HH:mm", Locale.US);

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "HomeActivity.onStart() called");
        if (storage.isInitialized()) {
            Log.d(TAG, "App has gone through first time setup already");
            NearbyMessagesManager.getInstance(this).update();
        } else {
            // First time setup
            Log.d(TAG, "First time setup detected");
            Intent intent = new Intent(this, NameEntryActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Home");
        setContentView(R.layout.activity_home);

        // Default year to current year
        year = Calendar.getInstance().get(Calendar.YEAR);

        storage = Utilities.getStorageInstance(this);

        currentQuarterDropDown=findViewById(R.id.currentQuarter_dropdown);
        ArrayAdapter<CharSequence> quarterAdapter=ArrayAdapter.createFromResource(this, R.array.currentQuarter, android.R.layout.simple_spinner_item);
        currentQuarterDropDown.setAdapter(quarterAdapter);

        Button startButton = findViewById(R.id.buttonStart3);
        startButton.setOnClickListener(this::onButtonStart);

        Button coursesButton = findViewById(R.id.buttonCourses);
        coursesButton.setOnClickListener(this::onButtonCourses);

        Button favoritesButton = findViewById(R.id.buttonFavorites);
        favoritesButton.setOnClickListener(this::onButtonFavorites);

        timebutton = findViewById(R.id.time_mock_btn);
        datebutton = findViewById(R.id.date_mock_btn);
    }

    private void onButtonStart(View view) {
        // go to main
        Log.d(TAG, "start button called");
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int selectedQuarterIdx = currentQuarterDropDown.getSelectedItemPosition();
        String[] quarterCodes = getResources().getStringArray(R.array. quarter_list);
        String selectedQuarter = quarterCodes[selectedQuarterIdx];
        intent.putExtra("quarter", selectedQuarter);
        intent.putExtra("year", year);

        startActivity(intent);
    }

    private void onButtonCourses(View view) {
        // go to courses
        Log.d(TAG, "courses button called");
        Intent intent = new Intent(this, CourseEntryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void onButtonFavorites(View view) {
        // go to favorites
        Intent intent = new Intent(this, FavoriteListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void updateMock() {
        try {
            Date mockedTime = DATE_PARSER.parse(dateString + " " + timeString);
            SessionManager.getInstance(this).setMockedTime(mockedTime);
            Toast.makeText(this,
                    "Successfully set mocked time and date to " + mockedTime,
                    Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Log.e(TAG, "Date parse error", e);
        }
    }

    public void initiateTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                int hour, minute;
                hour = selectedHour;
                minute = selectedMinute;
                timeString = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                updateMock();
                timebutton.setText(timeString);
            }
        };

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Pick Time");
        timePickerDialog.show();
    }

    public void initiateDatePicker(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay)
            {
                int day, month;
                year = selectedYear;
                month = selectedMonth + 1;
                day = selectedDay;
                dateString = month + "/" + day + "/" + year;
                updateMock();
                datebutton.setText(dateString);
            }
        };

        Calendar cal = Calendar.getInstance();
        int thisYear = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this,dateSetListener, thisYear, month, day);
        datePickerDialog.show();
    }
}