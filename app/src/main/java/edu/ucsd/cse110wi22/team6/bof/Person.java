package edu.ucsd.cse110wi22.team6.bof;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;

// Person Class to represent student objects
public class Person implements IPerson {

    UUID uuid;
    String name;
    List<Course> courseList;
    String photoURL;

    // Constructor left for compatibility
    public Person(String name, List<Course> courseList, String photoURL) {
        this(UUID.randomUUID(), name, courseList, photoURL);
    }

    // Constructor
    public Person(UUID uuid, String name, List<Course> courseList, String photoURL) {
        this.uuid = uuid;
        this.name = name;
        this.courseList = courseList;
        this.photoURL = photoURL;
    }

    // UUID getter
    @Override
    public UUID getUUID() {
        return uuid;
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

    @NonNull
    @Override
    public String toString() {
        return "Person{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", courseList=" + courseList +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }

    public String toJSON(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
