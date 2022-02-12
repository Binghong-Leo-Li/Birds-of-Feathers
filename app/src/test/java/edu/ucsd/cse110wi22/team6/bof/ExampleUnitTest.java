package edu.ucsd.cse110wi22.team6.bof;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public void testPerson2(){
        List<Course> courses = new ArrayList<Course>();
        courses.add(new Course(2019, "SP", "ECE", "65"));
        courses.add(new Course(2018, "FA", "CSE", "30"));
        courses.add(new Course(2017, "WI", "CSE", "21"));
        courses.add(new Course(2016, "FA", "MATH", "171"));
        courses.add(new Course(2016, "FA", "CSE", "20"));

        Person tmp = new Person("Alex", courses, "https://www.example.com/nothing");

        assertEquals(tmp.getCourseList(), courses);
        assertEquals("https://www.example.com/nothing", tmp.getUrl());

        assertEquals("Alex", tmp.getName());
        tmp.setName("Bob");
        assertEquals("Bob", tmp.getName());

        tmp.addCourse(new Course(2019, "FA", "HUM", "1"));
        courses.add(new Course(2019, "FA", "HUM", "1"));
        assertEquals(tmp.getCourseList(), courses);

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

    @Test
    public void testNumCoursesTogether() {
        IPerson p1 = new Person("Rick", Utilities.parseCourseList("2022 WI CSE 110"), "");
        IPerson p2 = new Person("Jim", Utilities.parseCourseList("2022 WI CSE 110,2021 FA CSE 100,2021 FA ECE 65,2020 FA CSE 11"), "");
        assertEquals(1, Utilities.numCoursesTogether(p1,p2));
    }

    @Test
    public void testGetCoursesTogether() {
        IPerson p1 = new Person("Rick", Utilities.parseCourseList("2021 FA CSE 100"), "");
        IPerson p2 = new Person("Jim", Utilities.parseCourseList("2022 WI CSE 110,2021 FA CSE 100,2021 FA ECE 65,2020 FA CSE 11"), "");
        ArrayList<Course> coursesTogether = new ArrayList<Course>();
        coursesTogether.add(new Course(2021, "FA", "CSE", "100"));
        assertArrayEquals(coursesTogether.toArray(), Utilities.getCoursesTogether(p1,p2).toArray());
    }

    @Test
    public void testGetBoFs(){
        IPerson p1 = new Person("Diego", Utilities.parseCourseList("2021 FA CSE 100"), "");
        IPerson p2 = new Person("April", Utilities.parseCourseList("2022 WI CSE 110,2021 FA CSE 100,2021 FA ECE 65,2020 FA CSE 11"), "");
        IPerson p3 = new Person("Harrison", Utilities.parseCourseList("2020 WI HIEU 128,2021 FA ECE 101"), "");
        IPerson p4 = new Person("Josh", Utilities.parseCourseList("2021 FA CSE 100,2020 WI CSE 105"), "");
        List<IPerson> allClassmates = new ArrayList<IPerson>();
        allClassmates.add(p2);
        allClassmates.add(p3);
        allClassmates.add(p4);
        List<IPerson> boFs = new ArrayList<IPerson>();
        boFs.add(p2);
        boFs.add(p4);
        assertArrayEquals(boFs.toArray(), Utilities.getBofList(p1,allClassmates).toArray());

    }

    @Test
    public void testCSVParsing() {
        // Scatter both \r\n (Windows line ending) and \n (Unix line ending)
        String csv = "Bill,,,\r\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,\n" +
                "2021,FA,CSE,210\n" +
                "2022,WI,CSE,110\r\n" +
                "2022,SP,CSE,110\n";
        IPerson person = Utilities.parsePersonFromCSV(csv);
        assertEquals("Bill", person.getName());
        assertEquals(Utilities.parseCourseList("2021 FA CSE 210,2022 WI CSE 110,2022 SP CSE 110"), person.getCourseList());
        assertEquals("https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0",
                person.getUrl());
    }
}