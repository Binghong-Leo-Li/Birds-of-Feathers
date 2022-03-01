package edu.ucsd.cse110wi22.team6.bof.model;

import android.content.SharedPreferences;

import java.util.Set;

// Adapter from SharedPreferences to IKeyValueStore
public class SharedPreferencesMapping implements IKeyValueStore {
    private final SharedPreferences prefs;

    // Constructor
    public SharedPreferencesMapping(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        return prefs.getStringSet(key, defaultValues);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    @Override
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void putStringSet(String key, Set<String> values) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(key, values);
        editor.apply();
    }
}
