package edu.ucsd.cse110wi22.team6.bof;
import static org.junit.Assert.assertEquals;

import android.widget.FrameLayout;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.core.app.ActivityScenario;

import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
public class TestListBoF{
    @Test
    public void testListOfBoFsDisplayed() {
        Utilities.setPersistence(false);
        IUserInfoStorage storage = Utilities.getStorageInstance(null);
        storage.setInitialized(true);
        storage.setName("Ava");
        storage.setPhotoUrl("https://www.example.com");
        storage.setCourseList(Utilities.parseCourseList("2022 WI CSE 110,2021 FA CSE 100,2021 FA ECE 65,2020 FA CSE 11"));
        MainActivity.setNearbyPeople(Arrays.asList(
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
            activity.updateUI();
            RecyclerView view = activity.findViewById(R.id.bof_list);
            assertEquals(6, view.getAdapter().getItemCount());
            try {
                FrameLayout framelayout = (FrameLayout) view.getChildAt(0);
                TextView editText = framelayout.findViewById(R.id.bof_row_name);
                String info = editText.getText().toString();
                assertEquals("Replicant", info);
            }
            catch(NullPointerException n){
                System.out.println("Empty List of BoFs");
            }
        });
    }
}


