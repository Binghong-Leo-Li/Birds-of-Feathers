package edu.ucsd.cse110wi22.team6.bof;

public enum CourseSize {
    TINY(1.00),
    SMALL(0.33),
    MEDIUM(0.18),
    LARGE(0.10),
    HUGE(0.06),
    GIGANTIC(0.03);

    CourseSize(double weight) {
        this.weight = weight;
    }

    private final double weight;

    public double getWeight() {
        return weight;
    }
}
