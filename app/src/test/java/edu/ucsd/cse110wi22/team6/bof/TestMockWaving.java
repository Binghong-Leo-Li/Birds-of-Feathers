package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowLog;

import java.util.Objects;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.CourseSize;

// BDD scenario tests for User Story 13: Mock Waving
@RunWith(AndroidJUnit4.class)
public class TestMockWaving {

    private static final String BILL_URL = "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0";
    private static final String OLIVIA_URL = "https://avatars.githubusercontent.com/u/59716405?v=4";
    private static final String BILL_CSV_NO_WAVE = "a4ca50b6-941b-11ec-b909-0242ac120002,,,,\n" +
            "Bill,,,,\n" +
            BILL_URL + ",,,,\n" +
            "2021,FA,CSE,210,Small\n" +
            "2022,WI,CSE,110,Large";

    private static final String OLIVIA_CSV_NO_WAVE = "62e7694b-82d8-4c1a-9b08-aadb63fdb76c,,,,\n" +
            "Olivia,,,,\n" +
            OLIVIA_URL + ",,,,\n" +
            "2021,FA,ECE,65,Small\n" +
            "2022,WI,CSE,110,Large";

    private static final String BILL_CSV_WAVE = BILL_CSV_NO_WAVE + "\n" +
            "4b295157-ba31-4f9f-8401-5d85d9cf659a,wave,,,";

    // Helper method to test that the n-th row of bof list of some person with the correct number of common courses
    private void assertRow(RecyclerView view, int position, String expectedName, int expectNumCommonCourses, int expectedVisibility) {
        FrameLayout framelayout = (FrameLayout) view.getChildAt(position);

        TextView editText = framelayout.findViewById(R.id.bof_row_name);
        String info = editText.getText().toString();
        assertEquals(expectedName, info);

        TextView numCoursesText = framelayout.findViewById(R.id.num_common_courses_view);
        int numCommon = Integer.parseInt(numCoursesText.getText().toString());
        assertEquals(expectNumCommonCourses, numCommon);

        ImageView waved = framelayout.findViewById(R.id.waved);
        assertEquals(expectedVisibility, waved.getVisibility());
    }

    @Before
    public void setup() {
        ShadowLog.stream = System.out;
    }

    // Setting up the given clause for BDD scenario 1
    private void givenScenario1() {
        App.resetInstance(false);
        Context context = InstrumentationRegistry
                .getInstrumentation().getTargetContext();
        AppStorage storage = Utilities.getStorageInstance(context);
        SessionManager manager = SessionManager.getInstance(context);
        storage.setInitialized(true);
        storage.setName("Ava");
        storage.setPhotoUrl("https://www.example.com");
        storage.setCourseList(Utilities.parseCourseList("2022 WI CSE 110,2021 FA ECE 65"));
        storage.setUserUUID(UUID.fromString("4b295157-ba31-4f9f-8401-5d85d9cf659a"));

        // Ava's course sizes
        storage.registerCourse(Utilities.parseCourse("2022 WI CSE 110"), CourseSize.LARGE);
        storage.registerCourse(Utilities.parseCourse("2022 FA ECE 65"), CourseSize.SMALL);

        manager.startNewSession();
    }

    // When I paste pastedContents[0], AND I paste pastedContents[1], AND ..., then
    private void whenPasting(String[] pastedContents, Runnable then) {
        ActivityScenario<MockingPasting> scenario = ActivityScenario.launch(MockingPasting.class);
        scenario.onActivity(activity -> {
            EditText input = activity.findViewById(R.id.bof_info_pasted);
            Button enter = activity.findViewById(R.id.mocking_enter_button);
            for (String pastedContent : pastedContents) {
                input.setText(pastedContent);
                enter.performClick();
            }
            then.run();
        });
    }

    // BDD scenario #1: see Canvas
    @Test
    public void testArrivalWithoutWave() {
        givenScenario1();
        whenPasting(new String[]{BILL_CSV_NO_WAVE}, () -> {
            ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

            scenario.onActivity(activity -> {
                RecyclerView view = activity.findViewById(R.id.bof_list);
                assertEquals(1, Objects.requireNonNull(view.getAdapter()).getItemCount());
                assertRow(view, 0, "Bill", 1, View.INVISIBLE);
                assertEquals(BILL_URL, activity.getPeopleList().get(0).getUrl());
            });
        });
    }

    // BDD scenario #2: See Canvas
    @Test
    public void testArrivalWithWave() {
        givenScenario1();
        whenPasting(new String[]{OLIVIA_CSV_NO_WAVE, BILL_CSV_NO_WAVE, BILL_CSV_WAVE}, () -> {
            // Given the final then of #1 ^
            ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

            scenario.onActivity(activity -> {
                RecyclerView view = activity.findViewById(R.id.bof_list);
                assertEquals(2, Objects.requireNonNull(view.getAdapter()).getItemCount());
                assertRow(view, 0, "Bill", 1, View.VISIBLE);
                assertEquals(BILL_URL, activity.getPeopleList().get(0).getUrl());
            });
        });
    }
}
