package edu.ucsd.cse110wi22.team6.bof;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class NearbyMessagesManager {
    private static final String TAG = NearbyMessagesManager.class.getSimpleName();
//    private AppStorage storage;

    private Message currentMessage;
    private final MessageListener currentListener;
//    private boolean published;
//    private boolean subscribed;
    private final INearbyState nearbyState;

    public NearbyMessagesManager(INearbyState nearbyState, MessageListener listener) {
//        storage = Utilities.getStorageInstance(context);
//        assert storage.isInitialized();
        this.nearbyState = nearbyState;
        currentListener = listener;
        currentMessage = new Message(MessageProcessor.Encoder.nil());
    }

    public void notifyNearbyMessageChanged(Activity activity) {
        Log.d(TAG, "notifyNearbyMessagesChanged(" + activity + ")");
        changeMessage(activity, nearbyState.currentMessage());
    }

    // MUST be actual change in publicity to avoid double-publish/unpublish
    public void notifyChangePublicity(Activity activity) {
        Log.d(TAG, "notifyChangePublicity(" + activity + ")");
        if (nearbyState.shouldPublish()) {
            publish(activity);
        } else {
            unpublish(activity);
        }
    }

    // MUST be actual change in subscription to avoid double-subscribe/unsubscribe
    public void notifyChangeSubscription(NearbyActivity activity) {
        Log.d(TAG, "notifyChangeSubscription(" + activity + ")");
        if (nearbyState.shouldSubscribe()) {
            subscribe(activity);
        } else {
            unsubscribe(activity);
        }
    }

    private void changeMessage(Activity activity, byte[] content) {
        boolean shouldPublish = nearbyState.shouldPublish();
        Log.d(TAG, activity + " tries to change the message");
        if (Arrays.equals(content, currentMessage.getContent())) {
            Log.d(TAG, "Message content unchanged, skip unpublish and re-publish");
            return;
        }
        if (shouldPublish)
            unpublish(activity);
        Log.d(TAG, "Unpublished old message");

        currentMessage = new Message(content);
        if (shouldPublish) {
            publish(activity);
            Log.d(TAG, "Re-published new message");
        }
        else
            Log.d(TAG, "Message changed to " + new String(content, StandardCharsets.UTF_8) + " but not published");
    }

    private void publish(Activity activity) {
//        assert currentMessage != null;
//        assert !published;
//        published = true;
        MockedMessagesClient
                .getMessagesClient(activity)
                .publish(currentMessage)
                .addOnFailureListener(e -> nearbyError("publish", e));
    }

    private void unpublish(Activity activity) {
//        assert published;
//        published = false;
        MockedMessagesClient
                .getMessagesClient(activity)
                .unpublish(currentMessage)
                .addOnFailureListener(e -> nearbyError("unpublish", e));
    }

    private void subscribe(NearbyActivity activity) {
//        assert !subscribed;
//        subscribed = true;
        MockedMessagesClient
                .getMessagesClient(activity)
                .subscribe(currentListener)
                .addOnFailureListener(e -> nearbyError("subscribe", e));
    }

    private void unsubscribe(NearbyActivity activity) {
//        assert subscribed;
//        subscribed = false;
        MockedMessagesClient
                .getMessagesClient(activity)
                .unsubscribe(currentListener)
                .addOnFailureListener(e -> nearbyError("unsubscribe", e));
    }

    private void nearbyError(String action, Exception e) {
        Log.e(TAG, "Failed to " + action + "()", e);
    }

    public void onStart(NearbyActivity activity) {
        Log.d(TAG, "onStart " + activity);
        if (nearbyState.shouldPublish())
            publish(activity);
        if (nearbyState.shouldSubscribe())
            subscribe(activity);
    }

    public void onStop(NearbyActivity activity) {
        Log.d(TAG, "onStop " + activity);
//        if (published)
        if (nearbyState.shouldPublish())
            unpublish(activity);
        if (nearbyState.shouldSubscribe())
            unsubscribe(activity);
    }
}
