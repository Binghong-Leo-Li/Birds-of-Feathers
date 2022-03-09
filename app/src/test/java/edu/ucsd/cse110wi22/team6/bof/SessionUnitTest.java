package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    // The date format used by the tests
    private static final DateFormat INPUT_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);

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
        assertNull(session.getName());
        session.setName("test");
        assertEquals("test", session.getName());
    }

    @Test
    public void testSessionGetDisplayNamePM() throws ParseException {
        Session session = new Session(INPUT_FORMAT.parse( "08/12/1995 12:01 PM"));
        assertEquals("8/12/95 12:01PM", session.getDisplayName());
    }

    @Test
    public void testSessionGetDisplayNameAM() throws ParseException {
        Session session = new Session(INPUT_FORMAT.parse( "12/07/2020 01:05 AM"));
        assertEquals("12/7/20 1:05AM", session.getDisplayName());
    }

    @Test
    public void testSessionGetDisplayNameWithName() throws ParseException {
        Session session = new Session(INPUT_FORMAT.parse( "08/07/2020 01:05 AM"));
        session.setName("Hello, world!");
        assertEquals("Hello, world!", session.getDisplayName());
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

    @Test
    public void testSessionUnregister() {
        Session session = new Session(UUID.fromString("14467823-22d5-4a6d-88f3-78a50516fa37"), new Date(2021, 10, 11));
        final String[] reg = {"unregistered"};
        SessionChangeListener listener = session1 -> reg[0] = "registered";
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