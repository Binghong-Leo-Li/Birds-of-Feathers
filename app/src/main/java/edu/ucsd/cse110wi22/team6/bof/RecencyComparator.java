package edu.ucsd.cse110wi22.team6.bof;

import java.util.Comparator;
import java.util.List;

public class RecencyComparator implements Comparator<IPerson> {

    IPerson user;
    String currentQuarter;
    int currentYear;


    public RecencyComparator(IPerson user, int currentYear, String currentQuarter) {
        this.user = user;
        this.currentQuarter = currentQuarter;
        this.currentYear = currentYear;
    }

    @Override
    public int compare(IPerson a, IPerson b) {
        if (calculateScore(a) > calculateScore(b)) return 1;
        else if (calculateScore(a) == calculateScore(b)) return 0;

        return -1;
    }

    public int calculateScore(IPerson a) {
        List<Course> coursesTogether = Utilities.getCoursesTogether(this.user, a);
        int score = 0;

        for (Course c : coursesTogether) {
            score += getRemember(calculateAge(c));
        }

        return score;
    }

    public int calculateAge(Course c) {
        int yearDiff = this.currentYear - c.getYear();
        yearDiff *= 4;
        int quarterDiff = quarterToInt(this.currentQuarter) - quarterToInt(c.getQuarter());
        return Math.max(yearDiff + quarterDiff - 1, 0);
    }

    public int getRemember(int age) {
        if (age < 0) return 5;
        if (age > 4) return 1;
        int[] ages = {5,4,3,2,1};
        return ages[age];
    }

    public int quarterToInt(String quarter) {
        String[] quarters = {"WI", "SP", "SS1", "SS2", "FA"};
        if (quarter.equals(quarters[0])) return 1;
        if (quarter.equals(quarters[1])) return 2;
        if (quarter.equals(quarters[2])) return 3;
        if (quarter.equals(quarters[3])) return 3;
        if (quarter.equals(quarters[4])) return 4;
        return -1;
    }

}
