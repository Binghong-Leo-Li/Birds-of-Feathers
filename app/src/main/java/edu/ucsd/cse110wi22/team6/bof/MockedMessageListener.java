package edu.ucsd.cse110wi22.team6.bof;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

// Class mocking receiving nearby messages
public class MockedMessageListener extends MessageListener {
    private final MessageListener messageListener;
    private static MockedMessageListener instance;

    // Handling receiving message
    public static void mockReceiveMessage(Message message) {
        instance.messageListener.onFound(message);
    }

    // Constructor
    public MockedMessageListener(MessageListener realMessageListener) {
        this.messageListener = realMessageListener;
        instance = this;
    }
}
