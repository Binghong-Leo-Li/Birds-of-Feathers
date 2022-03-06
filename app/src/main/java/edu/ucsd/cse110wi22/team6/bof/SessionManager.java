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
        // Create a dummy session with no BoFs so that first launch will not crash
        this.currentSession = new Session(new UUID(0,0), Calendar.getInstance().getTime());
        this.storage = Utilities.getStorageInstance(context);
        // TODO: register storage as listener of session to enable autosave
        // remember to keep track of session registration as the current session changes
        // and also fire the listener on first creation
        messagesClient = MockedMessagesClient.getInstance(context);
    }

    // Allow mocking the current time since it is otherwise untestable
    public void setMockedTime(Date mockedTime) {
        this.mockedTime = mockedTime;
    }

    // Resume an existing session
    public void startSession(Session newSession) {
        assert !running;
        currentSession = newSession;
        running = true;
        messagesClient.subscribe(this.messageProcessor);
    }

    // Start a NEW session
    public void startNewSession() {
        startSession(new Session(mockedTime == null ?
                Calendar.getInstance().getTime() :
                mockedTime));
    }

    // Stop a session
    public void stopSession() {
        assert running;
        running = false;
        messagesClient.unsubscribe(this.messageProcessor);
    }

    // Getter for the current session
    public Session getCurrentSession() {
        return currentSession;
    }

    // Getter for whether there is a started session that is still running
    public boolean isRunning() {
        return running;
    }

    // Get the singleton instance
    public static SessionManager getInstance(Context context) {
        if (INSTANCE == null)  {
            INSTANCE = new SessionManager(context.getApplicationContext());
        }
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
