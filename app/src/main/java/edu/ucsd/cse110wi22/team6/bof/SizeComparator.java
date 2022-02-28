package edu.ucsd.cse110wi22.team6.bof;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

// Essentially sorter for the "sort by class" size option
public class SizeComparator implements Comparator<IPerson> {

    IPerson user;
    Function<Course, CourseSize> getCourseSize;


    // constructor
    public SizeComparator(IPerson user, Function<Course, CourseSize> getCourseSize) {
        this.user = user;
        this.getCourseSize = getCourseSize;
    }

    @Override
    public int compare(IPerson a, IPerson b) {
        if (calculatePersonScore(a) > calculatePersonScore(b)) return 1;
        else if (calculatePersonScore(a) == calculatePersonScore(b)) return 0;
        return -1;
    }

    // Calculates the total score of a person regarding all the overlapping courses
    public double calculatePersonScore(IPerson a) {
        List<Course> coursesTogether = Utilities.getCoursesTogether(this.user, a);
        double score = 0;

        for (Course c : coursesTogether) { // for each overlapping course
            score += calculateCourseScore(c);
        }

        return score;
    }

    // Calculates the score of one course based on its size
    public double calculateCourseScore(Course c) {
        return this.getCourseSize.apply(c).getWeight(); // getting weight of a course
    }


}
