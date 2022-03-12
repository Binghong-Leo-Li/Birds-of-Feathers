package edu.ucsd.cse110wi22.team6.bof;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import edu.ucsd.cse110wi22.team6.bof.model.AppStorage;
import edu.ucsd.cse110wi22.team6.bof.model.InMemoryMapping;
import edu.ucsd.cse110wi22.team6.bof.model.SharedPreferencesMapping;

// The one singleton class that contains all the singleton instances/static states in this app
// We can make testing more consistent if we reset this instance
// (which will reset the state of the entire app)
public class App {
    private static App INSTANCE = null;
    private static boolean persistence = true;
    private final AppStorage storage;
    private final SessionManager sessionManager;
    private final InMemoryMapping inMemoryMapping;
    private final Collection<MessageListener> mockingListeners = new ArrayList<>();

    private App(Context context, boolean persistence) {
        inMemoryMapping = new InMemoryMapping(new HashMap<>());
        storage = new AppStorage(persistence ? new SharedPreferencesMapping(
                context.getSharedPreferences(Constants.PREFERENCE_STRING, MODE_PRIVATE)
        ) : inMemoryMapping);
        sessionManager = new SessionManager(storage);
    }

    public AppStorage getStorage() {
        return storage;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public InMemoryMapping getInMemoryMapping() {
        return inMemoryMapping;
    }

    public Collection<MessageListener> getMockingListeners() {
        return mockingListeners;
    }

    public static App getInstance(Context context) {
        assert context != null;
        if (INSTANCE == null)  {
            INSTANCE = new App(context.getApplicationContext(), persistence && Utilities.getPersistence());
        }
        return INSTANCE;
    }

    public static void resetInstance(boolean newPersistence) {
        persistence = newPersistence;
        INSTANCE = null;
    }
}
