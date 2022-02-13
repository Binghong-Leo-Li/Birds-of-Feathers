package edu.ucsd.cse110wi22.team6.bof;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;

public class MockedMessageListener extends MessageListener {
    private final MessageListener messageListener;
    private static MockedMessageListener instance;

    public static void mockReceiveMessage(Message message) {
        instance.messageListener.onFound(message);
    }

    public MockedMessageListener(MessageListener realMessageListener) {
        this.messageListener = realMessageListener;
        instance = this;
    }
}
