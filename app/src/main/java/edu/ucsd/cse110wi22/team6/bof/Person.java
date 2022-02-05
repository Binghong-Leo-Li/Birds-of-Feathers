package edu.ucsd.cse110wi22.team6.bof;

import java.util.List;

public class Person implements IPerson {

    String name;
    List<Course> courseList;
    String photoURL;

    public Person(String name, List<Course> courseList, String photoURL) {
        this.name = name;
        this.courseList = courseList;
        this.photoURL = photoURL;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Course> getCourseList() {
        return courseList;
    }

    @Override
    public String getUrl() {
        return this.photoURL;
    }

    @Override
    public void addCourse(Course course) {
        courseList.add(course);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
