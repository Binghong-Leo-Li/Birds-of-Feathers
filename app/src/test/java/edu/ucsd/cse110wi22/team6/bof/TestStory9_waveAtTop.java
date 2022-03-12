package edu.ucsd.cse110wi22.team6.bof;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.content.Context;
import android.view.View;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;

// JUnit test for Story 9, waved at top
@RunWith(AndroidJUnit4.class)
public class TestStory9_waveAtTop {

    // State of the app
    private Context context;
    // Dummy people
    IPerson A, B, C, D, Rd;
    SessionManager manager;
    AppStorage storage;

    // Helper method to initialize session/nearby
    private void setNearbyPeople(List<IPerson> list) {
        this.manager = SessionManager.getInstance(context);
        if (manager.isRunning()) {
            manager.stopSession();
        }
        manager.startNewSession();
        for (IPerson student : list) {
            storage.registerPerson(student);
            manager.getCurrentSession().addNearbyStudent(student);
        }
    }

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

        App.resetInstance(false);
        context = InstrumentationRegistry
                .getInstrumentation().getTargetContext();
        storage = Utilities.getStorageInstance(context);
        storage.setInitialized(true);
        storage.setName("Leo");
        storage.setUserUUID(UUID.randomUUID());
        storage.setPhotoUrl("https://media.istockphoto.com/photos/cool-elegant-golden-" +
                "retriever-puppy-wearing-sunglasees-and-tie-picture-id1304715703?s=612x612");
        storage.setCourseList(Utilities.parseCourseList("2022 WI CSE 110,2022 WI CSE 101,2022 WI CSE 130," +
                "2021 FA CSE 105,2021 SP CSE 30,2021 WI HITO 87"));


        // 2 common courses
        A = new Person("Alex", Utilities.parseCourseList("2022 WI CSE 110,2022 WI CSE 101"), "");
        // 1 common course
        B = new Person("Bob", Utilities.parseCourseList("2022 WI CSE 110,2019 SP CSE 101"), "");
        // 2 common course
        C = new Person("Chris", Utilities.parseCourseList("2022 WI CSE 110,2022 WI CSE 101,2019 SP CSE 175"), "");
        // 2 common course
        D = new Person("David", Utilities.parseCourseList("2022 WI CSE 110,2022 WI CSE 130,2019 SP CSE 99"), "");
        // No course
        Rd = new Person("Rando", Utilities.parseCourseList("2022 WI CSE 110"), "");
    }

    private boolean atTop(RecyclerView view, String expectedName) {
        FrameLayout framelayout = (FrameLayout) view.getChildAt(0);

        TextView editText = framelayout.findViewById(R.id.bof_row_name);
        String info = editText.getText().toString();
        return info.equals(expectedName);
    }

    /**
     * Story 9 Wave At Top BDD Scenario:
     *
     * [Given] Leo has entered courses "2022 WI CSE 110,2022 WI CSE 101,2022 WI CSE 130," +
     *                 "2021 FA CSE 105,2021 SP CSE 30,2021 WI HITO 87"
     * [And]   Alex, Chris, David are all taking 2 common courses with Leo
     * [And]   Bob and Rando are only taking 1 common course with Leo
     * [And]   The courses are entered correctly by them
     * [And]   All 5 of them are found during the same session
     * [When]  Leo takes a look at the BoF List
     * [Then]  Bob should not appear at the top of the list
     * [When]  Bob waves to Leo
     * [Then]  Bob should be at the top of the list
     * [When]  Alex also wave to Leo
     * [Then]  Alex should be at the top of the list
     * [And]   Bob should be second to the top of the list
     * [When]  Rando waves to Leo
     * [Then]  Alex should still be at the top of the list
     *
     * P.S: since default is sorted by number of classes taken together
     */

    @Test
    public void BDDTest() {
        setNearbyPeople(Arrays.asList(A,B,C,D,Rd));
        manager.getCurrentSession().setName("CSE 110");

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            assertFalse(atTop(view, "Bob"));
        });

        // bob waves
        storage.waveFrom(B);

        ActivityScenario<MainActivity> scenario1 = ActivityScenario.launch(MainActivity.class);
        scenario1.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            // checking all 5 are there
            assertEquals(5, Objects.requireNonNull(view.getAdapter()).getItemCount());
            assertRow(view, 0, "Bob", 1, View.VISIBLE);
        });

        // alex waves
        storage.waveFrom(A);
        ActivityScenario<MainActivity> scenario2 = ActivityScenario.launch(MainActivity.class);
        scenario2.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            // alex top, then bob
            assertRow(view, 0, "Alex", 2, View.VISIBLE);
            assertRow(view, 1, "Bob", 1, View.VISIBLE);
        });

        // rando waves
        storage.waveFrom(Rd);
        ActivityScenario<MainActivity> scenario3 = ActivityScenario.launch(MainActivity.class);
        scenario3.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            // alex still top
            assertRow(view, 0, "Alex", 2, View.VISIBLE);
        });

        manager.stopSession();
    }
}
