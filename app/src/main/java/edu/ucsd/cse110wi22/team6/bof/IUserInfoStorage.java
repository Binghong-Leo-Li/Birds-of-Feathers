package edu.ucsd.cse110wi22.team6.bof;

import java.util.List;

// Interface for all types of storages that may be used
public interface IUserInfoStorage {
    void setInitialized(boolean initialized);
    boolean isInitialized();
    void setName(String name);
    String getName();
    void setCourseList(List<Course> courses);
    List<Course> getCourseList();
    void setPhotoUrl(String url);
    String getPhotoUrl();
}
