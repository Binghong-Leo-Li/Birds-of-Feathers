package edu.ucsd.cse110wi22.team6.bof;

import java.util.Comparator;
import java.util.List;

public class CourseComparator implements Comparator<Course> {
    private final List<String> quartersOrdered;
    // Starting from WINTER since that's the first quarter of the year!

    public CourseComparator(List<String> quartersOrdered) {
        this.quartersOrdered = quartersOrdered;
    }

    @Override
    public int compare(Course c1, Course c2) {
        int result = Integer.compare(c1.getYear(), c2.getYear());
        if (result == 0) {
            return Integer.compare(quartersOrdered.indexOf(c1.getQuarter()),
                    quartersOrdered.indexOf(c2.getQuarter()));
        }
        return result;
    }
}
