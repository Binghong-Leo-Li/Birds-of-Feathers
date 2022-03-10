package edu.ucsd.cse110wi22.team6.bof;

import android.content.Context;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessagesClient;

import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;

public class NearbyMessagesManager {
    private final MessagesClient messagesClient;
    private Message currentMessage;
    private AppStorage storage;

    private static NearbyMessagesManager INSTANCE;

    private NearbyMessagesManager(Context context) {
        messagesClient = MockedMessagesClient.getInstance(context);
        storage = Utilities.getStorageInstance(context);
        assert storage.isInitialized();
    }

    // Publish the first message
    public void update() {
//        changeMessage(MessageProcessor.Encoder.advertisePerson(storage.getUser()));
        UUID[] uuids = storage.getWaveToList().stream().map(IPerson::getUUID).toArray(UUID[]::new);
        changeMessage(MessageProcessor.Encoder.wave(storage.getUser(), uuids));
    }

    public void changeMessage(byte[] content) {
        if (currentMessage != null)
            messagesClient.unpublish(currentMessage);
        currentMessage = new Message(content);
        messagesClient.publish(currentMessage);
    }

    public void waveTo(IPerson person) {
        storage.waveTo(person);
        update();
    }

    public static NearbyMessagesManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NearbyMessagesManager(context);
        }
        return INSTANCE;
    }
}
