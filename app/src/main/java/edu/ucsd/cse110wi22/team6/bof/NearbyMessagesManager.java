package edu.ucsd.cse110wi22.team6.bof;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessagesClient;

import java.util.Arrays;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;

public class NearbyMessagesManager {
    private static final String TAG = NearbyMessagesManager.class.getSimpleName();
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
        changeMessage(MessageProcessor.Encoder.advertisePerson(storage.getUser()));
        UUID[] uuids = storage.getWaveToList().stream().map(IPerson::getUUID).toArray(UUID[]::new);
        changeMessage(MessageProcessor.Encoder.wave(storage.getUser(), uuids));
    }

    public void changeMessage(byte[] content) {
        if (currentMessage != null) {
            if (Arrays.equals(content, currentMessage.getContent())) {
                Log.d(TAG, "Message content unchanged, skip unpublish and re-publish");
                return;
            }
            messagesClient.unpublish(currentMessage).addOnFailureListener(this::nearbyError);
            Log.d(TAG, "Unpublished old message");
        }
        currentMessage = new Message(content);
        messagesClient.publish(currentMessage).addOnFailureListener(this::nearbyError);
        Log.d(TAG, "Re-published new message");
    }

    private void nearbyError(Exception e) {
        Log.e(TAG, "Failed to publish a message! ", e);
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
