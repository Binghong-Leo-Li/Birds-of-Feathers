package edu.ucsd.cse110wi22.team6.bof.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// Adapter from Map to IKeyValueStore
public class InMemoryMapping implements IKeyValueStore {
    private final Map<String, Object> map;  // Mapping the String id to its Object
    private static final InMemoryMapping INSTANCE = new InMemoryMapping(new HashMap<>());

    // In memory mapping singleton to unify the data seen from different clients
    public static InMemoryMapping getInstance() {
        return INSTANCE;
    }

    // Use a custom map, note that using this constructor is unsafe
    // This creates a NEW instance rather than sharing a single instance which cause
    // different clients to see different results
    // Only use this method for unit testing, never use this in the app
    public InMemoryMapping(Map<String, Object> map) {
        this.map = map;
    }

    private <T> T getValue(String key, Class<T> type, T defValue) {
        Object value = map.get(key);
        if (value == null) {
            return defValue;
        }
        return type.cast(value);
    }

    private void putValue(String key, Object value) {
        map.put(key, value); // Updating the value of certain String id
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return getValue(key, Boolean.class, defaultValue);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return getValue(key, String.class, defaultValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        return (Set<String>) getValue(key, Set.class, defaultValues);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        putValue(key, value); // Updating the boolean value of certain String id
    }

    @Override
    public void putString(String key, String value) {
        putValue(key, value);  // Updating the String value of certain String id
    }

    @Override
    public void putStringSet(String key, Set<String> values) {
        putValue(key, values);  // Updating the String set value of certain String id
    }
}
