package edu.ucsd.cse110wi22.team6.bof;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.CourseSize;

// Activity handling the add course UI
public class AddNewCourseActivity extends AppCompatActivity {
    private static final int NUM_YEARS_BACK = 6; // we choose to provide options for at most 6 years back
    private Spinner yearDropDown;
    private Spinner quarterDropDown;
    private Spinner sizeDropDown;

    private TextView subjectEntryView;
    private TextView courseNumberEntryView;

    private AppStorage appStorage;

    private static int mockCurrentYear;

    // setter
    public static void setMockCurrentYear(int mockCurrentYear) {
        AddNewCourseActivity.mockCurrentYear = mockCurrentYear;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add New Course");
        setContentView(R.layout.activity_add_new_course);

        Button button = findViewById(R.id.add_new_course_enter_button);
        button.setOnClickListener(this::onEnter); // linking to button

        String[] yearsChoices = new String[NUM_YEARS_BACK];
        int currentYear = mockCurrentYear == 0 ? Calendar.getInstance().get(Calendar.YEAR) : mockCurrentYear;
                                                    // get current year with mocking

        // Creating all the options that we can use in a dropdown
        for (int i = 0; i < NUM_YEARS_BACK; i++) {
            yearsChoices[i] = String.valueOf(currentYear - i);
        }

        sizeDropDown=findViewById(R.id.dropdownClassSize);
        ArrayAdapter<CharSequence>sizeAdapter=ArrayAdapter.createFromResource(this, R.array.classSizes, android.R.layout.simple_spinner_item);

        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sizeDropDown.setAdapter(sizeAdapter);

        yearDropDown = findViewById(R.id.year_dropdown);

        // setting the dropdown with the choices
        ArrayAdapter<String> yearAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearsChoices);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearDropDown.setAdapter(yearAdapter);

        // setting the quarter dropdown
        quarterDropDown = findViewById(R.id.quarter_dropdown);
        ArrayAdapter<CharSequence> quarterAdapter = ArrayAdapter
                .createFromResource(this,
                        R.array.quarter_list,
                        android.R.layout.simple_spinner_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterDropDown.setAdapter(quarterAdapter);

        subjectEntryView = findViewById(R.id.subject_entry_view);
        courseNumberEntryView = findViewById(R.id.course_number_entry_view);

        // handling subject and course number entry
        // making sure the edittext is prefilled if there are previously-entered course
        String prefill = getIntent().getStringExtra("preFillCourse");
        if (!prefill.isEmpty()) { // if has prefill, then update default stage of the dropdowns and edittexts
            Course course = Utilities.parseCourse(prefill);
            yearDropDown.setSelection(yearAdapter.getPosition(Integer.toString(course.getYear())));
            quarterDropDown.setSelection(quarterAdapter.getPosition(course.getQuarter()));
            subjectEntryView.setText(course.getSubject());
            courseNumberEntryView.setText(course.getCourseNumber());
        }

        appStorage = Utilities.getStorageInstance(this);
    }

    // handling enter button being pressed
    private void onEnter(View view) {
        String year = (String) yearDropDown.getSelectedItem();
        String quarter = (String) quarterDropDown.getSelectedItem();
        String subject = subjectEntryView.getText().toString();
        String number = courseNumberEntryView.getText().toString();
        int sizeItemIndex = sizeDropDown.getSelectedItemPosition();

        if (!subject.matches("[A-Z]+")) { // checking if subject input is invalid
            Utilities.showAlert(this, "Subject must be non blank and in ALL CAPS.");
            return;
        }

        if (!number.matches("[0-9A-Z]+")) { // checking if course number input is invalid
            Utilities.showAlert(this,
                    "Course number must be non blank and consist of numbers and " +
                            "uppercase letters only.");
            return;
        }

        // No input validation for year needed since the drop box only has integer selections
        Course course = new Course(Integer.parseInt(year), quarter, subject, number);
        appStorage.registerCourse(course, CourseSize.values()[sizeItemIndex]);

        // course entering is successful
        Intent intent = new Intent();
        intent.putExtra("course", course.toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}