package edu.ucsd.cse110wi22.team6.bof;

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
import java.util.List;

// A mocked version of Android's Nearby messages API client
public class MockedMessagesClient implements MessagesClient {
    private final String TAG = "MockedMessagesClient";

    private final MessagesClient realClient;
    private final List<MessageListener> listeners = new ArrayList<>();

    private static MockedMessagesClient instance;

    private MockedMessagesClient(Context context) {
        realClient = Nearby.getMessagesClient(context);
    }

    public static MockedMessagesClient getInstance(Context context) {
        if (instance == null)
            instance = new MockedMessagesClient(context.getApplicationContext());
        return instance;
    }

    public void mockMessageArrival(Message message) {
        for (MessageListener listener : listeners) {
            listener.onFound(message);
        }
    }

    @NonNull
    @Override
    public Task<Void> publish(@NonNull Message message) {
        Log.d(TAG, "publish() message " + new String(message.getContent()));
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
        Log.d(TAG, "unpublish() message " + new String(message.getContent()));
        return realClient.unpublish(message);
    }

    @NonNull
    @Override
    public Task<Void> subscribe(@NonNull MessageListener messageListener) {
        Log.d(TAG, "subscribe(" + messageListener + ")");
        listeners.add(messageListener);
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
        throw new UnsupportedOperationException("PendingIntent variants not supported.");
    }

    @NonNull
    @Override
    public Task<Void> subscribe(@NonNull PendingIntent pendingIntent) {
        throw new UnsupportedOperationException("PendingIntent variants not supported.");
    }

    @NonNull
    @Override
    public Task<Void> unsubscribe(@NonNull MessageListener messageListener) {
        Log.d(TAG, "unsubscribe(" + messageListener + ")");
        listeners.remove(messageListener);
        return realClient.unsubscribe(messageListener);
    }

    @NonNull
    @Override
    public Task<Void> unsubscribe(@NonNull PendingIntent pendingIntent) {
        throw new UnsupportedOperationException("PendingIntent variants not supported.");
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
        throw new UnsupportedOperationException("handleIntent not supported.");
    }

    @NonNull
    @Override
    public ApiKey<MessagesOptions> getApiKey() {
        return realClient.getApiKey();
    }
}
