package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.InMemoryMapping;

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
    // registerCourse
    // getCourseSize
    // registerPerson
    // getPersonFromID
    // registerNewSession
    // getSessionList
    // isSessionNameTaken
}
