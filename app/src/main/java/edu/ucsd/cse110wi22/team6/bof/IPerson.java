package edu.ucsd.cse110wi22.team6.bof;

import java.util.List;

// Interface for all types of person may be used
public interface IPerson {
    String getName();
    List<Course> getCourseList();
    String getUrl();
    void addCourse(Course course);
    void setName(String name);
    String toJSON();
}
