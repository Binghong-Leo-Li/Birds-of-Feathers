package edu.ucsd.cse110wi22.team6.bof;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.nearby.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Class for mocking nearby people to enable testing
public class MockingPasting extends NearbyActivity {
    private static final String TAG = "MockingPasting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocking_pasting);
        setTitle("Nearby Messages Mock");

        this.<Button>findViewById(R.id.mocking_return_home_button)
                .setOnClickListener((view) -> finish());
        this.<Button>findViewById(R.id.mocking_enter_button)
                .setOnClickListener(this::onEnterClicked);
        EditText uuidView = this.findViewById(R.id.uuidText);
        uuidView.setKeyListener(null);
        uuidView.setText(Utilities.getStorageInstance(this).getUser().getStringID());
    }

    private byte[] getMockedMessageContent(String csv) {
        IPerson originator;
        UUID waveUUID = null;
        try {
            String[] lines = csv.split("\\r?\\n");
            UUID originUUID = UUID.fromString(lines[0].split(",")[0]);
            String name = lines[1].split(",")[0];
            String photoURL = lines[2].split(",")[0];
            List<Course> courses = new ArrayList<>();
            for (int i = 3; i < lines.length; i++) {
                String[] lineSplit = lines[i].split(",");
                if (lineSplit[1].equals("wave")) {
                    waveUUID = UUID.fromString(lineSplit[0]);
                } else {
                    int year = Integer.parseInt(lineSplit[0]);
                    courses.add(new Course(
                            year, // Year
                            lineSplit[1], // Quarter
                            lineSplit[2], // Subject
                            lineSplit[3])); // Number
                }
            }
            originator = new Person(originUUID, name, courses, photoURL);
        } catch (RuntimeException exception) {
            // If anything doesn't parse, since instructors are humans as well
            Log.w(TAG, "Invalid CSV format!", exception);
            return null;
        }
        if (waveUUID == null) {
            return MessageProcessor.Encoder.advertisePerson(originator);
        } else {
            return MessageProcessor.Encoder.wave(originator, new UUID[]{waveUUID});
        }
    }

    // Handling when Enter button is clicked
    private void onEnterClicked(View view) {
        EditText input = findViewById(R.id.bof_info_pasted);
        byte[] data = getMockedMessageContent(input.getText().toString());
        input.getText().clear();
        if (data == null) {
            Utilities.showAlert(this, "Something went wrong. Check the format.");
            return;
        }
        Log.d(TAG, "Mocking message arrival: " + new String(data, StandardCharsets.UTF_8));
        MockedMessagesClient.mockMessageArrival(new Message(data), this);
    }
}