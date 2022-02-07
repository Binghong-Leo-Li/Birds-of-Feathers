package edu.ucsd.cse110wi22.team6.bof;

import android.content.SharedPreferences;

import java.util.List;

public class SharedPreferencesStorage implements IUserInfoStorage {
    private final SharedPreferences prefs;
    // Key names
    private static final String INITIALIZED = "initialized";
    private static final String NAME = "name";
    private static final String COURSES = "courses";
    private static final String PHOTO_URL = "photoUrl";

    public SharedPreferencesStorage(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public void setInitialized(boolean initialized) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(INITIALIZED, initialized);
        editor.apply();
    }

    @Override
    public boolean isInitialized() {
        return prefs.getBoolean(INITIALIZED, false);
    }

    @Override
    public void setName(String name) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NAME, name);
        editor.apply();
    }

    @Override
    public String getName() {
        return prefs.getString(NAME, null);
    }

    @Override
    public void setCourseList(List<Course> courses) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(COURSES, Utilities.encodeCourseList(courses));
        editor.apply();
    }

    @Override
    public List<Course> getCourseList() {
        return Utilities.parseCourseList(prefs.getString(COURSES, ""));
    }

    @Override
    public void setPhotoUrl(String url) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PHOTO_URL, url);
        editor.apply();
    }

    @Override
    public String getPhotoUrl() {
        return prefs.getString(PHOTO_URL, null);
    }
}
