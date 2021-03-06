package edu.ucsd.cse110wi22.team6.bof.model;

import java.util.Set;

// An abstract interface for storage based on key-to-value mapping, whether persistent or not
// The interface is very similar (and supposed to simulate) SharedPreferences
// except that the put*() methods commit changes directly rather than going through
// an Editor
public interface IKeyValueStore {
    // See SharedPreferences documentation for the meaning of these methods
    boolean getBoolean(String key, boolean defaultValue);
    String getString(String key, String defaultValue);
    Set<String> getStringSet(String key, Set<String> defaultValues);
    void putBoolean(String key, boolean value);
    void putString(String key, String value);
    void putStringSet(String key, Set<String> values);
}
