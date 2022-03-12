package edu.ucsd.cse110wi22.team6.bof.model;

import androidx.annotation.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import edu.ucsd.cse110wi22.team6.bof.Course;
import edu.ucsd.cse110wi22.team6.bof.IPerson;
import edu.ucsd.cse110wi22.team6.bof.IUserInfoStorage;
import edu.ucsd.cse110wi22.team6.bof.Person;
import edu.ucsd.cse110wi22.team6.bof.Utilities;

// A class for storing the persistent state of this app
public class AppStorage implements IUserInfoStorage, SessionChangeListener {
    private final IKeyValueStore kvMapping;
    private final IDMapping<CourseData> courseMap;
    private final IDMapping<IPerson> peopleMap;
    private final IDMapping<Session> sessionMap;

    private final Collection<WaveListener> waveListeners;

    // Key names
    private static final String INITIALIZED = "initialized";
    private static final String NAME = "name";
    private static final String COURSES = "courses";
    private static final String PHOTO_URL = "photoUrl";
    private static final String USER_UUID = "userUUID";
    private static final String SESSION_LIST = "sessionList"; // As a list of UUIDs
    private static final String FAVORITE_LIST = "favoriteList"; // As a list of person UUIDs
    private static final String WAVE_TO_LIST = "waveToList"; // List of students the user is waving to
    private static final String WAVE_FROM_LIST = "waveFromList"; // List of students the user received wave from

    public AppStorage(IKeyValueStore kvMapping) {
        this.kvMapping = kvMapping;
        this.courseMap = new IDMapping<>(kvMapping, "course", new CourseDataFactory());
        this.peopleMap = new IDMapping<>(kvMapping, "person", new IPersonFactory());
        this.sessionMap = new IDMapping<>(kvMapping, "session", new Session.Factory(this::getPersonFromID));
        this.waveListeners = new HashSet<>();
    }

    public void addToFavorites(IPerson person) {
        assert !isFavorited(person);
        mutateSet(favorites -> favorites.add(person.getStringID()), FAVORITE_LIST);
    }

    public void removeFromFavorites(IPerson person) {
        assert isFavorited(person);
        mutateSet(favorites -> favorites.remove(person.getStringID()), FAVORITE_LIST);
    }

    public boolean isFavorited(IPerson person) {
        return getStringSet(FAVORITE_LIST).contains(person.getStringID());
    }

    public boolean isWavingToUser(IPerson person) {
        return getStringSet(WAVE_FROM_LIST).contains(person.getStringID());
    }

    public void registerCourse(Course course, CourseSize size) {
        courseMap.registerObject(new CourseData(course, size));
    }

    public CourseSize getCourseSize(Course course) {
        return courseMap.getObjectByID(course.toString()).getSize();
    }

    public void registerPerson(IPerson person) {
        peopleMap.registerObject(person);
    }

    public IPerson getPersonFromID(UUID uuid) {
        return peopleMap.getObjectByID(uuid.toString());
    }

    // Check for name collisions if the name is not null
    // If the name is null, return false (no collision)
    // Otherwise, return true if there the name of this session collides with
    // The name of an existing session whose name is not null

    public boolean isSessionNameTaken(String name) {
        assert name != null;
        return getSessionList().stream().anyMatch(session -> name.equals(session.getName()));
    }

    // Notify AppStorage of the existence of a NEW session
    public void registerNewSession(Session session) {
        mutateSet(sessions -> sessions.add(session.getStringID()), SESSION_LIST);
        sessionMap.registerObject(session);
    }

    public List<Session> getSessionList() {
        return getListAsObjects(SESSION_LIST, sessionMap);
    }

    public List<IPerson> getFavoriteList() {
        return getListAsObjects(FAVORITE_LIST, peopleMap);
    }

    public void waveTo(IPerson person) {
        mutateSet(waveList -> waveList.add(person.getStringID()), WAVE_TO_LIST);
    }

    public List<IPerson> getWaveToList() {
        return getListAsObjects(WAVE_TO_LIST, peopleMap);
    }

    public void waveFrom(IPerson person) {
        mutateSet(waveList -> waveList.add(person.getStringID()), WAVE_FROM_LIST);
        notifyListeners();
    }

//    public List<IPerson> getWaveFromList() {
//        return getListAsObjects(WAVE_FROM_LIST, peopleMap);
//    }

    // Register observers
    public void registerWaveListener(WaveListener listener) {
        waveListeners.add(listener);
    }

    // Unregister observers
    public void unregisterWaveListener(WaveListener listener) {
        boolean removed = waveListeners.remove(listener);
        // Make sure the listener existed before, Java's not SRP so let's deal with it
        assert removed;
    }

    // Notify all the observers
    private void notifyListeners() {
        for (WaveListener listener : waveListeners)
            listener.onWaveInformationChanged();
    }

    @NonNull
    private <T extends Identifiable> List<T> getListAsObjects(String listKeyName, IDMapping<T> mapping) {
        return getStringSet(listKeyName)
                .stream()
                .map(mapping::getObjectByID)
                .collect(Collectors.toList());
    }

    // call on change state, insert ...
    private void mutateSet(Consumer<Set<String>> mutator, String setKey) {
        Set<String> s = new HashSet<>(getStringSet(setKey));
        mutator.accept(s);
        kvMapping.putStringSet(setKey, s);
    }

    private Set<String> getStringSet(String setKey) {
        return kvMapping.getStringSet(setKey, Collections.emptySet());
    }

    @Override
    public void onSessionModified(Session session) {
        // Update (Re-register) existing session in the map to auto-save
        sessionMap.registerObject(session);
    }

    private String getUserUUIDString() {
        return kvMapping.getString(USER_UUID, null);
    }

    // Lazy creation of user UUID
    private void ensureUserHasUUID() {
        if (getUserUUIDString() == null) {
            setUserUUID(UUID.randomUUID());
        }
    }

    public void setUserUUID(UUID uuid) {
        kvMapping.putString(USER_UUID, uuid.toString());
    }

    public IPerson getUser() {
        assert isInitialized();
        ensureUserHasUUID();
        String uuidString = getUserUUIDString();
        assert uuidString != null;
        IPerson user = new Person(UUID.fromString(uuidString), getName(), getCourseList(), getPhotoUrl());
        registerPerson(user);
        return user;
    }

    @Override
    public void setInitialized(boolean initialized) {
        kvMapping.putBoolean(INITIALIZED, initialized);
    }

    @Override
    public boolean isInitialized() {
        return kvMapping.getBoolean(INITIALIZED, false);
    }

    @Override
    public void setName(String name) {
        kvMapping.putString(NAME, name);
    }

    @Override
    public String getName() {
        return kvMapping.getString(NAME, null);
    }

    @Override
    public void setCourseList(List<Course> courses) {
        kvMapping.putString(COURSES, Utilities.encodeCourseList(courses));
    }

    @Override
    public List<Course> getCourseList() {
        return Utilities.parseCourseList(kvMapping.getString(COURSES, ""));
    }

    @Override
    public void setPhotoUrl(String url) {
        kvMapping.putString(PHOTO_URL, url);
    }

    @Override
    public String getPhotoUrl() {
        return kvMapping.getString(PHOTO_URL, null);
    }
}
