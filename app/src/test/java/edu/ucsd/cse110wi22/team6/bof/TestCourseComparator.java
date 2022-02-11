package edu.ucsd.cse110wi22.team6.bof;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(AndroidJUnit4.class)
public class TestCourseComparator {
    private Comparator<Course> courseComparator;
    // Need Robolectric since using Android resources

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();

        courseComparator = new CourseComparator(Arrays.asList
                (context.getResources().getStringArray(R.array.quarter_list)));
    }

    @Test
    public void testSortCourses() {
        List<Course> courses = Utilities
                .parseCourseList("2021 FA CSE 15L,2021 WI MATH 100A,2020 SS1 HUM 1,2020 FA MATH 31AH");
        assertEquals(Utilities
                .parseCourseList("2020 SS1 HUM 1,2020 FA MATH 31AH,2021 WI MATH 100A,2021 FA CSE 15L"),
                courses.stream().sorted(courseComparator).collect(Collectors.toList()));
    }
}

