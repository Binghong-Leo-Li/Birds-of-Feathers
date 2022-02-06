package edu.ucsd.cse110wi22.team6.bof;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Utilities {
    // TODO: write unit tests for this
    public static List<Course> parseCourseList(String courseList) {
        if (courseList.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(courseList.split(",")).map(Utilities::parseCourse)
                .collect(Collectors.toList());
    }

    // No input validation is performed
    public static Course parseCourse(String course) {
        String[] fields = course.split(" ");
        return new Course(
                Integer.parseInt(fields[0]),
                fields[1],
                fields[2],
                fields[3]
        );
    }

    // TODO: do the inverse: construct string from course list

    public static int numCoursesTogether(IPerson a, IPerson b) {
        Set<Course> aCourseList = new HashSet<>(a.getCourseList());
        aCourseList.retainAll(new HashSet<>(b.getCourseList()));
        return aCourseList.size();
    }
}