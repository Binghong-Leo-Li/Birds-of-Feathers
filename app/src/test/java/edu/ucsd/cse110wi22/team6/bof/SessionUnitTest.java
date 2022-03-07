package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import edu.ucsd.cse110wi22.team6.bof.model.Session;
import edu.ucsd.cse110wi22.team6.bof.model.SessionChangeListener;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SessionUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSessionConstructorSpecifiedTime(){
        Session session = new Session(new Date(2021, 10, 11));
        assertEquals(2021, session.getStartTime().getYear());
        assertEquals(10, session.getStartTime().getMonth());
        assertEquals(11, session.getStartTime().getDate());
        assertNotNull(session.getSessionId());
    }

    @Test
    public void testSessionConstructorSpecifiedTimeAndUUID(){
        Session session = new Session(UUID.fromString("14467823-22d5-4a6d-88f3-78a50516fa37"), new Date(2021, 10, 11));
        assertEquals(2021, session.getStartTime().getYear());
        assertEquals(10, session.getStartTime().getMonth());
        assertEquals(11, session.getStartTime().getDate());
        assertEquals(UUID.fromString("14467823-22d5-4a6d-88f3-78a50516fa37"), session.getSessionId());
    }

    @Test
    public void testSessionSetNameAndGetName() {
        Session session = new Session(new Date(2021, 10, 11));
        assertEquals(null, session.getName());
        session.setName("test");
        assertEquals("test", session.getName());
    }

    @Test
    public void testSessionGetDisplayName() {
        Session session = new Session(new Date("Sat, 12 Aug 1995 13:30:00 GMT+0430"));
        assertEquals("Sat Aug 12 02:00:00 PDT 1995", session.getDisplayName());
    }

    @Test
    public void testSessionGetStringID() {
        Session session = new Session(UUID.fromString("14467823-22d5-4a6d-88f3-78a50516fa37"), new Date(2021, 10, 11));
        assertEquals("14467823-22d5-4a6d-88f3-78a50516fa37", session.getStringID());
    }

    @Test
    public void testSessionSerializeToString() {
        Session session = new Session(UUID.fromString("14467823-22d5-4a6d-88f3-78a50516fa37"), new Date(2021, 10, 11));
        assertEquals("{\"startTime\":\"Nov 11, 3921, 12:00:00 AM\",\"uuidList\":[]}", session.serializeToString());
    }

    @Test
    public void testSessionRegister() {
        Session session = new Session(UUID.fromString("14467823-22d5-4a6d-88f3-78a50516fa37"), new Date(2021, 10, 11));
        final String[] reg = {"unregistered"};
        SessionChangeListener listener = new SessionChangeListener() {
            @Override
            public void onSessionModified(Session session) {
                reg[0] = "registered";
            }
        };
        session.registerListener(listener);
        session.setName("test");
        assertEquals("registered", reg[0]);
    }

    public void testSessionUnregister() {
        Session session = new Session(UUID.fromString("14467823-22d5-4a6d-88f3-78a50516fa37"), new Date(2021, 10, 11));
        final String[] reg = {"unregistered"};
        SessionChangeListener listener = new SessionChangeListener() {
            @Override
            public void onSessionModified(Session session) {
                reg[0] = "registered";
            }
        };
        session.registerListener(listener);
        session.unregisterListener(listener);
        session.setName("test");
        assertEquals("unregistered", reg[0]);
    }

    @Test
    public void testSessionDeserialize() {
        Session session = new Session(UUID.fromString("d61f3be1-418b-4ae8-aee4-d9186f5fc848"), new Date(2021, 10, 11));
        Person olivia = new Person(
                UUID.fromString("19b4747e-995c-4bb2-b5e5-1dc7a5fc8f64"),
                "Olivia", Utilities.parseCourseList("2019 SP CSE 101,2021 WI SYN 2"),
                "");
        Person bob = new Person(
                UUID.fromString("d61f3be1-418b-4ae8-aee4-d9186f5fc848"),
                "Bob",
                Utilities.parseCourseList("2021 FA CSE 100,2019 SP CSE 101"),
                "");
        Function<UUID, IPerson> Function = (uuid -> {
            switch (uuid.toString()) {
                case "19b4747e-995c-4bb2-b5e5-1dc7a5fc8f64":
                    return olivia;
                case "d61f3be1-418b-4ae8-aee4-d9186f5fc848" :
                    return bob;
            }
            return null;
        });
        Session.Factory fact = new Session.Factory(Function);
        Session returned = fact.deserialize(session.getStringID(), session.serializeToString());
        assertEquals(session.getSessionId(), returned.getSessionId());
        assertEquals(session.getName(), returned.getName());
        assertEquals(session.getStartTime(), returned.getStartTime());
        assertEquals(session.getNearbyStudentList(), returned.getNearbyStudentList());
        assertEquals(session.getStartTime(), returned.getStartTime());
    }








}