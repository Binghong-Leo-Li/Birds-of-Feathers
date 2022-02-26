package edu.ucsd.cse110wi22.team6.bof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Class for representing individual object storage
public class InMemoryStorage implements IUserInfoStorage {
    private boolean initialized;
    private String name;
    private List<Course> courseList = Collections.emptyList();
    private String photoUrl;

    private static final InMemoryStorage INSTANCE = new InMemoryStorage();

    // getter
    public static InMemoryStorage getInstance() {
        return INSTANCE;
    }

    // setter
    @Override
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    // getter
    @Override
    public boolean isInitialized() {
        return initialized;
    }

    // setter
    @Override
    public void setName(String name) {
        this.name = name;
    }

    // getter
    @Override
    public String getName() {
        return name;
    }

    // setter
    @Override
    public void setCourseList(List<Course> courses) {
        courseList = new ArrayList<>(courses);
    }

    // getter
    @Override
    public List<Course> getCourseList() {
        return new ArrayList<>(courseList);
    }

    // setter
    @Override
    public void setPhotoUrl(String url) {
        photoUrl = url;
    }

    // getter
    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }
}
