package edu.ucsd.cse110wi22.team6.bof;


import androidx.annotation.NonNull;

import java.util.Objects;

public class Course {
    int year;
    String quarter;
    String subject;
    String courseNumber;

    Course(int year, String quarter, String subject, String courseNumber) {
        this.year = year;
        this.quarter = quarter;
        this.subject = subject;
        this.courseNumber = courseNumber;
    }

    int getYear() {
        return year;
    }

    String getQuarter() {
        return quarter;
    }

    String getSubject() {
        return subject;
    }

    String getCourseNumber() {
        return courseNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return year + " " + quarter + " " + subject + " " + courseNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return year == course.year && quarter.equals(course.quarter) && subject.equals(course.subject) && courseNumber.equals(course.courseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, quarter, subject, courseNumber);
    }
}
