package edu.ucsd.cse110wi22.team6.bof;

import android.content.SharedPreferences;

import java.util.List;

// Implementing Shared Preferences as the local storage
public class SharedPreferencesStorage implements IUserInfoStorage {
    private final SharedPreferences prefs;
    // Key names
    private static final String INITIALIZED = "initialized";
    private static final String NAME = "name";
    private static final String COURSES = "courses";
    private static final String PHOTO_URL = "photoUrl";

    // Constructor
    public SharedPreferencesStorage(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    // Initialization
    @Override
    public void setInitialized(boolean initialized) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(INITIALIZED, initialized);
        editor.apply();
    }

    // Check if initialized
    @Override
    public boolean isInitialized() {
        return prefs.getBoolean(INITIALIZED, false);
    }

    // Name setter
    @Override
    public void setName(String name) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    // Name getter
    @Override
    public String getName() {
        return prefs.getString(NAME, null);
    }

    // Course List setter
    @Override
    public void setCourseList(List<Course> courses) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(COURSES, Utilities.encodeCourseList(courses));
        editor.apply();
    }

    // Course List getter
    @Override
    public List<Course> getCourseList() {
        return Utilities.parseCourseList(prefs.getString(COURSES, ""));
    }

    // Photo Url setter
    @Override
    public void setPhotoUrl(String url) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PHOTO_URL, url);
        editor.apply();
    }

    // Photo Url getter
    @Override
    public String getPhotoUrl() {
        return prefs.getString(PHOTO_URL, null);
    }
}
