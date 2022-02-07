package edu.ucsd.cse110wi22.team6.bof;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testPerson(){
        List<Course> courses = new ArrayList<>();
        IPerson tmp = new Person("Alex", courses, "https://www.example.com/nothing");
        assertEquals("Alex", tmp.getName());
        assertTrue(tmp.getCourseList().isEmpty());
        assertEquals("https://www.example.com/nothing", tmp.getUrl());
    }

    @Test
    public void testParseCourse() {
        assertEquals(new Course(2020, "WI", "CSE", "15L"), Utilities.parseCourse("2020 WI CSE 15L"));
    }

    @Test
    public void testParseCourseList() {
        assertEquals(Collections.emptyList(), Utilities.parseCourseList(""));
        assertEquals(Collections.singletonList(new Course(2019, "SP", "ECE", "65")),
                Utilities.parseCourseList("2019 SP ECE 65"));
        assertEquals(Arrays.asList(new Course(2019, "SP", "ECE", "65"),
                new Course(2018, "FA", "CSE", "100")),
                Utilities.parseCourseList("2019 SP ECE 65,2018 FA CSE 100"));
    }

    @Test
    public void testEncodeCourseList() {
        assertEquals("2019 SP ECE 65,2018 FA CSE 100", Utilities.encodeCourseList(Arrays.asList(new Course(2019, "SP", "ECE", "65"),
                new Course(2018, "FA", "CSE", "100"))));
    }
}