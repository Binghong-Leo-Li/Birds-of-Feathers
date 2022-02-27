package edu.ucsd.cse110wi22.team6.bof;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Activity to display List of BoFs
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    protected RecyclerView bofRecyclerView;
    protected RecyclerView.LayoutManager personsLayoutManager;
    protected BoFsViewAdapter personsViewAdapter;
    private IPerson user;
    private IUserInfoStorage storage;
    private MessageListener messageListener;

    // Dummy
    private final List<IPerson> nobody = Collections.emptyList();

    private static List<IPerson> nearbyPeople = new ArrayList<>();

    // Setter
    public static void setNearbyPeople(List<IPerson> nearbyPeople) {
        MainActivity.nearbyPeople = nearbyPeople;
    }

    // handling start up of the app
    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "MainActivity.onStart() called");

        Nearby.getMessagesClient(this).subscribe(messageListener);
    }

    // Updating UI to display all nearbyPeople
    public void updateUI() {
        for (IPerson person : nearbyPeople) {
            Log.d(TAG, person.getName() +
                    ": " +
                    Utilities.numCoursesTogether(user, person) +
                    " classes in common");
        }

        // the Adapter handles updating display
        personsViewAdapter.setPeopleList(Utilities.getBofList(user, nearbyPeople));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity.onCreate() called");

        Button stop_button= (Button)findViewById(R.id.stop_button);

        stop_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(final View view){
                final EditText edittext = new EditText(MainActivity.this);
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setView(edittext);
                alert.setTitle("Save Session");
                alert.setMessage("Enter Session Name");
                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String session_name = edittext.getText().toString();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    }
                });
                alert.show();
            }
        });

        storage = Utilities.getStorageInstance(this);

        bofRecyclerView = findViewById(R.id.bof_list);

        personsLayoutManager = new LinearLayoutManager(this);
        bofRecyclerView.setLayoutManager(personsLayoutManager);

        personsViewAdapter = new BoFsViewAdapter(nobody);
        bofRecyclerView.setAdapter(personsViewAdapter);

        Spinner preferences_dropdown=findViewById(R.id.preferences_dropdown);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.preferences, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        preferences_dropdown.setAdapter(adapter);

        findViewById(R.id.mock_ui_button).setOnClickListener(view -> {
            // for mocking purpose
            Intent intent = new Intent(this, MockingPasting.class);
            startActivity(intent);
        });

        // Applying Mocked data
        this.messageListener = new MockedMessageListener(new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                super.onFound(message);
                byte[] content = message.getContent();
                Log.d(TAG, "Received message: " + Arrays.toString(content));
                try {
                    nearbyPeople.add(Utilities.deserializePerson(content));
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    Log.w(TAG, "Invalid message!");
//                    return; // Uncomment these lines if not mocking
                }
//                updateUI();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Stop nearby
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }

}