package edu.ucsd.cse110wi22.team6.bof;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.function.Function;

// Testing SizeComparator Class
@RunWith(AndroidJUnit4.class)
public class TestSizeComparator {

    private IPerson user;
    private String url;
    private Function<Course, CourseSize> f;
    private SizeComparator sc;

    @Before
    public void setup() {
        List<Course> userCourses = Utilities
                .parseCourseList("2022 WI CSE 110," +
                        "2021 FA CSE 105," +
                        "2020 FA CSE 103," +
                        "2019 FA MATH 31AH," +
                        "2018 FA MATH 31BH," +
                        "2017 FA MATH 31CH");

        url = "https://avatars.githubusercontent.com/u/32375681?v";
        user = new Person("Leo", userCourses,url); // mocked user

        // f to mock the real mapping from course to course size
        f = c -> {
            switch(c.getYear()) {
                case 2022: return CourseSize.TINY;
                case 2021: return CourseSize.SMALL;
                case 2020: return CourseSize.MEDIUM;
                case 2019: return CourseSize.LARGE;
                case 2018: return CourseSize.HUGE;
                case 2017: return CourseSize.GIGANTIC;
            }
            return null;
        };

        sc = new SizeComparator(user, f);
    }


    @Test
    public void testCalculateScore() {
        Course a1 = new Course(2022, "WI", "CSE", "110");
        Course a2 = new Course(2021, "FA", "CSE", "105");
        Course a3 = new Course(2020, "FA", "CSE", "103");
        Course a4 = new Course(2019, "FA", "MATH", "31AH");
        Course a5 = new Course(2018, "FA", "MATH", "31BH");
        Course a6 = new Course(2017, "FA", "MATH", "31CH");

        Assert.assertEquals(1.00, sc.calculateCourseScore(a1), 0.001);
        Assert.assertEquals(0.33, sc.calculateCourseScore(a2), 0.001);
        Assert.assertEquals(0.18, sc.calculateCourseScore(a3), 0.001);
        Assert.assertEquals(0.10, sc.calculateCourseScore(a4), 0.001);
        Assert.assertEquals(0.06, sc.calculateCourseScore(a5), 0.001);
        Assert.assertEquals(0.03, sc.calculateCourseScore(a6), 0.001);

    }

    @Test
    public void  testCalculatePersonScore() {
        List<Course> clA = Utilities //course list A, all same
                .parseCourseList("2022 WI CSE 110," +
                        "2021 FA CSE 105," +
                        "2020 FA CSE 103," +
                        "2019 FA MATH 31AH," +
                        "2018 FA MATH 31BH," +
                        "2017 FA MATH 31CH");
        IPerson a = new Person("Aike", clA, url); // all same
        Assert.assertEquals(1.7, sc.calculatePersonScore(a), 0.001);

        List<Course> clB = Utilities //course list B, first two same
                .parseCourseList("2022 WI CSE 110," +
                        "2021 FA CSE 105," +
                        "1011 FA CSE 103," +
                        "1234 FA MATH 31AH," +
                        "1493 FA MATH 31BH," +
                        "1 FA MATH 31CH");
        IPerson b = new Person("Bike", clB, url);
        Assert.assertEquals(1.33, sc.calculatePersonScore(b), 0.001); // only first two same
    }

    @Test
    public void  testCompare() {
        List<Course> clA = Utilities //course list A
                .parseCourseList("2022 WI CSE 110," +
                        "2021 FA CSE 105," +
                        "2020 FA CSE 103," +
                        "2019 FA MATH 31AH," +
                        "2018 FA MATH 31BH," +
                        "2017 FA MATH 31CH");

        IPerson a = new Person("Aike", clA, url); // all same
        IPerson c = new Person("Cike", clA, url); // duplicate to test equality

        List<Course> clB = Utilities //course list B, only frist two same
                .parseCourseList("2022 WI CSE 110," +
                        "2021 FA CSE 105," +
                        "1011 FA CSE 103," +
                        "1234 FA MATH 31AH," +
                        "1493 FA MATH 31BH," +
                        "1 FA MATH 31CH");
        IPerson b = new Person("Bike", clB, url);

        Assert.assertEquals(1, sc.compare(a,b));
        Assert.assertEquals(-1, sc.compare(b,a));
        Assert.assertEquals(0, sc.compare(c,a));
        Assert.assertEquals(0, sc.compare(a,c));
    }
}
