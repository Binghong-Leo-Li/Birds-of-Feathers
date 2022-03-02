package edu.ucsd.cse110wi22.team6.bof.model;

public interface IdentifiableFactory<T extends Identifiable> {
    T deserialize(String id, String serializedData);
}
