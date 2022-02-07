package edu.ucsd.cse110wi22.team6.bof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryStorage implements IUserInfoStorage {
    private boolean initialized;
    private String name;
    private List<Course> courseList = Collections.emptyList();
    private String photoUrl;

    private static final InMemoryStorage INSTANCE = new InMemoryStorage();

    public static InMemoryStorage getInstance() {
        return INSTANCE;
    }

    @Override
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setCourseList(List<Course> courses) {
        courseList = new ArrayList<>(courses);
    }

    @Override
    public List<Course> getCourseList() {
        return new ArrayList<>(courseList);
    }

    @Override
    public void setPhotoUrl(String url) {
        photoUrl = url;
    }

    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }
}
