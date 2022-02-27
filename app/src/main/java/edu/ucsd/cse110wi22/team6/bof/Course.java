package edu.ucsd.cse110wi22.team6.bof;


import androidx.annotation.NonNull;

import java.util.Objects;

// Object to store courses
public class Course {
    int year;
    String quarter;
    String subject;
    String courseNumber;

    // Constructor
    Course(int year, String quarter, String subject, String courseNumber) {
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.courseNumber = courseNumber;
    }

    // getter
    int getYear() {
        return year;
    }

    // getter
    String getQuarter() {
        return quarter;
    }

    // getter
    String getSubject() {
        return subject;
    }

    // getter
    String getCourseNumber() {
        return courseNumber;
    }

    // Conversion to string
    @NonNull
    @Override
    public String toString() {
        return year + " " + quarter + " " + subject + " " + courseNumber;
    }

    // Checking two courses are the same
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return year == course.year && quarter.equals(course.quarter)
                && subject.equals(course.subject) && courseNumber.equals(course.courseNumber);
    }

    // Hashing the course objects
    @Override
    public int hashCode() {
        return Objects.hash(year, quarter, subject, courseNumber);
    }
}
