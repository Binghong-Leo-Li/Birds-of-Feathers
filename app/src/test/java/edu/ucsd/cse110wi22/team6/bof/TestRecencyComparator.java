package edu.ucsd.cse110wi22.team6.bof;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TestRecencyComparator {

    private IPerson user;
    private String currentQuarter;
    private int currentYear;
    private RecencyComparator rc;
    private String url;

    @Before
    public void setup() {
        List<Course> userCourses = Utilities
                .parseCourseList("2022 WI CSE 110," +
                        "2021 FA CSE 105," +
                        "2021 SP CSE 30," +
                        "2021 WI CSE 15L," +
                        "2021 SS1 HUM 1," +
                        "2021 SS2 HUM 2," +
                        "2019 FA MATH 31AH");

        url = "https://avatars.githubusercontent.com/u/32375681?v";
        user = new Person("Leo", userCourses,url);
        currentQuarter = "WI";
        currentYear = 2022;

        rc = new RecencyComparator(user, currentYear, currentQuarter);
    }

    @Test
    public void testGetRemember() {
        Assert.assertEquals(5 ,rc.getRemember(-1));
        Assert.assertEquals(5 ,rc.getRemember(0));
        Assert.assertEquals(4 ,rc.getRemember(1));
        Assert.assertEquals(3 ,rc.getRemember(2));
        Assert.assertEquals(2 ,rc.getRemember(3));
        Assert.assertEquals(1 ,rc.getRemember(4));
        Assert.assertEquals(1 ,rc.getRemember(5));
    }

    @Test
    public void testQuarterToInt() {
        Assert.assertEquals(1 ,rc.quarterToInt("WI"));
        Assert.assertEquals(2 ,rc.quarterToInt("SP"));
        Assert.assertEquals(3 ,rc.quarterToInt("SS1"));
        Assert.assertEquals(3 ,rc.quarterToInt("SS2"));
        Assert.assertEquals(4 ,rc.quarterToInt("FA"));
    }

    @Test
    public void testCalculateAge() {
        Assert.assertEquals(0 ,rc.calculateAge(new Course(
                2022, "WI", "CSE", "110"
        )));
        Assert.assertEquals(0 ,rc.calculateAge(new Course(
                2021, "FA", "CSE", "105"
        )));
        Assert.assertEquals(1 ,rc.calculateAge(new Course(
                2021, "SS2", "HUM", "2"
        )));
        Assert.assertEquals(1 ,rc.calculateAge(new Course(
                2021, "SS1", "HUM", "1"
        )));
        Assert.assertEquals(2 ,rc.calculateAge(new Course(
                2021, "SP", "CSE", "30"
        )));
        Assert.assertEquals(3 ,rc.calculateAge(new Course(
                2021, "WI", "CSE", "15L"
        )));
    }

    @Test
    public void testCalculateScore() {
        List<Course> clA = Utilities.parseCourseList(
                "2021 FA CSE 105," +
                "2021 WI CSE 15L," +
                "2022 SS2 HUM 2"
        );
        IPerson a = new Person("Aike", clA, url);

        List<Course> clB = Utilities.parseCourseList(
                "2023 FA CSE 105," +
                "2312 WI CSE 15L," +
                "1999 SS2 HUM 2"
        );
        IPerson b = new Person("Aite", clB, url);

        Assert.assertEquals(7, rc.calculateScore(a));
        Assert.assertEquals(0, rc.calculateScore(b));
    }

    @Test
    public void testCompare() {
        List<Course> clA = Utilities.parseCourseList(
                "2021 FA CSE 105," +
                "2021 WI CSE 15L," +
                "2022 SS2 HUM 2"
        );
        IPerson a = new Person("Aike", clA, url);
        IPerson c = new Person("Bike", clA, url);

        List<Course> clB = Utilities.parseCourseList(
                "2023 FA CSE 105," +
                "2312 WI CSE 15L," +
                "1999 SS2 HUM 2"
        );
        IPerson b = new Person("Aite", clB, url);

        Assert.assertEquals(1, rc.compare(a,b));
        Assert.assertEquals(-1, rc.compare(b,a));
        Assert.assertEquals(0, rc.compare(c,a));
        Assert.assertEquals(0, rc.compare(a,c));
    }

    @Test
    public void testScoreDetailed() {
        List<Course> clA = Utilities
                .parseCourseList("2022 WI CSE 110," +
                        "2021 FA CSE 105," +
                        "2021 SP CSE 30," +
                        "2021 WI CSE 15L," +
                        "2021 SS1 HUM 1," +
                        "2021 SS2 HUM 2," +
                        "2019 FA MATH 31AH");

        IPerson a = new Person("Aike", clA, url);

        Assert.assertEquals(24, rc.calculateScore(a));


    }

}
