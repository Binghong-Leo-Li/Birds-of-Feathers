package edu.ucsd.cse110wi22.team6.bof;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class AddNewCourseActivity extends AppCompatActivity {
    private static final int NUM_YEARS_BACK = 6;
    private Spinner yearDropDown;
    private Spinner quarterDropDown;
    private TextView subjectEntryView;
    private TextView courseNumberEntryView;
    // TODO: add a cancel button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add New Course");
        setContentView(R.layout.activity_add_new_course);

        Button button = findViewById(R.id.add_new_course_enter_button);
        button.setOnClickListener(this::onEnter);

//        String[] yearsChoices = new String[]{"2022", "2021", "2020", "2019"};
        String[] yearsChoices = new String[NUM_YEARS_BACK];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < NUM_YEARS_BACK; i++) {
            yearsChoices[i] = String.valueOf(currentYear - i);
        }

        yearDropDown = findViewById(R.id.year_dropdown);
        ArrayAdapter<String> yearAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearsChoices);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearDropDown.setAdapter(yearAdapter);

        quarterDropDown = findViewById(R.id.quarter_dropdown);
        ArrayAdapter<CharSequence> quarterAdapter = ArrayAdapter
                .createFromResource(this,
                        R.array.quarter_list,
                        android.R.layout.simple_spinner_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterDropDown.setAdapter(quarterAdapter);

        subjectEntryView = findViewById(R.id.subject_entry_view);
        courseNumberEntryView = findViewById(R.id.course_number_entry_view);

        String prefill = getIntent().getStringExtra("preFillCourse");
        if (!prefill.isEmpty()) {
            Course course = Utilities.parseCourse(prefill);
            yearDropDown.setSelection(yearAdapter.getPosition(Integer.toString(course.getYear())));
            quarterDropDown.setSelection(quarterAdapter.getPosition(course.getQuarter()));
            subjectEntryView.setText(course.getSubject());
            courseNumberEntryView.setText(course.getCourseNumber());
        }
    }

    private void onEnter(View view) {
        String year = (String) yearDropDown.getSelectedItem();
        String quarter = (String) quarterDropDown.getSelectedItem();
        String subject = subjectEntryView.getText().toString();
        String number = courseNumberEntryView.getText().toString();

        if (!subject.matches("[A-Z]+")) {
            Utilities.showAlert(this, "Subject must be non blank and in ALL CAPS.");
            return;
        }

        if (!number.matches("[0-9A-Z]+")) {
            Utilities.showAlert(this,
                    "Course number must be non blank and consist of numbers and " +
                            "uppercase letters only.");
            return;
        }

        // No input validation for year needed since the drop box only has integer selections
        Course course = new Course(Integer.parseInt(year), quarter, subject, number);

        Intent intent = new Intent();
        intent.putExtra("course", course.toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}