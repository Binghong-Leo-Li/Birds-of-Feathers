package edu.ucsd.cse110wi22.team6.bof;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

// Person Class to represent student objects
public class Person implements IPerson {

    String name;
    List<Course> courseList;
    String photoURL;

    // Constructor
    public Person(String name, List<Course> courseList, String photoURL) {
        this.name = name;
        this.courseList = courseList;
        this.photoURL = photoURL;
    }

    // Name getter
    @Override
    public String getName() {
        return name;
    }

    // Course List getter
    @Override
    public List<Course> getCourseList() {
        return courseList;
    }

    // Url getter
    @Override
    public String getUrl() {
        return this.photoURL;
    }

    // Course adder
    @Override
    public void addCourse(Course course) {
        courseList.add(course);
    }

    // Name setter
    @Override
    public void setName(String name) {
        this.name = name;
    }

    // Conversion to string
    @NonNull
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", courseList=" + courseList +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
