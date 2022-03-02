package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertEquals;

import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowLog;

import java.util.Arrays;
import java.util.Objects;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.CourseSize;

// Test the sorting of the list of BoFs based on recency and size
// BDD Scenario tests for Story 1 and 2 on rubric and story 4.1 and 4.2 on ZenHub
@RunWith(AndroidJUnit4.class)
public class TestListBoFsMS2 {
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
        Utilities.setPersistence(false);
        AppStorage storage = Utilities.getStorageInstance(null);
        storage.setInitialized(true);
        storage.setName("Ava");
        storage.setPhotoUrl("https://www.example.com");
        storage.setCourseList(Utilities.parseCourseList("2021 FA CSE 100,2019 SP CSE 101,2021 WI SYN 2"));

        MainActivity.setNearbyPeople(Arrays.asList(
                new Person("Olivia", Utilities.parseCourseList("2019 SP CSE 101,2021 WI SYN 2"), ""),
                new Person("Bob", Utilities.parseCourseList("2021 FA CSE 100,2019 SP CSE 101"), "")
        ));

        // Ava's course sizes
        storage.registerCourse(Utilities.parseCourse("2021 FA CSE 100"), CourseSize.HUGE);
        storage.registerCourse(Utilities.parseCourse("2019 SP CSE 101"), CourseSize.LARGE);
        storage.registerCourse(Utilities.parseCourse("2021 WI SYN 2"), CourseSize.TINY);

        // See logs
        ShadowLog.stream = System.out;
    }

    // Default option should be same as MS 1
    @Test
    public void testNumCoursesInCommon() {
        IUserInfoStorage storage = Utilities.getStorageInstance(null);
        storage.setCourseList(Utilities.parseCourseList("2021 WI CSE 100,2019 SP CSE 101,2021 WI SYN 2"));
        MainActivity.setNearbyPeople(Arrays.asList(
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

    // <string-array name="preferences">
    //        <item>Sort by number of classes in common</item>
    //        <item>prioritize small classes</item>
    //        <item>prioritize recent</item>
    //        <item>this quarter only</item>

    // Prioritize small classes
    @Test
    public void testSortBySmallClasses() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            Spinner sortDropDown = activity.findViewById(R.id.preferences_dropdown);
            sortDropDown.setSelection(1); // "prioritize small classes"
            view.measure(0,0); // dirty hack to force update of recycler view
            // See https://github.com/robolectric/robolectric/issues/3747
            assertEquals(2, Objects.requireNonNull(view.getAdapter()).getItemCount());
            assertTextAt("Olivia", 2, view, 0);
            assertTextAt("Bob", 2, view, 1);
        });
    }

    // Prioritize recent
    @Test
    public void testSortByRecent() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        scenario.onActivity(activity -> {
            activity.setYear(2021);
            activity.setQuarter("FA");
            RecyclerView view = activity.findViewById(R.id.bof_list);
            Spinner sortDropDown = activity.findViewById(R.id.preferences_dropdown);
            sortDropDown.setSelection(2); // "prioritize recent"
            view.measure(0,0); // dirty hack to force update of recycler view
            // See https://github.com/robolectric/robolectric/issues/3747
            assertEquals(2, Objects.requireNonNull(view.getAdapter()).getItemCount());
            assertTextAt("Bob", 2, view, 0);
            assertTextAt("Olivia", 2, view, 1);
        });
    }
}
