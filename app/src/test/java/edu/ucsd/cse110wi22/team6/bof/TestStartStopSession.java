package edu.ucsd.cse110wi22.team6.bof;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.gms.nearby.messages.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;

// BDD scenario tests for MS1-5 and MS1-6
@RunWith(AndroidJUnit4.class)
public class TestStartStopSession {
    SessionManager manager;
    MockedMessagesClient mockedMessagesClient;
    Context context;
    AppStorage storage;

    @Before
    public void setup() {
        context = InstrumentationRegistry
                .getInstrumentation().getTargetContext();
        manager = SessionManager.getInstance(context);
        mockedMessagesClient = MockedMessagesClient.getInstance(context);
        Utilities.setPersistence(false);
        storage = Utilities.getStorageInstance(context);
        storage.setInitialized(true);
        storage.setName("Ava");
        storage.setPhotoUrl("https://www.example.com");
        storage.setCourseList(Utilities.parseCourseList("2021 FA CSE 100,2019 SP CSE 101,2021 WI SYN 2"));
    }

    private void resetState() {
        if (manager.isRunning()) {
            manager.stopSession();
        }
        manager.startNewSession();
    }

    private void arrive(IPerson person) {
        mockedMessagesClient.mockMessageArrival(new Message(MessageProcessor.Encoder.advertisePerson(person)));
    }

    @Test
    public void test() {
        resetState();
        Person olivia = new Person(
                UUID.fromString("19b4747e-995c-4bb2-b5e5-1dc7a5fc8f64"),
                "Olivia", Utilities.parseCourseList("2019 SP CSE 101,2021 WI SYN 2"),
                "");
        Person bob = new Person(
                UUID.fromString("d61f3be1-418b-4ae8-aee4-d9186f5fc848"),
                "Bob",
                Utilities.parseCourseList("2021 FA CSE 100,2019 SP CSE 101"),
                "");
        Person abe = new Person(
                UUID.fromString("952e3c52-9eb8-4080-88db-bb48c213c897"),
                "Abe",
                Utilities.parseCourseList("2021 FA CSE 100,2019 SP CSE 101"),
                "");
        arrive(olivia);
        manager.stopSession();
        arrive(bob);
        manager.startSession(manager.getCurrentSession()); // Resume current session
        arrive(abe);
        assertTrue(manager.getCurrentSession().getNearbyStudentList().contains(olivia));
        assertFalse(manager.getCurrentSession().getNearbyStudentList().contains(bob));
        assertTrue(manager.getCurrentSession().getNearbyStudentList().contains(abe));
    }
}
