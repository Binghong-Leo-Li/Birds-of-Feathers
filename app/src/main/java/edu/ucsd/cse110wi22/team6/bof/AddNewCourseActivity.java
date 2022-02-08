package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class AddNewCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        Button button = findViewById(R.id.add_new_course_enter_button);
        button.setOnClickListener(this::onEnter);

        Spinner yearDropDown = findViewById(R.id.year_dropdown);
        String[] years = new String[]{"2022", "2021", "2020", "2019"}; // TODO: based it on current year and n years back
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearDropDown.setAdapter(adapter);

        Spinner quarterDropDown = findViewById(R.id.quarter_dropdown);
        ArrayAdapter<CharSequence> adapterQuarter = ArrayAdapter
                .createFromResource(this,
                        R.array.quarter_list,
                        android.R.layout.simple_spinner_item);
        adapterQuarter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterDropDown.setAdapter(adapterQuarter);
    }

    private void onEnter(View view) {
        finish();
    }
}