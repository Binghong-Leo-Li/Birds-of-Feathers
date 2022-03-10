package edu.ucsd.cse110wi22.team6.bof;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Adapter from (raw) MessageListener to IProcessedMessageListener
// Converts from raw to processed (decoded) messages
public class MessageProcessor extends MessageListener {
    private static final String TAG = "MessageProcessor";
    // JSON tag names
    public static final String TAG_TYPE = "type";
    public static final String TAG_PERSON = "person";
    public static final String TAG_FROM = "from";
    public static final String TAG_TO = "to";
    private final IProcessedMessageListener processedMessageListener;
    private final Gson gson = new Gson();

    public MessageProcessor(IProcessedMessageListener processedMessageListener) {
        this.processedMessageListener = processedMessageListener;
    }

    // onFound: notify the IProcessedMessageListener with the appropriate message
    @Override
    public void onFound(@NonNull Message message) {
        super.onFound(message);
        String messageContent = new String(message.getContent());
        Log.d(TAG, "onFound() message " + messageContent);
        JsonObject jsonObject = JsonParser.parseString(messageContent).getAsJsonObject();
        MessageType type = gson.fromJson(jsonObject.get(TAG_TYPE), MessageType.class);
        switch (type) {
            case ADVERTISE: {
                IPerson person = gson.fromJson(jsonObject.get(TAG_PERSON), Person.class);
                processedMessageListener.onAdvertise(person);
                break;
            }
            case WAVE: {
                IPerson from = gson.fromJson(jsonObject.get(TAG_FROM), Person.class);
                UUID to = gson.fromJson(jsonObject.get(TAG_TO), UUID.class);
                processedMessageListener.onWave(from, to);
                break;
            }
            default:
                assert false; // invalid message, should not happen!
        }
    }

    @Override
    public void onLost(@NonNull Message message) {
        super.onLost(message);
        String messageContent = new String(message.getContent());
        Log.d(TAG, "onLost() message " + messageContent);
    }

    public static class Encoder {
        private static final Gson gson = new Gson();
        // Generate message for advertising the arrival of a person
        public static byte[] advertisePerson(IPerson person) {
            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put(TAG_TYPE, MessageType.ADVERTISE);
            jsonObject.put(TAG_PERSON, person);
            return gson.toJson(jsonObject).getBytes(StandardCharsets.UTF_8);
        }
        // Generate message for sending a wave
        public static byte[] wave(IPerson from, UUID target) {
            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put(TAG_TYPE, MessageType.WAVE);
            jsonObject.put(TAG_FROM, from);
            jsonObject.put(TAG_TO, target);
            return gson.toJson(jsonObject).getBytes(StandardCharsets.UTF_8);
        }
    }

    public enum MessageType {
        ADVERTISE,WAVE
    }
}
