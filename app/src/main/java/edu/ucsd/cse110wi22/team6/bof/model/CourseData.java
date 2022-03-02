package edu.ucsd.cse110wi22.team6.bof.model;

import edu.ucsd.cse110wi22.team6.bof.Course;

public class CourseData implements Identifiable {
    private final Course identifier;
    private final CourseSize size;

    public CourseData(Course identifier, CourseSize size) {
        this.identifier = identifier;
        this.size = size;
    }

    @Override
    public String getStringID() {
        return identifier.toString();
    }

    @Override
    public String serializeToString() {
        return this.size.toString();
    }

    public Course getIdentifier() {
        return identifier;
    }

    public CourseSize getSize() {
        return size;
    }
}
