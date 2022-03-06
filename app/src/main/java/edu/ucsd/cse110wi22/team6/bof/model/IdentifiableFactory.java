package edu.ucsd.cse110wi22.team6.bof.model;

// Factory interface using the factory method pattern to create Identifiable objects
// Otherwise there is no way IDMapping know how to deserialized those objects
public interface IdentifiableFactory<T extends Identifiable> {
    T deserialize(String id, String serializedData);
}
