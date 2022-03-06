package edu.ucsd.cse110wi22.team6.bof.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import edu.ucsd.cse110wi22.team6.bof.IPerson;

// A session stores a list of nearby students seen via Bluetooth in a
// session/meeting such as a class
public class Session implements Identifiable {
    private final UUID sessionId;
    private String name;
    private final Date startTime;
    private final Set<IPerson> nearbyStudentList;
    private final List<SessionChangeListener> listeners = new ArrayList<>();
    private final Gson gson = new Gson();

    // Custom session with custom set, use only for testing
    public Session(UUID sessionId, Date startTime, Set<IPerson> nearbyStudentList) {
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.nearbyStudentList = nearbyStudentList;
    }

    // Create a new session at the specified time
    public Session(Date startTime) {
        this(UUID.randomUUID(), startTime, new HashSet<>());
    }

    // Create a new session with certain UUID at the specified time
    public Session(UUID sessionId, Date startTime) {
        this(sessionId, startTime, new HashSet<>());
    }

    // TODO: add factory method to instantiate this class from persistent storage object
    // or start one from scratch

    // Add a nearby student found to this session
    public void addNearbyStudent(IPerson student) {
        if (nearbyStudentList.add(student))
            notifyListeners();
    }

    public void removeNearbyStudent(IPerson student) {
        if (nearbyStudentList.remove(student))
            notifyListeners();
    }

    // Get the current list of ALL nearby students, including those not sharing no classes in common
    public List<IPerson> getNearbyStudentList() {
        return new ArrayList<>(nearbyStudentList);
    }

    // Get the unique identifier (UUID) associated with this session)
    public UUID getSessionId() {
        return sessionId;
    }

    // Get the name used to identify this session
    public String getName() {
        return name;
    }

    // Get the time the session starts, used to compute display name appearance
    public Date getStartTime() {
        return startTime;
    }

    public void setName(String name) {
        this.name = name;
        notifyListeners();
    }

    // Register observers
    public void registerListener(SessionChangeListener listener) {
        listeners.add(listener);
    }

    // Unregister observers
    public void unregisterListener(SessionChangeListener listener) {
        listeners.remove(listener);
    }

    // The name shown in the dropdown selection of sessions, either name or timestamp if
    // there is no name
    public String getDisplayName() {
        if (name == null) {
            return startTime.toString(); // TODO: format as something like 1/16/22 5:10PM
        }
        return name;
    }

    // Notify all the observers
    private void notifyListeners() {
        for (SessionChangeListener listener : listeners)
            listener.onSessionModified(this);
    }

    @Override
    public String getStringID() {
        return getSessionId().toString();
    }

    @Override
    public String serializeToString() {
        List<UUID> uuidList = nearbyStudentList.stream()
                .map(IPerson::getUUID)
                .collect(Collectors.toList());
        return gson.toJson(new SessionRecord(name, startTime, uuidList));
    }

    // Convenient object to allow serializing Session to/from JSON
    private static class SessionRecord {
        final String name;
        final Date startTime;
        final List<UUID> uuidList;

        SessionRecord(String name, Date startTime, List<UUID> uuidList) {
            this.name = name;
            this.startTime = startTime;
            this.uuidList = uuidList;
        }
    }

    // Allow deserializing JSON to session
    public static class Factory implements IdentifiableFactory<Session> {
        private final Function<UUID, IPerson> idToPersonMap;
        private static final Gson gson = new Gson();

        public Factory(Function<UUID, IPerson> idToPersonMap) {
            this.idToPersonMap = idToPersonMap;
        }

        @Override
        public Session deserialize(String id, String serializedData) {
            SessionRecord record = gson.fromJson(serializedData, SessionRecord.class);
            return new Session(UUID.fromString(id),
                    record.startTime,
                    record.uuidList.stream().map(idToPersonMap).collect(Collectors.toSet()));
        }
    }
}
