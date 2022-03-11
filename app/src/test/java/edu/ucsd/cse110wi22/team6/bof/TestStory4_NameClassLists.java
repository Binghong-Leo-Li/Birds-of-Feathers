package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.CourseSize;
import edu.ucsd.cse110wi22.team6.bof.model.Session;


// story 4 name class lists bdd test
@RunWith(AndroidJUnit4.class)
public class TestStory4_NameClassLists {
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

    // Helper get to get name
    private String getBofNameAtPos(int pos, RecyclerView view) {
        FrameLayout framelayout = (FrameLayout) view.getChildAt(pos);
        TextView editText = framelayout.findViewById(R.id.bof_row_name);
        String name = editText.getText().toString();
        return name;
    }

    // Helper to check if a recycler view contains a specific person with number of overlapping course
    private boolean contains(RecyclerView view, String expectedName, int expectNumCommonCourses) {
        int count = Objects.requireNonNull(view.getAdapter()).getItemCount();
        for (int i = 0; i < count; i++) {
            String name = getBofNameAtPos(i, view);

            FrameLayout framelayout = (FrameLayout) view.getChildAt(i);
            TextView numCoursesText = framelayout.findViewById(R.id.num_common_courses_view);
            int numCommon = Integer.parseInt(numCoursesText.getText().toString());
            if (name.equals(expectedName) && numCommon == expectNumCommonCourses) {
                return true;
            }
        }
        return false;
    }


    // Checking to make sure the recycler view does not contain
    private boolean noContain(RecyclerView view, String expectedName) {
        int count = Objects.requireNonNull(view.getAdapter()).getItemCount();
        for (int i = 0; i < count; i++) {
            String name = getBofNameAtPos(i, view);

            if (name.equals(expectedName)) {
                return false;
            }
        }
        return true;

    }


    // Setting up persisted storage
    @Before
    public void setup() {
        Utilities.setPersistence(false);
        context = InstrumentationRegistry.getInstrumentation().getContext();
        storage = Utilities.getStorageInstance(context);
        storage.setInitialized(true);
        storage.setName("Leo");
        storage.setUserUUID(UUID.randomUUID());
        storage.setPhotoUrl("https://media.istockphoto.com/photos/cool-elegant-golden-retriever-puppy-wearing-sunglasees-and-tie-picture-id1304715703?s=612x612");
        storage.setCourseList(Utilities.parseCourseList("2022 WI CSE 110,2022 WI CSE 101,2022 WI CSE 130," +
                "2021 FA CSE 105,2021 SP CSE 30,2021 WI HITO 87"));

        // 2 common courses
        A = new Person("Alex", Utilities.parseCourseList("2022 WI CSE 110,2022 WI CSE 101"), "");
        // 1 common course
        B = new Person("Bob", Utilities.parseCourseList("2022 WI CSE 110,2019 SP CSE 101"), "");
        // 1 common course
        C = new Person("Chris", Utilities.parseCourseList("2022 WI CSE 101,2019 SP CSE 175"), "");
        // 1 common course
        D = new Person("David", Utilities.parseCourseList("2022 WI CSE 130,2019 SP CSE 99"), "");
        // No course
        Rd = new Person("Rando", Utilities.parseCourseList(""), "");

        storage.registerCourse(Utilities.parseCourse("2022 WI CSE 110"), CourseSize.HUGE);
        storage.registerCourse(Utilities.parseCourse("2022 WI CSE 101"), CourseSize.LARGE);
        storage.registerCourse(Utilities.parseCourse("2022 WI CSE 130"), CourseSize.SMALL);
        storage.registerCourse(Utilities.parseCourse("2021 WI HITO 87"), CourseSize.TINY);
        storage.registerCourse(Utilities.parseCourse("2021 FA CSE 105"), CourseSize.MEDIUM);
        storage.registerCourse(Utilities.parseCourse("2021 SP CSE 30"), CourseSize.LARGE);

    }

    // contains for sessions
    private boolean contains(List<Session> sessions, String expectedCourse){
        for (int i = 0; i < sessions.size(); i++) {
            if (sessions.get(i).getName().equals(expectedCourse)) return true;
        }
        return false;
    }

    // contains for spinner
    private boolean contains(Spinner sp, String expectedCourseName) {
        for (int i = 0; i < sp.getAdapter().getCount(); i++) {
            if (sp.getAdapter().getItem(i).toString().contains(expectedCourseName))
                return true;
        }
        return false;
    }

    /**
     *
     * Story4 BDD Scenario:
     * [Given] Leo created three class lists
     * [And]   Leo saved the classes with names "CSE 110", "CSE 101", "CSE 130"
     * [When]  Leo clicks to select a class from list
     * [Then]  Leo should be seeing three options "CSE 110", "CSE 101", "CSE 130"
     *
     **/
    @Test
    public void BDDTest() {
        // Class CSE 110
        setNearbyPeople(Arrays.asList(A,B));
        manager.getCurrentSession().setName("CSE 110");
        Session s1 = manager.getCurrentSession();
        manager.onSessionModified(s1);
        manager.stopSession();

        // Class CSE 101
        setNearbyPeople(Arrays.asList(C));
        manager.getCurrentSession().setName("CSE 101");
        manager.stopSession();

        // Class CSE 130
        setNearbyPeople(Arrays.asList(D));
        manager.getCurrentSession().setName("CSE 130");

        manager.stopSession();

//        List<Session> sessions = storage.getSessionList();
//        assertEquals(3,sessions.size());
//
//        // Test class list names in storage
//        assertTrue(contains(sessions, "CSE 110"));
//        assertTrue(contains(sessions, "CSE 101"));
//        assertTrue(contains(sessions, "CSE 130"));

        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            // clicking the start button to select
            Button start = activity.findViewById(R.id.toggle_button);
            start.performClick();

            // should only have one alert dialog at the time after click
            assertEquals(1, ShadowAlertDialog.getShownDialogs().size());

            // Getting the alter dialog
            AlertDialog ad = (AlertDialog)(ShadowAlertDialog.getShownDialogs().get(0));

            // extracting spinner
            Spinner sp = (Spinner)ad.findViewById(R.id.spinner);

            // spinner should have 4 fields
            assertEquals(4, sp.getAdapter().getCount());

            // spinner should contain the following courses on screen
            assertTrue(contains(sp, "CSE 110"));
            assertTrue(contains(sp, "CSE 101"));
            assertTrue(contains(sp, "CSE 130"));

            // spinner should not contain other courses
            assertFalse(contains(sp, "CSE 100"));
            assertFalse(contains(sp, "Rando"));

        });
    }
}
