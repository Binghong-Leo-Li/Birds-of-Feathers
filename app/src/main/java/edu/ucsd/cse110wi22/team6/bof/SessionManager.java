package edu.ucsd.cse110wi22.team6.bof;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.Session;
import edu.ucsd.cse110wi22.team6.bof.model.SessionChangeListener;

// Mediator between sessions, nearby messages, and persistent storage
public class SessionManager implements IProcessedMessageListener, SessionChangeListener {
    private static final String TAG = "SessionManager";
    private Session currentSession;
    private final MessagesClient messagesClient;
//    private final Context context;
    private final AppStorage storage;
    private final MessageListener messageProcessor;
    private boolean running;

    private final Collection<SessionChangeListener> currentSessionChangeListeners;

    private Date mockedTime;

    private static final UUID NIL_UUID = new UUID(0,0);

    @SuppressLint("StaticFieldLeak")
    private static SessionManager INSTANCE;

    private SessionManager(Context context) {
//        this.context = context;
        this.messageProcessor = new MessageProcessor(this);
        // Create a dummy session with no BoFs so that first launch will not crash
        this.currentSession = new Session(NIL_UUID, Calendar.getInstance().getTime());
        this.currentSessionChangeListeners = new HashSet<>();
        this.storage = Utilities.getStorageInstance(context);
        messagesClient = MockedMessagesClient.getInstance(context);
    }

    // Register observers
    public void registerSessionChangeListener(SessionChangeListener listener) {
        currentSessionChangeListeners.add(listener);
    }

    // Unregister observers
    public void unregisterSessionChangeListener(SessionChangeListener listener) {
        boolean removed = currentSessionChangeListeners.remove(listener);
        // Make sure the listener existed before, Java's not SRP so let's deal with it
        assert removed;
    }

    // Notify all the observers
    private void notifySessionChangeListeners() {
        for (SessionChangeListener listener : currentSessionChangeListeners)
            listener.onSessionModified(currentSession);
    }

    // Allow mocking the current time since it is otherwise untestable
    public void setMockedTime(Date mockedTime) {
        Log.d(TAG, "Mocking time successfully set to " + mockedTime);
        this.mockedTime = mockedTime;
    }

    // Resume an existing session
    public void startSession(Session newSession) {
        assert !running;
        currentSession = newSession;
        running = true;
        messagesClient.subscribe(this.messageProcessor);
        currentSession.registerListener(storage);
        currentSession.registerListener(this);
    }

    // Start a NEW session
    public void startNewSession() {
        Session newSession = new Session(mockedTime == null ?
                Calendar.getInstance().getTime() :
                mockedTime);
        storage.registerNewSession(newSession);
        startSession(newSession);
    }

    // Stop a session
    public void stopSession() {
        assert running;
        running = false;
        messagesClient.unsubscribe(this.messageProcessor);
        if (!currentSession.getSessionId().equals(NIL_UUID)) {
            currentSession.unregisterListener(storage);
            currentSession.unregisterListener(this);
        }
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

    private void foundNearbyStudent(IPerson person) {
        if (person.equals(storage.getUser())) {
            Log.d(TAG, "Found user oneself");
        } else {
            Log.d(TAG, "Adding student to session");
            storage.registerPerson(person);
            currentSession.addNearbyStudent(person);
        }
    }

    @Override
    public void onSessionModified(Session session) {
        assert session == currentSession;
        notifySessionChangeListeners();
    }

    @Override
    public void onAdvertise(IPerson person) {
        Log.d(TAG, "Found student " + person);
        foundNearbyStudent(person);
    }

    @Override
    public void onWave(IPerson from, UUID[] to) {
        // In case an "advertise" message from this person is missed, add it nevertheless
        foundNearbyStudent(from);
        UUID userUUID = storage.getUser().getUUID();
        if (Arrays.asList(to).contains(userUUID)) {
            Log.d(TAG, "Wave received from " + from);
            storage.waveFrom(from);
        } else {
            Log.d(TAG, "Not waving to me: " + from);
        }
    }
}
