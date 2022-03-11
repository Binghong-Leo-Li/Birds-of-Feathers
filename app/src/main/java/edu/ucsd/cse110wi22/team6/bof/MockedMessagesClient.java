package edu.ucsd.cse110wi22.team6.bof;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.MessagesClient;
import com.google.android.gms.nearby.messages.MessagesOptions;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.StatusCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// A mocked version of Android's Nearby messages API client
public class MockedMessagesClient implements MessagesClient {
    private final String TAG = "MockedMessagesClient";

    private final MessagesClient realClient;
    private final Activity activity;
    // TODO: make this resettable across tests (instance field of App?)
    private static final Collection<MessageListener> listeners = new ArrayList<>();

//    private static MockedMessagesClient instance;

//    @Deprecated
//    private MockedMessagesClient(Context context) {
//        realClient = Nearby.getMessagesClient(context);
//    }

    private MockedMessagesClient(Activity activity) {
        realClient = Nearby.getMessagesClient(activity);
        this.activity = activity;
    }

//    @Deprecated
//    public static MockedMessagesClient getInstanceOld(Context context) {
//        if (instance == null)
//            instance = new MockedMessagesClient(context.getApplicationContext());
//        return instance;
//    }

    public static MessagesClient getMessagesClient(Activity activity) {
        return new MockedMessagesClient(activity);
    }

    public static void mockMessageArrival(Message message) {
        for (MessageListener listener : listeners) {
            listener.onFound(message);
        }
    }

    private void logListeners() {
        Log.d(TAG, "List of listeners right now: " + listeners);
    }

    @NonNull
    @Override
    public Task<Void> publish(@NonNull Message message) {
        Log.d(TAG, activity + " publish() message " + new String(message.getContent()));
        return realClient.publish(message);
    }

    @NonNull
    @Override
    public Task<Void> publish(@NonNull Message message, @NonNull PublishOptions publishOptions) {
        publish(message);
        return realClient.publish(message, publishOptions);
    }

    @NonNull
    @Override
    public Task<Void> unpublish(@NonNull Message message) {
        Log.d(TAG, activity + " unpublish() message " + new String(message.getContent()));
        return realClient.unpublish(message);
    }

    @NonNull
    @Override
    public Task<Void> subscribe(@NonNull MessageListener messageListener) {
        Log.d(TAG, activity + " subscribe(" + messageListener + ")");
        listeners.add(messageListener);
        logListeners();
        return realClient.subscribe(messageListener);
    }

    @NonNull
    @Override
    public Task<Void> subscribe(@NonNull MessageListener messageListener, @NonNull SubscribeOptions subscribeOptions) {
        subscribe(messageListener);
        return realClient.subscribe(messageListener, subscribeOptions);
    }

    @NonNull
    @Override
    public Task<Void> subscribe(@NonNull PendingIntent pendingIntent, @NonNull SubscribeOptions subscribeOptions) {
        return realClient.subscribe(pendingIntent, subscribeOptions);
    }

    @NonNull
    @Override
    public Task<Void> subscribe(@NonNull PendingIntent pendingIntent) {
        return realClient.subscribe(pendingIntent);
    }

    @NonNull
    @Override
    public Task<Void> unsubscribe(@NonNull MessageListener messageListener) {
        Log.d(TAG, activity + " unsubscribe(" + messageListener + ")");
        listeners.remove(messageListener);
        logListeners();
        return realClient.unsubscribe(messageListener);
    }

    @NonNull
    @Override
    public Task<Void> unsubscribe(@NonNull PendingIntent pendingIntent) {
        return realClient.unsubscribe(pendingIntent);
    }

    @NonNull
    @Override
    public Task<Void> registerStatusCallback(@NonNull StatusCallback statusCallback) {
        return realClient.registerStatusCallback(statusCallback);
    }

    @NonNull
    @Override
    public Task<Void> unregisterStatusCallback(@NonNull StatusCallback statusCallback) {
        return realClient.unregisterStatusCallback(statusCallback);
    }

    @Override
    public void handleIntent(@NonNull Intent intent, @NonNull MessageListener messageListener) {
        realClient.handleIntent(intent, messageListener);
    }

    @NonNull
    @Override
    public ApiKey<MessagesOptions> getApiKey() {
        return realClient.getApiKey();
    }
}
