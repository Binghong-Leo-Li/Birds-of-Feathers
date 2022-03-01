package edu.ucsd.cse110wi22.team6.bof.model;

import com.google.gson.Gson;

import edu.ucsd.cse110wi22.team6.bof.IPerson;
import edu.ucsd.cse110wi22.team6.bof.Person;

public class IPersonFactory implements IdentifiableFactory<IPerson> {
    @Override
    public IPerson deserialize(String id, String serializedData) {
        Gson gson = new Gson();
        return gson.fromJson(serializedData, Person.class);
    }
}
