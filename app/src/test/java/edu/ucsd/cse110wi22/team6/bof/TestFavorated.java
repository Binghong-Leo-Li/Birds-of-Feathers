package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowToast;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.CourseSize;

// Test the sorting of the list of BoFs based on recency and size
// BDD Scenario tests for Story 1 and 2 on rubric and story 4.1 and 4.2 on ZenHub
@RunWith(AndroidJUnit4.class)
public class TestFavorated {
    private static final int PRIORITIZE_SMALL_CLASSES_INDEX = 1;
    private static final int PRIORITIZE_RECENT_INDEX = 2;

    private Context context;

    // Helper method
    private void setNearbyPeople(List<IPerson> list) {
        SessionManager manager = SessionManager.getInstance(context);
        if (manager.isRunning()) {
            manager.stopSession();
        }
        manager.startNewSession();
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
    //And No other BoFs are show


    //Story 6 BDD Scenario Test: Favorite classmates [2]
    //As a student I want to be able to mark a classmate as a favorite so that I can later see those I identified as friends
    @Test
    public void testFavored() {
        AppStorage storage;
        Person bof;
        storage = Utilities.getStorageInstance(context);
        List<Course> courseList = Utilities.parseCourseList("2021 FA ECE 65,2020 FA CSE 11");
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), BoFsDetails.class);

        bof = new Person(UUID.randomUUID(), "Rick", courseList, "https://avatars.githubusercontent.com/u/32375681?v=4");
        intent.putExtra("uuid", bof.getUUID().toString());
        intent.putExtra("name", "Rick");
        intent.putExtra("courseListParsing", "2021 FA ECE 65,2020 FA CSE 11");
        intent.putExtra("url", "https://avatars.githubusercontent.com/u/32375681?v=4");
        ActivityScenario<BoFsDetails> scenario = ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            ImageButton buttonFav = activity.findViewById(R.id.favoriteButton);
            buttonFav.performClick();
            assertEquals(true, storage.isFavorited(bof));
        });
    }

    @Test
    public void testUnfavored() {
        AppStorage storage;
        Person bof;
        storage = Utilities.getStorageInstance(context);
        List<Course> courseList = Utilities.parseCourseList("2021 FA ECE 65,2020 FA CSE 11");
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), BoFsDetails.class);

        bof = new Person(UUID.randomUUID(), "Rick", courseList, "https://avatars.githubusercontent.com/u/32375681?v=4");
        intent.putExtra("uuid", bof.getUUID().toString());
        intent.putExtra("name", "Rick");
        intent.putExtra("courseListParsing", "2021 FA ECE 65,2020 FA CSE 11");
        intent.putExtra("url", "https://avatars.githubusercontent.com/u/32375681?v=4");
        ActivityScenario<BoFsDetails> scenario = ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            ImageButton buttonFav = activity.findViewById(R.id.favoriteButton);
            buttonFav.performClick();
            buttonFav.performClick();
            assertEquals(false, storage.isFavorited(bof));
        });


    }


}
