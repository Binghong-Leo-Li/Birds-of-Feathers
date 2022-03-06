package edu.ucsd.cse110wi22.team6.bof;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.Session;

// Mediator between sessions, nearby messages, and persistent storage
public class SessionManager implements IProcessedMessageListener {
    private static final String TAG = "SessionManager";
    private Session currentSession;
    private final MessagesClient messagesClient;
    // Have a storage handle object: AppStorage and instantiate it from Context
    private final Context context;
    private final AppStorage storage;
    private final MessageListener messageProcessor;
    private boolean running;

    private Date mockedTime;

    @SuppressLint("StaticFieldLeak")
    private static SessionManager INSTANCE;

    private SessionManager(Context context) {
        this.context = context;
        this.messageProcessor = new MessageProcessor(this);
        this.storage = Utilities.getStorageInstance(context);
        messagesClient = MockedMessagesClient.getInstance(context);
    }

    public void setMockedTime(Date mockedTime) {
        this.mockedTime = mockedTime;
    }

    // Used to resume from an existing session
    public void startSession(Session newSession) {
        assert !running;
        currentSession = newSession;
        running = true;
        messagesClient.subscribe(this.messageProcessor);
    }

    public void startNewSession() {
        startSession(new Session(mockedTime == null ?
                Calendar.getInstance().getTime() :
                mockedTime));
    }

    public void stopSession() {
        assert running;
        running = false;
        messagesClient.unsubscribe(this.messageProcessor);
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
    public void onAdvertise(IPerson person) {
        Log.d(TAG, "Added student " + person);
        currentSession.addNearbyStudent(person);
    }

    @Override
    public void onWave(IPerson from, UUID to) {
        if (storage.getUser().getUUID().equals(to)) {
            Log.d(TAG, "Wave received from " + from);
            // TODO: handle wave by adding to wave list in AppStorage
        }
    }
}
