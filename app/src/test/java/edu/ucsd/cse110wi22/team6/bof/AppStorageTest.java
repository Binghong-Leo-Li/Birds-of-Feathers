package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.CourseSize;
import edu.ucsd.cse110wi22.team6.bof.model.InMemoryMapping;
import edu.ucsd.cse110wi22.team6.bof.model.Session;

public class AppStorageTest {
    // Just use this if Photo URL is irrelevant
    private static final String TEST_URL = "https://avatars.githubusercontent.com/u/32375681?v=4";
    // Some sample people
    private static final IPerson BOB = new Person("Bob", Utilities.parseCourseList("2022 WI CSE 110"), TEST_URL);
    private static final IPerson JACKSON = new Person("Jackson", Utilities.parseCourseList("2022 WI CSE 120"), TEST_URL);

    // Use a fresh instance so that tests will not interfere with each other
    private AppStorage getFreshInstance() {
        return new AppStorage(new InMemoryMapping(new HashMap<>()));
    }

    // Test the basic functionality of this class, mostly ones from MS 1
    @Test
    public void testBasicAttributes() {
        AppStorage storage = getFreshInstance();
        List<Course> courseList = Utilities.parseCourseList("2022 SS1 ECE 35,2021 WI CSE 210");
        storage.setName("Ava");
        storage.setPhotoUrl(TEST_URL);
        storage.setCourseList(courseList);
        storage.setInitialized(true);
        IPerson ava = storage.getUser();
        assertTrue(storage.isInitialized());
        assertEquals("Ava", ava.getName());
        assertEquals(TEST_URL, ava.getUrl());
        assertEquals(courseList, ava.getCourseList());
        IPerson avaAgain = storage.getUser();
        assertEquals(ava.getUUID(), avaAgain.getUUID()); // UUID must remain constant
    }

    @Test
    public void testFavoriteList() {
        AppStorage storage = getFreshInstance();
        storage.addToFavorites(BOB);
        assertTrue(storage.isFavorited(BOB));
        storage.removeFromFavorites(BOB);
        assertFalse(storage.isFavorited(BOB));
    }

    @Test
    public void testFavoriteListMoreThanOnePeople() {
        AppStorage storage = getFreshInstance();
        assertFalse(storage.isFavorited(BOB));
        assertFalse(storage.isFavorited(JACKSON));
        storage.addToFavorites(BOB);
        assertTrue(storage.isFavorited(BOB));
        assertFalse(storage.isFavorited(JACKSON));
        storage.addToFavorites(JACKSON);
        assertTrue(storage.isFavorited(BOB));
        assertTrue(storage.isFavorited(JACKSON));
        storage.removeFromFavorites(BOB);
        assertFalse(storage.isFavorited(BOB));
        assertTrue(storage.isFavorited(JACKSON));
    }

    // TODO: add more tests to achieve better coverage
    // Specifically
    @Test
    public void testGetCourseSize(){
        AppStorage storage = getFreshInstance();
        Course course = new Course(2019, "SP", "CSE", "101");
        storage.registerCourse(course, CourseSize.HUGE);
        assertEquals(CourseSize.HUGE, storage.getCourseSize(course));
    }
    @Test
    public void testGetPersonFromID(){
        AppStorage storage = getFreshInstance();
        storage.registerPerson(BOB);
        assertEquals(storage.getPersonFromID(BOB.getUUID()), BOB);
    }
    @Test
    public void testRegisterNewSession(){
        AppStorage storage = getFreshInstance();
        Date d = new Date(2019, 12, 20);
        Session s = new Session(d);
        storage.registerNewSession(s);
        assertEquals(1,storage.getSessionList().size());
    }
    @Test
    public void testIsSessionNameTaken(){
        AppStorage storage = getFreshInstance();
        Date d = new Date(2019, 12, 20);
        Session s = new Session(d);
        s.setName("Session1");
        storage.registerNewSession(s);
        assertEquals(true, storage.isSessionNameTaken("Session1"));
    }
}
