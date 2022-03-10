package edu.ucsd.cse110wi22.team6.bof;

import android.content.Context;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessagesClient;

public class NearbyMessagesManager {
    private final MessagesClient messagesClient;
    private Message currentMessage;

    private static NearbyMessagesManager INSTANCE;

    private NearbyMessagesManager(Context context) {
        messagesClient = MockedMessagesClient.getInstance(context);
    }

    public void changeMessage(byte[] content) {
        if (currentMessage != null)
            messagesClient.unpublish(currentMessage);
        currentMessage = new Message(content);
        messagesClient.publish(currentMessage);
    }

    public static NearbyMessagesManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NearbyMessagesManager(context);
        }
        return INSTANCE;
    }
}
