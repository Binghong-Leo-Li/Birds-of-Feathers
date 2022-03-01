package edu.ucsd.cse110wi22.team6.bof.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.IPerson;

public class Session {
    private final UUID sessionId;
    private String name;
    private final Date startTime;
    private final Set<IPerson> nearbyStudentList;
    private final List<SessionChangeListener> listeners = new ArrayList<>();

    private Session(UUID sessionId, Date startTime, Set<IPerson> nearbyStudentList) {
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.nearbyStudentList = nearbyStudentList;
    }

    public Session(Date startTime) {
        this(UUID.randomUUID(), startTime, new HashSet<>());
    }

    // TODO: add factory method to instantiate this class from persistent storage object
    // or start one from scratch

    public void addNearbyStudent(IPerson student) {
        if (nearbyStudentList.add(student))
            notifyListeners();
    }

    public void removeNearbyStudent(IPerson student) {
        if (nearbyStudentList.remove(student))
            notifyListeners();
    }

    public List<IPerson> getNearbyStudentList() {
        return new ArrayList<>(nearbyStudentList);
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
