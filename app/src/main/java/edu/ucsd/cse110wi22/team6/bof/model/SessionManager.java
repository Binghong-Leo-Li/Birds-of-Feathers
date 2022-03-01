package edu.ucsd.cse110wi22.team6.bof.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;

import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110wi22.team6.bof.MockedMessagesClient;

// Mediator between sessions, nearby messages, and persistent storage
public class SessionManager extends MessageListener {
    private static final String TAG = "SessionManager";
    private Session currentSession;
    private final MessagesClient messagesClient;
    // Have a storage handle object: AppStorage and instantiate it from Context
    private final Context context;
    private boolean running;

    private Date mockedTime;

    @SuppressLint("StaticFieldLeak")
    private static SessionManager INSTANCE;

    private SessionManager(Context context) {
        this.context = context;
        messagesClient = MockedMessagesClient.getInstance(context);
    }

    public void setMockedTime(Date mockedTime) {
        this.mockedTime = mockedTime;
    }

    public void startNewSession() {
        assert !running;
        currentSession = new Session(mockedTime == null ?
                Calendar.getInstance().getTime() :
                mockedTime);
        running = true;
        messagesClient.subscribe(this);
    }

    public void stopSession() {
        assert running;
        running = false;
        messagesClient.unsubscribe(this);
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public boolean isRunning() {
        return running;
    }

    public static void initialize(Context context) {
        INSTANCE = new SessionManager(context.getApplicationContext());
    }

    public SessionManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void onFound(@NonNull Message message) {
        super.onFound(message);
        String messageContent = new String(message.getContent());
        Log.d(TAG, "onFound: " + messageContent);
        // TODO: update list of bof in session, possibly handle wave
    }

    @Override
    public void onLost(@NonNull Message message) {
        super.onLost(message);
        String messageContent = new String(message.getContent());
        Log.d(TAG, "onLost: " + messageContent);
        // TODO: update list of bof in session, possibly handle wave
    }
}
