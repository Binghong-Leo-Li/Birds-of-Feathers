package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.CourseSize;

// Test the sorting of the list of BoFs based on recency and size
// BDD Scenario tests for Story 1 and Story 2 on rubric and story 4.1 and 4.2 on ZenHub
@RunWith(AndroidJUnit4.class)
public class TestListBoFsMS2 {
    private static final int PRIORITIZE_SMALL_CLASSES_INDEX = 1;
    private static final int PRIORITIZE_RECENT_INDEX = 2;

    private Context context;

    // Helper method
    private void setNearbyPeople(List<IPerson> list) {
        SessionManager manager = SessionManager.getInstance(context);
        if (manager.isRunning()) {
            manager.stopSession();
        }
        manager.startNewSession(null);
        for (IPerson student : list) {
            manager.getCurrentSession().addNearbyStudent(student);
        }
    }

    // Helper method to test that the n-th row of bof list of some person with the correct number of common courses
    private void assertTextAt(String expectedName, int expectNumCommonCourses, RecyclerView view, int position) {
        FrameLayout framelayout = (FrameLayout) view.getChildAt(position);
        TextView editText = framelayout.findViewById(R.id.bof_row_name);
        String info = editText.getText().toString();
        assertEquals(expectedName, info);
        TextView numCoursesText = framelayout.findViewById(R.id.num_common_courses_view);
        int numCommon = Integer.parseInt(numCoursesText.getText().toString());
        assertEquals(expectNumCommonCourses, numCommon);
    }

    // Setting up persisted storage
    @Before
    public void setup() {
        context = InstrumentationRegistry
                .getInstrumentation().getTargetContext();

        App.resetInstance(false);
        AppStorage storage = Utilities.getStorageInstance(context);
        storage.setInitialized(true);
        storage.setName("Ava");
        storage.setPhotoUrl("https://www.example.com");
        storage.setCourseList(Utilities.parseCourseList("2021 FA CSE 100,2019 SP CSE 101,2021 WI SYN 2"));

        setNearbyPeople(Arrays.asList(
                new Person("Olivia", Utilities.parseCourseList("2019 SP CSE 101,2021 WI SYN 2"), ""),
                new Person("Bob", Utilities.parseCourseList("2021 FA CSE 100,2019 SP CSE 101"), "")
        ));

        // Ava's course sizes
        storage.registerCourse(Utilities.parseCourse("2021 FA CSE 100"), CourseSize.HUGE);
        storage.registerCourse(Utilities.parseCourse("2019 SP CSE 101"), CourseSize.LARGE);
        storage.registerCourse(Utilities.parseCourse("2021 WI SYN 2"), CourseSize.TINY);

        // Uncomment and see logs for debug
//        ShadowLog.stream = System.out;
    }

