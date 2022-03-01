package edu.ucsd.cse110wi22.team6.bof.model;

import edu.ucsd.cse110wi22.team6.bof.Utilities;

public class CourseDataFactory implements IdentifiableFactory<CourseData> {
    @Override
    public CourseData deserialize(String id, String serializedData) {
        return new CourseData(Utilities.parseCourse(id), CourseSize.valueOf(serializedData));
    }
}
