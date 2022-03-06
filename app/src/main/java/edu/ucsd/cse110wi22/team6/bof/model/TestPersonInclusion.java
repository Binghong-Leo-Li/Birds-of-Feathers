package edu.ucsd.cse110wi22.team6.bof.model;

import edu.ucsd.cse110wi22.team6.bof.IPerson;
import edu.ucsd.cse110wi22.team6.bof.Person;

public class TestPersonInclusion {
    private final Person person;
    private final int version;
    private final CourseSize cs;

    public TestPersonInclusion(Person person, int version, CourseSize cs) {
        this.person = person;
        this.version = version;
        this.cs = cs;
    }
}
