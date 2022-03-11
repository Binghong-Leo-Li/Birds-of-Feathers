package edu.ucsd.cse110wi22.team6.bof;

public interface INearbyState {
    boolean shouldPublish();
    boolean shouldSubscribe();
    byte[] currentMessage();
}
