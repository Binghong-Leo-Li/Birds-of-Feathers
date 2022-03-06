package edu.ucsd.cse110wi22.team6.bof.model;

import edu.ucsd.cse110wi22.team6.bof.IPerson;

// Factory for IPerson
public class IPersonFactory implements IdentifiableFactory<IPerson> {
    @Override
    public IPerson deserialize(String id, String serializedData) {
        return IPerson.deserialize(serializedData);
    }
}
