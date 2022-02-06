package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CourseEntryActivity extends AppCompatActivity {
    // TODO: Implement the UI for entering classes
    // Remember to validate input (year and course numbers must be positive integers)
    // quarter codes must be valid in UCSD (perhaps a drop down?)
    // Subject codes are only uppercase alphabets (convert all lowercase to uppercase?)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Enter past courses");
        setContentView(R.layout.activity_course_entry);

        Button doneButton = findViewById(R.id.course_entry_done_button);
        doneButton.setOnClickListener(this::onDone);
    }

    private void onDone(View view) {
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCE_STRING, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // TODO: Currently just use a hardcoded string for list of classes to exemplify
        // the format of the data
        editor.putString("courses",
                "2022 WI CSE 110,2021 FA CSE 100,2021 FA ECE 65,2020 FA CSE 11");
        editor.putBoolean("initialized", true);

        editor.apply();

        // Return to home
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}