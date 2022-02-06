package edu.ucsd.cse110wi22.team6.bof;



public class Course {
    int year;
    String quarter;
    String subject;
    int courseNumber;

    Course(int year, String quarter, String subject, int courseNumber) {
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

    int getCourseNumber() {
        return courseNumber;
    }

}