    // Default option should be same as MS 1
    // Scenario 1: Filtering using default option (number of courses in common)
    //
    //Given that Ava has not changed anything since first time setup
    //And Ava’s list of classes is
    //CSE 100 in Winter 2021
    //CSE 101 in Spring 2019
    //And Olivia’s list of classes is
    //CSE 100 in Winter 2021
    //CSE 101 in Spring 2019
    //And Bob’s list of classes is
    //MATH 20A in Spring 2019
    //MATH 20C in Winter 2021
    //CSE 101 in Spring 2019
    //And only Bob and Olivia is nearby
    //When Ava navigates to the home screen
    //Then Olivia should appear on the top, with number 2
    //And Bob should appear below Olivia, with number 1
    //And No other BoFs are shown
    @Test
    public void testNumCoursesInCommonSimple() {
        IUserInfoStorage storage = Utilities.getStorageInstance(context);
        storage.setCourseList(Utilities.parseCourseList("2021 WI CSE 100,2019 SP CSE 101,2021 WI SYN 2"));
        setNearbyPeople(Arrays.asList(
                new Person("Olivia", Utilities.parseCourseList("2021 WI CSE 100,2019 SP CSE 101"), ""),
                new Person("Bob", Utilities.parseCourseList("2019 SP MATH 20A,2021 WI MATH 20C,2019 SP CSE 101"), "")
        ));

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            assertEquals(2, Objects.requireNonNull(view.getAdapter()).getItemCount());
            assertTextAt("Olivia", 2, view, 0);
            assertTextAt("Bob", 1, view, 1);
        });
    }

    // Test the same thing as above but a more complex case, from MS 1
    @Test
    public void testListOfBoFsDisplayed() {
        IUserInfoStorage storage = Utilities.getStorageInstance(context);
        storage.setCourseList(Utilities.parseCourseList("2022 WI CSE 110,2021 FA CSE 100,2021 FA ECE 65,2020 FA CSE 11"));
        setNearbyPeople(Arrays.asList(
                /*1*/ new Person("Rick", Utilities.parseCourseList("2022 WI CSE 110"), ""),
                /*1*/ new Person("Gary", Utilities.parseCourseList("2021 FA CSE 100"), ""),
                /*0*/ new Person("Guy", Utilities.parseCourseList(""), ""),
                /*0*/ new Person("Greg", Utilities.parseCourseList("2022 SP CSE 110,2021 FA MATH 100,2021 FA ECE 35,2019 FA CSE 11"), ""),
                /*2*/ new Person("Bill", Utilities.parseCourseList("2021 FA ECE 65,2020 FA CSE 11"), ""),
                /*2*/ new Person("Will", Utilities.parseCourseList("2020 FA CSE 11,2021 FA ECE 65"), ""),
                /*4*/ new Person("Replicant", Utilities.parseCourseList("2022 WI CSE 110,2021 FA CSE 100,2021 FA ECE 65,2020 FA CSE 11"), ""),
                /*1*/ new Person("Bob", Utilities.parseCourseList("2020 FA CSE 11,2022 WI CSE 11"), "")
        ));

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            assertEquals(6, Objects.requireNonNull(view.getAdapter()).getItemCount());
            FrameLayout framelayout = (FrameLayout) view.getChildAt(0);
            TextView editText = framelayout.findViewById(R.id.bof_row_name);
            String info = editText.getText().toString();
            assertEquals("Replicant", info);
        });
    }

    // Prioritize small classes
    // Scenario 2: Filtering using “prioritize small classes”
    //
    //Given Ava’s list of classes is
    //CSE 100 in Fall 2021, size huge.
    //CSE 101 in Spring 2019, size large.
    //SYN 2 in Winter 2021, size tiny.
    //And Olivia’s list of classes is
    //CSE 101 in Spring 2019, size large.
    //SYN 2 in Winter 2021, size tiny.
    //And Bob’s list of classes is
    //CSE 100 in Fall 2021, size huge.
    //CSE 101 in Spring 2019, size large.
    //And only Bob and Olivia is nearby
    //When Ava navigates to the home screen
    //And select “Prioritize small classes” on the drop down
    //And “This quarter only” option is not selected
    //Then Olivia should appear on the top, with number 2
    //And Bob should appear below Olivia, with number 2
    //And No other BoFs are shown
    @Test
    public void testSortBySmallClasses() {
        // Given already implemented in setup()
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            Spinner sortDropDown = activity.findViewById(R.id.preferences_dropdown);
            sortDropDown.setSelection(PRIORITIZE_SMALL_CLASSES_INDEX);
            view.measure(0,0); // dirty hack to force update of recycler view
            // See https://github.com/robolectric/robolectric/issues/3747
            assertEquals(2, Objects.requireNonNull(view.getAdapter()).getItemCount());
            assertTextAt("Olivia", 2, view, 0);
            assertTextAt("Bob", 2, view, 1);
        });
    }

    // Prioritize recent
    // Scenario 3: Filtering using “prioritize recent”
    //
    //Given the current quarter is FA 2021
    //And Ava’s list of classes is
    //CSE 100 in Fall 2021, size huge.
    //CSE 101 in Spring 2019, size large.
    //SYN 2 in Winter 2021, size tiny.
    //And Olivia’s list of classes is
    //CSE 101 in Spring 2019, size large.
    //SYN 2 in Winter 2021, size tiny.
    //And Bob’s list of classes is
    //CSE 100 in Fall 2021, size huge.
    //CSE 101 in Spring 2019, size large.
    //And only Bob and Olivia is nearby
    //When Ava navigates to the home screen
    //And select “Prioritize recent” on the drop down
    //And “This quarter only” option is not selected
    //Then Bob should appear on the top, with number 2
    //And Olivia should appear below Bob, with number 2
    //And No other BoFs are shown
    @Test
    public void testSortByRecent() {
        // Given already implemented in setup()
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            activity.setYear(2021);
            activity.setQuarter("FA");
            RecyclerView view = activity.findViewById(R.id.bof_list);
            Spinner sortDropDown = activity.findViewById(R.id.preferences_dropdown);
            sortDropDown.setSelection(PRIORITIZE_RECENT_INDEX);
            view.measure(0,0); // dirty hack to force update of recycler view
            // See https://github.com/robolectric/robolectric/issues/3747
            assertEquals(2, Objects.requireNonNull(view.getAdapter()).getItemCount());
            assertTextAt("Bob", 2, view, 0);
            assertTextAt("Olivia", 2, view, 1);
        });
    }
}
