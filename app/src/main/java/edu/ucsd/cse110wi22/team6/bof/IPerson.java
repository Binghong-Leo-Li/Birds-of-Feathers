package edu.ucsd.cse110wi22.team6.bof;

import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.Identifiable;

// Interface for all types of person may be used
public interface IPerson extends Identifiable {
    static IPerson deserialize(String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, Person.class);
    }

    String getName();
    List<Course> getCourseList();
    String getUrl();
    void addCourse(Course course);
    void setName(String name);
    String serializeToString();
    UUID getUUID();

    @Override
    default String getStringID() {
        return getUUID().toString();
    }
}
