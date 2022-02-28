package edu.ucsd.cse110wi22.team6.bof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Session {
    private final UUID sessionId;
    private String name;
    private final Date startTime;
    private List<IPerson> nearbyStudentList;
    private final List<SessionChangeListener> listeners = new ArrayList<>();

    private Session(UUID sessionId, Date startTime, List<IPerson> nearbyStudentList) {
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.nearbyStudentList = nearbyStudentList;
    }

    public Session(Date startTime) {
        this(UUID.randomUUID(), startTime, Collections.emptyList());
    }

    // TODO: add factory method to instantiate this class from persistent storage object
    // or start one from scratch

    // Make copies to make sure the lists cannot be changed without notifying listeners
    public List<IPerson> getNearbyStudentList() {
        return new ArrayList<>(nearbyStudentList);
    }

    public void setNearbyStudentList(List<IPerson> nearbyStudentList) {
        this.nearbyStudentList = new ArrayList<>(nearbyStudentList);
        notifyListeners();
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setName(String name) {
        this.name = name;
        notifyListeners();
    }

    public void registerListener(SessionChangeListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(SessionChangeListener listener) {
        listeners.remove(listener);
    }

    public String getDisplayName() {
        if (name == null) {
            return startTime.toString(); // TODO: format as something like 1/16/22 5:10PM
        }
        return name;
    }

    private void notifyListeners() {
        for (SessionChangeListener listener : listeners)
            listener.onSessionModified(this);
    }
}
