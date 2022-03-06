package edu.ucsd.cse110wi22.team6.bof.model;

// Listener that can get notified when a Session object changes
public interface SessionChangeListener {
    void onSessionModified(Session session);
}
