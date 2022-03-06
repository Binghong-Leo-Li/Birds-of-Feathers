package edu.ucsd.cse110wi22.team6.bof.model;

// An interface for objects that can be mapped by IDMapping
public interface Identifiable {
    // Need an ID
    String getStringID();
    // Need a way to serialize it to a string so it can be stored in IKeyValueStore
    String serializeToString();
}
