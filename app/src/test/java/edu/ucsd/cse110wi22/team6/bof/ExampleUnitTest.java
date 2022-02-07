package edu.ucsd.cse110wi22.team6.bof;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

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
    public void testCourseConstructor() {
        Course exampleCourse = new Course(2021, "Fall", "CSE", "110");
        assertEquals(2021, exampleCourse.year);
        assertEquals("Fall", exampleCourse.quarter);
        assertEquals("CSE", exampleCourse.subject);
        assertEquals("110", exampleCourse.courseNumber);
    }

    @Test
    public void testCourseGetter() {
        Course exampleCourse = new Course(2021, "Fall", "CSE", "110");
        assertEquals(2021, exampleCourse.getYear());
        assertEquals("Fall", exampleCourse.getQuarter());
        assertEquals("CSE", exampleCourse.getSubject());
        assertEquals("110", exampleCourse.getCourseNumber());
    }

    @Test
    public void testCourseToString() {
        Course exampleCourse = new Course(2021, "Fall", "CSE", "110");
        assertEquals("2021 Fall CSE 110", exampleCourse.toString());
    }

    @Test
    public void testCourseEquals() {
        Course exampleCourse1 = new Course(2021, "Fall", "CSE", "110");
        Course exampleCourse2 = new Course(2021, "Fall", "CSE", "110");
        assertEquals(true, exampleCourse1.equals(exampleCourse2));
    }

    @Test
    public void testCourseNotEquals() {
        Course exampleCourse1 = new Course(2021, "Fall", "CSE", "110");
        Course exampleCourse2 = new Course(2022, "Winter", "CSE", "110");
        assertEquals(false, exampleCourse1.equals(exampleCourse2));
    }

    @Test
    public void testCourseHasCode() {
        Course exampleCourse1 = new Course(2021, "Fall", "CSE", "110");
        Course exampleCourse2 = new Course(2021, "Fall", "CSE", "110");
        assertEquals(exampleCourse1.hashCode(), exampleCourse1.hashCode());

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
}