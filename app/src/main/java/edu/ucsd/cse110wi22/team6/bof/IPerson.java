package edu.ucsd.cse110wi22.team6.bof;

import java.util.List;

public interface IPerson {
    String getName();
    List<Course> getCourseList();
    String getUrl();
    void addCourse(Course course);
    void setName(String name);
}
