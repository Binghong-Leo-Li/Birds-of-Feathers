package edu.ucsd.cse110wi22.team6.bof;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.nearby.messages.MessageListener;

// TODO: make every concerned activity implement this
public abstract class NearbyActivity extends AppCompatActivity {
    protected NearbyMessagesManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = SessionManager.getInstance(this).getNearbyMessagesManager();
    }

    @Override
    protected void onStop() {
        manager.onStop(this);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        manager.onStart(this);
    }
}
