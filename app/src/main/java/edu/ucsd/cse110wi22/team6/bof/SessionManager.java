package edu.ucsd.cse110wi22.team6.bof;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.nearby.messages.MessageListener;

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
public class SessionManager implements IProcessedMessageListener, SessionChangeListener, INearbyState {
    private static final String TAG = "SessionManager";
    private Session currentSession;
//    private final Context context;
    private final AppStorage storage;
    private final NearbyMessagesManager nearbyMessagesManager;
    private boolean running;

    private final Collection<SessionChangeListener> currentSessionChangeListeners;

    private Date mockedTime;

    private static final UUID NIL_UUID = new UUID(0,0);

    @SuppressLint("StaticFieldLeak")
    private static SessionManager INSTANCE;

    private SessionManager(Context context) {
//        this.context = context;
        MessageListener messageProcessor = new MessageProcessor(this);
        // Create a dummy session with no BoFs so that first launch will not crash
        this.currentSession = new Session(NIL_UUID, Calendar.getInstance().getTime());
        this.currentSessionChangeListeners = new HashSet<>();
        this.storage = Utilities.getStorageInstance(context);
        this.nearbyMessagesManager = new NearbyMessagesManager(this, messageProcessor);
    }

    public NearbyMessagesManager getNearbyMessagesManager() {
        return nearbyMessagesManager;
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

    // Resume an existing session (USE ONLY IN TESTING PLEASE)
    // Use the version with activity parameter if in non-testing mode to enable nearby update
    public void startSession(Session newSession) {
        startSession(newSession, null);
    }

    // This version is needed for nearby to work
    public void startSession(Session newSession, NearbyActivity activity) {
        assert !running;
        currentSession = newSession;
        setRunning(true, activity);
        currentSession.registerListener(storage);
        currentSession.registerListener(this);
    }

    public void startNewSession(NearbyActivity activity) {
        Session newSession = new Session(mockedTime == null ?
                Calendar.getInstance().getTime() :
                mockedTime);
        storage.registerNewSession(newSession);
        startSession(newSession, activity);
    }

    // Start a NEW session (USE ONLY IN TESTING PLEASE)
    public void startNewSession() {
        startNewSession(null);
    }

    // Stop a session (TESTING ONLY)
    public void stopSession() {
        stopSession(null);
    }

    public void stopSession(NearbyActivity activity) {
        assert running;
        setRunning(false, activity);
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

    public void waveTo(IPerson person, Activity activity) {
        storage.waveTo(person);
        nearbyMessagesManager.notifyNearbyMessageChanged(activity);
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

    public void setRunning(boolean running, NearbyActivity activity) {
        assert this.running == !running; // necessary for nearby to work
        this.running = running;
        if (activity != null) {
            nearbyMessagesManager.notifyChangePublicity(activity);
            nearbyMessagesManager.notifyChangeSubscription(activity);
        }
    }

    @Override
    public boolean shouldPublish() {
        return isRunning();
    }

    @Override
    public boolean shouldSubscribe() {
        return isRunning();
    }

    @Override
    public byte[] currentMessage() {
        UUID[] uuids = storage.getWaveToList().stream().map(IPerson::getUUID).toArray(UUID[]::new);
        return MessageProcessor.Encoder.wave(storage.getUser(), uuids);
    }
}
