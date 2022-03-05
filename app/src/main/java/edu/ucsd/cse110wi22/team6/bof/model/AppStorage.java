package edu.ucsd.cse110wi22.team6.bof.model;

import java.util.List;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.Course;
import edu.ucsd.cse110wi22.team6.bof.IPerson;
import edu.ucsd.cse110wi22.team6.bof.IUserInfoStorage;
import edu.ucsd.cse110wi22.team6.bof.Person;
import edu.ucsd.cse110wi22.team6.bof.Utilities;

public class AppStorage implements IUserInfoStorage {
    private final IKeyValueStore kvMapping;
    private final IDMapping<CourseData> courseMap;
    private final IDMapping<IPerson> peopleMap;
    // Key names
    private static final String INITIALIZED = "initialized";
    // TODO: deprecate the below in favor of user being its own UUID
    private static final String NAME = "name";
    private static final String COURSES = "courses";
    private static final String PHOTO_URL = "photoUrl";
    private static final String USER_UUID = "userUUID";

    public AppStorage(IKeyValueStore kvMapping) {
        this.kvMapping = kvMapping;
        this.courseMap = new IDMapping<>(kvMapping, "course", new CourseDataFactory());
        this.peopleMap = new IDMapping<>(kvMapping, "person", new IPersonFactory());
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

    public IPerson getPersonFromID(String uuidString) {
        return peopleMap.getObjectByID(uuidString);
    }

    private String getUserUUIDString() {
        return kvMapping.getString(USER_UUID, null);
    }

    // Lazy creation of user UUID
    private void ensureUserHasUUID() {
        if (getUserUUIDString() == null) {
            kvMapping.putString(USER_UUID, UUID.randomUUID().toString());
        }
    }

    public IPerson getUser() {
        assert isInitialized();
        ensureUserHasUUID();
        String uuidString = getUserUUIDString();
        assert uuidString != null;
        return new Person(UUID.fromString(uuidString), getName(), getCourseList(), getPhotoUrl());
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
