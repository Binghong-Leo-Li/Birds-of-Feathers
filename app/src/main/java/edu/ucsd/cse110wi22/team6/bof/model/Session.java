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

public class Session implements Identifiable {
    private final UUID sessionId;
    private String name;
    private final Date startTime;
    private final Set<IPerson> nearbyStudentList;
    private final List<SessionChangeListener> listeners = new ArrayList<>();
    private final Gson gson = new Gson();

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
