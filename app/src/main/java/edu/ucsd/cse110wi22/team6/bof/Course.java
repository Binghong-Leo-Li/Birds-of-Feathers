package edu.ucsd.cse110wi22.team6.bof;



public class Course {
    String year;
    String quarter;
    String courseName;
    String courseNumber;

    Course(String year, String quarter, String courseName, String courseNumber) {
        this.year = year;
        this.quarter = quarter;
        this.courseName = courseName;
        this.courseNumber = courseNumber;
    }

    String getYear() {
        return year;
    }

    String getQuarter() {
        return quarter;
    }

    String getCourseName() {
        return courseName;
    }

    String getCourseNumber() {
        return courseNumber;
    }

}
