package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.CourseSize;
import edu.ucsd.cse110wi22.team6.bof.model.Session;


@RunWith(AndroidJUnit4.class)
public class TestStory3_ClassLists {

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
        App.resetInstance(false);
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        storage = Utilities.getStorageInstance(context);
        storage.setInitialized(true);
        storage.setName("Leo");
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
    /**
     * BDD Scenarios:
     *
     *
     * Story3 BDD Scenario 1: BoF found with multiple overlapping classes
     * [Given] Leo is taking CSE 110 and CSE 101 with Alex (and no other class)
     * [And]   All the information is entered correctly
     * [And]   Alex is found with nearby during class CSE 110
     * [And]   Leo saved the class list for CSE 110
     * [When]  Leo selects to view the class list for CSE 110
     * [Then]  Alex should appear on the bof list with 2 overlapping courses
     * [When]  Leo selects to view the class list for CSE 101
     * [Then]  Alex should appear on the bof list with 2 overlapping courses
     * [When]  Leo selects to view the class list of any other class
     * [Then]  Alex should not appear on the class list
     *
     *
     * Story3 BDD Scenario 2: BoF never found
     * [Given] Leo has never taken any classes with Rando
     * [When]  Leo clicks on any class list
     * [Then]  Rando should not appear on the BoF List
     *
     *
     * Story3 BDD Scenario 3: Only one BoF found in a class
     * [Given] Leo is taking CSE 130 with David
     * [And]   David is the only person in the class
     * [And]   David is found by with NearBy during CSE 130
     * [And]   Leo saved the class list for CSE 130
     * [When]  Leo selects to view the class list for CSE 130
     * [Then]  David should appear in the list for CSE 130
     * [And]   David should be the only person appearing
     *
     *
     **/
    @Test
    public void BDDScenarioTests() {
        // Class CSE 110
        setNearbyPeople(Arrays.asList(A,B));
        manager.getCurrentSession().setName("CSE 110");
        Session s1 = manager.getCurrentSession();
        manager.stopSession();


        // Class CSE 101
        setNearbyPeople(Arrays.asList(C));
        manager.getCurrentSession().setName("CSE 101");
        Session s2 = manager.getCurrentSession();
        manager.stopSession();


        // Class CSE 130
        setNearbyPeople(Arrays.asList(D));
        manager.getCurrentSession().setName("CSE 130");
        Session s3 = manager.getCurrentSession();
        manager.stopSession();


        // Test class list names
        assertEquals("CSE 110", s1.getName());
        assertEquals("CSE 101", s2.getName());
        assertEquals("CSE 130", s3.getName());


        // See who is in CSE 110 class list
        manager.startSession(s1);
        ActivityScenario<MainActivity> scenario1 = ActivityScenario.launch(MainActivity.class);
        scenario1.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            assertEquals(2, Objects.requireNonNull(view.getAdapter()).getItemCount());

            Assert.assertTrue(contains(view, "Alex", 2));
            Assert.assertTrue(contains(view, "Bob", 1));
            Assert.assertTrue(noContain(view, "Chris"));
            Assert.assertTrue(noContain(view, "David"));
            Assert.assertTrue(noContain(view, "Rando"));
        });
        manager.stopSession();


        // See who is in CSE 101 class list
        manager.startSession(s2);
        ActivityScenario<MainActivity> scenario2 = ActivityScenario.launch(MainActivity.class);
        scenario2.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            assertEquals(1, Objects.requireNonNull(view.getAdapter()).getItemCount());

            Assert.assertTrue(noContain(view, "Alex"));
            Assert.assertTrue(noContain(view, "Bob"));
            Assert.assertTrue(contains(view, "Chris",1));
            Assert.assertTrue(noContain(view, "David"));
            Assert.assertTrue(noContain(view, "Rando"));
        });
        manager.stopSession();


        // See who is in CSE 130 class list
        manager.startSession(s3);
        ActivityScenario<MainActivity> scenario3 = ActivityScenario.launch(MainActivity.class);
        scenario3.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            assertEquals(1, Objects.requireNonNull(view.getAdapter()).getItemCount());

            Assert.assertTrue(noContain(view, "Alex"));
            Assert.assertTrue(noContain(view, "Bob"));
            Assert.assertTrue(noContain(view, "Chris"));
            Assert.assertTrue(contains(view, "David", 1));
            Assert.assertTrue(noContain(view, "Rando"));
        });


        // Check behavior of stopping session -> freezes the bof list state
        ActivityScenario<MainActivity> stopped = ActivityScenario.launch(MainActivity.class);
        stopped.onActivity(activity -> {
            activity.findViewById(R.id.toggle_button).performClick();
            RecyclerView view = activity.findViewById(R.id.bof_list);
            assertEquals(1, Objects.requireNonNull(view.getAdapter()).getItemCount());

            Assert.assertTrue(contains(view, "David", 1));
        });


        // New class to be empty
        manager.startNewSession();
        scenario3.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            assertEquals(1, Objects.requireNonNull(view.getAdapter()).getItemCount());

            Assert.assertTrue(noContain(view, "Alex"));
            Assert.assertTrue(noContain(view, "Bob"));
            Assert.assertTrue(noContain(view, "Chris"));
            Assert.assertTrue(contains(view, "David", 1));
            Assert.assertTrue(noContain(view, "Rando"));
        });
        manager.stopSession();


        // Check new class empty
        manager.startNewSession();
        ActivityScenario<MainActivity> empty = ActivityScenario.launch(MainActivity.class);
        empty.onActivity(activity -> {
            RecyclerView view = activity.findViewById(R.id.bof_list);
            assertEquals(0, Objects.requireNonNull(view.getAdapter()).getItemCount());
        });
        manager.stopSession();
    }



}
