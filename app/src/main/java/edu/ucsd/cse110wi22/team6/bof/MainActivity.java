package edu.ucsd.cse110wi22.team6.bof;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public static void setNearbyPeople(List<IPerson> nearbyPeople) {
        MainActivity.nearbyPeople = nearbyPeople;
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "MainActivity.onStart() called");

        Log.d(TAG, "App has gone through first time setup already");
        user = new Person(storage.getName(),
                storage.getCourseList(),
                storage.getPhotoUrl());

        personsViewAdapter.setUser(user);
        updateUI();



        Nearby.getMessagesClient(this).subscribe(messageListener);
    }

    public void updateUI() {
        for (IPerson person : nearbyPeople) {
            Log.d(TAG, person.getName() +
                    ": " +
                    Utilities.numCoursesTogether(user, person) +
                    " classes in common");
        }

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
            Intent intent = new Intent(this, MockingPasting.class);
            startActivity(intent);
        });


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

        Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }

}