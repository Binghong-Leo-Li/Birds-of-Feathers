package edu.ucsd.cse110wi22.team6.bof.model;

import java.util.List;

import edu.ucsd.cse110wi22.team6.bof.Course;
import edu.ucsd.cse110wi22.team6.bof.IUserInfoStorage;
import edu.ucsd.cse110wi22.team6.bof.Utilities;

public class AppStorage implements IUserInfoStorage {
    IKeyValueStore kvMapping;
    private IDMapping<CourseData> courseMap;
    // Key names
    private static final String INITIALIZED = "initialized";
    // TODO: deprecate the below in favor of user being its own UUID
    private static final String NAME = "name";
    private static final String COURSES = "courses";
    private static final String PHOTO_URL = "photoUrl";

    public AppStorage(IKeyValueStore kvMapping) {
        this.kvMapping = kvMapping;
    }

    public void registerCourse(Course course, CourseSize size) {
        courseMap.registerObject(new CourseData(course, size));
    }

    public CourseSize getCourseSize(Course course) {
        return courseMap.getObjectByID(course.toString()).getSize();
    }

    @Override
    public void setInitialized(boolean initialized) {
        kvMapping.putBoolean(INITIALIZED, true);
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
