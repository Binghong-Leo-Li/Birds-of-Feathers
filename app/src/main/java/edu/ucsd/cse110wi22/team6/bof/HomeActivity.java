package edu.ucsd.cse110wi22.team6.bof;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import edu.ucsd.cse110wi22.team6.bof.model.Session;

// The first activity started when the app is launched
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private Spinner currentQuarterDropDown;

    protected RecyclerView bofRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected BoFsViewAdapter personsViewAdapter;
    private IPerson user;
    private IUserInfoStorage storage;
    private MessageListener messageListener;
    private Button timebutton;
    private Button datebutton;
    private int hour, minute, day, month, year;
    private DatePickerDialog datePickerDialog;



    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "HomeActivity.onStart() called");
        if (storage.isInitialized()) {
            Log.d(TAG, "App has gone through first time setup already");
            //user = new Person(storage.getName(),
                    //storage.getCourseList(),
                    //storage.getPhotoUrl());

            //personsViewAdapter.setUser(user);
            //updateUI();
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

    public void initiateTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timebutton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

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
                year = selectedYear;
                month = selectedMonth + 1;
                day = selectedDay;
                String date = month + "/" + day + "/" + year;
                datebutton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this,dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    public void onBoFClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        String selectedQuarter = currentQuarterDropDown.getSelectedItem().toString();
        intent.putExtra("quarter", selectedQuarter);
        intent.putExtra("year", year);

        startActivity(intent);
    }
}