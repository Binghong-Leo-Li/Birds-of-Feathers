package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestEnterCourse {
    @Before
    public void setupMockYear() {
        AddNewCourseActivity.setMockCurrentYear(2020);
    }

    @Test
    public void testAddNewCourseActivitySuccessful() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AddNewCourseActivity.class);
        intent.putExtra("preFillCourse", "");

        ActivityScenario<AddNewCourseActivity> scenario = ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            Spinner yearDropDown = activity.findViewById(R.id.year_dropdown);
            Spinner quarterDropDown = activity.findViewById(R.id.quarter_dropdown);
            TextView subjectEntryView = activity.findViewById(R.id.subject_entry_view);
            TextView courseNumberEntryView = activity.findViewById(R.id.course_number_entry_view);
            Button enterButton = activity.findViewById(R.id.add_new_course_enter_button);

            yearDropDown.setSelection(0);
            quarterDropDown.setSelection(1); // SP in R.array.quarter_list
            subjectEntryView.setText("MATH");
            courseNumberEntryView.setText("31CH");

            enterButton.performClick();
        });
        Instrumentation.ActivityResult result = scenario.getResult();
        assertEquals(Activity.RESULT_OK, result.getResultCode());
        assertEquals("2020 SP MATH 31CH", result.getResultData().getStringExtra("course"));
    }

    @Test
    public void testAddNewCourseActivityPrefilled() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AddNewCourseActivity.class);
        intent.putExtra("preFillCourse", "2019 WI CSE 15L");

        ActivityScenario<AddNewCourseActivity> scenario = ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            Spinner quarterDropDown = activity.findViewById(R.id.quarter_dropdown);
            Button enterButton = activity.findViewById(R.id.add_new_course_enter_button);

            quarterDropDown.setSelection(2); // SS1 in R.array.quarter_list

            enterButton.performClick();
        });
        Instrumentation.ActivityResult result = scenario.getResult();
        assertEquals(Activity.RESULT_OK, result.getResultCode());
        assertEquals("2019 SS1 CSE 15L", result.getResultData().getStringExtra("course"));
    }
}
