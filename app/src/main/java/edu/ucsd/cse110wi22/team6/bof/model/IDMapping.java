package edu.ucsd.cse110wi22.team6.bof.model;

// An abstract String ID -> Identifiable mapping
public class IDMapping<T extends Identifiable> {
    private final String namespace;
    private final IKeyValueStore storage;
    private final IdentifiableFactory<T> factory;
    private static final String SEPARATOR = ":";

    // Namespace separates different IDMapping objects sharing a single backing IKeyValueStore
    public IDMapping(IKeyValueStore storage, String namespace, IdentifiableFactory<T> factory) {
        assert !namespace.contains(SEPARATOR);
        this.namespace = namespace;
        this.storage = storage;
        this.factory = factory;
    }

    // Add a new known object to this mapping
    public void registerObject(T obj) {
        storage.putString(namespace + SEPARATOR + obj.getStringID(), obj.serializeToString());
    }

    // Lookup a known object by its ID
    public T getObjectByID(String id) {
        String serialized = storage.getString(namespace + SEPARATOR + id, null);
        // Object does not exist if serialized == null, violates contract of registering it before using it
        assert serialized != null;
        return factory.deserialize(id, serialized);
    }
}
