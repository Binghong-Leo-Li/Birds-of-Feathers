package edu.ucsd.cse110wi22.team6.bof;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageButton;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;

// BDD Scenario tests for User Story 8
@RunWith(AndroidJUnit4.class)
public class TestWaveToFriendStory8 {
    SessionManager manager;
    Context context;
    AppStorage storage;
    public static final Person OLIVIA = new Person(
            UUID.fromString("19b4747e-995c-4bb2-b5e5-1dc7a5fc8f64"),
            "Olivia", Utilities.parseCourseList("2019 SP CSE 101,2021 WI SYN 2"),
            "");
    public static final Person BOB = new Person(
            UUID.fromString("d61f3be1-418b-4ae8-aee4-d9186f5fc848"),
            "Bob",
            Utilities.parseCourseList("2021 FA CSE 100,2019 SP CSE 101"),
            "");
    public static final Person ABE = new Person(
            UUID.fromString("952e3c52-9eb8-4080-88db-bb48c213c897"),
            "Abe",
            Utilities.parseCourseList("2021 FA CSE 100,2019 SP CSE 101"),
            "");

    @Before
    public void setup() {
        App.resetInstance(false);
        context = InstrumentationRegistry
                .getInstrumentation().getTargetContext();
        manager = SessionManager.getInstance(context);
        storage = Utilities.getStorageInstance(context);
        storage.setInitialized(true);
        storage.setName("Ava");
        storage.setPhotoUrl("https://www.example.com");
        storage.setCourseList(Utilities.parseCourseList("2021 FA CSE 100,2019 SP CSE 101,2021 WI SYN 2"));

        storage.registerPerson(OLIVIA);
        storage.registerPerson(BOB);
        storage.registerPerson(ABE);

        manager.startNewSession();
        manager.getCurrentSession().addNearbyStudent(OLIVIA);
    }

    // Given that Olivia is nearby
    // And I am on the waving screen
    // When I click the hand icon
    // Then a wave should be sent to Olivia
    @Test
    public void testWaveToFriend() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), BoFsDetails.class);
        intent.putExtra("uuid", OLIVIA.getUUID().toString());
        intent.putExtra("name", OLIVIA.getName());
        intent.putExtra("courseListParsing", Utilities.encodeCourseList(OLIVIA.getCourseList()));
        intent.putExtra("url", OLIVIA.getUrl());

        ActivityScenario<BoFsDetails> scenario = ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            ImageButton waveButton = activity.findViewById(R.id.wave_button);
            waveButton.performClick();
            Assert.assertTrue(storage.getWaveToList().contains(OLIVIA));
        });
    }
}
