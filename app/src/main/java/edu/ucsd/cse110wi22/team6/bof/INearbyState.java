package edu.ucsd.cse110wi22.team6.bof;

//Interface for all nearby states
public interface INearbyState {
    boolean shouldPublish();
    boolean shouldSubscribe();
    byte[] currentMessage();
}
